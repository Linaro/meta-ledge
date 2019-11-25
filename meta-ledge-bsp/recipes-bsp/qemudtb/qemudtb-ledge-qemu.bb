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
    touch dummy.wic bl1.bin
    qemu-system-aarch64 \
    -device virtio-net-pci,netdev=net0,mac=52:54:00:12:34:02 -netdev user,id=net0 \
    -drive id=disk0,file=dummy.wic,if=none,format=raw -device virtio-blk-device,drive=disk0 -show-cursor -device virtio-rng-pci \
    -monitor null -nographic -d unimp -semihosting-config enable,target=native -bios bl1.bin \
    -machine virt,secure=on -cpu cortex-a57 -m 4096 -serial mon:stdio -serial null \
    -machine dumpdtb=virt.dtb

    dtc -I dtb -O dts virt.dtb -o virt.dts
    patch -p1 -r - < ${WORKDIR}/0001-qemuarm64.dts-add-ftpm-support.patch
    dtc -I dts -O dtb virt.dts -o ${DEPLOYDIR}/ledge-qemuarm64.dtb
    rm virt.dts virt.dts.orig virt.dtb dummy.wic bl1.bin
}

addtask deploy before do_build after do_compile

DEPENDS = "dtc-native qemu-helper-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(ledge-qemuarm|ledge-qemuarm64)"
