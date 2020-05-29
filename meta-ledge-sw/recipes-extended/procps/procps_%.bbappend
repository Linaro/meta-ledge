FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

do_install_append() {
        echo kernel.printk = 1 1 1 1 >> ${D}${sysconfdir}/sysctl.conf
}
