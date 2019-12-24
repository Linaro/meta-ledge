DESCRIPTION = "Linux Kernel uefi keys"
LICENSE = "GPLv2"
SECTION = "kernel"

# for mkfs.ext4
DEPENDS += "e2fsprogs-native"
DEPENDS += "efitools-native"
DEPENDS += "coreutils-native"

DEPENDS += "linux-ledge"
DEPENDS += "virtual/kernel"

SRC_URI_append = " file://uefi-certificates/KEK.auth "
SRC_URI_append = " file://uefi-certificates/KEK.crt "
SRC_URI_append = " file://uefi-certificates/KEK.key "
SRC_URI_append = " file://uefi-certificates/PK.auth "

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit deploy native

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
ALLOW_EMPTY_${PN} = "1"

do_deploy() {
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

	KERNEL_IMAGE="${DEPLOY_DIR_IMAGE}/${img}"

	${bindir}/hash-to-efi-sig-list ${KERNEL_IMAGE} kernel.hash
	${bindir}/sign-efi-sig-list -c ${WORKDIR}/uefi-certificates/KEK.crt -k ${WORKDIR}/uefi-certificates/KEK.key db kernel.hash kernel.auth
	mkdir -p certimage
	cp ${WORKDIR}/uefi-certificates/PK.auth ${WORKDIR}/uefi-certificates/KEK.auth kernel.auth ./certimage
	truncate -s 4M certimage.ext4
	${base_sbindir}/mkfs.ext4 certimage.ext4 -d ./certimage
	rm -rf ./certimage kernel.hash
	mv certimage.ext4 ${DEPLOYDIR}/ledge-kernel-uefi-certs.ext4
}

addtask deploy before do_build after do_unpack
