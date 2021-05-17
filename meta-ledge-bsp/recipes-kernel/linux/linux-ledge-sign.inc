DEPENDS_append += " e2fsprogs-native  efitools-native  coreutils-native "

SRC_URI_append = " file://uefi-certificates/KEK.auth "
SRC_URI_append = " file://uefi-certificates/KEK.crt "
SRC_URI_append = " file://uefi-certificates/KEK.key "
SRC_URI_append = " file://uefi-certificates/PK.auth "
SRC_URI_append = " file://uefi-certificates/db.key "
SRC_URI_append = " file://uefi-certificates/db.crt "
SRC_URI_append = " file://uefi-certificates/db.auth "

inherit kernel siteinfo

do_compile_append() {
	if [ ${TUNE_ARCH} != "x86_64" ]; then
		KERNEL_IMAGE=`find ${B} -name ${KERNEL_IMAGETYPE}`
		${STAGING_BINDIR_NATIVE}/sbsign --key ${WORKDIR}/uefi-certificates/db.key --cert ${WORKDIR}/uefi-certificates/db.crt ${KERNEL_IMAGE} --output  ${KERNEL_IMAGETYPE}.signed
		cp ${KERNEL_IMAGETYPE}.signed ${KERNEL_IMAGE}
	fi
}

do_deploy_append() {
	if [ ${TUNE_ARCH} != "x86_64" ]; then
		install -m 0644 ${KERNEL_IMAGETYPE}.signed ${DEPLOYDIR}/
	fi
	mkdir -p certimage
	cp ${WORKDIR}/uefi-certificates/PK.auth ${WORKDIR}/uefi-certificates/KEK.auth ${WORKDIR}/uefi-certificates/db.auth ${WORKDIR}/uefi-certificates/db.crt ./certimage
	truncate -s 4M certimage.ext4
	${STAGING_DIR_NATIVE}/sbin/mkfs.ext4 certimage.ext4 -d ./certimage

	install -m 0644 certimage.ext4 ${DEPLOYDIR}/ledge-kernel-uefi-certs.ext4.img

	rm -rf ./certimage  certimage.ext4
}
