
do_install_append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/${SELINUX_SCRIPT_SRC}.service ${D}${systemd_unitdir}/system
        install -d ${D}${bindir}
        install -m 0755 ${WORKDIR}/${SELINUX_SCRIPT_SRC}.sh ${D}${bindir}
        sed -i -e '/.*HERE$/d' ${D}${bindir}/${SELINUX_SCRIPT_SRC}.sh
    fi

    #ledge rule
    sed -i "s|^/usr/sbin/selinuxenabled|/usr/bin/selinux-ledge.sh load\n/usr/sbin/selinuxenabled|" ${D}${bindir}/${SELINUX_SCRIPT_SRC}.sh
}
