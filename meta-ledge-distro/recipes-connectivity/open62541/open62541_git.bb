SUMMARY = "OPC UA implementation"
HOMEPAGE = "https://open62541.org/"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=815ca599c9df247a0c7f619bab123dad"
DEPENDS = "openssl python-six-native"

SRC_URI = "git://github.com/open62541/open62541.git;protocol=https;branch=0.3"
SRCREV = "d2735ff51b5fbd97f49c7c69eac70f666cea4f9b"

PV= "0.3+git${SRCPV}"

S = "${WORKDIR}/git"

inherit cmake pythonnative

EXTRA_OECMAKE += "\
    -DCMAKE_SKIP_RPATH=TRUE \
    "

PACKAGECONFIG ?= "release amalgamation shared examples"

PACKAGECONFIG[release] = "-DCMAKE_BUILD_TYPE=Release,-DCMAKE_BUILD_TYPE=Debug,"
# Enable Discovery Service with multicast support (LDS-ME)
PACKAGECONFIG[multicast] = "-DUA_ENABLE_DISCOVERY_MULTICAST=ON,-DUA_ENABLE_DISCOVERY_MULTICAST=OFF,"
# Concatenate the library to a single file open62541.h/.c
PACKAGECONFIG[amalgamation] = "-DUA_ENABLE_AMALGAMATION=ON,-DUA_ENABLE_AMALGAMATION=OFF,"
# Enable gcov coverage
PACKAGECONFIG[gcov] = "-DUA_ENABLE_COVERAGE=ON,-DUA_ENABLE_COVERAGE=OFF,"
# Enable building of shared libraries (dll/so)
PACKAGECONFIG[shared] = "-DBUILD_SHARED_LIBS=ON,-DBUILD_SHARED_LIBS=OFF,"
# Enable encryption support (uses mbedTLS)
PACKAGECONFIG[encryption] = "-DUA_ENABLE_ENCRYPTION=ON,-DUA_ENABLE_ENCRYPTION=OFF,"
# Build example servers and clients
PACKAGECONFIG[examples] = "-DUA_BUILD_EXAMPLES=ON,-DUA_BUILD_EXAMPLES=OFF,"

do_install_append() {
    # install examples if compiled
    if [ "${@bb.utils.contains('PACKAGECONFIG', 'examples', 'yes', 'no', d)}" = "yes" ]; then
        install -d ${D}${base_bindir}/examples/
        install -m 0755 ${B}/bin/examples/* ${D}${base_bindir}/examples/
    fi
}

PACKAGES =+ "${PN}-examples"
# limit ${PN} package to libdir only
FILES_${PN} = "${libdir}"
FILES_${PN}-examples = "${base_bindir}/examples"
FILES_${PN}-dev += "${datadir}/open62541"

# Release configuration, strip the binaries
INSANE_SKIP_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'release', 'already-stripped', '', d)}"
