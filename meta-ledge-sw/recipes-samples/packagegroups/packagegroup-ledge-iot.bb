SUMMARY = "IOT related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-iot = "\
	acl \
	attr \
	audit \
	augeas \
	bash \
	bash-completion \
	bluez5 \
	bluez5-noinst-tools \
	brotli \
	bzip2 \
	ca-certificates \
	cloud-init \
	checkpolicy \
	chkconfig \
	chrony \
	coreutils \
	cpio \
	cracklib \
	${@bb.utils.contains_any("TUNE_ARCH", [ "x86_64", "arm" ] , "criu", "", d)} \
	curl \
	diffutils \
	dnf \
	dnsmasq \
	dracut \
	e2fsprogs \
	ebtables \
	efibootmgr \
	efivar \
	elfutils \
	elfutils-binutils \
	expat \
	file \
	findutils \
	freetype \
	fuse \
	gawk \
	gettext \
	gdbm \
	glib-2.0 \
	gmp \
	gnupg \
	gnutls \
	gobject-introspection \
	gpgme \
	grep \
	gzip \
	info \
	ipcalc \
	iproute2 \
	iptables \
	iputils \
	jansson \
	json-c \
	json-glib \
	kbd \
	kbd-keymaps \
	kmod \
	krb5 \
	less \
	libbytesize \
	libpcre \
	libpcre2 \
	libblockdev \
	libpwquality \
	libpam \
	libselinux \
	lsof \
	lua \
	lvm2 \
	lvm2-scripts \
	lvm2-udevrules \
	lz4 \
	modemmanager \
	mozjs \
	mpfr \
	ncurses \
	nettle \
	net-tools \
	nftables \
	nmap \
	npth \
	nss \
	openldap \
	openssl \
	ostree \
	p11-kit \
	parted \
	pciutils \
	pinentry \
	packagegroup-core-ssh-openssh \
	packagegroup-security-tpm2 \
	packagegroup-core-selinux \
	popt \
	procps \
	protobuf \
	protobuf-c \
	python3 \
	python3-asn1crypto \
	python3-babel \
	python3-cffi \
	python3-chardet \
	python3-coverage \
	python3-cryptography \
	python3-dateutil \
	python3-dbus \
	python3-decorator \
	python3-docker \
	python3-docker-pycreds \
	python3-pygobject \
	python3-gpg \
	python3-idna \
	python3-iniparse \
	python3-jinja2 \
	python3-jsonpatch \
	python3-jsonpointer \
	python3-jsonschema \
	python3-pykickstart \
	python3-langtable \
	python3-libreport \
	python3-markupsafe \
	python3-meh \
	python3-ntplib \
	python3-oauthlib \
	python3-ordered-set \
	python3-pid \
	python3-ply \
	python3-pycparser \
	python3-pydbus \
	python3-pyparted \
	python3-pyserial \
	python3-pysocks \
	python3-pytz \
	python3-pyudev \
	python3-pyyaml \
	python3-requests \
	python3-requests-file \
	python3-requests-ftp \
	python3-rpm \
	python3-setuptools \
	python3-six \
	python3-systemd \
	python3-urllib3 \
	python3-websocket-client \
	python-setuptools \
	qrencode \
	readline \
	rng-tools \
	rsync \
	runc-docker \
	satyr \
	screen \
	sed \
	setools \
	shadow \
	sqlite3 \
	sudo \
	systemd \
	tar \
	tmux \
	traceroute \
	tzdata \
	util-linux \
	vim-tiny \
	which \
	wpan-tools \
	wpa-supplicant \
	xfsprogs \
	xz \
	zlib \
	zram \
	"

# Missing packages for Fedora-IoT sync
# INFO about rpm fedora package: https://rpmfind.net/linux/rpm2html/search.php
# anaconda see https://git.yoctoproject.org/git/meta-anaconda
#
# atomic : Tool for managing ProjectAtomic systems and containers
# atomic-devmode
# authconfig : Command line tool for setting up authentication from network services
# basesystem : Skeleton package which defines a simple Mandriva Linux system	Mandriva devel cooker for i586
# bind-export-libs : ISC libs for DHCP application
# bubblewrap : Core execution tool for unprivileged containers
# clevis : Automated decryption framework
# clevis-dracut
# clevis-luks
# clevis-systemd
# cloud-utils-growpart : Script for growing a partition
# containernetworking-plugins : Some CNI network plugins, maintained by the containernetworking team.
# containers-common : Configuration files for working with image signatures
# container-selinux : SELinux policies for container runtimes
# crypto-policies : Systemwide crypto policies
# cyrus-sasl-lib : Shared libraries needed by applications which use Cyrus SASL
# dbxtool : Secure Boot DBX updater
# efi-filesystem : The basic directory layout for EFI machines
# fedora-gpg-keys : Fedora RPM keys
# fedora-release : Fedora release files
# fedora-release-iot : Base package for Fedora IoT specific default configurations
# fedora-repos : Fedora package repositories
# filesystem : Basic Directory Layout
# fipscheck : A library for integrity verification of FIPS validated modules
# fipscheck-lib :  A library for integrity verification of FIPS validated modules
# firewalld : A firewall daemon with D-Bus interface providing a dynamic firewall
# firewalld-filesystem ! Firewalld directory layout and rpm macros
# fwupdate : Tools to manage UEFI firmware updates
# fwupdate-efi : UEFI binaries used by libfwup
# fwupdate-libs : Library to manage UEFI firmware updates
# gdbm-libs : Libraries files for gdbm
# gomtree : Go CLI tool for mtree support
# grub2-common : Provides files common to both grub2 and grub2-efi
# grub2-efi : Boot-loader with support for EFI
# grub2-pc : Bootloader with support for Linux, Multiboot, and more
# grub2-pc-modules : Modules used to build custom grub images
# grub2-tools : Support tools for GRUB.
# grub2-tools-extra : Support tools for GRUB.
# grub2-tools-minimal : Support tools for GRUB.
# ostree-grub2 : GRUB2 integration for OSTree
# ima-evm-utils : IMA/EVM support utilities
# initial-setup : Initial system configuration utility
# ipset : Netfilter ipset administration utility
# ipset-libs : Shared library providing the IP sets functionality
# iptables-services : iptables and ip6tables services for iptables
# iwl1000-firmware : Firmware for Intel® PRO/Wireless 1000 B/G/N network adaptors
# iwl100-firmware :
# iwl105-firmware :
# iwl135-firmware :
# iwl2000-firmware :
# iwl2030-firmware :
# iwl3160-firmware :
# iwl5000-firmware :
# iwl5150-firmware :
# iwl6000-firmware :
# iwl6050-firmware :
# iwl7260-firmware :
# jose : Tools for JSON Object Signing and Encryption (JOSE)
# kernel-tools : Assortment of tools for the Linux kernel
# kernel-tools-libs : Libraries for the kernels-tools
# keyutils-libs : Key utilities library
# langtable : Guessing reasonable defaults for locale, keyboard layout, territory, and language.
#langtable-data : Data files for langtable
#libacl : Dynamic library for access control list support
#libaio : 
#libarchive : 
#libargon2 : 
#libassuan : 
#libattr : 
#libblkid : 
#libblockdev : 
#libblockdev-utils
#libbytesize
#libcap
#libcap-ng
#libcom_err
#libcomps
#libcroco
#libcurl
#libdaemon
#libdb
#libdb-utils
#libdnf
#libedit
#libevent
#libfdisk
#libffi
#libgcc
#libgcrypt
#libgomp
#libgpg-error
#libgpiod
#libgpiod-utils
#libgudev
#libicu
#libidn2
#libjose
#libkcapi
#libkcapi-hmaccalc
#libksba
#libluksmeta
#libmbim
#libmbim-utils
#libmetalink
#libmnl
#libmodman
#libmodulemd1
#libmount
#libndp
#libnet
#libnetfilter_conntrack
#libnfnetlink
#libnftnl
#libnghttp2
#libnl3
#libnl3-cli
#libnsl2
#libpcap
#libpkgconf
#libpng
#libproxy
#libpsl
#libqb
#libqmi
#libqmi-utils
#librepo
#libreport
#libreport-anaconda
#libreport-cli
#libreport-filesystem
#libreport-plugin-bugzilla
#libreport-plugin-reportuploader
#libreport-web
#libseccomp
#libsecret
#libsemanage
#libsepol
#libsigsegv
#libsmartcols
#libsmbios
#libsolv
#libss
#libssh
#libsss_idmap
#libsss_nss_idmap
#libsss_sudo
#libstdc++
#libsysfs
#libtar
#libtasn1
#libteam
#libtirpc
#libunistring
#libusbx
#libuser
#libutempter
#libuuid
#libverto
#libxcrypt
#libxml2
#libyaml
#libzstd
#linux-firmware
# luksmeta : Utility for storing small metadata in the LUKSv1 header
#meta-core-anaconda
# microcode_ctl : Tool to transform and deploy CPU microcode update for x86.
# mokutil: Utilities for managing Secure Boot/MoK keys.
# NetworkManager : Network connection manager and user applications
# NetworkManager-libnm : Libraries for adding NetworkManager support to applications (new API).
# NetworkManager-team : Team device plugin for NetworkManager
# NetworkManager-wifi : Wifi plugin for NetworkManager
# NetworkManager-wwan : Mobile broadband device plugin for NetworkManager
# os-prober: Probes disks on the system for installed operating systems
# ostree-grub2 : GRUB2 integration for OSTree
# ostree-libs : Development headers for ostree
# pkgconf
# podman : Manage Pods, Containers and Container Images
#python3-asn1crypto
#python3-audit
#python3-babel
#python3-blivet
#python3-blockdev
#python3-bytesize
#python3-cffi
#python3-chardet
#python3-configobj
#python3-coverage
#python3-cryptography
#python3-dateutil
#python3-dbus
#python3-decorator
#python3-dnf
#python3-docker
#python3-docker-pycreds
#python3-firewall
#python3-gobject-base
#python3-gpg
#python3-hawkey
#python3-idna
#python3-iniparse
#python3-jinja2
#python3-jsonpatch
#python3-jsonpointer
#python3-jsonschema
# python3-jwt
#python3-kickstart
#python3-langtable
#python3-libcomps
#python3-libdnf
#python3-libreport
#python3-libs
#python3-libselinux
#python3-libsemanage
#python3-markupsafe
#python3-meh
#python3-ntplib
#python3-oauthlib
#python3-ordered-set
#python3-pid
#python3-ply
#python3-policycoreutils
#python3-prettytable
#python3-productmd
#python3-pwquality
#python3-pycparser
#python3-pydbus
#python3-pyOpenSSL
#python3-pyparted
#python3-pyserial
#python3-pysocks
#python3-pytz
#python3-pyudev
#python3-pyyaml
#python3-requests
#python3-requests-file
#python3-requests-ftp
#python3-rpm
#python3-setools
#python3-setuptools
#python3-simpleline
#python3-six
#python3-slip
#python3-slip-dbus
# python3-sssdconfig
#python3-systemd
#python3-urllib3
#python3-websocket-client
#python-pip-wheel
#python-setuptools-wheel
# skopeo : Inspect Docker images and repositories on registries
# rootfiles : The basic required files for the root user's directory
# rpm : The RPM package management system
# rpm-build-libs : Libraries for building RPM packages
# rpm-libs : Libraries for manipulating RPM packages
# rpm-ostree : Hybrid image/package system
# rpm-ostree-libs : Shared library for rpm-ostree
# rpm-plugin-selinux : Rpm plugin for SELinux functionality
# rpm-sign-libs : Libraries for signing RPM packages
# selinux-policy : SELinux policy configuration
# selinux-policy-targeted : SELinux targeted base policy
# setup : A set of system configuration and setup files
# shim-x64 : UEFI shim loader
# teamd : Team network device control daemon
# usbguard: A tool for implementing USB device usage policy
# usermode (OE: provided via meta-openembedded/meta-gnome)
# xmlrpc-c : Programming library for writing an XML-RPC server or client in C or C++ (OE available on native only)
# xmlrpc-c-client
# zchunk-libs: Zchunk library

