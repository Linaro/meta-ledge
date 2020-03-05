#/bin/sh

#Create PK
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=Linaro_LEDGE/ -keyout PK.key -out PK.crt -nodes -days 3650
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc PK.crt PK.esl
sign-efi-sig-list -c PK.crt -k PK.key PK PK.esl PK.auth

#Create KEK
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=Linaro_LEDGE/ -keyout KEK.key -out KEK.crt -nodes -days 3650
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc KEK.crt KEK.esl
sign-efi-sig-list -c PK.crt -k PK.key KEK KEK.esl KEK.auth

#Create DB
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=Linaro_LEDGE/ -keyout db.key -out db.crt -nodes -days 3650
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc db.crt db.esl
sign-efi-sig-list -c KEK.crt -k KEK.key db db.esl db.auth

#Create DBX
openssl req -x509 -sha256 -newkey rsa:2048 -subj /CN=Linaro_LEDGE/ -keyout dbx.key -out dbx.crt -nodes -days 3650
cert-to-efi-sig-list -g 11111111-2222-3333-4444-123456789abc dbx.crt dbx.esl
sign-efi-sig-list -c KEK.crt -k KEK.key dbx dbx.esl dbx.auth

#Sign image
#sbsign --key db.key --cert db.crt Image

#Digest image
#hash-to-efi-sig-list Image db_Image.hash
#sign-efi-sig-list -c KEK.crt -k KEK.key db db_Image.hash db_Image.auth
