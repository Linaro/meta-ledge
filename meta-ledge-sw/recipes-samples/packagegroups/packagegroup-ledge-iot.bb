SUMMARY = "IOT related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-iot = "\
	acl \
	attr \
	audit \
	bash \
	bash-completion \
	brotli \
	ca-certificates \
	cloud-init \
	checkpolicy \
	chkconfig \
	chrony \
	cpio \
	cracklib \
	criu \
	cryptsetup \
	curl \
	diffutils \
	dnsmasq \
	e2fsprogs \
	ebtables \
	expat \
	file \
	findutils \
	freetype \
	fuse \
	gawk \
	gettext \
	gmp \
	gnutls \
	gobject-introspection \
	gpgme \
	grep \
	gzip \
	info \
	ipcalc \
	iptables \
	iputils \
	jansson \
	json-c \
	kbd \
	kmod \
	less \
	libselinux \
	libpam \
	lsof \
	lvm2 \
	lz4 \
	mpfr \
	ncurses \
	nettle \
	net-tools \
	nftables \
	npth \
	openldap \
	openssl \
	ostree \
	parted \
	libpcre \
	libpcre2 \
	pinentry \
	pkgconf \
	policycoreutils \
	polkit \
	popt \
	protobuf \
	protobuf-c \
	python3 \
	readline \
	rng-tools \
	rsync \
	satyr \
	screen \
	sed \
	setools-console \
	sudo \
	systemd \
	tar \
	tmux \
	traceroute \
	tzdata \
	util-linux \
	which \
	wpan-tools \
	xfsprogs \
	xmlrpc-c \
	xz \
	zlib \
	zram \
	"

# Missing packages for Fedora-IoT sync
#anaconda-tui
#atomic
#atomic-devmode
#audit-libs
#augeas-libs
#authconfig
#basesyste m
#bind-export-libs
#blivet-data
#bluez
#bluez-libs
#bubblewrap
#bzip2-libs
#clevis
#clevis-dracut
#clevis-luks
#clevis-systemd
#cloud-utils-growpart
#containernetworking-plugins
#containers-common
#container-selinux
#coreutils-common
#cracklib-dicts
#crypto-policies
#cryptsetup-libs
#cyrus-sasl-lib
#dbus
#dbus-common
#dbus-daemon
#dbus-libs
#dbus-tools
#dbxtool
#device-mapper
#device-mapper-event
#device-mapper-persistent-data
#dhcp-client
#dhcp-common
#dhcp-libs
#dnf-data
#dracut
#dracut-config-generic
#dracut-network
#e2fsprogs-libs
#efi-filesystem
#efivar-libs
#elfutils-default-yama-scope
#elfutils-libelf
#elfutils-libs
#fedora-gpg-keys
#fedora-release
#fedora-release-iot
#fedora-repos
#file-libs
#filesystem
#fipscheck
#fipscheck-lib
#firewalld
#firewalld-filesystem
#fuse-common
#fuse-libs
#fwupdate
#fwupdate-efi
#fwupdate-libs
#gdbm-libs
#gettext-libs
#glib2
#glibc
#glibc-common
#glibc-langpack-en
#gnupg2
#gnupg2
#gomtree
#grub2-common
#grub2-efi-x64
#grub2-pc
#grub2-pc-modules
#grub2-tools
#grub2-tools-extra
#grub2-tools-minimal
#hostname
#ima-evm-utils
#initial-setup
#iproute
#ipset
#ipset-libs
#iptables-libs
#iptables-services
#iwl1000-firmware
#iwl100-firmware
#iwl105-firmware
#iwl135-firmware
#iwl2000-firmware
#iwl2030-firmware
#iwl3160-firmware
#iwl5000-firmware
#iwl5150-firmware
#iwl6000-firmware
#iwl6050-firmware
#iwl7260-firmware
#jose
#json-glib
#kbd-legacy
#kbd-misc
#kernel
#kernel-core
#kernel-modules
#kernel-tools
#kernel-tools-libs
#keyutils-libs
#kmod-libs
#krb5-libs
#langtable
#langtable-data
#libacl
#libaio
#libarchive
#libargon2
#libassuan
#libattr
#libblkid
#libblockdev
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
#libpwquality
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
#libselinux-utils
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
#linux-firmware-whence 
#lua-libs
#luksmeta
#lvm2-libs
#lz4-libs
#meta-core-anaconda
#microcode_ctl
#ModemManager
#ModemManager-glib
#mokutil
#mozjs52
#ncurses-base
#NetworkManager
#NetworkManager-libnm
#NetworkManager-team
#NetworkManager-wifi
#NetworkManager-wwan
#nmap-ncat
#nss-altfiles
#openssh
#openssh-clients
#openssh-clients
#openssh-server
#openssl-libs
#os-prober 
#ostree-grub2 
#ostree-libs
#p11-kit
#p11-kit-trust
#passwd
#pciutils-libs
#pkgconf-m4
#pkgconf-pkg-config
#podman
#policycoreutils-python-utils
#polkit-libs
#polkit-pkla-compat
#procps-ng
#publicsuffix-list-dafsa
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
#python3-sssdconfig
#python3-systemd
#python3-urllib3
#python3-websocket-client
#python-pip-wheel
#python-setuptools-wheel
#qrencode-libs
#rootfiles
#rpm
#rpm-build-libs
#rpm-libs
#rpm-ostree
#rpm-ostree-libs
#rpm-plugin-selinux
#rpm-sign-libs
#runc
#selinux-policy
#selinux-policy-targeted
#setup
#shadow-utils
#shim-x64
#skopeo
#sqlite-libs
#sssd-client
#systemd-libs
#systemd-udev
#teamd
#tpm2-tools
#tpm2-tss
#usbguard
#usermode
#vim-minimal
#wpa_supplicant
#xmlrpc-c-client
#xz-libs
#zchunk-libs
#ncurses-libs
#efibootmgr
