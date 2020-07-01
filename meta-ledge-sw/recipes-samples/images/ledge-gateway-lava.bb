require ledge-gateway.bb

SUMMARY = "Gateway image for LEDGE (for Lava test)"

CORE_IMAGE_BASE_INSTALL += "\
    ${@bb.utils.contains("MACHINE_FEATURES", "tpm2", "packagegroup-ledge-tpm-lava", "", d)} \
    fwts \
    "

