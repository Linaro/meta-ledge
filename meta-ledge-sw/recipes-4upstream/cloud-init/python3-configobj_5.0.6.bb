DESCRIPTION = "ConfigObj is a simple but powerful config file reader and writer"
LICENSE = "BSD"

LIC_FILES_CHKSUM = "file://PKG-INFO;beginline=8;endline=8;md5=23f9ad5cad3d8cc0336e2a5d8a87e1fa"

SRC_URI = "https://pypi.python.org/packages/source/C/ConfigObj/configobj-${PV}.tar.gz"

inherit setuptools3

S = "${WORKDIR}/configobj-${PV}"

SRC_URI[md5sum] = "e472a3a1c2a67bb0ec9b5d54c13a47d6"
SRC_URI[sha256sum] = "a2f5650770e1c87fb335af19a9b7eb73fc05ccf22144eb68db7d00cd2bcb0902"
