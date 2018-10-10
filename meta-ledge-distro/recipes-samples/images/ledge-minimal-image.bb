require recipes-samples/images/rpb-minimal-image.bb

SUMMARY = "Basic console image"

# For development, use ssh-server-dropbear instead of ssh-server-openssh which
# allow root login via ssh.
IMAGE_FEATURES += "package-management hwcodecs \
        ssh-server-dropbear allow-empty-password"

CORE_IMAGE_BASE_INSTALL += " \
    packagegroup-rpb \
"

# docker pulls runc/containerd, which in turn recommend lxc unecessarily
BAD_RECOMMENDATIONS_append = " lxc"
