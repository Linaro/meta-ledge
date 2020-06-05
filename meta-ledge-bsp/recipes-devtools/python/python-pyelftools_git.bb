SUMMARY = "pure-python library for parsing ELF and DWARF"
HOMEPAGE = "https://github.com/eliben/pyelftools"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5ce2a2b07fca326bc7c146d10105ccfc"

LICENSE = "unlicense"
NO_GENERIC_LICENSE[unlicense] = "LICENCE"
LICENSE_${PN} = "unlicense"

PV = "0.26"

SRC_URI[md5sum] = "0ba0de4b47127249c4d632ae299cb0e8"
SRC_URI[sha256sum] = "86ac6cee19f6c945e8dedf78c6ee74f1112bd14da5a658d8c9d4103aed5756a2"

inherit pypi
inherit setuptools


BBCLASSEXTEND = "native nativesdk"
