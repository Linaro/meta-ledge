#!/bin/sh


case $1 in
load)
    if $(/usr/sbin/getsebool -a | grep  systemd_tmpfiles_manage_all | grep -q "off");
    then
        /sbin/setsebool -P systemd_tmpfiles_manage_all 1
        /sbin/setsebool -P allow_polyinstantiation 1

        #Force ledge user to be unconfined_u user
        /usr/sbin/semanage login -a -s unconfined_u ledge
        # load ledge selinux rule
        #/sbin/semodule -i /etc/selinux/ledgeEnforcement.pp
    fi
    ;;
unload)
    # unload ledge selinux rule
    /sbin/semodule -r /etc/selinux/ledgeEnforcement.pp
    ;;
esac

