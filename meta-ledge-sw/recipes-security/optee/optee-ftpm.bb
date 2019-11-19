SUMMARY = "OPTEE fTPM Microsoft TA"
DESCRIPTION = "OPTEE fTPM"
HOMEPAGE = "https://github.com/microsoft/ms-tpm-20-ref/"

inherit autotools-brokensep pkgconfig gettext

FTPM_UUID="bc50d971-d4c9-42c4-82cb-343fb7f37896"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=5a3925ece0806073ae9ebbb08ff6f11e"

DEPENDS = "optee-client optee-os openssl"
DEPENDS += "openssl-native autoconf-archive-native"

# SRC_URI = "git://github.com/Microsoft/ms-tpm-20-ref;branch=master"
# Since this is not built as a pseudo TA, we can only use it as a kernel module and not built in.
# The TEE supplicant is also needed to provide access to secure storage.
# Secure storage access required by OP-TEE fTPM TA
# is provided via OP-TEE supplicant that's not available during boot.
# Fix this once we replace this with the MS implementation
SRC_URI = "git://github.com/apalos/ms-tpm-20-ref.git;branch=ftpm"
SRCREV = "8b2a8eaf9a5f165bc6acc2388490d6237c0de116"

S = "${WORKDIR}/git"
CONFIGURE_SCRIPT = "${S}/TPMCmd/configure"

OPTEE_CLIENT_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TEEC_EXPORT = "${STAGING_DIR_HOST}${prefix}"
TA_DEV_KIT_DIR = "${STAGING_INCDIR}/optee/export-user_ta"

EXTRA_OEMAKE += " TA_DEV_KIT_DIR=${TA_DEV_KIT_DIR} \
                 OPTEE_CLIENT_EXPORT=${OPTEE_CLIENT_EXPORT} \
                 TEEC_EXPORT=${TEEC_EXPORT} \
                 HOST_CROSS_COMPILE=${TARGET_PREFIX} \
                 TA_CROSS_COMPILE=${TARGET_PREFIX} \
                 V=1 \
               "

B = "${S}"

do_configure() {
    git submodule init
    git submodule update
    # Uncomment for MS implementation
    #cd TPMCmd
    #./bootstrap
    #oe_runconf
    sed -i -n '/-mcpu/!p' sub.mk
}

do_compile() {
    # Uncomment for MS implementation
    #cd TPMCmd
    oe_runmake
    #cd ../Samples/ARM32-FirmwareTPM/optee_ta
    #oe_runmake
}

do_install () {
    mkdir -p ${D}/lib/optee_armtz
    install -D -p -m0444 ${S}/out/${FTPM_UUID}.ta ${D}/lib/optee_armtz/
}

FILES_${PN} += "/lib/optee_armtz/${FTPM_UUID}.ta"

# Imports machine specific configs from staging to build
PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP_${PN} += "ldflags"
