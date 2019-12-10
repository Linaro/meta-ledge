# add dependency to generate bootfs partitions
python __anonymous () {
    # Gather all current tasks
    tasks = filter(lambda k: d.getVarFlag(k, "task", True), d.keys())
    image_partitions = [ "ledge-image-bootfs" ]
    for task in tasks:
        # Check that we are dealing with image recipe
        if task == 'do_image_complete':
            # Init current image name
            current_image_name = d.getVar('PN') or ""
            # Init RAMFS image if any
            initramfs = d.getVar('INITRAMFS_IMAGE') or ""

            # We need to append partition images generation only to image
            # that are not one of the defined partitions and not the InitRAMFS image.
            # Without this check we would create circular dependency
            if current_image_name not in image_partitions and current_image_name != initramfs:
                for partition in image_partitions:
                    d.appendVarFlag('do_image_complete', 'depends', ' %s:do_image_complete' % partition)
}


BOOTFS_IMAGE_NAME = "ledge-image-bootfs-${MACHINE}.ext4"

tsv_fld_template_for_ledge () {
    if [ "${PN}" = "ledge-image-bootfs" ]
    then
        return;
    fi

    cd ${DEPLOY_DIR_IMAGE};

    BOOTFS_IMAGE_NAME_DETECTED=$(readlink ${DEPLOY_DIR_IMAGE}/${BOOTFS_IMAGE_NAME})
    ROOTFS_IMAGE_NAME_DETECTED=$(readlink ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic)
    ROOTFS_IMAGE_NAME_DETECTED_NAME=$(basename ${ROOTFS_IMAGE_NAME_DETECTED} )
    echo "${ROOTFS_IMAGE_NAME_DETECTED} ${ROOTFS_IMAGE_NAME_DETECTED_NAME}"

    #bbwarn "tsv_fld_template_for_ledge: adapt template to image name"

    for f in $(ls -1 *.tsv.template)
    do
        name=$(echo $f | sed "s/tsv\.template/${IMAGE_LINK_NAME}\.tsv/")
        sed "s/%%IMAGE%/${ROOTFS_IMAGE_NAME_DETECTED}/" $f > $name
        sed -i "s/%%BOOTFS%/${BOOTFS_IMAGE_NAME_DETECTED}/" $name
    done
    for f in $(ls -1 *.fld.template);
    do
        name=$(echo $f | sed "s/\.fld\.template/-${IMAGE_LINK_NAME}\.fld/")
        sed "s/%%IMAGE%/${ROOTFS_IMAGE_NAME_DETECTED}/" $f > $name
        sed -i "s/%%BOOTFS%/${BOOTFS_IMAGE_NAME_DETECTED}/" $name
    done

}
IMAGE_POSTPROCESS_COMMAND_append = " tsv_fld_template_for_ledge; "
