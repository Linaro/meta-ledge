inherit image_types

# 120 MB
LEDGE_BOOTFS_IMG_SIZE_KB ??= "122660"

do_image_ledgebootfs[depends] += " \
        mtools-native:do_populate_sysroot \
        e2fsprogs-native:do_populate_sysroot \
        dosfstools-native:do_populate_sysroot \
        virtual/kernel:do_deploy \
        ledge-initramfs:do_image_complete \
        "

LEDGE_BOOTFS_NAME ?= "${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.vfat"

python __anonymous () {
    import re
    target = d.getVar('TARGET_ARCH')
    if target == "x86_64":
        kernel_efi_image = "bootx64.efi"
    elif re.match('i.86', target):
        kernel_efi_image = "bootia32.efi"
    elif re.match('aarch64', target):
        kernel_efi_image = "bootaa64.efi"
    elif re.match('arm', target):
        kernel_efi_image = "bootarm.efi"
    else:
        raise bb.parse.SkipRecipe("kernel efi is incompatible with target %s" % target)
    d.setVar("KERNEL_EFI_IMAGE", kernel_efi_image)
}

IMAGE_CMD_ledgebootfs () {
    # TREE of bootfs
    # ├── dtb
    # |    └── ...
    # ├── EFI
    # │   └── BOOT
    # │       └── bootarm.efi or bootaa64.efi
    # ├── ledge-initramfs.rootfs.cpio.gz

    rm -f ${LEDGE_BOOTFS_NAME}

    # create bootfs filesystem
    mkfs.vfat -n BOOTFS -S 512 -C ${LEDGE_BOOTFS_NAME} ${LEDGE_BOOTFS_IMG_SIZE_KB}

    #populate boot image
    cd ${IMGDEPLOYDIR};
    # put kernel
    mmd -i ${LEDGE_BOOTFS_NAME} ::/EFI
    mmd -i ${LEDGE_BOOTFS_NAME} ::/EFI/BOOT
    for img in ${KERNEL_IMAGETYPE}; do
        mcopy -i ${LEDGE_BOOTFS_NAME} -s ${DEPLOY_DIR_IMAGE}/$img.signed ::/EFI/BOOT/${KERNEL_EFI_IMAGE}
        break;
    done

    # put device tree
    mmd -i ${LEDGE_BOOTFS_NAME} ::/dtb
    find ${DEPLOY_DIR_IMAGE}/dtb/ -name "*.dtb" -type f -exec mcopy -i ${LEDGE_BOOTFS_NAME} -s {} ::/dtb/ \;
    # put initramfs
    mcopy -i ${LEDGE_BOOTFS_NAME} -s ${DEPLOY_DIR_IMAGE}/ledge-initramfs.rootfs.cpio.gz ::/
}

# Final stage - compress and create symlinks
IMAGE_CMD_ledgebootfs_append() {
    (cd ${IMGDEPLOYDIR};ln -sf ${IMAGE_NAME}.bootfs.vfat ${IMAGE_LINK_NAME}.bootfs.vfat)
    (cd ${IMGDEPLOYDIR};gzip -f -9 -c ${IMAGE_NAME}.bootfs.vfat > ${IMAGE_NAME}.bootfs.vfat.gz)
    (cd ${IMGDEPLOYDIR};cp ${IMAGE_NAME}.bootfs.vfat.gz ${IMAGE_LINK_NAME}.bootfs.vfat.gz)
}
