#!/bin/sh -

# Start all daemons

if [ "$1" == "-h" ]; then
    echo "Usage: $0 [<network interface>|-k]"
    echo "   eg: $0 eth1"
    echo "   eg: $0 -k (for killing the daemons)"
    echo ""
    exit
fi

if [ "$1" == "-k" ]; then
    echo "Stoping daemons"
    # stop gptp
    kill -9 `pidof /usr/bin/gptp`
    # stop mrpd
    kill -9 `pidof /usr/bin/mrpd`
    # stop maap
    kill -9 `pidof /usr/bin/maap_daemon`
    # stop shaper
    kill -9 `pidof /usr/bin/shaper_daemon`
    exit
fi

if [ "$1" == "" ]; then
    echo "Please enter network interface name as parameter. For example:"
    echo "sudo $0 eth1"
    echo ""
    echo "If you are using IGB, call \"sudo ./run_igb.sh\" before running this script."
    echo ""
    exit -1
fi



nic=$1
echo "Starting daemons on "$nic

groupadd ptp > /dev/null 2>&1

# start gptp
/usr/bin/gptp $nic &
# start mrpd
/usr/bin/mrpd -mvs -i $nic &
# start maap
/usr/bin/maap_daemon -i $nic -d /dev/null
# start shaper
/usr/bin/shaper_daemon -d &
