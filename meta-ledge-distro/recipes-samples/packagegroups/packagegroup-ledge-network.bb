SUMMARY = "Organize packages to avoid duplication across all images"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-network = "\
    openavnu-daemons \
    \
    ethtool \
    linuxptp \
    tcpdump \
    iputils \
    iputils-arping \
    iputils-clockdiff \
    iputils-ping \
    iputils-tracepath \
    iputils-traceroute6 \
    \
    open62541 \
    open62541-examples \
    "

