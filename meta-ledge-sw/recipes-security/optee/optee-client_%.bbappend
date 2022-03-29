FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

# 3.16
PV="3.146.0+git${SRCPV}"
SRCREV_ledgecommon = "945704e6433e31753fb6c3f05e1ce61673dec1d6"
SRC_URI += " file://pr302.patch "

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

EXTRA_OECMAKE_append = " \
    -DRPMB_EMU=0 \
"


