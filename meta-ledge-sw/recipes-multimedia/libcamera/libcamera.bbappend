SRCREV = "61670bb338dd4441b9d9dffdcd8849c2305eb4f3"

PV = "202201+git${SRCPV}"

DEPENDS += " libevent"

SRC_URI_remove = "file://0001-uvcvideo-Use-auto-variable-to-avoid-range-loop-warni.patch"

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
