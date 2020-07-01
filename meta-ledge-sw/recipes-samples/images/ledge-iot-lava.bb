require ledge-iot.bb

SUMMARY = "IOT image for LEDGE (for Lava test)"

CORE_IMAGE_BASE_INSTALL += "\
    ${@bb.utils.contains("MACHINE_FEATURES", "tpm2", "packagegroup-ledge-tpm-lava", "", d)} \
    fwts \
    "

