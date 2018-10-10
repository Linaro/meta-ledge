SUMMARY = "Network related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-network = " \
    iputils-traceroute6 \
    iputils-clockdiff \
    iputils-tracepath \
    iputils-arping \
    iputils-ping \
    iptables \
    ethtool \
    tcpdump \
    iputils \
    "
