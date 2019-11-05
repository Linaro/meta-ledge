# Copyright (C) 2018, STMicroelectronics - All Rights Reserved
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "OpenAVNU daemon: mprd, maap, shaper, gptp"
HOMEPAGE = "https://github.com/AVnu/OpenAvnu"
LICENSE = "BSD & GPLv2"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/BSD;md5=3775480a712fc46a69647678acb234cb \
    file://avdecc-lib/LICENSE;md5=89e9087cf8523e423297c8e41541ac39 \
    "

PV = "1.1+git${SRCPV}"

DEPENDS = " libpcap alsa-lib glib-2.0 libsndfile1 cmake-native jack \
    ${@bb.utils.contains('DISTRO_FEATURES', 'gstreamer', 'gstreamer1.0 gstreamer1.0-plugins-base', '', d)} \
    "

SRCREV_openavnu = "736ed600ab6c5bf962a093c8111e8f6e5246cd02"
SRCREV_cpputest = "1d95a3905413d99fddb5bcbd30be35a16dbf9119"
SRCREV_avdecc = "a04fef499f843b8a7f596bc15441a847b9a18b7a"
SRCREV_igb = "797496806aed8be065a55417e07b7ccd170429dd"

SRCREV_FORMAT = "openavnu-cpputest"
SRC_URI = "git://github.com/AVnu/OpenAvnu.git;protocol=https;name=openavnu \
    git://github.com/AVnu/cpputest.git;protocol=https;name=cpputest;nobranch=1;destsuffix=git/thirdparty/cpputest \
    git://github.com/AVnu/avdecc-lib.git;protocol=https;name=avdecc;nobranch=1;destsuffix=git/avdecc-lib \
    git://github.com/AVnu/igb_avb.git;protocol=https;name=igb;nobranch=1;destsuffix=git/lib/igb_avb \
    file://0001-deasctivate-CPU-test.patch \
    file://0002-CMAKE-pcap-support-through-openembedded.patch \
    file://0003-mrpd-correct-typedef-issue.patch \
    file://run_openavnu_daemon.sh \
    "

# to force the usage only with arm
COMPATIBLE_MACHINE = "(arm*|aarch64)"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = " \
    AVB_FEATURE_IGB=0 \
    ${@bb.utils.contains('DISTRO_FEATURES','gstreamer','AVB_FEATURE_GSTREAMER=1','AVB_FEATURE_GSTREAMER=0',d)} \
    IGB_LAUNCHTIME_ENABLED=0 \
    "

inherit cmake
OECMAKE_GENERATOR = "Unix Makefiles"

do_compile_append() {
    # Variables used via several makefile/cmake layers
    export PCAPDIR=${RECIPE_SYSROOT}/usr/
    export PCAP_INCLUDE_DIR=${RECIPE_SYSROOT}/usr/include

    # compile shaper
    oe_runmake -C ${S} shaper
#    # compile avdecc library
#    oe_runmake -C ${S} avtp_avdecc
#    oe_runmake -C ${S} avtp_pipeline
#    # examples
#    oe_runmake -C ${S} examples_common
#    oe_runmake -C ${S} simple_listener mrp_client jackd-listener
}

do_install_append() {
    rm -rf ${D}/*
    install -d ${D}${bindir}
    install -d ${D}${datadir}/openavnu
    install -d ${D}${datadir}/openavnu/examples

    # install gptp
    install -m 0755 ${B}/daemons/gptp/gptp ${D}${bindir}/

    # install mrpd
    install -m 0755 ${B}/daemons/mrpd/mrpd ${D}${bindir}/

    # install maap
    install -m 0755 ${B}/daemons/maap/maap_daemon ${D}${bindir}/

    # install shaper
    install -m 0755 ${S}/daemons/shaper/shaper_daemon ${D}${bindir}/

    #install script as example
    install -m 0755 ${WORKDIR}/run_openavnu_daemon.sh ${D}${datadir}/openavnu/
}

PACKAGES =+ "openavnu-examples"
FILES_${PN} += "${datadir}/openavnu/"
FILES_openavnu-examples = "${datadir}/openavnu/examples/"
