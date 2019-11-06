# This image are dedicated to bootfs partition

SUMMARY = "Ledge bootfs Image"
LICENSE = "MIT"

# force bootfs image type
IMAGE_FSTYPES = "tar.xz ext4"

inherit core-image

IMAGE_NAME_SUFFIX = ".bootfs"

# Specific for UBI volume
UBI_VOLNAME = "boot"

# Reset image feature
IMAGE_FEATURE = ""

# Set ROOTFS_MAXSIZE to expected ROOTFS_SIZE to use the whole disk partition and leave extra space to user
IMAGE_ROOTFS_SIZE     = "${LEDGE_BOOTFS_PARTITION_SIZE}"
IMAGE_ROOTFS_MAXSIZE  = "${LEDGE_BOOTFS_PARTITION_SIZE}"
IMAGE_OVERHEAD_FACTOR = "1"

# Reset PACKAGE_INSTALL to avoid getting installed packages added in machine through IMAGE_INSTALL_append:
PACKAGE_INSTALL = ""

# Reset LINGUAS_INSTALL to avoid getting installed any locale-base package
LINGUAS_INSTALL = ""
IMAGE_LINGUAS = ""

# Reset LDCONFIG to avoid runing ldconfig on image.
LDCONFIGDEPEND = ""

# Remove from IMAGE_PREPROCESS_COMMAND useless buildinfo
IMAGE_PREPROCESS_COMMAND_remove = "buildinfo;"

# Remove from IMAGE_PREPROCESS_COMMAND the prelink_image as it could be run after
# we clean rootfs folder leading to cp error if '/etc/' folder is missing:
#   cp: cannot create regular file
#   ‘/local/YOCTO/build/tmp-glibc/work/stm32mp1-openstlinux_weston-linux-gnueabi/st-image-userfs/1.0-r0/rootfs/etc/prelink.conf’:
#   No such file or directory
IMAGE_PREPROCESS_COMMAND_remove = "prelink_image;"

IMAGE_PREPROCESS_COMMAND_append = "reformat_rootfs;"

# Cleanup rootfs newly created and keep only /boot content at root location
reformat_rootfs() {
    if [ -d ${IMAGE_ROOTFS}/boot ]; then
        # Keep only /boot folder
        for f in $(ls -1 ${IMAGE_ROOTFS} | grep -v boot)
        do
            rm -rf ${IMAGE_ROOTFS}/${f}
        done

        # Move all expected files in /rootfs
        mv ${IMAGE_ROOTFS}/boot/* ${IMAGE_ROOTFS}/
        # Remove empty boot folder
        rm -rf ${IMAGE_ROOTFS}/boot/
    else
        bbwarn "/boot/ folder not available in rootfs folder, no reformat done..."
    fi
}

# Add specific package for our image:
PACKAGE_INSTALL += " \
    ${LEDGE_BOOTFS_PACKAGES} \
"
