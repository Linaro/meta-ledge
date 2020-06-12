SUMMARY = "OP-TEE related packages"

inherit packagegroup

RDEPENDS_packagegroup-ledge-optee = "\
    optee-client\
    optee-test \
    optee-ledge \
    ${@bb.utils.contains("MACHINE_FEATURES", "ftpm", "", "", d)} \
    "
