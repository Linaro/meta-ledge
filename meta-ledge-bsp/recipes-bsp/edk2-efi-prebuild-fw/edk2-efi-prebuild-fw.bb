SUMMARY = "Prebuilt EDK2 Firmware"
DESCRIPTION = "Package provides prebuilt EDK2 frimware for LEDGE targets."
HOMEPAGE = "https://retrage.github.io/edk2-nightly/"

PACKAGE_ARCH = "${MACHINE_ARCH}"

ALLOW_EMPTY_${PN} = "1"

LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

# https://retrage.github.io/edk2-nightly/
PV="edk2-stable202008"

inherit deploy

S = "${WORKDIR}/"

SRC_URI = "\
	file://RELEASEAARCH64_QEMU_EFI.fd \
	file://RELEASEAARCH64_QEMU_VARS.fd \
	file://RELEASEAARCH64_Shell.efi \
	file://RELEASEARM_QEMU_EFI.fd \
	file://RELEASEARM_QEMU_VARS.fd \
	file://RELEASEARM_Shell.efi \
	file://RELEASEX64_OVMF.fd \
	file://RELEASEX64_OVMF_CODE.fd \
	file://RELEASEX64_OVMF_VARS.fd \
	file://RELEASEX64_Shell.efi \
	"


python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    if target == "x86_64":
        efi_shell = "bootx64.efi"
        kernel_efi_image = "kernel-bootx64.efi"
    elif re.match('i.86', target):
        efi_shell = "bootia32.efi"
        kernel_efi_image = "kernel-bootia32.efi"
    elif re.match('aarch64', target):
        efi_shell = "bootaa64.efi"
        kernel_efi_image = "kernel-bootaa64.efi"
    elif re.match('arm', target):
        efi_shell = "bootarm.efi"
        kernel_efi_image = "kernel-bootarm.efi"
    else:
        raise bb.parse.SkipRecipe("kernel efi is incompatible with target %s" % target)
    d.setVar("KERNEL_EFI_IMAGE", kernel_efi_image)
    d.setVar("EFI_SHELL", efi_shell)
}

do_deploy() {
	mkdir -p ${D}/boot/EFI/BOOT
	mkdir -p ${DEPLOYDIR}
	echo "initrd ledge-initramfs.rootfs.cpio.gz" > ${D}/boot/startup.nsh
	echo "${KERNEL_EFI_IMAGE} rootwait initrd=/ledge-initramfs.rootfs.cpio.gz root=UUID=6091b3a4-ce08-3020-93a6-f755a22ef03b console=ttyS2,115200 console=ttyS0,115200 console=ttyAMA0,115200  " >> ${D}/boot/startup.nsh
	cp ${D}/boot/startup.nsh ${DEPLOYDIR}/
}

do_deploy_append_ledge-qemuarm64() {
	install -m 0644 ${B}/RELEASEAARCH64_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEAARCH64_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
	install -m 0644 ${B}/RELEASEAARCH64_QEMU_EFI.fd ${DEPLOYDIR}/firmware.uefi-edk2.bin
}

do_deploy_append_synquacer() {
	install -m 0644 ${B}/RELEASEAARCH64_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEAARCH64_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
}

do_deploy_append_ledge-qemuarm() {
        install -m 0644 ${B}/RELEASEARM_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEARM_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
	install -m 0644 ${B}/RELEASEARM_QEMU_EFI.fd ${DEPLOYDIR}/firmware.uefi-edk2.bin
}

do_deploy_append_ledge-stm32mp157c-dk2() {
	install -m 0644 ${B}/RELEASEARM_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEARM_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
}

do_deploy_append_ledge-ti-am572x() {
	install -m 0644 ${B}/RELEASEARM_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEARM_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
}

do_deploy_append_ledge-qemux86-64() {
        install -m 0644 ${B}/RELEASEX64_Shell.efi ${D}/boot/EFI/BOOT/${EFI_SHELL}
	install -m 0644 ${B}/RELEASEX64_Shell.efi ${DEPLOYDIR}/edk2_shell.efi
	install -m 0644 ${B}/RELEASEX64_OVMF.fd ${DEPLOYDIR}/firmware.uefi-edk2.bin
}

addtask deploy after do_install

FILES_${PN} = "/boot/"
