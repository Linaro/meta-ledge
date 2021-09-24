DESCRIPTION = "script for creating raw SDCARD image ready to flash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://create_raw_from_flashlayout.sh"

BBCLASSEXTEND = "native nativesdk"

RRECOMMENDS_${PN}_append_class-nativesdk = "nativesdk-gptfdisk"

inherit deploy

SCRIPT_DEPLOYDIR ?= "scripts"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/create_raw_from_flashlayout.sh ${D}/${bindir}
}

do_deploy() {
    install -d ${DEPLOYDIR}/${SCRIPT_DEPLOYDIR}
    install -m 0755 ${WORKDIR}/create_raw_from_flashlayout.sh ${DEPLOYDIR}/${SCRIPT_DEPLOYDIR}/
}
addtask deploy before do_build after do_compile

