SUMMARY = "Small, safe and fast formatting library http://fmtlib.net"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=af88d758f75f3c5c48a967501f24384b"

PV = "6.1.2"
SRCREV = "f94b7364b9409f05207c3af3fa4666730e11a854"
SRC_URI = "git://github.com/fmtlib/fmt.git;protocol=https;rev=${SRCREV}"

S = "${WORKDIR}/git"

inherit cmake

FILES_${PN}-dev += "${libdir}/cmake"
