SUMMARY = "Basic console-based gateway image"

IMAGE_FEATURES += "splash package-management ssh-server-openssh hwcodecs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit core-image distro_features_check extrausers

# let's make sure we have a good image..
REQUIRED_DISTRO_FEATURES = "pam systemd"

# addons for gateway usage
# 25-bt-6lowpan.network: systemd network configi for bt0
# modules-gateway.conf: launch at startup 6lowpan kernel module
# sysctl-gateway.conf: configuraton of linux for oops and kernel panic
SRC_URI = "\
    file://25-bt-6lowpan.network \
    file://modules-gateway.conf \
    file://sysctl-gateway.conf \
"

CORE_IMAGE_BASE_INSTALL += " \
    docker \
    bluez5-noinst-tools \
    linux-firmware-ar3k \
    linux-firmware-ath9k \
    linux-firmware-ath10k \
    linux-firmware-qca \
    linux-firmware-wl18xx \
    openssh-sftp-server \
    packagegroup-core-full-cmdline-utils \
    packagegroup-core-full-cmdline-extended \
    packagegroup-core-full-cmdline-multiuser \
    \
    alsa-utils-aplay \
    coreutils \
    cpufrequtils \
    gptfdisk \
    hostapd \
    htop \
    iptables \
    kernel-modules \
    networkmanager \
    networkmanager-nmtui \
    openssh-sftp-server \
    python-misc \
    python-modules \
    rsync \
    sshfs-fuse \
    \
    python-compression \
    python-distutils \
    python-json \
    python-netclient \
    python-pkgutil \
    python-shell \
    python-subprocess \
    python-unixadmin \
    pciutils \
    strace \
    tcpdump \
    vim-tiny \
    \
    coreutils \
    cpufrequtils \
    hostapd \
    htop \
    iptables \
    kernel-modules \
"

fakeroot do_populate_rootfs_src () {
    # Allow sudo group users to use sudo
    echo "%sudo ALL=(ALL) ALL" >> ${IMAGE_ROOTFS}/etc/sudoers

    # Local configs that are specific to this image
    cp ${WORKDIR}/25-bt-6lowpan.network ${IMAGE_ROOTFS}/etc/systemd/network/
    cp ${WORKDIR}/modules-gateway.conf ${IMAGE_ROOTFS}/etc/modules-load.d/
    cp ${WORKDIR}/sysctl-gateway.conf ${IMAGE_ROOTFS}/etc/sysctl.d/

    # Disable bluetooth service by default (allow to be contained in docker)
    #ln -sf /dev/null ${IMAGE_ROOTFS}/etc/systemd/system/bluetooth.service

    # Useful for development
    echo 'export PATH=$PATH:/sbin' >> ${IMAGE_ROOTFS}/home/linaro/.bashrc
}

IMAGE_PREPROCESS_COMMAND += "do_populate_rootfs_src; "

addtask rootfs after do_unpack

python () {
    # Ensure we run these usually noexec tasks
    d.delVarFlag("do_fetch", "noexec")
    d.delVarFlag("do_unpack", "noexec")
}

# docker pulls runc/containerd, which in turn recommend lxc unecessarily
BAD_RECOMMENDATIONS_append = " lxc"

EXTRA_USERS_PARAMS = "\
useradd -P linaro linaro; \
usermod -a -G docker,sudo,users,plugdev linaro; \
"
