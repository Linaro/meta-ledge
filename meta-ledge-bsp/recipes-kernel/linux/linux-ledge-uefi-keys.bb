DESCRIPTION = "Linux Kernel uefi keys"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""
DEPENDS += "virtual/kernel"

python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    if target == "x86_64":
        kernel_efi_image = "bootx64.efi"
    elif re.match('i.86', target):
        kernel_efi_image = "bootia32.efi"
    elif re.match('aarch64', target):
        kernel_efi_image = "bootaa64.efi"
    elif re.match('arm', target):
        kernel_efi_image = "bootarm.efi"
    else:
        raise bb.parse.SkipRecipe("kernel efi is incompatible with target %s" % target)
    d.setVar("KERNEL_EFI_IMAGE", kernel_efi_image)
}

do_deploy() {
	hash-to-efi-sig-list ${D}/boot/efi/boot/${KERNEL_EFI_IMAGE} kernel.hash
	sign-efi-sig-list -c ${WORKDIR}/uefi-certificates/KEK.crt -k ${WORKDIR}/uefi-certificates/KEK.key db kernel.hash kernel.auth
	mkdir -p certimage
	cp ${WORKDIR}/uefi-certificates/PK.auth ${WORKDIR}/uefi-certificates/KEK.auth kernel.auth ./certimage
	truncate -s 4M certimage.ext4
	mkfs.ext4 certimage.ext4 -d ./certimage
	rm -rf ./certimage kernel.hash
	mv certimage.ext4 ${DEPLOYDIR}/ledge-kernel-uefi-certs.ext4
}
