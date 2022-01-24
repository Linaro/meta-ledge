require ledge-minimal.bb

SUMMARY = "Basic console image for LEDGE IoT"

# For development, use ssh-server-dropbear instead of ssh-server-openssh which
# allow root login via ssh.
IMAGE_FEATURES += "package-management ssh-server-openssh allow-empty-password"

CORE_IMAGE_BASE_INSTALL += "\
    packagegroup-ledge-iot \
    ${@bb.utils.contains("MACHINE_FEATURES", "optee", "packagegroup-ledge-optee", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "tsn", "packagegroup-ledge-tsn", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "tpm2", "packagegroup-security-tpm2", "", d)} \
    \
    efivar \
    systemd-analyze \
    openvpn	\
    "
