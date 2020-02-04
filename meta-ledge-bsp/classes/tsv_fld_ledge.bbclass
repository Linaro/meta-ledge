BOOTFS_IMAGE_NAME = "${IMAGE_LINK_NAME}.bootfs.vfat"

tsv_fld_template_for_ledge () {
    if [ "${PN}" = "${INITRAMFS_IMAGE}" ];
    then
        return;
    fi

    cd ${DEPLOY_DIR_IMAGE};

    BOOTFS_IMAGE_NAME_DETECTED=$(readlink ${IMGDEPLOYDIR}/${BOOTFS_IMAGE_NAME})
    ROOTFS_IMAGE_NAME_DETECTED=$(readlink ${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic)
    ROOTFS_IMAGE_NAME_DETECTED_NAME=$(basename ${ROOTFS_IMAGE_NAME_DETECTED} )
    echo "${ROOTFS_IMAGE_NAME_DETECTED} ${ROOTFS_IMAGE_NAME_DETECTED_NAME}"

    #bbwarn "tsv_fld_template_for_ledge: adapt template to image name"

    for f in $(ls -1 *.tsv.template)
    do
        name=$(echo $f | sed "s/tsv\.template/${IMAGE_LINK_NAME}\.tsv/")
        sed "s/%%IMAGE%/${ROOTFS_IMAGE_NAME_DETECTED}.bin/" $f > $name
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
