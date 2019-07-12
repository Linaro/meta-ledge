inherit image_types
#
# Create an image that can by written onto a SD card or USB key using dd.
#
# this class use the scipr provided by raw-tool package (create_raw_from_flashlayout.sh)
# and a flashlayout.
# The Flashlayout are specified on machine via variable LEDGE_RAW_FLASHLAYOUTS
# (it can have several files specified on this variables)
do_image_ledgeraw[depends] += " \
        parted-native:do_populate_sysroot \
        mtools-native:do_populate_sysroot \
        e2fsprogs-native:do_populate_sysroot \
        gptfdisk-native:do_populate_sysroot \
        dosfstools-native:do_populate_sysroot \
        coreutils-native:do_populate_sysroot \
        virtual/bootloader:do_deploy \
        raw-tools-native:do_deploy \
        ledge-flashlayout:do_deploy \
        "

# This image depends on the rootfs image
IMAGE_TYPEDEP_ledgeraw_append = " ext4 "


IMAGE_CMD_ledgeraw () {
    cd ${DEPLOY_DIR_IMAGE};

    for f in ${LEDGE_RAW_FLASHER_TSV};
    do
        name=$(echo $f | sed "s/tsv\.template/${IMAGE_LINK_NAME}\.tsv/")
        sed "s/%%IMAGE%/${IMAGE_LINK_NAME}.ext4/" $f > $name
    done
    for f in $(ls -1 *.fld);
    do
        sed -i "s/%%IMAGE%/${IMAGE_LINK_NAME}.ext4/" $f
    done

    for f in ${LEDGE_RAW_FLASHLAYOUTS};
    do
        yes 'yes' | ${DEPLOY_DIR_IMAGE}/scripts/create_raw_from_flashlayout.sh $f

        # get suffix
        binaryname=$(echo $f | cut -d'.' -f1)
        cd ${DEPLOY_DIR_IMAGE};gzip -f -9 $binaryname.raw
        #cd ${DEPLOY_DIR_IMAGE};gzip -f -9 -c $binaryname.raw > $binaryname.raw.gz
    done
}
