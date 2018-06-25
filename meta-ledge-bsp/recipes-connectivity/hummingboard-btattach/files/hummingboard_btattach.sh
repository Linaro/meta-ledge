#!/bin/sh -

# restart bluetooth service
#systemctl restart bluetooth

# attach bluetootch serial
while [ 1 ];
do
	/usr/bin/hciattach /dev/ttymxc3 texas 3000000 flow
done

