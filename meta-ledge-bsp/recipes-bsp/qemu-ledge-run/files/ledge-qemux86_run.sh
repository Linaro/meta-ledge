#!/bin/bash

DISK="$1"
OVMF="$2"
OVMF_FORMAT="${OVMF_FORMAT:-raw}"
QEMU="${QEMU:-qemu-system-x86_64}"

${QEMU} \
	-cpu host -enable-kvm -nographic -net nic,model=virtio,macaddr=DE:AD:BE:EF:36:03 -net tap -m 1024 -monitor none \
	-drive file=${DISK},id=hd,format=raw \
	-drive if=pflash,format=${OVMF_FORMAT},file=${OVMF} \
	-m 4096 -serial mon:stdio -show-cursor -object rng-random,filename=/dev/urandom,id=rng0 -device virtio-rng-pci,rng=rng0
