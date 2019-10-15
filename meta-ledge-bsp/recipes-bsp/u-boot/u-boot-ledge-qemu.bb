HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
DEPENDS += "flex-native bison-native"
PROVIDES += "u-boot-ledge-qemu"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"
PE = "1"

# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "cdbde65166dd9936d52c6acdc8e38cf685f82195"

SRC_URI = "git://git.linaro.org/people/takahiro.akashi/u-boot.git"

S = "${WORKDIR}/git"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"
