SUMMARY = "Organize packages to avoid duplication across all images"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-network = "\
    openavnu-daemons \
    \
    ethtool \
    linuxptp \
    \
    open62541 \
    open62541-examples \
    "

