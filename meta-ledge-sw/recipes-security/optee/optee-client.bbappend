FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# 3.9
PV="3.9.0+git${SRCPV}"
SRCREV_ledgecommon = "e9e55969d76ddefcb5b398e592353e5c7f5df198"

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

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
