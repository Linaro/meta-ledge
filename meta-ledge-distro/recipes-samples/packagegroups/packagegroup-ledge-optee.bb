SUMMARY = "Organize packages to avoid duplication across all images"

inherit packagegroup

RDEPENDS_packagegroup-ledge-optee = "\
    optee-test \
    optee-client\
    \
    optee-ledge \
    "

