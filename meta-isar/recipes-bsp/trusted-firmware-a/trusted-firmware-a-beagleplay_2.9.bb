#
# Copyright (c) Siemens AG, 2023
#
# SPDX-License-Identifier: MIT

require recipes-bsp/trusted-firmware-a/trusted-firmware-a-custom.inc

SRC_URI += "https://git.trustedfirmware.org/TF-A/trusted-firmware-a.git/snapshot/trusted-firmware-a-${PV}.tar.gz"
SRC_URI[sha256sum] = "76a66a1de0c01aeb83dfc7b72b51173fe62c6e51d6fca17cc562393117bed08b"

S = "${WORKDIR}/trusted-firmware-a-${PV}"

TF_A_PLATFORM = "k3"
TF_A_EXTRA_BUILDARGS = "CFG_ARM64=y TARGET_BOARD=lite SPD=opteed"
TF_A_BINARIES = "lite/release/bl31.bin"
