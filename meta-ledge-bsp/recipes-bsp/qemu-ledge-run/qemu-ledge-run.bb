SECTION = "tools"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

S = "${WORKDIR}/git"
PR = "r1"

inherit deploy

ALLOW_EMPTY_${PN} = "1"

SRC_URI_append_x86-64 = " file://ledge-qemux86_run.sh;apply=no"

do_deploy () {

}

do_deploy_append_ledge-qemux86-64() {
	cp ${WORKDIR}/ledge-qemux86_run.sh ${DEPLOYDIR}/ledge-qemux86_run.sh
	chmod +x ${DEPLOYDIR}/ledge-qemux86_run.sh
}

addtask deploy before do_build after do_compile

DEPENDS = "dtc-native"
PACKAGE_ARCH = "${MACHINE_ARCH}"
