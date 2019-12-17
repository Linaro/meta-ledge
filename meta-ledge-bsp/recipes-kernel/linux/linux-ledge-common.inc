inherit kernel siteinfo

DEPENDS += "coreutils-native"
DEPENDS += "efitools-native"
# for mkfs.ext4
DEPENDS += "e2fsprogs-native"

KERNEL_DEFCONFIG = "defconfig"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-01-core.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-02-systemd.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-10-ledge.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-11-virtio.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-12-security.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-13-cmdline.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', '${WORKDIR}/fragment-14-module-signature.config','')} "

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_DEFCONFIG}"

SRC_URI_append = " file://fragment-01-core.config "
SRC_URI_append = " file://fragment-02-systemd.config "
SRC_URI_append = " file://fragment-10-ledge.config "
SRC_URI_append = " file://fragment-11-virtio.config "
SRC_URI_append = " file://fragment-12-security.config "
SRC_URI_append = " file://fragment-13-cmdline.config "
SRC_URI_append = " file://fragment-14-module-signature.config "

SRC_URI_append = " file://uefi-certificates/KEK.auth "
SRC_URI_append = " file://uefi-certificates/KEK.crt "
SRC_URI_append = " file://uefi-certificates/KEK.key "
SRC_URI_append = " file://uefi-certificates/PK.auth "

EXTRA_OEMAKE += "${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', 'INSTALL_MOD_STRIP=1','')}"

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
    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'efi', '1', '0', d)}" = "1" ]; then
	   for img in bzImage zImage Image; do
		if [ "x$img" = "x${KERNEL_IMAGETYPE}" ]; then
			break;
		fi
		if [ "x$img" = "x${KERNEL_ALT_IMAGETYPE}" ]; then
			break;
		fi
	   done

	install -d ${D}/boot/efi/boot
	cat ${D}/boot/${img} > ${D}/boot/efi/boot/${KERNEL_EFI_IMAGE}

	for t in ${KERNEL_IMAGETYPE} ${KERNEL_ALT_IMAGETYPE}; do
	    rm -rf ${D}/boot/$t*
	done
    fi
}
FILES_${KERNEL_PACKAGE_NAME}-image += "/boot/efi"

python __anonymous () {
    types = d.getVar('KERNEL_IMAGETYPES') or ""
    kname = d.getVar('KERNEL_PACKAGE_NAME') or "kernel"
    for type in types.split():
        typelower = type.lower()
        if typelower == 'zimage':
            d.appendVar('FILES_' + kname + '-image-' + typelower, ' /boot/efi/boot ')
        if typelower == 'image':
            d.appendVar('FILES_' + kname + '-image-' + typelower, ' /boot/efi/boot ')
}
FILES_${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo "

# for debian purpose
do_deploy_append() {
    if [ ${MODULE_TARBALL_DEPLOY} = "1" ] && (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
        mkdir -p ${D}${root_prefix}/lib
        tar -cvzf $deployDir/modules-stripped-${MODULE_TARBALL_NAME}.tgz -C ${WORKDIR}/package/${root_prefix} lib
        ln -sf modules-stripped-${MODULE_TARBALL_NAME}.tgz $deployDir/modules-stripped-${MODULE_TARBALL_LINK_NAME}.tgz
        cp $deployDir/modules-stripped-${MODULE_TARBALL_NAME}.tgz $deployDir/modules-stripped-${MACHINE}-for-debian.tgz
    fi

    for t in ${KERNEL_IMAGETYPE} ${KERNEL_ALT_IMAGETYPE}; do
        cp -f $deployDir/$t $deployDir/$t-for-debian
    done

    for d in  ${KERNEL_DEVICETREE}; do
        cp -f $deployDir/$d $deployDir/$d-for-debian
    done

    hash-to-efi-sig-list ${D}/boot/efi/boot/${KERNEL_EFI_IMAGE} kernel.hash
    sign-efi-sig-list -c ${WORKDIR}/uefi-certificates/KEK.crt -k ${WORKDIR}/uefi-certificates/KEK.key db kernel.hash kernel.auth
    mkdir -p certimage
    cp ${WORKDIR}/uefi-certificates/PK.auth ${WORKDIR}/uefi-certificates/KEK.auth kernel.auth ./certimage
    truncate -s 4M certimage.ext4
    ${STAGING_DIR_NATIVE}${base_sbindir}/mkfs.ext4 certimage.ext4 -d ./certimage
    rm -rf ./certimage kernel.hash
    mv certimage.ext4 ${DEPLOYDIR}/ledge-kernel-uefi-certs.ext4
}
do_deploy[depends] += " virtual/kernel:do_package "
