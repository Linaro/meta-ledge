require ledge-minimal-image.bb

SUMMARY = "Basic gateway image with network tools"


CORE_IMAGE_BASE_INSTALL += " \
    packagegroup-ledge-network \
    \
    ${@bb.utils.contains("MACHINE_FEATURES", "optee", "packagegroup-ledge-optee", "", d)} \
"

EXTRA_USERS_PARAMS += "\
useradd -p '' ledge; \
"
