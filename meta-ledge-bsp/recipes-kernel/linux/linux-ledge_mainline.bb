DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

include linux-ledge-common.inc

LEDGE_KVERSION = "5.2"

# Stable kernel URL
SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v5.x/linux-${LEDGE_KVERSION}.tar.xz;name=kernel"

SRC_URI[kernel.md5sum] = "ddf994de00d7b18395886dd9b30b9262"
SRC_URI[kernel.sha256sum] = "54ad66f672e1a831b574f5e704e8a05f1e6180a8245d4bdd811208a6cb0ac1e7"
# force SOURCE_DATE_EPOCH for build reproductible
# SOURCE_DATE_EPOCH = "1557103378"

# RC kernel URL
#SRC_URI = "https://git.kernel.org/torvalds/t/linux-${LEDGE_KVERSION}.tar.gz;name=kernel"

SRC_URI_append_ledge-stm32mp157c-dk2 = " file://0001-STM32mp157c-dk2-optee-and-ethernet-support.patch "
SRC_URI_append_ledge-stm32mp157c-dk2 = " file://0002-ftpm.patch "
SRC_URI_append_ledge-stm32mp157c-dk2 = " file://0001-KERNEL-stm32mp157-dts-add-ftpm-support.patch "

PV = "mainline-5.2"
S = "${WORKDIR}/linux-5.2"

COMPATIBLE_MACHINE = "(ledge-synquacer|ledge-stm32mp157c-dk2|ledge-qemux86-64|ledge-qemuarm|ledge-qemuarm64|ledge-ti-am572x)"


