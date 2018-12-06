# Copyright (C) 2017, STMicroelectronics - All Rights Reserved
DESCRIPTION = "HummingBoard script to attach the Bluethooth at startup"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
DEPENDS = "systemd"

PV = "1.0"

SRC_URI = " \
	file://hummingboard_btattach \
	file://hummingboard_btattach.service \
	file://hummingboard_btattach.sh \
"

#INITSCRIPT_PACKAGES = "${PN}"
#INITSCRIPT_NAME = "hummingboard_btattach"
#INITSCRIPT_PARAMS = "defaults"

#inherit update-rc.d
inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "hummingboard_btattach.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
	#install -d ${D}${sysconfdir}/init.d
	#install -m 0755 ${WORKDIR}/hummingboard_btattach ${D}${sysconfdir}/init.d/hummingboard_btattach

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${systemd_unitdir}/system ${D}${bindir}
		install -m 0644 ${WORKDIR}/hummingboard_btattach.service ${D}${systemd_unitdir}/system/
		install -m 0755 ${WORKDIR}/hummingboard_btattach.sh ${D}${bindir}/
	fi
}
