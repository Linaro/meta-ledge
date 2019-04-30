require ledge-minimal.bb

SUMMARY = "Basic console image for LEDGE IoT"

# For development, use ssh-server-dropbear instead of ssh-server-openssh which
# allow root login via ssh.
IMAGE_FEATURES += "package-management ssh-server-dropbear allow-empty-password"

CORE_IMAGE_BASE_INSTALL += "\
    coreutils \
    packagegroup-ledge-iot \
    "
