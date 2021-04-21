HOMEPAGE = "https://podman.io/"
SUMMARY =  "A daemonless container engine"
DESCRIPTION = "Podman is a daemonless container engine for developing, \
    managing, and running OCI Containers on your Linux System. Containers can \
    either be run as root or in rootless mode. Simply put: \
    `alias docker=podman`. \
    "
PR = "r03.ledge"
PARALLEL_MAKE = ""

DEPENDS = " \
    go-metalinter-native \
    go-md2man-native \
    gpgme \
    libseccomp \
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    libselinux-native \
"

python __anonymous() {
    msg = ""
    # ERROR: Nothing PROVIDES 'libseccomp' (but meta-virtualization/recipes-containers/podman/ DEPENDS on or otherwise requires it).
    # ERROR: Required build target 'meta-world-pkgdata' has no buildable providers.
    # Missing or unbuildable dependency chain was: ['meta-world-pkgdata', 'podman', 'libseccomp']
    if 'security' not in d.getVar('BBFILE_COLLECTIONS').split():
        msg += "Make sure meta-security should be present as it provides 'libseccomp'"
        raise bb.parse.SkipRecipe(msg)
}

SRCREV = "444a19cdd2e6108c75f6c1aadc1a2a9138a8bd73"
SRC_URI = " \
    git://github.com/containers/libpod.git;branch=master \
"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e"

GO_IMPORT = "import"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

PV = "1.8.1+git${SRCREV}"

PACKAGES =+ "${PN}-contrib"

PODMAN_PKG = "github.com/containers/libpod"
BUILDTAGS ?= "seccomp varlink \
${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
exclude_graphdriver_btrfs exclude_graphdriver_devicemapper"

# overide LDFLAGS to allow podman to build without: "flag provided but not # defined: -Wl,-O1
export LDFLAGS=""

inherit go goarch
inherit systemd pkgconfig

do_configure[noexec] = "1"

EXTRA_OEMAKE = " \
     PREFIX=${prefix} BINDIR=${bindir} LIBEXECDIR=${libexecdir} \
     ETCDIR=${sysconfdir} TMPFILESDIR=${nonarch_libdir}/tmpfiles.d \
     SYSTEMDDIR=${systemd_unitdir}/system USERSYSTEMDDIR=${systemd_unitdir}/user \
"

OTMPDIR = "${B}/go-tmp"

# remove 'docker' from the packageconfig if you don't want podman to
# build and install the docker wrapper. If docker is enabled in the
# packageconfig, the podman package will rconfict with docker.
PACKAGECONFIG ?= "docker"

do_compile() {
	rm -rf ${B}
	cp -a ${S} ${B}
	cd ${B}/src
	rm -rf .gopath
	mkdir -p .gopath/src/"$(dirname "${PODMAN_PKG}")"
	ln -sf ../../../../import/ .gopath/src/"${PODMAN_PKG}"

	ln -sf "../../../import/vendor/github.com/varlink/" ".gopath/src/github.com/varlink"

	export GOARCH="${BUILD_GOARCH}"
	export GOPATH="${B}/src/.gopath"
	export GOROOT="${STAGING_DIR_NATIVE}/${nonarch_libdir}/${HOST_SYS}/go"

	cd ${B}/src/.gopath/src/"${PODMAN_PKG}"

	oe_runmake cmd/podman/varlink/iopodman.go GO=go

	# Pass the needed cflags/ldflags so that cgo
	# can find the needed headers files and libraries
	export GOARCH=${TARGET_GOARCH}
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	oe_runmake BUILDTAGS="${BUILDTAGS}"
}

do_install() {
	cd ${B}/src/.gopath/src/"${PODMAN_PKG}"

	export GOARCH=${TARGET_GOARCH}
	export CGO_ENABLED="1"
	export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
	export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"

	ulimit -n 10240
	export PSEUDO_UNLOAD=1

	if ${@bb.utils.contains('PACKAGECONFIG', 'docker', 'true', 'false', d)}; then
		oe_runmake install install.docker DESTDIR="${D}"
	else
		oe_runmake install.docker DESTDIR="${D}"
	fi

	if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}${systemd_unitdir}/system
		install -m 644 ${B}/src/import/contrib/systemd/system/podman.service ${D}/${systemd_unitdir}/system
		install -m 644 ${B}/src/import/contrib/systemd/system/podman.socket ${D}/${systemd_unitdir}/system
		rm -f ${D}/${systemd_unitdir}/system/docker.service.rpm
	fi
}

FILES_${PN} += " \
    ${systemd_unitdir}/system/* \
    ${systemd_unitdir}/user/* \
    ${nonarch_libdir}/tmpfiles.d/* \
    ${sysconfdir}/cni \
"

SYSTEMD_SERVICE_${PN} = "podman.service podman.socket"

RDEPENDS_${PN} += "conmon virtual/runc iptables cni skopeo"
RRECOMMENDS_${PN} += "slirp4netns"
RCONFLICTS_${PN} = "${@bb.utils.contains('PACKAGECONFIG', 'docker', 'docker', '', d)}"
