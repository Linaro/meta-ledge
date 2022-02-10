# LEDGE Reference Platform

meta-ledge is the layer that implements the LEDGE reference platform (LEFGE RP).
LEDGE RP is designed to be a secure minimal distro that can be built upon. It
aims to be 'secure by default' saving vendors from having to do the same potentially
error prone integration themselves.

Examples of security features that are pre-integrated here are: -
 * UEFI secure boot
 * LUKS with passphrase sealed to TPM PCRs
 * fTPM support
 * Parsec TPM integration
 * SELinux support

LEDGE RP is commonly used in conjunction with Trusted Substrate project to build
the devices firmware. More information can be found
<http://trustedsubstrate.org/> and <https://git.codelinaro.org/linaro/dependable-boot/meta-ts>

## LEDGE Documentation

The project source documentation can be viewed at
<https://github.com/Linaro/ledge-doc>

meta-ledge layer is used by
<https://github.com/Linaro/ledge-oe-manifest>

CI system is located at https://ci.linaro.org/job/ledge-oe

Mailing list: team-ledge@linaro.org
