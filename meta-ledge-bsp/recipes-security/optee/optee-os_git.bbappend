FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# 3.6
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"
PV="3.6.0+git${SRCPV}"
SRCREV = "f398d4923da875370149ffee45c963d7adb41495"

SRC_URI_append_ledge-stm32mp157c-dk2 = " file://0001-stm32mp1-BSEC-SiP-SMC-service-to-read-fuses.patch "
SRC_URI_append = " file://0001-backport-ta_bin_to_c.py-switch-to-python3.patch \
		   file://0002-scripts-ta_bin_to_c.py-remove-blank-lines.patch \
		   file://0003-core-early_ta-expose-TA-flags-in-struct-early_ta.patch \
		   file://0004-core-move-for_each_early_ta-macro-to-kernel-early_ta.patch \
		   file://0005-core-device-pta-enumerate-early-TAs.patch \
		   file://0006-pta-pseudo-optee3.7-compat.patch \
		   file://0007-debug.patch \
		   file://0008-ta-pseudo-watch-for-running-tee-supplicant.patch"

SRC_URI_append_ledge-qemuarm64 = " file://bc50d971-d4c9-42c4-82cb-343fb7f37896.stripped.elf "

DEPENDS += " dtc-native"
DEPENDS += " python3-pyelftools-native"
inherit python3native

# ledge-ti-am572x
OPTEEMACHINE_ledge-ti-am572x = "ti-am57xx"
OPTEEOUTPUTMACHINE_ledge-ti-am572x = "ti"

EXTRA_OEMAKE_remove_ledge-ti-am572x = "CFG_ARM64_core=y"
EXTRA_OEMAKE_remove_ledge-ti-am572x = "ta-targets=ta_arm64"
EXTRA_OEMAKE_append_ledge-ti-am572x = " CFG_ARM32_core=y ta-targets=ta_arm32 "
EXTRA_OEMAKE_append_ledge-ti-am572x = " CROSS_COMPILE_ta_arm32=${HOST_PREFIX} CROSS_COMPILE=${CROSS_COMPILE} "

EXTRA_OEMAKE_remove_armv7a = "CFG_ARM64_core=y"
EXTRA_OEMAKE_remove_armv7a = "ta-targets=ta_arm64"
EXTRA_OEMAKE_append_armv7a = " CFG_ARM32_core=y ta-targets=ta_arm32 "
EXTRA_OEMAKE_append_armv7a = " CROSS_COMPILE_ta_arm32=${HOST_PREFIX} CROSS_COMPILE=${CROSS_COMPILE} "

# ledge-stm32mp157c-dk2
OPTEEMACHINE_ledge-stm32mp157c-dk2 = "stm32mp1"
OPTEEOUTPUTMACHINE_ledge-stm32mp157c-dk2 = "stm32mp1"
EXTRA_OEMAKE_append_ledge-stm32mp157c-dk2 = " CFG_EMBED_DTB_SOURCE_FILE=stm32mp157c-dk2.dts "

# add traces at startup
EXTRA_OEMAKE_append =  " CFG_TEE_CORE_DEBUG=n CFG_TEE_CORE_LOG_LEVEL=2 V=1 "

OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_armv7ve = "arm32"

FTPM_UUID="bc50d971-d4c9-42c4-82cb-343fb7f37896"
EXTRA_OEMAKE_append_ledge-qemuarm64='CFG_EARLY_TA=y EARLY_TA_PATHS="../${FTPM_UUID}.stripped.elf" '

do_install_append_ledge-stm32mp157c-dk2() {
    # install optee bianries with stm32 images
    install -m 644 ${B}/out/arm-plat-${OPTEEOUTPUTMACHINE}/core/*.stm32 ${D}${nonarch_base_libdir}/firmware/
}

do_deploy_append_ledge-qemuarm() {
    cd ${DEPLOYDIR}
    ln -sf optee/tee-header_v2.bin   bl32.bin
    ln -sf optee/tee-pager_v2.bin    bl32_extra1.bin
    ln -sf optee/tee-pageable_v2.bin bl32_extra2.bin
    cd -
}

do_deploy_append_ledge-qemuarm64() {
    cd ${DEPLOYDIR}
    ln -sf optee/tee-header_v2.bin   bl32.bin
    ln -sf optee/tee-pager_v2.bin    bl32_extra1.bin
    ln -sf optee/tee-pageable_v2.bin bl32_extra2.bin
    cd -
}
