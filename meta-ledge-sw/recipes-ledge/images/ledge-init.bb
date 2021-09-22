SUMMARY = "Basic init for initramfs to mount and pivot root"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "\
    file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://init.ledge \
			file://init.cryptfs"

do_install() {
    install -m 0755 ${WORKDIR}/init.ledge ${D}/init
    install -m 0755 ${WORKDIR}/init.cryptfs ${D}/

    # Create device nodes expected by kernel in initramfs
    # before executing /init.
    install -d "${D}/dev"
    install -d "${D}/run"
    mknod -m 0600 "${D}/dev/console" c 5 1
}

FILES_${PN} = "\
    /init \
    /init.cryptfs \
    /dev \
    /run \
"

RDEPENDS_${PN} += "\
    coreutils \
    util-linux-mount \
    grep \
    gawk \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'udev', 'eudev', d)} \
"
