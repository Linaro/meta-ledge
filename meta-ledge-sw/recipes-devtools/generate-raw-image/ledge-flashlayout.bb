DESCRIPTION = "Flashlayout associated to board for creating raw SDCARD image ready to flash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI_ledge-stm32mp157c-dk2 = " \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-optee.fld.template \
    \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-optee.tsv.template \
    file://tf-a-stm32mp157c-dk2-flasher.stm32 \
    file://u-boot-stm32mp157c-dk2-flasher.stm32 \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-debian.fld \
    "

SRC_URI_ledge-qemuarm = " \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-optee.tsv.template \
    file://tf-a-stm32mp157c-dk2-flasher.stm32 \
    file://u-boot-stm32mp157c-dk2-flasher.stm32 \
    "

S = "${WORKDIR}"

inherit deploy

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
ALLOW_EMPTY_${PN} = "1"

do_deploy() {
    install -d ${DEPLOYDIR}/
    find ${S} -name "*.fld.template" -exec install -m 0644 {} ${DEPLOYDIR}/ \;

    #for ST Flasher
    find ${S} -name "*.tsv.template" -exec install -m 0644 {} ${DEPLOYDIR}/ \;

    if [ -e  ${S}/tf-a-stm32mp157c-dk2-flasher.stm32 ]; then
        install -m 0644 ${S}/tf-a-stm32mp157c-dk2-flasher.stm32 ${DEPLOYDIR}/
    fi
    if [ -e ${S}/u-boot-stm32mp157c-dk2-flasher.stm32 ]; then
        install -m 0644 ${S}/u-boot-stm32mp157c-dk2-flasher.stm32 ${DEPLOYDIR}/
    fi
}
addtask deploy before do_build after do_unpack
