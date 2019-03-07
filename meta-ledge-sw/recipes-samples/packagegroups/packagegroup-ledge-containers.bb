SUMMARY = "Containers related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-containers = " \
    docker \
    runc-docker \
    "
