# This software is a part of ISAR.
# Copyright (C) Siemens AG, 2019
#
# SPDX-License-Identifier: MIT
#
# This class extends the image.bbclass to supply the creation of a sdk

do_populate_sdk[stamp-extra-info] = "${DISTRO}-${MACHINE}"
do_populate_sdk[depends] = "sdkchroot:do_build"
do_populate_sdk() {
    sudo umount -R ${SDKCHROOT_DIR}/dev || true
    sudo umount ${SDKCHROOT_DIR}/proc || true
    sudo umount -R ${SDKCHROOT_DIR}/sys || true

    # Remove isar-apt repo entry
    sudo rm -f ${SDKCHROOT_DIR}/etc/apt/sources.list.d/isar-apt.list

    # Remove setup scripts
    sudo rm -f ${SDKCHROOT_DIR}/chroot-setup.sh ${SDKCHROOT_DIR}/configscript.sh

    # Make all links relative
    for link in $(find ${SDKCHROOT_DIR}/ -type l); do
        target=$(readlink $link)

        if [ "${target#/}" != "${target}" ]; then
            basedir=$(dirname $link)
            new_target=$(realpath --no-symlinks -m --relative-to=$basedir ${SDKCHROOT_DIR}/${target})

            # remove first to allow rewriting directory links
            sudo rm $link
            sudo ln -s $new_target $link
        fi
    done

    # Set up sysroot wrapper
    for tool_pattern in "gcc-[0-9]*" "g++-[0-9]*" "cpp-[0-9]*" "ld.bfd" "ld.gold"; do
        for tool in $(find ${SDKCHROOT_DIR}/usr/bin -type f -name "*-linux-gnu-${tool_pattern}"); do
            sudo mv "${tool}" "${tool}.bin"
            sudo ln -sf gcc-sysroot-wrapper.sh ${tool}
        done
    done

    # Copy mount_chroot.sh for convenience
    sudo cp ${ISARROOT}/scripts/mount_chroot.sh ${SDKCHROOT_DIR}

    # Create SDK archive
    cd -P ${SDKCHROOT_DIR}/..
    sudo tar --transform="s|^rootfs|sdk-${DISTRO}-${DISTRO_ARCH}|" \
        -c rootfs | xz -T0 > ${DEPLOY_DIR_IMAGE}/sdk-${DISTRO}-${DISTRO_ARCH}.tar.xz
}
addtask populate_sdk after do_rootfs
