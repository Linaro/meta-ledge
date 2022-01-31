
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRCREV = "61670bb338dd4441b9d9dffdcd8849c2305eb4f3"

PV = "202201+git${SRCPV}"

DEPENDS += " libevent"

SRC_URI_remove = "file://0001-uvcvideo-Use-auto-variable-to-avoid-range-loop-warni.patch"

SRC_URI_append = " \
     file://0001-ipa-rkisp1-Introduce-sensor-degamma.patch \
     file://0002-ipa-rkisp1-Use-frame-index.patch \
     file://0003-ipa-rkisp1-Introduce-Black-Level-Correction.patch \
     file://0004-ipa-rkisp1-Introduce-AWB.patch \
     file://0005-ipa-rkisp1-Introduce-crosstalk-correction.patch \
     file://0006-ipa-rkisp1-agc-Introduce-histogram-calculation.patch \
"

EXTRA_OEMESON = " \
     -Dpipelines=raspberrypi,rkisp1,uvcvideo -Dv4l2=true -Dcam=enabled \
     --buildtype=release -Dtest=false -Dlc-compliance=disabled -Ddocumentation=disabled \
"

FILES_${PN}-dev = "${includedir} ${libdir}/pkgconfig \
                 ${libdir}/libcamera.so ${libdir}/libcamera-base.so"
 
FILES_${PN} += " \
     ${libdir}/libcamera.so.* \
     ${libdir}/libcamera-base.so.* \
     ${libdir}/v4l2-compat.so \
     ${libexecdir}/${BPN}/* \
     ${bindir}/cam \
"
