#!/bin/bash -
#===============================================================================
#
#          FILE: create_sdcard_from_flashlayout.sh
#
#         USAGE: ./create_sdcard_from_flashlayout.sh
#
#   DESCRIPTION: generate raw image with information from flash layout
#
# SPDX-License-Identifier: MIT
#        AUTHOR: Christophe Priouzeau (christophe.priouzeau@st.com),
#  ORGANIZATION: STMicroelectronics
#     COPYRIGHT: Copyright (C) 2017, STMicroelectronics - All Rights Reserved
#       CREATED: 11/22/2017 15:03
#      REVISION:  ---
#===============================================================================
unset FLASHLAYOUT_data
unset FLASHLAYOUT_filename
unset FLASHLAYOUT_rawname
unset FLASHLAYOUT_filename_path
unset FLASHLAYOUT_prefix_image_path
unset FLASHLAYOUT_number_of_line
unset FLASHLAYOUT_uri
unset FLASHLAYOUT_module
unset FLASHLAYOUT_kernel
unset FLASHLAYOUT_dtb

declare -A FLASHLAYOUT_data
FLASHLAYOUT_uri=""
FLASHLAYOUT_module=""
FLASHLAYOUT_kernel=""
FLASHLAYOUT_dtb=""
FLASHLAYOUT_arch=""
# Size of 4GB
#DEFAULT_RAW_SIZE=4096
# Size of 2,5GB
#DEFAULT_RAW_SIZE=2560
# Size of 2GB
#DEFAULT_RAW_SIZE=2048
# Size of 1GB
DEFAULT_RAW_SIZE=1024

# 32 MB of Padding on B
DEFAULT_PADDING_SIZE=33554432

# Columns name on FLASHLAYOUT_data
COL_SELECTED_OPT=0
COL_PARTNAME=2
COL_PARTYPE=3
COL_OFFSET=4
COL_BIN2FLASH=5

# SELECTED/OPT variable meaning:
# - : boot stage
# P: programme
# E: erase
# D: delete

# add on path the /sbin to have access to mkfs.vfat and sgdisk
PATH=$PATH:/sbin

WARNING_TEXT=""

usage() {
	echo "Usage: $0 <flashlayout>"
	exit 0
}

debug() {
	if [ $DEBUG ];
	then
		echo ""
		echo "[DEBUG]: $@"
	fi
}

function exec_print() {
	if [ $DEBUG ];
	then
		echo ""
		echo "[DEBUG]: $@"
		$@
	else
		$@ 2> /dev/null > /dev/null
	fi
}

# Read Flash Layout file and put information on array: FLASHLAYOUT_data
function read_flash_layout() {
	local i=0
	declare -a flashlayout_data     # Create an indexed array (necessary for the read command).
	FLASHLAYOUT_number_of_line=$(wc -l "$FLASHLAYOUT_filename" | cut -sf 1 -d ' ')
	debug "Number of line: $FLASHLAYOUT_number_of_line"
	while read -ra flashlayout_data; do
		selected=${flashlayout_data[0]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			# Selected=
			FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]=${flashlayout_data[0]}
			#PartName
			FLASHLAYOUT_data[$i,$COL_PARTNAME]=${flashlayout_data[1]}
			#PartType
			FLASHLAYOUT_data[$i,$COL_PARTYPE]=${flashlayout_data[2]}
			#Offset
			FLASHLAYOUT_data[$i,$COL_OFFSET]=${flashlayout_data[3]}
			#Bin2flash
			FLASHLAYOUT_data[$i,$COL_BIN2FLASH]=${flashlayout_data[4]}
			i=$(($i+1))

			debug "READ: ${flashlayout_data[0]} ${flashlayout_data[1]} ${flashlayout_data[2]} ${flashlayout_data[3]} ..."
		fi
		if [ "$selected" == "URI" ]
		then
			FLASHLAYOUT_uri=${flashlayout_data[1]}
		fi
		if [ "$selected" == "MODULE" ]
		then
			FLASHLAYOUT_module=${flashlayout_data[1]}
		fi
		if [ "$selected" == "KERNEL" ]
		then
			FLASHLAYOUT_kernel=${flashlayout_data[1]}
		fi
		if [ "$selected" == "DTB" ]
		then
			FLASHLAYOUT_dtb=${flashlayout_data[1]}
		fi
		if [ "$selected" == "ARCH" ]
		then
			FLASHLAYOUT_arch=${flashlayout_data[1]}
		fi
	done < "$FLASHLAYOUT_filename"

	FLASHLAYOUT_number_of_line=$i
}

function debug_dump_flashlayout_data_array() {
	columns=8
	for i in $(seq 0 $FLASHLAYOUT_number_of_line); do
		for j in $(seq 0 $columns); do
			echo -n " ${FLASHLAYOUT_data[$i,$j]}"
		done
		echo ""
	done
}

function generate_rootfs_from_tarball() {
	tarball=$1
	_rootfs_name="${tarball%.*.*}" #tar.xz

	if [ -f $FLASHLAYOUT_prefix_image_path/$_rootfs_name.ext4 ];
	then
		# ext4 already generated
		# update FLASHLAYOUT_data for rootfs
		for i in $(seq 0 $FLASHLAYOUT_number_of_line)
		do
			selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
			partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
			bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}
			if [ "$partName" == "rootfs" ];
			then
				FLASHLAYOUT_data[$i,$COL_BIN2FLASH]=$_rootfs_name.ext4
			fi
			if [ "$partName" == "bootfs" ];
			then
				FLASHLAYOUT_data[$i,$COL_BIN2FLASH]=$_rootfs_name.bootfs.vfat
			fi
		done
		return
	fi

	localpath=$(pwd)
	dtb_name=$(echo $FLASHLAYOUT_dtb | sed -e "s/-for-debian//")
	mkdir temp_rootfs
	cd temp_rootfs
	sudo tar xf ../$tarball
	sudo tar xf ../$FLASHLAYOUT_module

	sudo mkdir -p boot/efi/boot/
	sudo cp ../$FLASHLAYOUT_kernel boot/efi/boot/bootarm.efi
	if [ "$FLASHLAYOUT_dtb" != "none" ];
	then
		sudo cp ../$FLASHLAYOUT_dtb boot/$dtb_name
	fi
	cd ..
	#generate vfat version of bootfs (64MB -512K) (-512 are to not erase the gpt partion information
	_vfat_block=$((64*1024-512))
	mkfs.vfat -n BOOT -S 512 -C $_rootfs_name.bootfs.vfat $_vfat_block
	if [ "$FLASHLAYOUT_dtb" != "none" ];
	then
		mcopy -i $_rootfs_name.bootfs.vfat -s $FLASHLAYOUT_dtb ::/$dtb_name
	fi
	mmd -i $_rootfs_name.bootfs.vfat ::/efi
	mmd -i $_rootfs_name.bootfs.vfat ::/efi/boot
	case $FLASHLAYOUT_arch in
	armhf)
		mcopy -i $_rootfs_name.bootfs.vfat -s $FLASHLAYOUT_kernel ::/efi/boot/bootarm.efi
		;;
	aarch64|arm64)
		mcopy -i $_rootfs_name.bootfs.vfat -s $FLASHLAYOUT_kernel ::/efi/boot/bootaa64.efi
		;;
	esac

	size_full=$(sudo du -s temp_rootfs| tr '\t' ' ' | cut -f 1 -d' ')
	_size=$(($size_full/1024))
	size=$(($_size + 32))
	addons=$(echo $size"M")
	echo "[SIZE = $_size M] mkfs.ext4 -d temp_rootfs -m 5 -L debian_rootfs -F -O ^64bit $_rootfs_name.ext4 $addons"
	sudo mkfs.ext4 -d temp_rootfs -m 5 -L debian_rootfs -F -O ^64bit $_rootfs_name.ext4 $addons
	# update FLASHLAYOUT_data for rootfs
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}
		if [ "$partName" == "rootfs" ];
		then
			FLASHLAYOUT_data[$i,$COL_BIN2FLASH]=$_rootfs_name.ext4
		fi
		if [ "$partName" == "bootfs" ];
		then
			FLASHLAYOUT_data[$i,$COL_BIN2FLASH]=$_rootfs_name.bootfs.vfat
		fi
	done
	# update tsv file if exist
	tsv_template_file=$(echo "$FLASHLAYOUT_filename" | sed -e "s/fld/tsv.template/")
	tsv_file=$(echo "$FLASHLAYOUT_filename" | sed -e "s/fld/tsv/")
	if [ -e $tsv_template_file ]
	then
		sed -e "s|%%IMAGE%|$_rootfs_name.ext4|g" $tsv_template_file > $tsv_file
		sed -i -e "s|%%BOOTFS_IMAGE%|$_rootfs_name.bootfs.vfat|g" $tsv_file
	fi
	# clean
	sudo rm -rf temp_rootfs
}

# Verify and precise the path to image specified on Flash layout
function get_last_image_path() {
	local i=0
	root_image=""

	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}

		case "$selected" in
		1|P)
			if [ "$partName" == 'rootfs' ];
			then
				root_image=$bin2flash
				echo $bin2flash | grep -q tar;
				is_tar=$?
				if [ $is_tar -eq 0 ];
				then
					continue
				fi
			fi
			if [ -f $FLASHLAYOUT_filename_path/$bin2flash ];
			then
				FLASHLAYOUT_prefix_image_path="$FLASHLAYOUT_filename_path"
			else
				if [ -f $FLASHLAYOUT_filename_path/../$bin2flash ];
				then
					FLASHLAYOUT_prefix_image_path="$FLASHLAYOUT_filename_path/.."
				else
					if [ -z $FLASHLAYOUT_uri ];
					then
						echo "[ERROR] couldn't find $bin2flash on $FLASHLAYOUT_filename_path/"
						exit 0
					fi
					FLASHLAYOUT_prefix_image_path="$FLASHLAYOUT_filename_path"
					# try to download file
					dirname_bin2flash=$(dirname $bin2flash)
					if [ "$dirname_bin2flash" != '.' ];
					then
						mkdir -p $dirname_bin2flash
					fi
					if [ "$bin2flash" != "none" ];
					then
						echo "[Try to Download]: wget $FLASHLAYOUT_uri/$bin2flash -O $FLASHLAYOUT_prefix_image_path/$bin2flash"
						wget $FLASHLAYOUT_uri/$bin2flash -O $FLASHLAYOUT_prefix_image_path/$bin2flash 2> /dev/null
						ret=$?
						if [ $ret -ne 0 ];
						then
							# clean last wget request
							rm -f $bin2flash
							# try to get tge xz version
							echo "[Try to Download]: wget $FLASHLAYOUT_uri/$bin2flash -O $FLASHLAYOUT_prefix_image_path/$bin2flash.xz"
							wget $FLASHLAYOUT_uri/$bin2flash.xz -O $FLASHLAYOUT_prefix_image_path/$bin2flash.xz
							ret=$?
							if [ $ret -ne 0 ];
							then
								echo "[ERROR] couldn't download $bin2flash on $FLASHLAYOUT_uri/"
								exit 0
							else
								gunzip $FLASHLAYOUT_prefix_image_path/$bin2flash.xz
							fi
						fi
					fi
				fi
			fi
			;;
		*)
			;;
		esac
	done
	# modules
	if [ ! -f $FLASHLAYOUT_module ];
	then
		if [ -z $FLASHLAYOUT_uri ];
		then
			echo "[ERROR] couldn't download $FLASHLAYOUT_module because URI are not declared"
			exit 0
		else
			echo "[Try to Download]: wget $FLASHLAYOUT_uri/$FLASHLAYOUT_module -O $FLASHLAYOUT_prefix_image_path/$FLASHLAYOUT_module"
			wget $FLASHLAYOUT_uri/$FLASHLAYOUT_module -O $FLASHLAYOUT_module 2> /dev/null
			ret=$?
			if [ $ret -ne 0 ];
			then
				echo "[ERROR] couldn't download $FLASHLAYOUT_module on $FLASHLAYOUT_uri/"
				exit 0
			fi

			echo "[Try to Download]: wget $FLASHLAYOUT_uri/$FLASHLAYOUT_kernel -O $FLASHLAYOUT_prefix_image_path/$FLASHLAYOUT_kernel"
			wget $FLASHLAYOUT_uri/$FLASHLAYOUT_kernel -O $FLASHLAYOUT_kernel 2> /dev/null
			ret=$?
			if [ $ret -ne 0 ];
			then
				echo "[ERROR] couldn't download $FLASHLAYOUT_kernel on $FLASHLAYOUT_uri/"
				exit 0
			fi

			if [ "$FLASHLAYOUT_dtb" != "none" ];
			then
				echo "[Try to Download]: wget $FLASHLAYOUT_uri/$FLASHLAYOUT_dtb -O $FLASHLAYOUT_prefix_image_path/$FLASHLAYOUT_dtb"
				wget $FLASHLAYOUT_uri/$FLASHLAYOUT_dtb -O $FLASHLAYOUT_dtb 2> /dev/null
				ret=$?
				if [ $ret -ne 0 ];
				then
					echo "[ERROR] couldn't download $FLASHLAYOUT_dtb on $FLASHLAYOUT_uri/"
					exit 0
				fi
			fi
		fi
	fi

	#generate rootfs filesystem if not exist
	if [ ! -z "$root_image" ];
	then
		echo $root_image | grep -q tar;
		is_tar=$?
		if [ $is_tar -eq 0 ];
		then
			tarball=$(ls -1 $FLASHLAYOUT_filename_path/$root_image | head -n 1)
			generate_rootfs_from_tarball $tarball
		fi
	fi
}

# -------------------------------
# calculate number of parition enable
function calculate_number_of_partition() {
	num=0
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			num=$(($num+1))
		fi
	done

	echo "$num"
}

# ----------------------------------------
function generate_gpt_partition_table_from_flash_layout() {
	local j=1
	local p=0
	local index_of_rootfs=20
	new_next_partition_offset_b=0
	number_of_partition=$( calculate_number_of_partition )

	exec_print "sgdisk -og -a 1 $FLASHLAYOUT_rawname"

	echo "Create partition table:"

	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		partType=${FLASHLAYOUT_data[$i,$COL_PARTYPE]}
		offset=${FLASHLAYOUT_data[$i,$COL_OFFSET]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}
		debug "DUMP Process for $partName partition"

		case "$selected" in
		P|E)
			# partition are present and must be created
			;;
		*)
			continue
			;;
		esac
		# add boot flags on gpt partition
		if [ "$partType" == 'System' ];
		then
			bootfs_param=" -A $j:set:2"
		else
			bootfs_param=""
		fi

		# get size of image to put on partition
		if [ -n "$bin2flash" ];
		then
			if [ -e $FLASHLAYOUT_prefix_image_path/$bin2flash ];
			then
				image_size=$(du -Lb $FLASHLAYOUT_prefix_image_path/$bin2flash | tr '\t' ' ' | cut -d ' ' -f1)
				image_size_in_mb=$((image_size/1024/1024))
			else
				image_size=0
				image_size_in_mb=0
			fi
		else
			image_size=0
			image_size_in_mb=0
		fi

		# get offset
		offset=$(echo $offset | sed -e "s/0x//")
		offset_b=$(echo $((16#$offset)) )

		offset=$((2 * $offset_b / 1024))

		if [ $p -ne $(($number_of_partition -1)) ];
		then
			# get the begin offset of next partition
			next_offset=${FLASHLAYOUT_data[$(($i+1)),$COL_OFFSET]}
			next_offset=$(echo $next_offset | sed -e "s/0x//")
			next_offset_b=$(echo $((16#$next_offset)))

			next_offset=$((2 * $next_offset_b / 1024))
			next_offset=$(($next_offset -1))
			if [ $next_offset -eq -1 ];
			then
			next_offset=" "
			next_offset_b="0"
			fi
		else
			next_offset=" "
			next_offset_b="0"
		fi

		# calculate the size of partition
		partition_size=$(($next_offset_b - $offset_b))
		if [ $partition_size -lt 0 ];
		then
			partition_size=0
		fi

		if [ $i -ne $(($FLASHLAYOUT_number_of_line -1)) ];
		then
			free_size=$(($partition_size - $image_size))
		else
			free_size=0
			partition_size=0
		fi

		debug "   DUMP selected  $selected"
		debug "   DUMP partName  $partName"
		#debug "   DUMP partType  $partType"
		debug "   DUMP offset    ${FLASHLAYOUT_data[$i,$COL_OFFSET]} ($offset)"
		#debug "   DUMP bin2flash $bin2flash"
		debug "   DUMP image size     $image_size"
		debug "   DUMP partition size $partition_size"
		debug "   DUMP free size      $free_size "
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			if [ $free_size -lt 0 ];
			then
				echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
				echo "[ERROR]: IMAGE TOO BIG [$partName:$bin2flash $image_size_in_mb MB [requested $partition_size B]"
				echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
				exit 1
			fi

			if [ $p -eq $(($number_of_partition -1)) ];
			then
				temp_end_offset_b=$(($offset_b + $image_size))

				if [ $temp_end_offset_b -gt $(($DEFAULT_RAW_SIZE * 1024*1024)) ];
				then
					echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
					echo "[ERROR]: IMAGE TOO BIG [$partName:$bin2flash $image_size_in_mb MB]"
					echo "[ERROR]: There is not enough place on last partition($partName)"
					echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
					exit 1
				fi
			fi
			if [ "$partType" == "Binary" ];
			then
				# Linux reserved: 0x8301
				gpt_code="8301"
			else
				# Linux File system: 0x8300
				gpt_code="8300"
			fi

			printf "part %d: %8s ..." $j "$partName"
			exec_print "sgdisk -a 1 -n $j:$offset:$next_offset -c $j:$partName -t $j:$gpt_code $bootfs_param $FLASHLAYOUT_rawname"
			partition_size=$(sgdisk -p $FLASHLAYOUT_rawname | grep $partName | awk '{ print $4}')
			partition_size_type=$(sgdisk -p $FLASHLAYOUT_rawname | grep $partName | awk '{ print $5}')
			printf "\r[CREATED] part %d: %8s [partition size %s %s]\n" $j "$partName"  "$partition_size" "$partition_size_type"

			j=$(($j+1))
		fi
		p=$(($p+1))
	done

	echo ""
	echo "Partition table from $FLASHLAYOUT_rawname"
	sudo sgdisk -p $FLASHLAYOUT_rawname
	echo ""
}

function generate_empty_raw_image() {
	# Initialize image file (due to bs we force seek on K)
	echo "Create Raw empty image: $FLASHLAYOUT_rawname of ${DEFAULT_RAW_SIZE}MB"
	exec_print "dd if=/dev/zero of=$FLASHLAYOUT_rawname bs=1024 count=0 seek=${DEFAULT_RAW_SIZE}K"
}

function populate_gpt_partition_table_from_flash_layout() {
	local i=1
	local j=1
	echo "Populate raw image with image content:"

	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		#debug "DUMP LINE=${FLASHLAYOUT_data[$i]}"
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		partType=${FLASHLAYOUT_data[$i,$COL_PARTYPE]}
		offset=${FLASHLAYOUT_data[$i,$COL_OFFSET]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}

		offset=$(echo $offset | sed -e "s/0x//")
		offset=$(echo $((16#$offset)))

		debug "   DUMP $selected $partName $partType"
		#debug "   DUMP selected  $selected"
		#debug "   DUMP partName  $partName"
		#debug "   DUMP partType  $partType"
		#debug "   DUMP ip        $ip"
		#debug "   DUMP offset    $offset "
		#debug "   DUMP bin2flash $bin2flash"
		if [ "$selected" == "P" ];
		then
			# Populate only the partition in "P"
			if [ -e $FLASHLAYOUT_prefix_image_path/$bin2flash ];
			then
				printf "part %d: %8s, image: %s ..." $j "$partName" "$bin2flash"
				exec_print "dd if=$FLASHLAYOUT_prefix_image_path/$bin2flash of=$FLASHLAYOUT_rawname conv=fdatasync,notrunc seek=1 bs=$offset"
				printf "\r[ FILLED ] part %d: %8s, image: %s \n" $j "$partName" "$bin2flash"
			else
				printf "\r[UNFILLED] part %d: %8s, image: %s (not present) \n" $j "$partName" "$bin2flash"
				echo "   [WARNING]: THE FILE $FLASHLAYOUT_prefix_image_path/$bin2flash ARE NOT PRESENT."
				echo "   [WARNING]: THE PARTITION $partName ARE NOT FILL."
				WARNING_TEXT+="[WARNING]: THE PARTITION $partName ARE NOT FILL (file $FLASHLAYOUT_prefix_image_path/$bin2flash are not present) #"
			fi
			j=$(($j+1))
		else
			if [ "$selected" == "E" ];
			then
				printf "\r[UNFILLED] part %d: %8s, \n" $j "$partName"
				j=$(($j+1))
			fi
		fi

	done
}

# ----------------------------------------
# ----------------------------------------
function print_shema_on_infofile() {
	local j=1
	local i=1
	# print schema of partition

	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo -n "=============" >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname

	#empty line
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo -n "=            " >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	# part name
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			printf "=  %08s  " "$partName" >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	#empty
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo -n "=            " >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	# partition number
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			printf "= %08s%-2d " "mmcblk0p" $j>> $FLASHLAYOUT_infoname
			j=$(($j+1))
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	j=1
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			printf "=    (%-2d)    " $j>> $FLASHLAYOUT_infoname
			j=$(($j+1))
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo -n "=            " >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo -n "=============" >> $FLASHLAYOUT_infoname
		fi
	done
	echo "=" >> $FLASHLAYOUT_infoname
	# print legend of partition
	j=1
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			echo "($j):" >> $FLASHLAYOUT_infoname
			echo "    Device: /dev/mmcblk0p$j" >> $FLASHLAYOUT_infoname
			echo "    Label:  $partName" >> $FLASHLAYOUT_infoname
			if [ -n "$bin2flash" ];
			then
				echo "    Image:  $bin2flash" >> $FLASHLAYOUT_infoname
			else
				echo "    Image:" >> $FLASHLAYOUT_infoname
			fi
			j=$(($j+1))
		fi
	done
}

function print_populate_on_infofile() {
	local j=1
	for i in $(seq 0 $FLASHLAYOUT_number_of_line)
	do
		selected=${FLASHLAYOUT_data[$i,$COL_SELECTED_OPT]}
		partName=${FLASHLAYOUT_data[$i,$COL_PARTNAME]}
		bin2flash=${FLASHLAYOUT_data[$i,$COL_BIN2FLASH]}
		if [ "$selected" == "P" ] || [ "$selected" == "E" ];
		then
			if [ "$selected" == "E" ];
			then
				echo "- Populate partition $partName (/dev/mmcblk0p$j)" >> $FLASHLAYOUT_infoname
				if [ -n "$bin2flash" ];
				then
					echo "    dd if=$bin2flash of=/dev/mmcblk0p$j bs=1M conv=fdatasync status=progress" >> $FLASHLAYOUT_infoname
				else
					echo "    dd if=<raw image of $partName> of=/dev/mmcblk0p$j bs=1M conv=fdatasync status=progress" >> $FLASHLAYOUT_infoname
				fi
			else
				echo "- Populate partition $partName (/dev/mmcblk0p$j)" >> $FLASHLAYOUT_infoname
				echo "    dd if=$bin2flash of=/dev/mmcblk0p$j bs=1M conv=fdatasync status=progress" >> $FLASHLAYOUT_infoname
			fi
			echo "" >> $FLASHLAYOUT_infoname
			j=$(($j+1))
		fi
	done
}

function create_info() {

cat > $FLASHLAYOUT_infoname  << EOF
This file describe How to update manually the partition of SDCARD:
1. SDCARD schema of partition
2. How to populate each partition
3. How to update the kernel/devicetree

1. SDCARD schema of partition:
------------------------------

EOF
print_shema_on_infofile

cat >> $FLASHLAYOUT_infoname  << EOF

2. How to populate each partition
---------------------------------
EOF

print_populate_on_infofile

cat >> $FLASHLAYOUT_infoname  << EOF

3. How to update the kernel/devicetree
--------------------------------------
The kernel and devicetree are present on "rootfs" partition.
To change kernel and devicetree, you can copy the file on this partitions:
- plug SDCARD on your PC
- copy kernel uImage on SDCARD
   sudo cp uImage /media/\$USER/rootfs/boot/
- copy devicetree uImage on SDCARD
   sudo cp stm32mp1*.dtb /media/\$USER/rootfs/boot/
- umount partitions of SDCARD
   sudo umount /media/\$USER/rootfs/
   (dont't forget to umount the other partitions of SDCARD:
   sudo umount \`lsblk --list | grep mmcblk0 | grep part | gawk '{ print \$7 }' | tr '\\n' ' '\`
   )

EOF

}
# ----------------------------------------
# ----------------------------------------

function print_info() {
	echo ""
	echo "###########################################################################"
	echo "###########################################################################"
	echo ""
	echo "RAW IMAGE generated: $FLASHLAYOUT_rawname"
	echo ""
	echo "WARNING: before to use the command dd, please umount all the partitions"
	echo "	associated to SDCARD."
	echo "    sudo umount \`lsblk --list | grep mmcblk0 | grep part | gawk '{ print \$7 }' | tr '\\n' ' '\`"
	echo ""
	echo "To put this raw image on sdcard:"
	echo "    sudo dd if=$FLASHLAYOUT_rawname of=/dev/mmcblk0 bs=8M conv=fdatasync status=progress"
	echo ""
	echo "(mmcblk0 can be replaced by:"
	echo "     sdX if it's a device dedicated to receive the raw image "
	echo "          (where X can be a, b, c, d, e)"
	echo ""
	echo "###########################################################################"
	echo "###########################################################################"
}

function print_warning() {
	if [ -n "$WARNING_TEXT" ];
	then
		echo ""
		echo "???????????????????????????????????????????????????????????????????????????"
		echo "???????????????????????????????????????????????????????????????????????????"
		for t in "`echo $WARNING_TEXT | tr '#' '\n'`";
		do
			echo "$t"
		done
		echo "[WARNING]: IT'S POSSIBLE, THE BOARD DOES NOT BOOT CORRECTLY DUE TO "
		echo "           FILE(s) NOT PRESENT."
		echo "???????????????????????????????????????????????????????????????????????????"
		echo "???????????????????????????????????????????????????????????????????????????"
	fi
}

function usage() {
	echo ""
	echo "Help:"
	echo "   $0 <FlashLayout file>"
	echo ""
	exit 1
}
# ------------------
#        Main
# ------------------
# check opt args
if [ $# -ne 1 ];
then
	echo "[ERROR]: bad number of parameters"
	echo ""
	usage
else
	FLASHLAYOUT_filename=$1
	FLASHLAYOUT_filename_path=$(dirname $FLASHLAYOUT_filename)
	FLASHLAYOUT_filename_name=$(basename $FLASHLAYOUT_filename)
	FLASHLAYOUT_dirname=$(basename $FLASHLAYOUT_filename_path)

	_extension="${FLASHLAYOUT_filename##*.}"
	if [ ! "$_extension" == "fld" ];
	then
		echo ""
		echo "[ERROR]: bad extension of Flashlayout file."
		echo "[ERROR]: the flashlayout must have a fld extension (Flash Layout Description)."
		usage
	fi
	# File have a correct extension
	#
	if echo $FLASHLAYOUT_dirname | grep -q flashlayout
	then
		# add directory name as prefix for raw image
		new_filename=$(echo "$FLASHLAYOUT_dirname/$FLASHLAYOUT_filename_name" | sed -e "s|/|_|g")
		filename_for_raw_to_use="$FLASHLAYOUT_filename_path/$new_filename"
	else
		filename_for_raw_to_use=$FLASHLAYOUT_filename
	fi
	FLASHLAYOUT_rawname=$(basename $filename_for_raw_to_use | sed -e "s/fld/raw/")
	FLASHLAYOUT_infoname=$(basename $filename_for_raw_to_use | sed -e "s/fld/how_to_update.txt/")

	read_flash_layout
	#debug_dump_flashlayout_data_array
	get_last_image_path

	#put the raw image generate near the binaries images
	FLASHLAYOUT_rawname=$FLASHLAYOUT_prefix_image_path/$FLASHLAYOUT_rawname
	FLASHLAYOUT_infoname=$FLASHLAYOUT_prefix_image_path/$FLASHLAYOUT_infoname

	# erase previous raw image
	if [ -f $FLASHLAYOUT_rawname ];
	then
		echo ""
		echo "[WARNING]: A previous raw image are present on this directory"
		echo "[WARNING]:    $FLASHLAYOUT_rawname"
		echo "[WARNING]: would you like to erase it: [Y/n]"
		read answer
		if [[ "$answer" =~ ^[Yy]+[ESes]* ]]; then
			rm -f $FLASHLAYOUT_rawname $FLASHLAYOUT_infoname
		fi
	fi

	debug "DUMP FlashLayout name:      $FLASHLAYOUT_filename"
	debug "DUMP FlashLayout dir path:  $FLASHLAYOUT_filename_path"
	debug "DUMP images dir path:       $FLASHLAYOUT_prefix_image_path"
	debug "DUMP RAW SDCARD image name: $FLASHLAYOUT_rawname"
fi

generate_empty_raw_image
generate_gpt_partition_table_from_flash_layout
populate_gpt_partition_table_from_flash_layout

create_info
print_info
print_warning
