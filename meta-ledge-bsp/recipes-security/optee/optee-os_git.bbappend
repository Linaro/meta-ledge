# ledge-ti-am572x
OPTEEMACHINE_ledge-ti-am572x = "ti-am57xx"
OPTEEOUTPUTMACHINE_ledge-ti-am572x = "ti"

EXTRA_OEMAKE_remove_ledge-ti-am572x = "CFG_ARM64_core=y"
EXTRA_OEMAKE_remove_ledge-ti-am572x = "ta-targets=ta_arm64"
EXTRA_OEMAKE_append_ledge-ti-am572x = "CFG_ARM32_core=y ta-targets=ta_arm32 "

EXTRA_OEMAKE_append_ledge-ti-am572x = " CROSS_COMPILE_ta_arm32=${HOST_PREFIX} CROSS_COMPILE=${CROSS_COMPILE} "

# ledge-stm32mp157c-dk2
OPTEEMACHINE_ledge-stm32mp157c-dk2 = "stm32mp1"
OPTEEOUTPUTMACHINE_ledge-stm32mp157c-dk2 = "stm32mp1"

EXTRA_OEMAKE_remove_ledge-stm32mp157c-dk2 = "CFG_ARM64_core=y"
EXTRA_OEMAKE_remove_ledge-stm32mp157c-dk2 = "ta-targets=ta_arm64"
EXTRA_OEMAKE_append_ledge-stm32mp157c-dk2 = " CFG_ARM32_core=y ta-targets=ta_arm32 "
EXTRA_OEMAKE_append_ledge-stm32mp157c-dk2 = " CROSS_COMPILE_ta_arm32=${HOST_PREFIX} CROSS_COMPILE=${CROSS_COMPILE} "
OPTEE_ARCH_armv7ve = "arm32"

do_install_append_ledge-stm32mp157c-dk2() {
    # install optee bianries with stm32 images
    install -m 644 ${B}/out/arm-plat-${OPTEEOUTPUTMACHINE}/core/*.stm32 ${D}${nonarch_base_libdir}/firmware/
}
