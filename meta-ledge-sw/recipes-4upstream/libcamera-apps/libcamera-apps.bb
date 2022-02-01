SUMMARY = "Small suite of libcamera-based apps"
HOMEPAGE = "https://github.com/raspberrypi/libcamera-apps"
SECTION = "multimedia"
LICENSE = "BSD-2-Clause"

LIC_FILES_CHKSUM = "file://license.txt;md5=a0013d1b383d72ba4bdc5b750e7d1d77"

SRC_URI = "git://github.com/raspberrypi/${BPN}.git;protocol=https;branch=main"

SRCREV = "7ac5197216eaef7d8ae3d48951f047ec87589060"

S = "${WORKDIR}/git"

DEPENDS = "libdrm libexif boost libcamera jpeg tiff libpng libcamera"

RDEPENDS_${PN} = "python3 python3-core"

inherit cmake

EXTRA_OECMAKE += " \
    -DENABLE_DRM=1 \
    -DENABLE_X11=0 \
    -DENABLE_QT=0 \
    -DENABLE_OPENCV=0 \
    -DENABLE_TFLITE=0 \
"

FILES_${PN}-dev = ""

FILES_${PN} = " ${libdir}/libpreview.so \
    ${libdir}/liboutputs.so \
    ${libdir}/libpost_processing_stages.so \
    ${libdir}/libimages.so \
    ${libdir}/libcamera_app.so \
    ${libdir}/libencoders.so \
    ${libdir}/libpreview.so \
    ${bindir}/libcamera-vid \
    ${bindir}/libcamera-raw \
    ${bindir}/libcamera-hello \
    ${bindir}/camera-bug-report \
    ${bindir}/libcamera-still \
    ${bindir}/libcamera-jpeg \
"
