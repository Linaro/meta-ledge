SUMMARY = "Enable systemd watchdog"
FILESEXTRAPATH_prepend := "${THISDIR}/${PN}"

do_install_append() {
if ${@bb.utils.contains('MACHINE_FEATURES', 'watchdog', 'true', 'false', d)}; then
    sed -i -e 's/.*RuntimeWatchdogSec.*/RuntimeWatchdogSec=30/' \
        ${D}${sysconfdir}/systemd/system.conf

    sed -i -e 's/.*ShutdownWatchdogSec.*/ShutdownWatchdogSec=5min/' \
        ${D}${sysconfdir}/systemd/system.conf
fi
}
