FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
# 3.14
SRC_URI = "git://github.com/OP-TEE/optee_test.git"

PV="3.14.0+git${SRCPV}"
SRCREV_ledgecommon = "f2eb88affbb7f028561b4fd5cbd049d5d704f741"

COMPATIBLE_MACHINE = "(ledge-qemuarm64|ledge-qemuarm)"

EXTRA_OEMAKE_append_armv7a = " COMPILE_NS_USER=32"
EXTRA_OEMAKE_append_armv7e = " COMPILE_NS_USER=32"

# for libgcc.a
EXTRA_OEMAKE_append = " LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
                    CFG_PKCS11_TA=y \
                    "

SRC_URI_append_arm = " file://0001-Correct-support-of-32bits.patch "
