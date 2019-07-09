DESCRIPTION = "Linux Kernel"
HOMEPAGE = "www.kernel.org"
LICENSE = "GPLv2"
SECTION = "kernel"
DEPENDS = ""

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel siteinfo

DEPENDS += "coreutils-native"
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

PV = "mainline-5.2"
S = "${WORKDIR}/linux-5.2"

KERNEL_DEFCONFIG = "defconfig"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-01-core.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-02-systemd.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-10-ledge.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-11-virtio.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-12-security.config "
KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_DEFCONFIG}"

SRC_URI_append = " file://fragment-01-core.config "
SRC_URI_append = " file://fragment-02-systemd.config "
SRC_URI_append = " file://fragment-10-ledge.config "
SRC_URI_append = " file://fragment-11-virtio.config "
SRC_URI_append = " file://fragment-12-security.config "

COMPATIBLE_MACHINE = "(ledge-espressobin|ledge-stm32mp157c-dk2|ledge-qemux86-64|ledge-qemuarm|ledge-qemuarm64|ledge-ti-am572x)"

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

# -----------------------------------------------------
#             EFI
# Determine the target arch for kernel as EFI firmware
python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    if target == "x86_64":
        kernel_efi_image = "bootx64.efi"
    elif re.match('i.86', target):
        kernel_efi_image = "bootia32.efi"
    elif re.match('aarch64', target):
        kernel_efi_image = "bootaa64.efi"
    elif re.match('arm', target):
        kernel_efi_image = "bootarm.efi"
    else:
        raise bb.parse.SkipRecipe("kernel efi is incompatible with target %s" % target)
    d.setVar("KERNEL_EFI_IMAGE", kernel_efi_image)
}

do_install_append() {
#    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'efi', '1', '0', d)}" = "1" ]; then
        for t in ${KERNEL_IMAGETYPE} ${KERNEL_ALT_IMAGETYPE}; do
            if [ "$t" = "zImage" ]; then
                install -d ${D}/boot/efi/boot
                ln -s ../../zImage ${D}/boot/efi/boot/${KERNEL_EFI_IMAGE}
            fi
        done

#    fi
}
python __anonymous () {
    types = d.getVar('KERNEL_IMAGETYPES') or ""
    kname = d.getVar('KERNEL_PACKAGE_NAME') or "kernel"
    for type in types.split():
        typelower = type.lower()
        if typelower == 'zimage':
            d.appendVar('FILES_' + kname + '-image-' + typelower, ' /boot/efi/boot ')
}
FILES_${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "
