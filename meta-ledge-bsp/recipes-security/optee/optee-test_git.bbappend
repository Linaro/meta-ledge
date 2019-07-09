FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
# 3.6
SRC_URI = "git://github.com/OP-TEE/optee_test.git \
	file://0001-optee-test-fix-build-failure-with-GCC-6.patch \
	"

PV="3.6.0+git${SRCPV}"
SRCREV = "40aacb6dc33bbf6ee329f40274bfe7bb438bbf53"
