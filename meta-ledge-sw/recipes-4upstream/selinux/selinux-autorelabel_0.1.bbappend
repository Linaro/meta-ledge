do_install_append() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -d ${D}${bindir}
		install -m 0755 ${WORKDIR}/${SELINUX_SCRIPT_SRC}.sh ${D}${bindir}
		sed -i -e '/.*HERE$/d' ${D}${bindir}/${SELINUX_SCRIPT_SRC}.sh
		echo "# first boot relabelling" > ${D}/.autorelabel
	fi
}
