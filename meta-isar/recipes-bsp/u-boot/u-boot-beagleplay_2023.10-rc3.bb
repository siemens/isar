#
# Copyright (c) Siemens AG, 2023
#
# SPDX-License-Identifier: MIT

require recipes-bsp/u-boot/u-boot-custom.inc

TI_FIRMWARE_SRCREV = "c067ff8fad032a78690e7efd9a010e33e68d7e22"

SRC_URI += " \
    https://ftp.denx.de/pub/u-boot/u-boot-${PV}.tar.bz2 \
    https://git.ti.com/cgit/processor-firmware/ti-linux-firmware/plain/ti-sysfw/ti-fs-firmware-am62x-gp.bin?id=${TI_FIRMWARE_SRCREV};downloadfilename=ti-fs-firmware-am62x-gp.bin;name=sysfw \
    https://git.ti.com/cgit/processor-firmware/ti-linux-firmware/plain/ti-sysfw/ti-fs-stub-firmware-am62x-gp.bin?id=${TI_FIRMWARE_SRCREV};downloadfilename=ti-fs-stub-firmware-am62x-gp.bin;name=sysfw-stub \
    https://git.ti.com/cgit/processor-firmware/ti-linux-firmware/plain/ti-dm/am62xx/ipc_echo_testb_mcu1_0_release_strip.xer5f?id=${TI_FIRMWARE_SRCREV};downloadfilename=ipc_echo_testb_mcu1_0_release_strip.xer5f;name=dm \
    file://0001-scripts-kconfig-Add-config-fragment-support-in-board.patch \
    file://0002-TMP-board-ti-am62x-Add-basic-initialization-for-usb-.patch \
    file://0003-env_default-Allow-CONFIG_EXTRA_ENV_TEXT-to-override-.patch \
    file://0004-configs-am62x_evm-Enable-EMMC_BOOT-configuration.patch \
    file://0005-arm-mach-k3-am625-Add-support-for-UDA-FS.patch \
    file://0006-drivers-mmc-am654_sdhci-Update-OTAP-ITAP-delay.patch \
    file://0007-arm-dts-k3-am625-sk-binman-Add-labels-for-unsigned-b.patch \
    file://0008-arm-dts-Add-k3-am625-beagleplay.patch \
    file://0009-board-ti-am62x-Add-am62x_beagleplay_-defconfigs-and-.patch \
    file://rules-beagleplay"
SRC_URI[sha256sum] = "4c29d64e764060b49e7658f0cf9f86911d0b2bc081f13980a19b99cc89584ba0"
SRC_URI[sysfw.sha256sum] = "9e8aea07d421b63dc5cb6dcfad6930ee739cfa03fb46a6e1cbd6aba084272de2"
SRC_URI[sysfw-stub.sha256sum] = "94534f9f1aa21dd34e722a1dd1ef53da1991c28e86b2daa6fa4efe951d85ad95"
SRC_URI[dm.sha256sum] = "1eb47c9900688806f0d3ae13bbbd0769123ba99e9240ed8bc79b1666f1140681"

S = "${WORKDIR}/u-boot-${PV}"

COMPATIBLE_MACHINE = "beagleplay"

U_BOOT_BIN_INSTALL = "tiboot3-am62x-gp-evm.bin tispl.bin_unsigned u-boot.img_unsigned"

DEPENDS += "trusted-firmware-a-beagleplay optee-os-beagleplay"
DEBIAN_BUILD_DEPENDS =. "gcc-arm-linux-gnueabihf, \
    libssl-dev:native, libssl-dev, \
    swig, python3-dev:native, python3-setuptools, python3-pyelftools, \
    python3-jsonschema:native, python3-yaml:native, \
    trusted-firmware-a-beagleplay, optee-os-beagleplay,"

do_prepare_build:append() {
    mkdir -p ${S}/ti-sysfw
    cp ${WORKDIR}/ti-fs-*firmware-am62x-gp.bin ${S}/ti-sysfw
    mkdir -p ${S}/ti-dm/am62xx
    cp ${WORKDIR}/ipc_echo_testb_mcu1_0_release_strip.xer5f ${S}/ti-dm/am62xx
    cp ${WORKDIR}/rules-beagleplay ${S}/debian/rules

    sed -i "s/CONFIG_BOOTCOMMAND=.*/CONFIG_BOOTCOMMAND=\"setenv boot_targets 'mmc1 mmc0 pxe dhcp'; run distro_bootcmd\"/" \
        ${S}/board/ti/am62x/beagleplay_a53.config
}
