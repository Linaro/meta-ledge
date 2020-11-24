# Enable http2 binary protocol.
# Enable ca certs for host curl.

DEPENDS_class-native += " nghttp2 ca-certificates-native "

PACKAGECONFIG_class-native += "nghttp2"

EXTRA_OECONF = " \
    --disable-libcurl-option \
    --disable-ntlm-wb \
    --enable-crypto-auth \
    --without-libmetalink \
    --without-libpsl \
"
EXTRA_OECONF_append_class-target = " \
    --with-ca-bundle=${sysconfdir}/ssl/certs/ca-certificates.crt \
"

do_configure_prepend_class-native() {
    mkdir -p ${DEPLOY_DIR}
    cp ${STAGING_ETCDIR_NATIVE}/ssl/certs/ca-certificates.crt ${DEPLOY_DIR}/ca-certificates.crt
}

EXTRA_OECONF_append_class-native = " \
    --with-ca-bundle=${DEPLOY_DIR}/ca-certificates.crt \
"
