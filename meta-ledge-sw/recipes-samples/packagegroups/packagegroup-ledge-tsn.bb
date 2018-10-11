SUMMARY = "TSN related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-tsn = "\
    openavnu-daemons \
    linuxptp \
    "
