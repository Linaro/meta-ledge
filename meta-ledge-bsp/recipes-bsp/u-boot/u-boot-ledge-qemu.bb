HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"
PROVIDES += "u-boot-ledge-qemu"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

SRCREV = "53df526d7020e6062e0e13b5172441667beccfa8"

SRC_URI = "git://github.com/Linaro/ledge-uboot.git;branch=efi_secboot_v2"

S = "${WORKDIR}/git"

SRC_URI_append_ledge-qemuarm = " file://ledge-qemuarm_defconfig"
SRC_URI_append_ledge-qemuarm64 = " file://ledge-qemuarm64_defconfig"

SRC_URI_append = " file://0001-fix-position-independent-code-without-dtb.patch "

require recipes-bsp/u-boot/u-boot.inc

do_compile_prepend() {
    for conf in ${UBOOT_MACHINE};
    do
        if [ -f ${WORKDIR}/$conf ] ;
        then
            cp ${WORKDIR}/$conf ${S}/configs/
        fi
    done
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

DEPENDS += "bc-native dtc-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(ledge-qemuarm|ledge-qemuarm64)"
