DESCRIPTION = "Init scripts for use on cloud images"
HOMEPAGE = "https://launchpad.net/cloud-init"
SECTION = "devel/python"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c6dd79b6ec2130a3364f6fa9d6380408"

PR = "r0"

SRC_URI = "https://launchpad.net/cloud-init/trunk/${PV}/+download/${BPN}-${PV}.tar.gz \
           file://distros-add-windriver-skeleton-distro-file.patch"


SRC_URI[md5sum] = "7cd3ad83f18f03d1b4b5f65f67fd795b"
SRC_URI[sha256sum] = "75be8cbff1431883227c05356cb69400f20bbb2666fd05e085f846ecf1d153cb"

S = "${WORKDIR}/${BPN}-${PV}"

DISTUTILS_INSTALL_ARGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '--init-system=sysvinit_deb', '', d)}"
DISTUTILS_INSTALL_ARGS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--init-system=systemd', '', d)}"

MANAGE_HOSTS ?= "False"
HOSTNAME ?= ""

do_install_prepend() {
    sed -e 's:/lib/${BPN}:${base_libdir}/${BPN}:' -i ${S}/setup.py
}

do_install_append() {
    ln -s ${libdir}/${BPN}/uncloud-init ${D}${sysconfdir}/cloud/uncloud-init
    ln -s ${libdir}/${BPN}/write-ssh-key-fingerprints ${D}${sysconfdir}/cloud/write-ssh-key-fingerprints
}

inherit setuptools3 update-rc.d

PACKAGES += "${PN}-systemd"

FILES_${PN} += "${sysconfdir}/* \
                ${datadir}/*"

FILES_${PN}-systemd += "${systemd_unitdir}/*"

INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME_${BPN} = "cloud-init"

RDEPENDS_${PN} = "dmidecode \
		  python3-certifi \
		  python3-chardet \
		  python3-configobj \
		  python3-idna \
		  python3-modules \
		  python3-pyyaml \
		  python3-requests \
		  python3-urllib3 \
		  python3-six \
		  python3-jinja2 \
		 "

DEPENDS += "python3-certifi-native \
	    python3-chardet-native \
	    python3-idna-native \
	    python3-jinja2-native \
	    python3-markupsafe-native \
	    python3-requests-native \
	    python3-setuptools-native \
	    python3-six-native \
	    python3-urllib3-native \
	    python3-pyyaml-native \
          "
