FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

COMPATIBLE_MACHINE = "ledge-qemuarm64"

# 3.14
PV="3.14.0+git${SRCPV}"
SRCREV_ledgecommon = "06e1b32f6a7028e039c625b07cfc25fda0c17d53"

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

EXTRA_OECMAKE_append = " \
    -DRPMB_EMU=0 \
"


