DESCRIPTION = "Install SELINUX ledge addons rules"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += " virtual/refpolicy "

SRC_URI = " \
    file://ledgeEnforcement.pp \
    file://ledgeEnforcement.te \
    file://selinux-ledge.sh \
"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/selinux
    install -m 0644 ledgeEnforcement.pp ${D}${sysconfdir}/selinux/
    install -m 0644 ledgeEnforcement.te ${D}${sysconfdir}/selinux/

    install -d ${D}${bindir}
    install -m 0755 selinux-ledge.sh ${D}${bindir}
}
