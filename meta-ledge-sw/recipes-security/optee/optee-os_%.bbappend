
# 3.14
PV="3.14.0+git${SRCPV}"
SRCREV_ledgecommon = "d21befa5e53eae9db469eba1685f5aa5c6f92c2f"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

SRC_URI_remove = " \
    file://0001-libutils-provide-empty-__getauxval-implementation.patch \
    file://0002-link.mk-implement-support-for-libnames-after-libgcc-.patch \
"
