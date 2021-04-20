PR = "r03.ledge"

do_install_prepend() {
	# Sometimes install fails with pseudo crash. This issue needs to be
	# investigated and fixed.
	# https://projects.linaro.org/browse/LSS-2219
	export PSEUDO_UNLOAD=1
}
