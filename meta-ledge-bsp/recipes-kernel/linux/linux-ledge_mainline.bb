DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

include linux-ledge-common.inc
include linux-ledge-sign.inc

LEDGE_KVERSION = "5.3.6"

# Stable kernel URL
SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-${LEDGE_KVERSION}.tar.xz;name=kernel"

SRC_URI[kernel.md5sum] = "9905ef7f60b2ad0b26f614d95f3eb1e4"
SRC_URI[kernel.sha256sum] = "e84021a94784de7bb10e4251fb1a87859a8d1c97bd78fb55ad47ab6ce475ec1f"

# force SOURCE_DATE_EPOCH for build reproductible
# SOURCE_DATE_EPOCH = "1557103378"

# RC kernel URL
#SRC_URI = "https://git.kernel.org/torvalds/t/linux-${LEDGE_KVERSION}.tar.gz;name=kernel"

SRC_URI += " \
    file://0001-STM32mp157c-dk2-optee-and-ethernet-support.patch \
    file://0002-ftpm.patch \
    file://0003-KERNEL-stm32mp157-dts-add-ftpm-support.patch \
    file://0004-op-tee-shm.patch \
    file://0005-op-tee-multi-page-shm.patch \
    file://0006-efi-libstub-Fix-kernel-command-line.patch \
    "

PV = "mainline-5.3"
S = "${WORKDIR}/linux-5.3.6"

COMPATIBLE_MACHINE = "(ledge-synquacer|ledge-stm32mp157c-dk2|ledge-qemux86-64|ledge-qemuarm|ledge-qemuarm64|ledge-ti-am572x)"

# enable module signing
# The signing configuration are located on fragment-14-module-signature
# to veirfy if a module are signed:
# tail -c 28 <kernel_module.ko>
KERNEL_SIGN_ENABLE = "1"

# rename DTB
# DTB_RENAMING = "xyz.dtb:zyx.dtb "
