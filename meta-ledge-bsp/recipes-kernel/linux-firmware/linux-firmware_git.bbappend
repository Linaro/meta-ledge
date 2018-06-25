FILESEXTRAPATHS_prepend_lite-hummingboard2 := "${THISDIR}/${PN}:"

# Firmware for BT TI WL18xx
SRC_URI_append_lite-hummingboard2 = "\
    file://TIInit_11.8.32.bts_hummingboard2 \
    file://wl18xx-conf.bin_hummingboard2\
    file://wl18xx-fw-4.bin_hummingboard2 \
"


do_install_append() {
     # No need to install check_whence, triggers QA error (reported upstream)
#     rm ${D}${nonarch_base_libdir}/firmware/check_whence.py
}

do_install_append_lite-hummingboard2() {
     cp ${WORKDIR}/TIInit_11.8.32.bts_hummingboard2 ${D}/lib/firmware/ti-connectivity/TIInit_11.8.32.bts
     cp ${WORKDIR}/wl18xx-conf.bin_hummingboard2 ${D}/lib/firmware/ti-connectivity/wl18xx-conf.bin
     cp ${WORKDIR}/wl18xx-fw-4.bin_hummingboard2 ${D}/lib/firmware/ti-connectivity/wl18xx-fw-4.bin
}

