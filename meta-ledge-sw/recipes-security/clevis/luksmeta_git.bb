SUMMARY = "library for storing metadata in the LUKSv1 header"
DESCRIPTION = "library for storing metadata in the LUKSv1 header"
HOMEPAGE = "https://github.com/latchset/luksmeta"
SECTION = "networking"
PR="r1"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	git://github.com/latchset/luksmeta.git;protocol=git;branch=master \
"
SRCREV="fb67087a2cc87bf4d6a1b77589a3230118968607"

S="${WORKDIR}/git"

inherit pkgconfig autotools

DEPENDS = "zlib jansson openssl cryptsetup"

FILES_${PN} += "/usr"
