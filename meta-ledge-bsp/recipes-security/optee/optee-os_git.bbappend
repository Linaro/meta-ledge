FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# 3.9
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"
PV="3.10"
SRCREV = "d1c635434c55b7d75eadf471bde04926bd1e50a7"

DEPENDS += "dtc-native"
DEPENDS += "python3-pyelftools-native dtc-native python3-pycryptodomex-native python3-pycrypto-native"

SRC_URI_append_ledge-qemuarm64 = " file://bc50d971-d4c9-42c4-82cb-343fb7f37896.stripped.elf "
# random
SRC_URI_append_ledge-qemuarm64 = " file://b6c53aba-9669-4668-a7f2-205629d00f86.stripped.elf "

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
EXTRA_OEMAKE_append =  " CFG_TEE_CORE_DEBUG=n CFG_TEE_CORE_LOG_LEVEL=2 "

OPTEE_ARCH_armv7a = "arm32"
OPTEE_ARCH_armv7ve = "arm32"

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
