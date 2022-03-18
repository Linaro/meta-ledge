FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DESCRIPTION = "Small ramdisk image for running tests (bootrr, etc)"
PR="r3.ledge"

# Always fetch the latest initramfs image
do_install[nostamp] = "1"

# packagegroup-luks-initramfs
PACKAGE_INSTALL = " \
   packagegroup-tpm2-initramfs \
   packagegroup-luks \
   ${VIRTUAL-RUNTIME_base-utils} \
   base-passwd \
   ${ROOTFS_BOOTSTRAP_INSTALL} \
   coreutils \
   util-linux-mount \
   grep \
   gawk \
   bash \
   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'udev', 'eudev', d)} \
   rng-tools \
   kernel-module-dm-crypt \
   kernel-module-tpm-tis \
   kernel-module-tpm-tis-core \
   ${@bb.utils.contains("MACHINE_FEATURES", "optee", "optee-client", "", d)} \
   e2fsprogs-mke2fs \
   tpm2-abrmd \
   tpm2-tss \
   tpm2-tss-engine \
   dbus \
   clevis \
   ledge-init \
"

PACKAGE_INSTALL_append_ledge-qemuarm += " \
		kernel-module-tpm-ftpm-tee "

PACKAGE_INSTALL_append_ledge-qemuarm64 += " \
		kernel-module-tpm-ftpm-tee "


# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_NAME = "ledge-initramfs"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

IMAGE_ROOTFS_SIZE = "65536"
IMAGE_ROOTFS_EXTRA_SPACE = "4096"

# Disable installation of kernel and modules via packagegroup-core-boot
NO_RECOMMENDATIONS = "1"
