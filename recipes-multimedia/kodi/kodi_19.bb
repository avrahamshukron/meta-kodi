SUMMARY = "Kodi Media Center"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=7b423f1c9388eae123332e372451a4f7"

FILESPATH =. "${FILE_DIRNAME}/kodi-19:"

inherit cmake gettext python3-dir python3native systemd

DEPENDS += " \
            libfmt \
            flatbuffers flatbuffers-native \
            fstrcmp \
            rapidjson \
            crossguid \
            texturepacker-native \
            libdvdnav libdvdcss libdvdread \
            git-native \
            curl-native \
            gperf-native \
            jsonschemabuilder-native \
            nasm-native \
            swig-native \
            unzip-native \
            yasm-native \
            zip-native \
            spdlog \
            avahi \
            boost \
            bzip2 \
            curl \
            dcadec \
            enca \
            expat \
            faad2 \
            ffmpeg \
            fontconfig \
            fribidi \
            giflib \
            libass \
            libcdio \
            libcec \
            libdvdcss \
            libdvdread \
            libinput \
            libmad \
            libmicrohttpd \
            libmms \
            libmodplug \
            libnfs \
            libpcre \
            libplist \
            libsamplerate0 \
            libsquish \
            libssh \
            libtinyxml \
            libusb1 \
            libxkbcommon \
            libxslt \
            lzo \
            mpeg2dec \
            python3 \
            samba \
            sqlite3 \
            taglib \
            virtual/egl \
            wavpack \
            yajl \
            zlib \
          "

SRCREV = "54be31bc5c2a52827b6c7584f51b9a083e9f3d61"

# 'patch' doesn't support binary diffs
PATCHTOOL = "git"

PV = "19.0a1+gitr${SRCPV}"
SRC_URI = "git://github.com/xbmc/xbmc.git;protocol=https;rev=${SRCREV} \
           file://0001-Add-support-for-musl.patch \
           file://0002-Fix-file_Emu-on-musl.patch \
           file://0004-Replace-u_int64_t-with-uint64_t-from-stdint.h.patch \
           file://0005-estuary-move-recently-added-entries-to-the-top-in-ho.patch \
           file://0006-kodi.sh-set-mesa-debug.patch \
           file://0007-peripheral-settings-export-CEC-device_name-in-GUI.patch \
           file://0008-libgui-speed-up-jpeg-scaling.patch \
           file://0010-flatbuffers.patch \
           file://PR15286-shader-nopow.patch \
           file://15941.patch \
           file://kodi.service \
           file://kodi-x11.service \
          "

S = "${WORKDIR}/git"

# breaks compilation
CCACHE = ""
ASNEEDED = ""

ACCEL ?= ""
ACCEL_x86 = "vaapi vdpau"
ACCEL_x86-64 = "vaapi vdpau"

# Default to GBM everywhere, sucks to be nvidia
WINDOWSYSTEM ?= "gbm"

PACKAGECONFIG ??= "${ACCEL} ${WINDOWSYSTEM} pulseaudio lcms"

# Core windowing system choices

PACKAGECONFIG[x11] = "-DCORE_PLATFORM_NAME=x11,,libxinerama libxmu libxrandr libxtst glew"
PACKAGECONFIG[gbm] = "-DCORE_PLATFORM_NAME=gbm -DGBM_RENDER_SYSTEM=gles,,"
PACKAGECONFIG[raspberrypi] = "-DCORE_PLATFORM_NAME=rbpi,,userland"
PACKAGECONFIG[amlogic] = "-DCORE_PLATFORM_NAME=aml,,"
PACKAGECONFIG[wayland] = "-DCORE_PLATFORM_NAME=wayland -DWAYLAND_RENDER_SYSTEM=gles,,wayland waylandpp"

PACKAGECONFIG[vaapi] = "-DENABLE_VAAPI=ON,-DENABLE_VAAPI=OFF,libva"
PACKAGECONFIG[vdpau] = "-DENABLE_VDPAU=ON,-DENABLE_VDPAU=OFF,libvdpau"
PACKAGECONFIG[mysql] = "-DENABLE_MYSQLCLIENT=ON,-DENABLE_MYSQLCLIENT=OFF,mysql5"
PACKAGECONFIG[pulseaudio] = "-DENABLE_PULSEAUDIO=ON,-DENABLE_PULSEAUDIO=OFF,pulseaudio"
PACKAGECONFIG[lcms] = ",,lcms"

LDFLAGS += "${TOOLCHAIN_OPTIONS}"
LDFLAGS_append_mips = " -latomic"
LDFLAGS_append_mipsel = " -latomic"
LDFLAGS_append_mips64 = " -latomic"
LDFLAGS_append_mips64el = " -latomic"

KODI_ARCH = ""
KODI_ARCH_mips = "-DWITH_ARCH=${TARGET_ARCH}"
KODI_ARCH_mipsel = "-DWITH_ARCH=${TARGET_ARCH}"
KODI_ARCH_mips64 = "-DWITH_ARCH=${TARGET_ARCH}"
KODI_ARCH_mips64el = "-DWITH_ARCH=${TARGET_ARCH}"

EXTRA_OECMAKE = " \
    ${KODI_ARCH} \
    \
    -DNATIVEPREFIX=${STAGING_DIR_NATIVE}${prefix} \
    -DJava_JAVA_EXECUTABLE=/usr/bin/java \
    -DWITH_TEXTUREPACKER=${STAGING_BINDIR_NATIVE}/TexturePacker \
    -DWITH_JSONSCHEMABUILDER=${STAGING_BINDIR_NATIVE}/JsonSchemaBuilder \
    -DENABLE_INTERNAL_FSTRCMP=0 \
    \
    -DENABLE_LDGOLD=ON \
    -DENABLE_STATIC_LIBS=FALSE \
    -DCMAKE_NM='${NM}' \
    -DUSE_LTO=${@oe.utils.cpu_count()} \
    -DCROSSCOMPILING=ON \
    -DCMAKE_MESSAGE_LOG_LEVEL=VERBOSE \
    -DFFMPEG_PATH=${STAGING_DIR_TARGET} \
    -DENABLE_INTERNAL_FFMPEG=OFF \
    -DENABLE_INTERNAL_CROSSGUID=OFF \
    -DLIBDVD_INCLUDE_DIRS=${STAGING_INCDIR} \
    -DNFS_INCLUDE_DIR=${STAGING_INCDIR} \
    -DSHAIRPLAY_INCLUDE_DIR=${STAGING_INCDIR} \
    \
    -DENABLE_AIRTUNES=ON \
    -DENABLE_OPTICAL=OFF \
    -DENABLE_DVDCSS=OFF \
    -DENABLE_DEBUGFISSION=OFF \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
"

# OECMAKE_GENERATOR="Unix Makefiles"
#PARALLEL_MAKE = " "

FULL_OPTIMIZATION_armv7a = "-fexpensive-optimizations -fomit-frame-pointer -O4 -ffast-math"
BUILD_OPTIMIZATION = "${FULL_OPTIMIZATION}"

# for python modules
export HOST_SYS
export BUILD_SYS
export STAGING_LIBDIR
export STAGING_INCDIR
export PYTHON_DIR

export TARGET_PREFIX

do_configure_prepend() {
	# Ensure 'nm' can find the lto plugins
	liblto=$(find ${STAGING_DIR_NATIVE} -name "liblto_plugin.so.0.0.0")
	mkdir -p ${STAGING_LIBDIR_NATIVE}/bfd-plugins
	ln -sf $liblto ${STAGING_LIBDIR_NATIVE}/bfd-plugins/liblto_plugin.so

	sed -i -e 's:CMAKE_NM}:}${TARGET_PREFIX}gcc-nm:' ${S}/xbmc/cores/DllLoader/exports/CMakeLists.txt
}

do_install_append() {
	install -d ${D}/lib/systemd/system

	if [ -e ${D}${libdir}/kodi/kodi-gbm ] ; then
		install -m 0644 ${WORKDIR}/kodi.service ${D}/lib/systemd/system/kodi.service
	else
		install -m 0644 ${WORKDIR}/kodi-x11.service ${D}/lib/systemd/system/kodi.service

	fi
}


SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "kodi.service"
INSANE_SKIP_${PN} = "rpaths"

FILES_${PN} += "${datadir}/xsessions ${datadir}/icons ${libdir}/xbmc ${datadir}/xbmc ${libdir}/firewalld"
FILES_${PN}-dbg += "${libdir}/kodi/.debug ${libdir}/kodi/*/.debug ${libdir}/kodi/*/*/.debug ${libdir}/kodi/*/*/*/.debug"

# kodi uses some kind of dlopen() method for libcec so we need to add it manually
# OpenGL builds need glxinfo, that's in mesa-demos
RRECOMMENDS_${PN}_append = " libcec \
                             libcurl \
                             libnfs \
                             ${@bb.utils.contains('PACKAGECONFIG', 'x11', 'xdyinfo xrandr xinit mesa-demos', '', d)} \
                             python3 \
                             python3-ctypes \
                             python3-netclient \
                             python3-html \
                             python3-difflib \
                             python3-json \
                             python3-shell \
                             python3-sqlite3 \
                             python3-compression \
                             python3-xmlrpc \
                             tzdata-africa \
                             tzdata-americas \
                             tzdata-antarctica \
                             tzdata-arctic \
                             tzdata-asia \
                             tzdata-atlantic \
                             tzdata-australia \
                             tzdata-europe \
                             tzdata-pacific \
                             xkeyboard-config \
                           "
RRECOMMENDS_${PN}_append_libc-glibc = " glibc-charmap-ibm850 \
                                        glibc-gconv-ibm850 \
                                        glibc-charmap-ibm437 \
                                        glibc-gconv-ibm437 \
					glibc-gconv-unicode \
                                        glibc-gconv-utf-32 \
					glibc-charmap-utf-8 \
					glibc-localedata-en-us \
                                      "
