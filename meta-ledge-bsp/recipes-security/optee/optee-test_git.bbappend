FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
# 3.9
SRC_URI = "git://github.com/OP-TEE/optee_test.git"

PV="3.9.0+git${SRCPV}"
SRCREV_ledgecommon = "f461e1d47fcc82eaa67508a3d796c11b7d26656e"

DEPENDS_append_ledgecommon += "python3-pycryptodomex-native python3-pycrypto-native"

EXTRA_OEMAKE_append_armv7a = " COMPILE_NS_USER=32"
EXTRA_OEMAKE_append_armv7e = " COMPILE_NS_USER=32"

SRC_URI_append_arm = " file://0001-Correct-support-of-32bits.patch "
