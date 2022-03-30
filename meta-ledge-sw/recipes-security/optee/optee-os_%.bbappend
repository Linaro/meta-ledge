
# 3.16
PV="3.16.0+git${SRCPV}"
SRCREV_ledgecommon = "d0b742d1564834dac903f906168d7357063d5459"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

DEPENDS_append += "python3-cryptography-native "

SRC_URI_remove = " \
    file://0001-libutils-provide-empty-__getauxval-implementation.patch \
    file://0002-link.mk-implement-support-for-libnames-after-libgcc-.patch \
"
