DESCRIPTION = "Flashlayout associated to board for creating raw SDCARD image ready to flash"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI_ledge-stm32mp157c-dk2 = " \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-basic.fld.template \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-optee.fld.template \
    \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-basic.tsv.template \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-optee.tsv.template \
    file://tf-a-stm32mp157c-dk2-flasher.stm32 \
    file://u-boot-stm32mp157c-dk2-flasher.stm32 \
    file://FlashLayout_sdcard_ledge-stm32mp157c-dk2-debian.fld \
    "

S = "${WORKDIR}"

inherit deploy

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"
ALLOW_EMPTY_${PN} = "1"

do_deploy() {
    install -d ${DEPLOYDIR}/
    LIST=`ls ${S}/*.fld.template`
    if [ -n "$LIST" ]; then
        install -m 0644 ${S}/*.fld.template ${DEPLOYDIR}/
    fi
    #for ST Flasher
    install -m 0644 ${S}/*.tsv.template ${DEPLOYDIR}/
    install -m 0644 ${S}/tf-a-stm32mp157c-dk2-flasher.stm32 ${DEPLOYDIR}/
    install -m 0644 ${S}/u-boot-stm32mp157c-dk2-flasher.stm32 ${DEPLOYDIR}/
}
addtask deploy before do_build after do_unpack
