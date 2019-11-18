do_install () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/${SELINUX_SCRIPT_SRC}.sh ${D}${sysconfdir}/init.d/${SELINUX_SCRIPT_DST}
	# Insert the relabelling code which is only needed with sysvinit
	sed -i -e '/HERE/r ${WORKDIR}/${SELINUX_SCRIPT_SRC}.sh.sysvinit' \
	       -e '/.*HERE$/d' -e '/.*Contents.*sysvinit/d' \
	       ${D}${sysconfdir}/init.d/${SELINUX_SCRIPT_DST}

	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/${SELINUX_SCRIPT_SRC}.service ${D}${systemd_unitdir}/system
}

