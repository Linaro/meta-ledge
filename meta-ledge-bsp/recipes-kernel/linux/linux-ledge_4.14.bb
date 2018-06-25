DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""
PR = "r0"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"


inherit kernel siteinfo

DEPENDS += " dtc-native "

SRC_URI = "https://cdn.kernel.org/pub/linux/kernel/v4.x/linux-4.14.tar.xz;name=kernel"
SRC_URI[kernel.md5sum] = "bacdb9ffdcd922aa069a5e1520160e24"
SRC_URI[kernel.sha256sum] = "f81d59477e90a130857ce18dc02f4fbe5725854911db1e7ba770c7cd350f96a7"

SRC_URI += "file://0001-ARM-dts-support-of-imxqdl-HummingBoard2-with-som-ver.patch"

PV = "4.14"
S = "${WORKDIR}/linux-4.14"

KERNEL_DEFCONFIG        = "multi_v7_defconfig"
KERNEL_CONFIG_FRAGMENTS = " ${WORKDIR}/fragment-01-systemd.config "
KERNEL_CONFIG_FRAGMENTS = " ${WORKDIR}/fragment-02-lite.config "

SRC_URI += "file://fragment-01-systemd.config"
SRC_URI += "file://fragment-02-lite.config"

COMPATIBLE_MACHINE = "(lite-hummingboard2)"


KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_DEFCONFIG}"
do_configure() {
    touch ${B}/.scmversion ${S}/.scmversion

    if [ ! -z ${KERNEL_DEFCONFIG} ];
    then
        bbnote "Kernel customized: configuration of linux STI by using DEFCONFIG: ${KERNEL_DEFCONFIG}"
        oe_runmake ${PARALLEL_MAKE} -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_DEFCONFIG}
    else
        if [ ! -z ${KERNEL_EXTERNAL_DEFCONFIG} ];
        then
            bbnote "Kernel customized: configuration of linux STI by using external DEFCONFIG"
            install -m 0644 ${WORKDIR}/${KERNEL_EXTERNAL_DEFCONFIG} ${B}/.config
            oe_runmake -C ${S} O=${B} CC="${KERNEL_CC}" LD="${KERNEL_LD}" oldconfig
        else
            bbwarn "You must specify KERNEL_DEFCONFIG or KERNEL_EXTERNAL_DEFCONFIG"
            die "NO DEFCONFIG SPECIFIED"
        fi
    fi
    if [ ! -z "${KERNEL_CONFIG_FRAGMENTS}" ]
    then
        for f in ${KERNEL_CONFIG_FRAGMENTS}
        do
            # Check if the config fragment was copied into the WORKDIR from
            # the OE meta data
            if [ ! -e "$f" ]
            then
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
