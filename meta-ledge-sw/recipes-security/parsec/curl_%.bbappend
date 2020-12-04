# Enable http2 binary protocol.
# Enable ca certs for host curl.

DEPENDS_class-native += " nghttp2 ca-certificates-native "

PACKAGECONFIG_class-native += "nghttp2"

PR = "ledge.01"
