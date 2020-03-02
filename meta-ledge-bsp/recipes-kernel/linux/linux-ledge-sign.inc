DEPENDS_append += " e2fsprogs-native  efitools-native  coreutils-native "

SRC_URI_append = " file://uefi-certificates/KEK.auth "
SRC_URI_append = " file://uefi-certificates/KEK.crt "
SRC_URI_append = " file://uefi-certificates/KEK.key "
SRC_URI_append = " file://uefi-certificates/PK.auth "

inherit kernel siteinfo

do_deploy_append() {
       # WIC image creates efi partition with ${D}/boot/efi/boot/${KERNEL_EFI_IMAGE}
       # @todo: rethink how to sign kernel image after wic chooses one.
       for img in bzImage zImage Image; do
           if [ "x$img" = "x${KERNEL_IMAGETYPE}" ]; then
               break;
           fi
           if [ "x$img" = "x${KERNEL_ALT_IMAGETYPE}" ]; then
               break;
           fi
        done

	KERNEL_IMAGE="${D}/boot/${img}"

	${STAGING_BINDIR_NATIVE}/hash-to-efi-sig-list ${KERNEL_IMAGE} kernel.hash
	${STAGING_BINDIR_NATIVE}/sign-efi-sig-list -c ${WORKDIR}/uefi-certificates/KEK.crt -k ${WORKDIR}/uefi-certificates/KEK.key db kernel.hash kernel.auth
	mkdir -p certimage
	cp ${WORKDIR}/uefi-certificates/PK.auth ${WORKDIR}/uefi-certificates/KEK.auth kernel.auth ./certimage
	truncate -s 4M certimage.ext4
	${STAGING_DIR_NATIVE}/sbin/mkfs.ext4 certimage.ext4 -d ./certimage

	install -m 0644 certimage.ext4 ${DEPLOYDIR}/ledge-kernel-uefi-certs.ext4.img

	rm -rf ./certimage kernel.hash certimage.ext4
}
