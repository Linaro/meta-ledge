SUMMARY = "OPTEE fTPM Microsoft TA"
DESCRIPTION = "OPTEE fTPM"
HOMEPAGE = "https://github.com/microsoft/ms-tpm-20-ref/"

inherit autotools-brokensep pkgconfig gettext

FTPM_UUID="bc50d971-d4c9-42c4-82cb-343fb7f37896"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=27e94c0280987ab296b0b8dd02ab9fe5"

DEPENDS = "optee-client optee-os openssl"
DEPENDS += "openssl-native autoconf-archive-native"

# SRC_URI = "git://github.com/Microsoft/ms-tpm-20-ref;branch=master"
# Since this is not built as a pseudo TA, we can only use it as a kernel module and not built in.
# The TEE supplicant is also needed to provide access to secure storage.
# Secure storage access required by OP-TEE fTPM TA
# is provided via OP-TEE supplicant that's not available during boot.
# Fix this once we replace this with the MS implementation
SRC_URI = "git://github.com/microsoft/MSRSec"
SRCREV = "81abeb9fa968340438b4b0c08aa6685833f0bfa1"

S = "${WORKDIR}/git"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR = "${STAGING_INCDIR}/optee/export-user_ta"

EXTRA_OEMAKE += "\
    CFG_FTPM_USE_WOLF=y \
    TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
    TA_CROSS_COMPILE=${TARGET_PREFIX} \
    LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
"

EXTRA_OEMAKE_append_aarch64 = "\
    CFG_ARM64_ta_arm64=y \
"

B = "${S}"

do_configure() {
    cd ${S}
    git submodule update --init
    sed -i 's/-mcpu=$(TA_CPU)//' TAs/optee_ta/fTPM/sub.mk
}

do_compile() {
    # there's also a secure variable storage TA called authvars
    cd ${S}/TAs/optee_ta
    # fails with j > 1
    oe_runmake -j1 ftpm
}

do_install () {
    mkdir -p ${D}/lib/optee_armtz
    install -D -p -m0444 ${S}/TAs/optee_ta/out/fTPM/${FTPM_UUID}.ta ${D}/lib/optee_armtz/
}

FILES_${PN} += "/lib/optee_armtz/${FTPM_UUID}.ta"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP_${PN} += "ldflags"
