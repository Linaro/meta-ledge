SUMMARY = "IOT related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-iot = "\
	acl \
	anaconda-core \
	anaconda-tui \
	atomic \
	atomic-devmode \
	attr \
	audit \
	audit-libs \
	augeas-libs \
	bash \
	bash-completion \
	blivet-data \
	bluez \
	brotli \
	bzip2-libs \
	ca-certificates \
	checkpolicy \
	chkconfig \
	chrony \
	cloud-init \
	cloud-utils-growpart \
	containers-common \
	container-selinux \
	coreutils \
	cpio \
	cracklib \
	cracklib-dicts \
	criu \
	cryptsetup \
	cryptsetup-libs \
	curl \
	dbxtool \
	device-mapper \
	device-mapper-event \
	device-mapper-persistent-data \
	dhcp-client \
	dhcp-common \
	dhcp-libs \
	diffutils \
	dnf-data \
	dnsmasq \
	dracut \
	dracut-config-generic \
	dracut-network \
	e2fsprogs \
	e2fsprogs-libs \
	ebtables \
	efibootmgr \
	efi-filesystem \
	efivar-libs \
	elfutils-default-yama-scope \
	elfutils-libelf \
	elfutils-libs
	expat \
	file \
	file-libs \
	findutils \
	fipscheck \
	firewalld \
	firewalld-filesystem \
	freetype \
	fuse \
	fwupdate \
	fwupdate-efi \
	fwupdate-libs \
	gawk \
	gettext \
	gettext-libs \
	glib2 \
	glibc \
	glibc-common \
	gmp \
	gnupg2 \
	gnutls \
	gobject-introspection \
	gomtree \
	gpgme \
	grep \
	gzip \
	hostname \
	ima-evm-utils \
	info \
	initial-setup \
	ipcalc \
	iproute \
	ipset \
	ipset-libs
	iptables \
	iptables-libs \
	iptables-services \
	iputils \
	jansson \
	jose \
	json-c \
	json-glib \
	kbd \
	kbd-legacy \
	kmod \
	kmod-libs \
	krb5-libs \
	langtable \
	less \
	linux-firmware-whence \
	lsof \
	lua-libs \
	lvm2 \
	lvm2-libs \
	lz4 \
	lz4-libs \
	microcode_ctl \
	ModemManager \
	ModemManager-glib \
	mokutil \
	mozjs52 \
	mpfr \
	ncurses \
	ncurses-base \
	ncurses-libs \
	nettle \
	net-tools \
	nftables \
	nmap-ncat \
	npth \
	nss-altfiles \
	openldap \
	openssh \
	openssh-clients \
	openssh-server \
	openssl \
	openssl-libs \
	os-prober \
	ostree \
	ostree-libs \
	p11-kit \
	p11-kit-trust \
	pam \
	parted \
	passwd \
	pciutils-libs
	pcre \
	pcre2 \
	pinentry \
	pkgconf \
	pkgconf-m4
	pkgconf-pkg-config \
	podman \
	policycoreutils \
	policycoreutils-python-utils \
	polkit \
	polkit-libs \
	polkit-pkla-compat \
	popt \
	procps-ng \
	protobuf \
	protobuf-c \
	publicsuffix-list-dafsa \
	python3 \
	python3-asn1crypto \
	python3-audit \
	python3-babel \
	python3-blivet \
	python3-blockdev \
	python3-bytesize \
	python3-cffi \
	python3-chardet \
	python3-configobj \
	python3-coverage \
	python3-cryptography \
	python3-dateutil \
	python3-dbus \
	python3-decorator \
	python3-dnf \
	python3-docker \
	python3-docker-pycreds \
	python3-firewall \
	python3-gobject-base \
	python3-gpg \
	python3-hawkey \
	python3-idna \
	python3-iniparse \
	python3-jinja2 \
	python3-jsonpatch \
	python3-jsonpointer \
	python3-jsonschema \
	python3-jwt \
	python3-kickstart \
	python3-langtable \
	python3-libcomps \
	python3-libdnf \
	python3-libreport \
	python3-libs \
	python3-libselinux \
	python3-libsemanage \
	python3-markupsafe \
	python3-meh \
	python3-ntplib \
	python3-oauthlib \
	python3-ordered-set \
	python3-pid \
	python3-ply \
	python3-policycoreutils \
	python3-prettytable \
	python3-productmd \
	python3-pwquality \
	python3-pycparser \
	python3-pydbus \
	python3-pyOpenSSL \
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
	python3-setools \
	python3-setuptools \
	python3-simpleline \
	python3-six \
	python3-slip \
	python3-slip-dbus \
	python3-sssdconfig \
	python3-systemd \
	python3-urllib3 \
	python3-websocket-client \
	python-pip-wheel \
	python-setuptools-wheel \
	qrencode-libs \
	readline \
	rng-tools \
	rootfiles \
	rsync \
	runc \
	satyr \
	screen \
	sed \
	selinux-policy \
	selinux-policy-targeted \
	setools-console \
	shadow-utils \
	skopeo \
	sqlite-libs \
	sssd-client \
	sudo \
	systemd \
	systemd-libs \
	systemd-pam \
	systemd-udev \
	tar \
	teamd \
	tmux \
	traceroute \
	tzdata \
	usbguard \
	usermode \
	util-linux \
	vim-minimal
	which \
	wpan-tools \
	wpa_supplicant \
	xfsprogs \
	xmlrpc-c \
	xmlrpc-c-client \
	xz \
	xz-libs \
	zchunk-libs \
	zlib \
	zram \
	"

# Missing packages for Fedora-IoT sync
#authconfig
#basesyste m
#bind-export-libs
#bluez-libs
#bubblewrap
#clevis
#clevis-dracut
#clevis-luks
#clevis-systemd
#containernetworking-plugins
#coreutils-common
#crypto-policies
#cyrus-sasl-lib
#dbus
#dbus-common
#dbus-daemon
#dbus-libs
#dbus-tools
#fedora-gpg-keys
#fedora-release
#fedora-release-iot
#fedora-repos
#filesystem
#fipscheck-lib
#fuse-common
#gdbm-libs
#fuse-libs
#glibc-langpack-en
#grub2-common
#grub2-efi-x64
#grub2-pc
#grub2-pc-modules
#grub2-tools
#grub2-tools-extra
#grub2-tools-minimal
#ostree-grub2 
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
#kbd-misc
#kernel
#kernel-core
#kernel-modules
#kernel-tools
#kernel-tools-libs
#keyutils-libs
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
#libselinux
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
#luksmeta
#NetworkManager
#NetworkManager-libnm
#NetworkManager-team
#NetworkManager-wifi
#NetworkManager-wwan
#setup
#rpm
#rpm-build-libs
#rpm-libs
#rpm-ostree
#rpm-ostree-libs
#rpm-plugin-selinux
#rpm-sign-libs
#tpm2-tools
#tpm2-tss
#shim-x64 
