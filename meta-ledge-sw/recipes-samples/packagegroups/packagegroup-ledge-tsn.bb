SUMMARY = "TSN related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-tsn = "\
    openavnu-daemons \
    linuxptp \
    open62541 \
    open62541-examples \
    "
