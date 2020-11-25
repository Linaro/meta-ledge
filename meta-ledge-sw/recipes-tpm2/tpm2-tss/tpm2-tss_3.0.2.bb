SUMMARY = "Software stack for TPM2."
DESCRIPTION = "OSS implementation of the TCG TPM2 Software Stack (TSS2) "
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=500b2e742befc3da00684d8a1d5fd9da"
SECTION = "tpm"

DEPENDS = "autoconf-archive-native libgcrypt openssl json-c curl"

SRCREV = "a99e733ba66c359502689a9c42fd5e02ed1dd7d6"

SRC_URI = "https://github.com/tpm2-software/${BPN}/releases/download/${PV}/${BPN}-${PV}.tar.gz"
SRC_URI[md5sum] = "6fbb683dc0bfa9bf6f2bcc49ad880c38"
SRC_URI[sha256sum] = "8f3b7ac8b7404a361013d1adb38be33f69384f2f9fbb44dabe94597f63ab0e00"
SRC_URI[sha1sum] = "72e9beb5d01879056d23aa574f0e8fd7087eb684"
SRC_URI[sha384sum] = "9540b8f05b684de1ccabf575bbb58f35605a1d7470f40f53de1c2b67aab71cf98245d4e1a02a0c9b0d50b0e3e5142e50"
SRC_URI[sha512sum] = "0b295bb1a135c904344e8a11ae9a293f71d60ea045bacd59a9b778ba19e88e1b91022f24a4775615c215db450584574901838146bc4d4d286ee8b686b72b0bab"

inherit autotools pkgconfig systemd extrausers

PACKAGECONFIG ??= ""
PACKAGECONFIG[oxygen] = ",--disable-doxygen-doc, "

EXTRA_OECONF += "--enable-static --with-udevrulesdir=${base_prefix}/lib/udev/rules.d/"
EXTRA_OECONF_remove = " --disable-static"


EXTRA_USERS_PARAMS = "\
	useradd -p '' tss; \
	groupadd tss; \
	"

PROVIDES = "${PACKAGES}"
PACKAGES = " \
    ${PN} \
    ${PN}-dbg \
    ${PN}-doc \
    libtss2-mu \
    libtss2-mu-dev \
    libtss2-mu-staticdev \
    libtss2-tcti-device \
    libtss2-tcti-device-dev \
    libtss2-tcti-device-staticdev \
    libtss2-tcti-mssim \
    libtss2-tcti-mssim-dev \
    libtss2-tcti-mssim-staticdev \
    libtss2 \
    libtss2-dev \
    libtss2-staticdev \
"

FILES_libtss2-tcti-device = "${libdir}/libtss2-tcti-device.so.*"
FILES_libtss2-tcti-device-dev = " \
    ${includedir}/tss2/tss2_tcti_device.h \
    ${libdir}/pkgconfig/tss2-tcti-device.pc \
    ${libdir}/libtss2-tcti-device.so"
FILES_libtss2-tcti-device-staticdev = "${libdir}/libtss2-tcti-device.*a"

FILES_libtss2-tcti-mssim = "${libdir}/libtss2-tcti-mssim.so.*"
FILES_libtss2-tcti-mssim-dev = " \
    ${includedir}/tss2/tss2_tcti_mssim.h \
    ${libdir}/pkgconfig/tss2-tcti-mssim.pc \
    ${libdir}/libtss2-tcti-mssim.so"
FILES_libtss2-tcti-mssim-staticdev = "${libdir}/libtss2-tcti-mssim.*a"

FILES_libtss2-mu = "${libdir}/libtss2-mu.so.*"
FILES_libtss2-mu-dev = " \
    ${includedir}/tss2/tss2_mu.h \
    ${libdir}/pkgconfig/tss2-mu.pc \
    ${libdir}/libtss2-mu.so"
FILES_libtss2-mu-staticdev = "${libdir}/libtss2-mu.*a"

FILES_libtss2 = "${libdir}/libtss2*so.*"
FILES_libtss2-dev = " \
    ${includedir} \
    ${libdir}/pkgconfig \
    ${libdir}/libtss2*so"
FILES_libtss2-staticdev = "${libdir}/libtss*a"

FILES_${PN} = "${libdir}/udev ${base_prefix}/lib/udev /etc/"

RDEPENDS_libtss2 = "libgcrypt"
