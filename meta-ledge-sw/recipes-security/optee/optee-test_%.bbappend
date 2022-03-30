FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
# 3.16
SRC_URI = "git://github.com/OP-TEE/optee_test.git"

PV="3.16.0+git${SRCPV}"
SRCREV_ledgecommon = "1cf0e6d2bdd1145370033d4e182634458528579d"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

DEPENDS_append += "python3-cryptography-native "

EXTRA_OEMAKE_append_armv7a = " COMPILE_NS_USER=32"
EXTRA_OEMAKE_append_armv7e = " COMPILE_NS_USER=32"

# for libgcc.a
EXTRA_OEMAKE_append = " LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
                    CFG_PKCS11_TA=y \
                    "

SRC_URI_append_arm = " file://0001-Correct-support-of-32bits.patch "
