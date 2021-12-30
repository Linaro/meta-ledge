FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"
#
# Do not preset console speed to OE setting. Console tty and speed
# are specified by kernel parameter and systemd should respect it.
#
SRC_URI = "file://serial-getty@.service"
