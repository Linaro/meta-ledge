DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""

include linux-ledge-common.inc
include linux-ledge-sign.inc

LEDGE_KVERSION = "5.8"

# Stable kernel URL
SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-${LEDGE_KVERSION}.tar.xz;name=kernel"
SRC_URI[kernel.sha256sum] = "e7f75186aa0642114af8f19d99559937300ca27acaf7451b36d4f9b0f85cf1f5"

# force SOURCE_DATE_EPOCH for build reproductible
# SOURCE_DATE_EPOCH = "1557103378"

# RC kernel URL
#SRC_URI = "https://git.kernel.org/torvalds/t/linux-${LEDGE_KVERSION}.tar.gz;name=kernel"
#SRC_URI[kernel.sha256sum] = "a65ccee5e098c62bce7a053c322b3eba96904561a785e42c46b76069b323dc87"

SRC_URI += " \
    file://0001-STM32mp157c-dk2-optee-reboot.patch \
    file://0002-KERNEL-stm32mp157-dts-add-ftpm-support.patch \
    file://0003-optee-use-uuid-for-sysfs-driver-entry.patch \
    file://0004-optee-enable-support-for-multi-stage-bus-enumeration.patch \
    file://0005-tpm_ftpm_tee-register-driver-on-TEE-bus.patch \
    file://0006-efi-libstub-DRAM-base-calculation.patch \
"

PV = "mainline-5.8"
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
