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

SRCREV_openavnu = "df587b7509a4dee7fa79a40eec5d6327310c472b"
SRCREV_cpputest = "1d95a3905413d99fddb5bcbd30be35a16dbf9119"
SRCREV_avdecc = "a04fef499f843b8a7f596bc15441a847b9a18b7a"

SRCREV_FORMAT = "openavnu-cpputest"
SRC_URI = "git://github.com/AVnu/OpenAvnu.git;protocol=https;name=openavnu \
    git://github.com/AVnu/cpputest.git;protocol=https;name=cpputest;nobranch=1;destsuffix=git/thirdparty/cpputest \
    git://github.com/AVnu/avdecc-lib.git;protocol=https;name=avdecc;nobranch=1;destsuffix=git/avdecc-lib \
    file://0001-MAKEFILE-disable-igb-for-arm-platform.patch \
    file://0002-CMAKE-pcap-support-through-openembedded.patch \
    file://0003-add-GNU_HASH-in-the-elf-binary.patch \
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

do_compile() {
    # Variables used via several makefile/cmake layers
    export PCAPDIR=${RECIPE_SYSROOT}/usr/
    export PCAP_INCLUDE_DIR=${RECIPE_SYSROOT}/usr/include

    # compile mprd, maap, shaper, gptp
    oe_runmake -C ${S} daemons_all
    # compile avdecc library
    oe_runmake -C ${S} avtp_avdecc
    oe_runmake -C ${S} avtp_pipeline
    # examples
    oe_runmake -C ${S} examples_common
    oe_runmake -C ${S} simple_listener mrp_client jackd-listener
}

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${datadir}/openavnu
    install -d ${D}${datadir}/openavnu/examples

    # install gptp
    install -m 0755 ${S}/daemons/gptp/linux/build/obj/daemon_cl ${D}${bindir}/gptp

    # install mrpd
    install -m 0755 ${S}/daemons/mrpd/mrpd ${D}${bindir}/
    install -m 0755 ${S}/daemons/mrpd/mrpctl ${D}${bindir}/

    # install maap
    install -m 0755 ${S}/daemons/maap/linux/build/maap_daemon ${D}${bindir}/

    # install shaper
    install -m 0755 ${S}/daemons/shaper/shaper_daemon ${D}${bindir}/

    # install avtp_pipeline
    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/openavb_avdecc ${D}${bindir}/
    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/openavb_harness ${D}${bindir}/
    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/openavb_host ${D}${bindir}/

    install -m 0755 ${S}/lib/avtp_pipeline/build/bin/avdecc.ini ${D}${datadir}/openavnu/

    # install examples
    install -m 0755 ${S}/examples/simple_listener/simple_listener ${D}${datadir}/openavnu/examples/
    install -m 0755 ${S}/examples/mrp_client/mrpl ${D}${datadir}/openavnu/examples/
    install -m 0755 ${S}/examples/mrp_client/mrpq ${D}${datadir}/openavnu/examples/
    install -m 0755 ${S}/examples/mrp_client/mrpValidate ${D}${datadir}/openavnu/examples/
    install -m 0755 ${S}/examples/jackd-listener/jack_listener ${D}${datadir}/openavnu/examples/

    #install script as example
    install -m 0755 ${WORKDIR}/run_openavnu_daemon.sh ${D}${datadir}/openavnu/
}

PACKAGES =+ "openavnu-examples"
FILES_${PN} += "${datadir}/openavnu/"
FILES_openavnu-examples = "${datadir}/openavnu/examples/"
