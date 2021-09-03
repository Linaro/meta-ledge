SUMMARY = "José is a C-language implementation of the Javascript Object Signing and Encryption standards."
DESCRIPTION = "José is a C-language implementation of the Javascript Object Signing and Encryption standards."
HOMEPAGE = "https://github.com/latchset/jose"
SECTION = "networking"
PR="r1"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	git://github.com/latchset/jose.git;protocol=git;branch=master \
"
SRCREV="145c41a4ec70c15f6f8aa12a915e16cb60f0991f"

S="${WORKDIR}/git"

inherit meson pkgconfig systemd

DEPENDS = "zlib cmake-native jansson openssl"

FILES_${PN} += "/usr"
