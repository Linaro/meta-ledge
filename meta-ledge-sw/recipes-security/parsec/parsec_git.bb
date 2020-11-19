inherit cargo rust systemd

SRC_URI = "git://github.com/parallaxsecond/parsec.git;protocol=https"
SRCREV="0.4.0"
SRC_URI_append = " file://parsec.service"
LIC_FILES_CHKSUM="file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SUMMARY = "Parsec security extension"
LICENSE = "Apache-2.0"

DEPENDS = "curl-native openssl-native"
RDEPENDS_${PN} = "tpm2-tss mbedtls tpm2-pkcs11"

S = "${WORKDIR}/git"

CARGO_FEATURES_append = "tpm-provider"

cargo_do_compile_prepend() {
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
    cp config.toml ${D}/etc/parsec/config.toml

    install -d ${D}${systemd_system_unitdir}
    install -D -p -m0644 ${WORKDIR}/parsec.service ${D}${systemd_system_unitdir}/parsec.service

    install -d -m 755 ${D}/home/parsec
    chown -R parsec ${D}/home/parsec
    chgrp -R parsec ${D}/home/parsec
}

inherit useradd
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-g parsec -d /home/parsec -r parsec"
GROUPADD_PARAM_${PN} = "-g 1001 parsec"

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "parsec.service"

FILES_${PN} += "${systemd_system_unitdir}/parsec.service"
FILES_${PN} += "/lib/systemd /home/parsec"
