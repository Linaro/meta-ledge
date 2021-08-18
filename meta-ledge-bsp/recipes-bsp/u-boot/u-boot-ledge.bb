HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse

PV = "2021.01"

SRC_URI = "git://git.denx.de/u-boot.git"
SRCREV = "c4fddedc48f336eabc4ce3f74940e6aa372de18c"

SRC_URI += " \
    file://0000-ti-am572x-enable-boot_distrocmd.patch \
    file://0001-stm32mp-update-MMU-config-before-the-relocation.patch \
    file://0002-stm32mp-update-the-mmu-configuration-for-SPL-and-pre.patch \
    file://0003-arm-remove-TTB_SECT_XN_MASK-in-DCACHE_WRITETHROUGH.patch \
    file://0004-arm-cosmetic-align-TTB_SECT-define-value.patch \
    file://0005-arm-cp15-update-DACR-value-to-activate-access-contro.patch \
    file://0006-arm-omap2-remove-arm_init_domains.patch \
    file://0007-arm-cp15-remove-weak-function-arm_init_domains.patch \
    file://0008-arm-remove-set_dacr-get_dacr-functions.patch \
    file://0009-tpm2-Introduce-TIS-tpm-core.patch \
    file://0010-tpm2-Add-a-TPMv2-MMIO-TIS-driver.patch \
    file://ledge_stm32mp157c_dk2_trusted_defconfig \
    file://ubootefi.var \
    "

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SRC_URI_append_ledge-qemuarm = " file://ledge-qemuarm_defconfig"
SRC_URI_append_ledge-qemuarm64 = " file://ledge-qemuarm64_defconfig"
SRC_URI_append_ledge-ti-am572x = " file://ledge-ti-am572x_defconfig"

PACKAGE_ARCH = "${MACHINE_ARCH}"

require recipes-bsp/u-boot/u-boot.inc
PROVIDES += "u-boot virtual/bootloader"
RPROVIDES_${PN} += "u-boot virtual/bootloader"

DEPENDS += "bc-native dtc-native"

do_configure_prepend() {
    for conf in ${UBOOT_MACHINE};
    do
        if [ -f ${WORKDIR}/$conf ] ;
        then
            cp ${WORKDIR}/$conf ${S}/configs/
        fi
    done
    cp ${WORKDIR}/ubootefi.var ${S}/
}

# -----------------------------------------------------------------------------
# Append deploy to handle specific device tree binary deployement
#
SPL_BINARY_LEDGE_ledge-stm32mp157c-dk2 = "spl/u-boot-spl.stm32"
do_deploy_append() {
if [ -n "${SPL_BINARY_LEDGE}" ]; then
    # Clean deploydir from any available binary first
    # This allows to only install the devicetree binary ones
    rm -rf ${DEPLOYDIR}

    # Install destination folder
    install -d ${DEPLOYDIR}

    if [ -n "${UBOOT_CONFIG}" ]; then
        unset i j k
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    for binary in ${UBOOT_BINARIES}; do
                        binarysuffix=$(echo ${binary} | cut -d'.' -f2)
                        k=$(expr $k + 1);
                        if [ $k -eq $i ]; then
                            if [ -f ${B}/${config}/${binary} ];
                            then
                                install -m 644 ${B}/${config}/${binary} ${DEPLOYDIR}/u-boot-${type}.${binarysuffix}
                            fi
                            # As soon as SPL binary exists, install it
                            # This allow to mix u-boot configuration, with and without SPL
                            if [ -f ${B}/${config}/${SPL_BINARY_LEDGE} ]; then
                                install -d ${DEPLOYDIR}/spl
                                install -m 644 ${B}/${config}/${SPL_BINARY_LEDGE} ${DEPLOYDIR}/${SPL_BINARY_LEDGE}-${type}
                            fi
                        fi
                    done
                    unset k
                fi
            done
            unset j
        done
        unset i
    else
            bbfatal "Wrong u-boot-ledge configuration: please make sure to use UBOOT_CONFIG through BOOTSCHEME_LABELS config"
    fi
fi
}
do_deploy_append_ledge-qemuarm() {
    cd ${DEPLOYDIR}
    ln -sf u-boot-ledge-qemuarm.bin bl33.bin
    cd -
}

do_deploy_append_ledge-qemuarm64() {
    cd ${DEPLOYDIR}
    ln -sf u-boot-ledge-qemuarm64.bin bl33.bin
    cd -
}

