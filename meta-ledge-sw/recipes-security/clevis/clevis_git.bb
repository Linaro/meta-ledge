SUMMARY = "Clevis is a pluggable framework for automated decryption."
DESCRIPTION = "Clevis is a pluggable framework for automated decryption. It can be used to provide automated decryption of data or even automated unlocking of LUKS volumes."
HOMEPAGE = "https://github.com/latchset/clevis"
SECTION = "networking"
PR="r2"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	git://github.com/latchset/clevis.git;protocol=git;branch=master \
	file://0001-do-not-search-for-installed-binaries.patch \
	file://0002-clevis-tpm-tools-can-be-built-without-version-specif.patch \
	file://0003-encrypt-bash-fix.patch \
"
SRCREV="d8a25e36ba73d91419c69c3ad23d3a24b393d049"

S="${WORKDIR}/git"

inherit meson pkgconfig systemd

DEPENDS = "jose cmake-native luksmeta openssl tpm2-tools cryptsetup jq libpwquality systemd udisks2"
RDEPENDS_${PN} = "bash cryptsetup libpwquality"

FILES_${PN} += "/usr /lib"

