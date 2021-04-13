inherit cargo rust systemd

SRC_URI = "git://github.com/parallaxsecond/parsec.git;protocol=https;branch=main"
SRCREV="0.6.0"
SRC_URI_append = " file://parsec.service \
                   file://config.toml "
LIC_FILES_CHKSUM="file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SUMMARY = "Parsec security extension"
LICENSE = "Apache-2.0"

DEPENDS = "curl-native openssl-native libtss2 tpm2-tss tpm2-pkcs11 \
           libtss2 \
           libtss2-dev \
           libtss2-staticdev \
           libtss2-tcti-device \
	   llvm-native \
"

RDEPENDS_${PN} += " libtss2-tcti-device tpm2-abrmd "

S = "${WORKDIR}/git"

CARGO_BUILD_FLAGS += "--features all-providers "

cargo_do_compile_prepend() {
    export CARGO_HTTP_CAINFO=`find ${WORKDIR} -name ca-certificates.crt`

    export CARGO_HOME=".cargo"

    export RUSTFLAGS="${RUSTFLAGS}"
    export RUST_TARGET_PATH="${RUST_TARGET_PATH}"

    mkdir -p .cargo
    echo "[target.${HOST_SYS}]" >  .cargo/config
    echo "linker = '${RUST_TARGET_CCLD}'" >> .cargo/config
    if [ "${HOST_SYS}" != "${BUILD_SYS}" ]; then
        echo "[target.${BUILD_SYS}]" >> .cargo/config
        echo "linker = '${RUST_BUILD_CCLD}'" >> .cargo/config
    fi
    ${CARGO} fetch --target ${HOST_SYS}
}

do_install() {
    mkdir -p ${D}/bin/
    cp target/${HOST_SYS}/release/parsec ${D}/bin/

    install -d "${D}${rustlibdir}"
    install -m755 target/${HOST_SYS}/release/*.rlib ${D}${rustlibdir}/

    mkdir -p ${D}/etc/parsec
    cp ${WORKDIR}/config.toml ${D}/etc/parsec/config.toml

    install -d ${D}${systemd_system_unitdir}
    install -D -p -m0644 ${WORKDIR}/parsec.service ${D}${systemd_system_unitdir}/parsec.service

    install -d -m 755 ${D}/home/parsec
    chown -R parsec ${D}/home/parsec
    chgrp -R parsec ${D}/home/parsec

    mkdir -p ${D}/var/lib/parsec
    chown -R parsec ${D}/var/lib/parsec
    chgrp -R parsec ${D}/var/lib/parsec
}

inherit useradd
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-g parsec -d /home/parsec -r parsec"
GROUPADD_PARAM_${PN} = "-g 1001 parsec"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "parsec.service"

FILES_${PN} += "${systemd_system_unitdir}/parsec.service"
FILES_${PN} += "/lib/systemd /home/parsec /var/lib/parsec"
