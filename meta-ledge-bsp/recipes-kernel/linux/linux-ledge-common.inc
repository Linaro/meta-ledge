inherit kernel siteinfo

DEPENDS += "coreutils-native"

KERNEL_DEFCONFIG = "defconfig"
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-01-core.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-02-systemd.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-10-ledge.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-11-virtio.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-12-security.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-13-cmdline.config "
KERNEL_CONFIG_FRAGMENTS_append = " ${@oe.utils.ifelse(d.getVar('KERNEL_SIGN_ENABLE') == '1', '${WORKDIR}/fragment-14-module-signature.config','')} "
KERNEL_CONFIG_FRAGMENTS_append = " ${WORKDIR}/fragment-15-cleanup.config "

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_DEFCONFIG}"

SRC_URI_append = " file://fragment-01-core.config "
SRC_URI_append = " file://fragment-02-systemd.config "
SRC_URI_append = " file://fragment-10-ledge.config "
SRC_URI_append = " file://fragment-11-virtio.config "
SRC_URI_append = " file://fragment-12-security.config "
SRC_URI_append = " file://fragment-13-cmdline.config "
SRC_URI_append = " file://fragment-14-module-signature.config "
SRC_URI_append = " file://fragment-15-cleanup.config "

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

do_compile_append() {
    config="${B}/.config"
    if grep -q 'CONFIG_ARM_APPENDED_DTB=y' $config; then
        oe_runmake -C ${B} dtbs
    fi
}

do_install_append() {
    config="${B}/.config"

    if grep -q 'CONFIG_ARM_APPENDED_DTB=y' $config; then
        oe_runmake -C ${B} DEPMOD=echo INSTALL_DTBS_PATH=${D}/boot/dtb dtbs_install
    fi

    if [ ! -d ${D}/boot/dtb ]; then
        # force the creation of dtb directory on boot to have
        install -d ${D}/boot/dtb
        echo "Empty content on case there is no devicetree" > ${D}/boot/dtb/.emtpy
    fi
}

FILES_${KERNEL_PACKAGE_NAME}-devicetree += "/${KERNEL_IMAGEDEST}/dtb"
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
    if [ -d ${WORKDIR}/package/boot/dtb ];then
        cp -rf ${WORKDIR}/package/boot/dtb $deployDir/
    fi
}
do_deploy[depends] += " virtual/kernel:do_package "
