#
# Copyright (c) Siemens AG, 2023
#
# SPDX-License-Identifier: MIT

IMAGE_TYPEDEP:beagleplay_hybrid = "wic"

IMAGE_CMD:beagleplay_hybrid() {
    ln -sf "${IMAGE_FULLNAME}.wic" "${IMAGE_FILE_HOST}"
    ${SUDO_CHROOT} /usr/sbin/sgdisk "${IMAGE_FILE_CHROOT}" --hybrid 1:EE
    ${SUDO_CHROOT} /usr/sbin/fdisk "${IMAGE_FILE_CHROOT}" << EOF
M
t
1
c
a
1
w
q
EOF
    ${SUDO_CHROOT} /usr/bin/bmaptool create ${IMAGE_FILE_CHROOT} -o ${PP_DEPLOY}/${IMAGE_FULLNAME}.wic.bmap
}
