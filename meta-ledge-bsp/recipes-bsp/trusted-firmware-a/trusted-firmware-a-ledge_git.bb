SUMMARY = "ARM Trusted Firmware"
DESCRIPTION = "ARM Trusted Firmware provides a reference implementation of \
Secure World software for ARMv8-A, including Exception Level 3 (EL3) software. \
It provides implementations of various ARM interface standards such as the \
Power State Coordination Interface (PSCI), Trusted Board Boot Requirements \
(TBBR) and Secure monitor code."
HOMEPAGE = "http://infocenter.arm.com/help/topic/com.arm.doc.dui0928e/CJHIDGJF.html"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PV = "2.4"

SRC_URI = "git://github.com/ARM-software/arm-trusted-firmware.git;protocol=https;nobranch=1"
SRCREV = "e2c509a39c6cc4dda8734e6509cdbe6e3603cdfc"

SRC_URI += "file://0001-plat-qemu-trigger-reboot-with-secure-pl061.patch"

ALLOW_EMPTY_${PN} = "1"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit deploy

DEPENDS += "dtc-native"
DEPENDS_ledge-qemuarm += " optee-os virtual/bootloader "
DEPENDS_ledge-qemuarm64 += " optee-os virtual/bootloader "

# ledge-stm32mp157c-dk2 specific
TF_A_PLATFORM_ledge-stm32mp157c-dk2 = "stm32mp1"
TF_A_DEVICETREE_ledge-stm32mp157c-dk2 = "stm32mp157c-dk2"

# ledge-qemuarm specific
TF_A_PLATFORM_ledge-qemuarm = "qemu"

# ledge-qemuarm64 specific
TF_A_PLATFORM_ledge-qemuarm64 = "qemu"

# Extra make settings
EXTRA_OEMAKE = ' CROSS_COMPILE=${TARGET_PREFIX} '
EXTRA_OEMAKE += ' PLAT=${TF_A_PLATFORM} '
EXTRA_OEMAKE_append_armv7a = ' ARCH=aarch32 ARM_ARCH_MAJOR=7 '
EXTRA_OEMAKE_append_armv7ve = ' ARCH=aarch32 ARM_ARCH_MAJOR=7 '
EXTRA_OEMAKE_append_ledge-stm32mp157c-dk2 = "AARCH32_SP=optee STM32MP_SDMMC=1 STM32MP_EMMC=1"
EXTRA_OEMAKE_append_ledge-qemuarm = ' AARCH32_SP=optee ARM_TSP_RAM_LOCATION=tdram BL32_RAM_LOCATION=tdram '
EXTRA_OEMAKE_append_aarch64 = "${@bb.utils.contains('MACHINE_FEATURES', 'optee', ' SPD=opteed ', '', d)}"

# FIP image
EXTRA_OEMAKE_append_ledge-qemuarm = 'BL32=${STAGING_DIR_TARGET}/lib/firmware/tee-header_v2.bin BL32_EXTRA1=${STAGING_DIR_TARGET}/lib/firmware/tee-pager_v2.bin BL32_EXTRA2=${STAGING_DIR_TARGET}/lib/firmware/tee-pageable_v2.bin  BL33=${DEPLOY_DIR_IMAGE}/u-boot.bin fip GENERATE_COT=1 BL32_RAM_LOCATION=tdram '
EXTRA_OEMAKE_append_ledge-qemuarm64 = 'BL32=${STAGING_DIR_TARGET}/lib/firmware/tee-header_v2.bin BL32_EXTRA1=${STAGING_DIR_TARGET}/lib/firmware/tee-pager_v2.bin BL32_EXTRA2=${STAGING_DIR_TARGET}/lib/firmware/tee-pageable_v2.bin  BL33=${DEPLOY_DIR_IMAGE}/u-boot.bin fip GENERATE_COT=1 BL32_RAM_LOCATION=tdram '

# Debug support
EXTRA_OEMAKE += 'DEBUG=1'
EXTRA_OEMAKE += "LOG_LEVEL=40"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure[noexec] = "1"

do_compile() {
    oe_runmake -C ${S} BUILD_PLAT=${B}/${config} all
}
do_compile_ledge-stm32mp157c-dk2() {
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            oe_runmake -C ${S} DTB_FILE_NAME=${dt}.dtb BUILD_PLAT=${B} all
        done
    else
            oe_runmake -C ${S} BUILD_PLAT=${B} all
    fi
}

do_install() {
    install -d ${D}/boot
    if [ -f ${B}/bl1.bin ] ; then
        install -m 0644 ${B}/bl1.bin ${D}/boot/
        install -m 0644 ${B}/bl1/bl1.elf ${D}/boot/
    fi
    if [ -f ${B}/bl2.bin ] ; then
        install -m 0644 ${B}/bl2.bin ${D}/boot/
        install -m 0644 ${B}/bl2/bl2.elf ${D}/boot/
    fi
    if [ -f ${B}/bl3.bin ] ; then
        install -m 0644 ${B}/bl3.bin ${D}/boot/
        install -m 0644 ${B}/bl3/bl3.elf ${D}/boot/
    fi
    if [ -f ${B}/bl31.bin ] ; then
        install -m 0644 ${B}/bl31.bin ${D}/boot/
        install -m 0644 ${B}/bl31/bl31.elf ${D}/boot/
    fi
    if [ -f ${B}/bl33.bin ] ; then
        install -m 0644 ${B}/bl32.bin ${D}/boot/
        install -m 0644 ${B}/bl32/bl32.elf ${D}/boot/
    fi
}

do_install_ledge-qemuarm() {
	echo "nothing"
}

do_install_ledge-qemuarm64() {
	echo "nothing"
}

do_deploy() {
    install -d ${DEPLOYDIR}/arm-trusted-firmware
    if [ -f ${B}/bl1.bin ] ; then
        install -m 0644 ${B}/bl1.bin ${DEPLOYDIR}/arm-trusted-firmware/
        install -m 0644 ${B}/bl1/bl1.elf ${DEPLOYDIR}/arm-trusted-firmware/
    fi
    if [ -f ${B}/bl2.bin ] ; then
        install -m 0644 ${B}/bl2.bin ${DEPLOYDIR}/arm-trusted-firmware/
        install -m 0644 ${B}/bl2/bl2.elf ${DEPLOYDIR}/arm-trusted-firmware/
    fi
    if [ -f ${B}/bl3.bin ] ; then
        install -m 0644 ${B}/bl3.bin ${DEPLOYDIR}/arm-trusted-firmware/
        install -m 0644 ${B}/bl3/bl3.elf ${DEPLOYDIR}/arm-trusted-firmware/
    fi
    if [ -f ${B}/bl31.bin ] ; then
        install -m 0644 ${B}/bl31.bin ${DEPLOYDIR}/arm-trusted-firmware/
        install -m 0644 ${B}/bl31/bl31.elf ${DEPLOYDIR}/arm-trusted-firmware/
    fi
    if [ -f ${B}/bl33.bin ] ; then
        install -m 0644 ${B}/bl32.bin ${DEPLOYDIR}/arm-trusted-firmware/
        install -m 0644 ${B}/bl32/bl32.elf ${DEPLOYDIR}/arm-trusted-firmware/
    fi

    if [ -f ${B}/fip.bin ] ; then
        install -m 0644 ${B}/fip.bin ${DEPLOYDIR}/
        dd if=/dev/zero of=${DEPLOYDIR}/firmware.uefi.uboot.bin count=131072
        dd if=${B}/bl1.bin of=${DEPLOYDIR}/firmware.uefi.uboot.bin bs=4096 conv=notrunc
        dd if=${B}/fip.bin of=${DEPLOYDIR}/firmware.uefi.uboot.bin seek=64 bs=4096 conv=notrunc
        ln -sf firmware.uefi.uboot.bin ${DEPLOYDIR}/firmware.bin
    fi
}

do_deploy_append_ledge-qemuarm() {
    cd ${DEPLOYDIR}
    ln -sf arm-trusted-firmware/bl1.bin  bl1.bin
    ln -sf arm-trusted-firmware/bl2.bin  bl2.bin
    cd -
}

do_deploy_append_ledge-qemuarm64() {
    cd ${DEPLOYDIR}
    ln -sf arm-trusted-firmware/bl1.bin  bl1.bin
    ln -sf arm-trusted-firmware/bl2.bin  bl2.bin
    ln -sf arm-trusted-firmware/bl31.bin bl31.bin
    cd -
}

do_deploy_append_ledge-stm32mp157c-dk2() {
    if [ -n "${TF_A_DEVICETREE}" ]; then
        for dt in ${TF_A_DEVICETREE}; do
            install -m 644 ${B}/tf-a-${dt}.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
        done
    else
        # Get tf-a binary basename to copy
        tf_a_binary_basename=$(find ${B}/ -name "tf-a-*.stm32" -exec basename {} \; | sed 's|\.stm32||g')
        install -m 644 ${B}/${tf_a_binary_basename}.stm32 ${DEPLOYDIR}/arm-trusted-firmware/
    fi
}

addtask deploy before do_build after do_compile

FILES_${PN} = "/boot"

