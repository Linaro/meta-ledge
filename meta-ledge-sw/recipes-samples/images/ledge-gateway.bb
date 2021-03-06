require ledge-minimal.bb

SUMMARY = "Basic console image for LEDGE"

# For development, use ssh-server-dropbear instead of ssh-server-openssh which
# allow root login via ssh.
IMAGE_FEATURES += "package-management ssh-server-dropbear allow-empty-password"

CORE_IMAGE_BASE_INSTALL += "\
    sudo \
    coreutils \
    cpufrequtils \
    bluez5-noinst-tools \
    docker \
    ostree \
    pciutils \
    packagegroup-ledge-network \
    packagegroup-ledge-containers \
    packagegroup-ledge-python3 \
    packagegroup-core-selinux \
    auditd \
    audit-python \
    audispd-plugins \
    selinux-ledge \
    ${@bb.utils.contains("MACHINE_FEATURES", "optee", "packagegroup-ledge-optee", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "tsn", "packagegroup-ledge-tsn", "", d)} \
    ${@bb.utils.contains("MACHINE_FEATURES", "tpm2", "packagegroup-security-tpm2", "", d)} \
    \
    efivar \
    systemd-analyze \
    "

# docker pulls runc/containerd, which in turn recommend lxc unecessarily
BAD_RECOMMENDATIONS_append = "lxc"
