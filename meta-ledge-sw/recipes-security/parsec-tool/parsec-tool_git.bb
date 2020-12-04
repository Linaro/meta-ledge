inherit cargo rust systemd

SRC_URI = "git://github.com/parallaxsecond/parsec-tool.git;protocol=https"
SRCREV="1a11b0ade3bca02c57d798ca68ea2d76bf2d6c98"
LIC_FILES_CHKSUM="file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SUMMARY = "Parsec security extension tool"
LICENSE = "Apache-2.0"

DEPENDS = "curl-native openssl-native"
RDEPENDS_${PN} = "parsec"

S = "${WORKDIR}/git"

CARGO_FEATURES_append = "testing"

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
    cp target/${HOST_SYS}/release/parsec-tool ${D}/bin/

    install -d "${D}${rustlibdir}"
    install -m755 target/${HOST_SYS}/release/*.rlib ${D}${rustlibdir}/

}
