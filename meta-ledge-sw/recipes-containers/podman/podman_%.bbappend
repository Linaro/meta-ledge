FILESEXTRAPATH_prepend := "${THISDIR}/${PN}"

PR = "r04.ledge"

SRCREV = "ab4d0cf908e9d24d321b52b419ebfb4ab5802029"
PV = "3.2.1+git${SRCPV}"
SRC_URI = " \
     git://github.com/containers/libpod.git;branch=v3.2 \
 "
