IMAGE_FEATURES_remove = "ssh-server-openssh"
IMAGE_FEATURES_append = " ssh-server-dropbear allow-empty-password "

# add tools for TSN work
CORE_IMAGE_BASE_INSTALL += "openavu-daemons ethtool linuxptp"