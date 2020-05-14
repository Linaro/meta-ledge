
do_install_append() {
    sed -i  "s|^ExecStart=|ExecStartPre=mkdir -p /var/volatile/log/audit\nExecStart=|" ${D}${systemd_unitdir}/system/auditd.service
}
