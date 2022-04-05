FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

# 3.16
PV="3.16.0+git${SRCPV}"
SRCREV_ledgecommon = "06db73b3f3fdb8d23eceaedbc46c49c0b45fd1e2"

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

SRC_URI_append = " \
file://0001-libckteec-add-support-for-ECDH-derive.patch \
	file://0002-tee-supplicant-introduce-struct-tee_supplicant_param.patch \
	file://0003-tee-supplicant-refactor-argument-parsing-in-main.patch \
	file://0004-tee-supplicant-rpmb-introduce-readn-wrapper-to-the-r.patch \
	file://0005-tee-supplicant-rpmb-read-CID-in-one-go.patch \
	file://0006-tee-supplicant-add-rpmb-cid-command-line-option.patch \
	file://create-tee-supplicant-env \
	"

EXTRA_OECMAKE_append = " \
    -DRPMB_EMU=0 \
"

do_install_append() {
	install -D -p -m0755 ${WORKDIR}/create-tee-supplicant-env ${D}${sbindir}/create-tee-supplicant-env
	sed -i "s|^ExecStart=.*$|EnvironmentFile=-@localstatedir@/run/tee-supplicant.env\nExecStartPre=@sbindir@/create-tee-supplicant-env @localstatedir@/run/tee-supplicant.env\nExecStart=@sbindir@/tee-supplicant $RPMB_CID $OPTARGS|" ${D}${systemd_system_unitdir}/tee-supplicant.service
	sed -i -e s:@sbindir@:${sbindir}:g \
	       -e s:@localstatedir@:${localstatedir}:g ${D}${systemd_system_unitdir}/tee-supplicant.service
}
