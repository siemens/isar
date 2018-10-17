# Example recipe for building the mainline kernel
#
# This software is a part of ISAR.
# Copyright (c) Siemens AG, 2018
#
# SPDX-License-Identifier: MIT

require recipes-kernel/linux/linux-custom.inc

SRC_URI += " \
    https://git.kernel.org/torvalds/t/linux-4.19-rc8.tar.gz \
    file://x86_64_defconfig"
SRC_URI[sha256sum] = "365e2e43a8377044b3c771b5224ecaf9d80100b9247297ef168b44627f7e88f7"

SRC_URI_append_de0-nano-soc = " \
    file://0001-ARM-dts-socfpga-Rename-socfpga_cyclone5_de0_-sockit-.patch"

S = "${WORKDIR}/linux-4.19-rc8"

KERNEL_DEFCONFIG_qemuamd64 = "x86_64_defconfig"
