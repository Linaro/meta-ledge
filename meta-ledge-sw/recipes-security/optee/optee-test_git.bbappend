FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

EXTRA_OEMAKE_append_armv7a = " COMPILE_NS_USER=32"
EXTRA_OEMAKE_append_armv7e = " COMPILE_NS_USER=32"

SRC_URI_append_arm = " file://0002-Correct-support-of-32bits.patch "
