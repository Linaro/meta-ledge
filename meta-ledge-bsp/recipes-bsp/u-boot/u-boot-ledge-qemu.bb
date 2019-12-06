HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"
PROVIDES += "u-boot-ledge-qemu"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "49c18f12f6377afde18dd7b38ad3bae1db31cec3"

SRC_URI = "git://git.linaro.org/people/takahiro.akashi/u-boot.git;branch=efi/secboot"

S = "${WORKDIR}/git"

SRC_URI_append_ledge-qemuarm = " file://ledge-qemuarm_defconfig"
SRC_URI_append_ledge-qemuarm64 = " file://ledge-qemuarm64_defconfig"

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
