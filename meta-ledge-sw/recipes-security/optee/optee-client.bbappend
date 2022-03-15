FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# 3.14
PV="3.14.0+git${SRCPV}"
SRCREV_ledgecommon = "06e1b32f6a7028e039c625b07cfc25fda0c17d53"

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

EXTRA_OEMAKE = " \
    RPMB_EMU=0 \
"

do_install() {
    oe_runmake install

    install -D -p -m0755 ${S}/out/export/usr/sbin/tee-supplicant ${D}${sbindir}/tee-supplicant

    install -D -p -m0644 ${S}/out/export/usr/lib/libteec.so.1.0 ${D}${libdir}/libteec.so.1.0
    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so
    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so.1

    cp -a ${S}/out/export/usr/include ${D}/usr/

    sed -i -e s:/etc:${sysconfdir}:g \
           -e s:/usr/bin:${sbindir}:g \
              ${WORKDIR}/tee-supplicant.service

    install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
}
