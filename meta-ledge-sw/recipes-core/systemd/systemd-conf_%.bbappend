#  "Enable systemd watchdog"

do_install_append() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'watchdog', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system.conf.d/
        echo "[Manager]" > ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
        echo "RuntimeWatchdogSec=15" >> ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
        echo "ShutdownWatchdogSec=5min" >> ${D}${systemd_unitdir}/system.conf.d/01-watchdog.conf
    fi
}
