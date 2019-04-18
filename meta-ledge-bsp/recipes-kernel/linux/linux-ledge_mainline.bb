DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel siteinfo

LEDGE_KVERSION = "4.19.1"

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v4.x/linux-${LEDGE_KVERSION}.tar.xz;name=kernel"
SRC_URI[kernel.md5sum] = "6de5f0c6c0eb5d27386579894fcf31ab"
SRC_URI[kernel.sha256sum] = "64d637c65c0b210659ff1719bcc9e34c5576fc3a4df9aa67087fa00bc2e08829"

PV = "mainline-4.19.1"
S = "${WORKDIR}/linux-4.19.1"

KERNEL_DEFCONFIG = "defconfig"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-02-systemd.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-10-ledge.config "
KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_DEFCONFIG}"

SRC_URI_append = " file://fragment-02-systemd.config "
SRC_URI_append = " file://fragment-10-ledge.config "

COMPATIBLE_MACHINE = "(ledge-espressobin|ledge-stm32mp157c-dk2|ledge-qemux86-64)"

do_configure() {
    touch ${B}/.scmversion ${S}/.scmversion

    if [ ! -z ${KERNEL_DEFCONFIG} ]; then
        bbnote "Kernel customized: configuration of linux by using DEFCONFIG: ${KERNEL_DEFCONFIG}"
        oe_runmake ${PARALLEL_MAKE} -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_DEFCONFIG}
    else
        if [ ! -z ${KERNEL_EXTERNAL_DEFCONFIG} ]; then
            bbnote "Kernel customized: configuration of linux by using external DEFCONFIG"
            install -m 0644 ${WORKDIR}/${KERNEL_EXTERNAL_DEFCONFIG} ${B}/.config
            oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" oldconfig
        else
            bbwarn "You must specify KERNEL_DEFCONFIG or KERNEL_EXTERNAL_DEFCONFIG"
            die "NO DEFCONFIG SPECIFIED"
        fi
    fi
    if [ ! -z "${KERNEL_CONFIG_FRAGMENTS}" ]; then
        for f in ${KERNEL_CONFIG_FRAGMENTS}
        do
            # Check if the config fragment was copied into the WORKDIR from
            # the OE meta data
            if [ ! -e "$f" ]; then
                echo "Could not find kernel config fragment $f"
                exit 1
            fi
        done

        bbnote "${S}/scripts/kconfig/merge_config.sh -m -r -O ${B} ${B}/.config ${KERNEL_CONFIG_FRAGMENTS} 1>&2"
        # Now that all the fragments are located merge them.
        (${S}/scripts/kconfig/merge_config.sh -m -r -O ${B} ${B}/.config ${KERNEL_CONFIG_FRAGMENTS} 1>&2 )
    fi

    yes '' | oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" oldconfig
    #oe_runmake -C ${S} O=${B} savedefconfig && cp ${B}/defconfig ${WORKDIR}/defconfig.saved

    bbplain "Saving defconfig to:\n${B}/defconfig"
    oe_runmake -C ${B} savedefconfig
    cp -a ${B}/defconfig ${DEPLOYDIR}
}
