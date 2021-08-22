SUMMARY = "C++ framework for implementing distributed and networked applications"
DESCRIPTION = "C++ network programming framework that implements many core \
patterns for concurrent communication software"
LICENSE = "ACE-TAO-CIAO"
HOMEPAGE = "http://www.dre.vanderbilt.edu/~schmidt/ACE.html"
LIC_FILES_CHKSUM = "file://COPYING;md5=d2c090e9c730fd91677782d8e2091d77"

DEPENDS += "openssl gperf-native"

ACE_PV_DIR = "${@d.getVar("PV").replace('.','_')}"
SRC_URI = "https://github.com/DOCGroup/ACE_TAO/releases/download/ACE%2BTAO-${ACE_PV_DIR}/ACE+TAO-${PV}.tar.bz2 \
           file://ace_config.patch \
           file://no_sysctl.patch \
          "

SRC_URI[md5sum] = "b7117ee50b6fd6537e82b5828771d46e"

COMPATIBLE_HOST:libc-musl = "null"

S = "${WORKDIR}/ACE_wrappers"
B = "${WORKDIR}/ACE_wrappers/ace"
export ACE_ROOT="${WORKDIR}/ACE_wrappers"

inherit pkgconfig

CXXFLAGS:append = " -fpermissive -Wnodeprecated-declarations"
CXX:append = " -ffile-prefix-map=${WORKDIR}= -fdebug-prefix-map=${WORKDIR}= "
EXTRA_OEMAKE += "INSTALL_LIB=${baselib}"

do_install() {
    export D="${D}"
    oe_runmake install

    for i in $(find ${D} -name "*.pc") ; do
        sed -i -e s:${D}::g \
               -e s:/${TARGET_SYS}::g \
                  $i
    done

    rm -r ${D}/usr/share
}

UPSTREAM_CHECK_URI = "https://github.com/DOCGroup/ACE_TAO/releases"

BBCLASSEXTEND = "native nativesdk"
