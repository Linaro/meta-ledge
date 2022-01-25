DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = "${@bb.utils.contains('ARCH', 'x86', 'elfutils-native', '', d)}"
DEPENDS += "openssl-native util-linux-native"
DEPENDS += "gmp-native"

include linux-ledge-common.inc
include linux-ledge-sign.inc

PR = "r3.ledge"

LEDGE_KVERSION = "5.15"

# Stable kernel URL
SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-${LEDGE_KVERSION}.tar.xz;name=kernel"
SRC_URI[kernel.sha256sum] = "57b2cf6991910e3b67a1b3490022e8a0674b6965c74c12da1e99d138d1991ee8"

# force SOURCE_DATE_EPOCH for build reproductible
# SOURCE_DATE_EPOCH = "1557103378"

# RC kernel URL
#SRC_URI = "https://git.kernel.org/torvalds/t/linux-${LEDGE_KVERSION}.tar.gz;name=kernel"
#SRC_URI[kernel.sha256sum] = "a65ccee5e098c62bce7a053c322b3eba96904561a785e42c46b76069b323dc87"

SRC_URI += " \
    file://0001-STM32mp157c-dk2-optee-reboot.patch \
    file://0002-KERNEL-stm32mp157-dts-add-ftpm-support.patch \
    file://0003-rk3399-rock-pi-4.dtsi-enable-imx219-isp.patch \
"

PV = "mainline-5.15"
S = "${WORKDIR}/linux-${LEDGE_KVERSION}"
LIC_FILES_CHKSUM = "file://${S}/LICENSES/preferred/GPL-2.0;md5=e6a75371ba4d16749254a51215d13f97"

COMPATIBLE_MACHINE = "(ledge-synquacer|ledge-stm32mp157c-dk2|ledge-qemux86-64|ledge-qemuarm|ledge-qemuarm64|ledge-ti-am572x)"

# enable module signing
# The signing configuration are located on fragment-14-module-signature
# to veirfy if a module are signed:
# tail -c 28 <kernel_module.ko>
KERNEL_SIGN_ENABLE = "1"

# rename DTB
# DTB_RENAMING = "xyz.dtb:zyx.dtb "
