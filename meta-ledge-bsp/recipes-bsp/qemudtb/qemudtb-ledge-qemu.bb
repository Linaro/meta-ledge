SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

S = "${WORKDIR}/git"
PR = "r1"

inherit deploy

SRC_URI_append_ledge-qemuarm64 = " file://0001-qemuarm64.dts-add-ftpm-support.patch;apply=no"

do_deploy () {

}

do_deploy_append_ledge-qemuarm64() {
    qemu-system-aarch64 -nographic -machine virt,secure -cpu cortex-a57 -machine dumpdtb=virt.dtb
    dtc -I dtb -O dts virt.dtb -o virt.dts
    patch -p1 -r - < ${WORKDIR}/0001-qemuarm64.dts-add-ftpm-support.patch
    dtc -I dts -O dtb virt.dts -o ${DEPLOYDIR}/ledge-qemuarm64.dtb
    rm virt.dts virt.dts.orig virt.dtb
}

addtask deploy before do_build after do_compile

DEPENDS = "dtc-native qemu-helper-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(ledge-qemuarm|ledge-qemuarm64)"
