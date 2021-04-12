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
	file://RELEASEAARCH64_QEMU_EFI.fd.gz \
	file://LEDGE_AARCH64_QEMU_VARS.fd.gz \
	file://RELEASEARM_QEMU_EFI.fd.gz \
	file://LEDGE_ARM_QEMU_VARS.fd.gz \
	file://LEDGE_RELEASEX64_OVMF.fd.gz \
	"

do_deploy() {
	mkdir -p ${D}/boot/EFI/BOOT
}

do_deploy_append_ledge-qemuarm64() {
    dd if=/dev/zero bs=1M count=64 of=${DEPLOYDIR}/firmware.uefi.edk2.bin
    dd if=${B}/RELEASEAARCH64_QEMU_EFI.fd  bs=1M of=${DEPLOYDIR}/firmware.uefi.edk2.bin conv=notrunc
    install -m 0644  ${B}/LEDGE_AARCH64_QEMU_VARS.fd ${DEPLOYDIR}/LEDGE_AARCH64_QEMU_VARS.bin
}

do_deploy_append_ledge-qemuarm() {
    dd if=/dev/zero bs=1M count=64 of=${DEPLOYDIR}/firmware.uefi.edk2.bin
    dd if=${B}/RELEASEARM_QEMU_EFI.fd bs=1M of=${DEPLOYDIR}/firmware.uefi.edk2.bin conv=notrunc
    install -m 0644 ${B}/LEDGE_ARM_QEMU_VARS.fd ${DEPLOYDIR}/LEDGE_ARM_QEMU_VARS.bin
}

do_deploy_append_ledge-qemux86-64() {
    install -m 0644 ${B}/LEDGE_RELEASEX64_OVMF.fd ${DEPLOYDIR}/firmware.uefi.edk2.bin
}

addtask deploy after do_install

FILES_${PN} = "/boot/"
