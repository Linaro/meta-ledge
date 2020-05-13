# Disable password for user in group wheel
do_install_append() {
	if [ "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}" ]; then
		if ${@bb.utils.contains('PACKAGECONFIG', 'pam-wheel', 'true', 'false', d)} ; then
			sed -i 's/\(%wheel ALL=(ALL) ALL\)/%wheel ALL=(ALL) NOPASSWD:ALL/' ${D}${sysconfdir}/sudoers
		fi
	fi
}
