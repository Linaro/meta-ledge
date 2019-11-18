SUMMARY = "Python3 related packages"

inherit packagegroup

# contains basic dependencies for network tools
RDEPENDS_packagegroup-ledge-python3 = " \
    python3 \
    python3-asn1crypto \
    python3-babel \
    python3-cffi \
    python3-chardet \
    python3-coverage \
    python3-cryptography \
    python3-dateutil \
    python3-dbus \
    python3-decorator \
    python3-docker \
    python3-idna \
    python3-iniparse \
    python3-jsonpatch \
    python3-jsonpointer \
    python3-jsonschema \
    python3-langtable \
    python3-markupsafe \
    python3-meh \
    python3-ntplib \
    python3-oauthlib \
    python3-ordered-set \
    python3-pid \
    python3-ply \
    python3-pycparser \
    python3-pydbus \
    python3-pyparted \
    python3-pyserial \
    python3-pysocks \
    python3-pytz \
    python3-pyudev \
    python3-pyyaml \
    python3-requests \
    python3-requests-file \
    python3-requests-ftp \
    python3-setuptools \
    python3-six \
    python3-systemd \
    python3-urllib3 \
    python3-websocket-client \
    "
