#!/usr/bin/expect -f
spawn android update sdk --filter platform-tools,build-tools-18.1.1,extra-android-support,add-on,android-17,sysimg-17,android-19,sysimg-19,extra-google-play_billing,extra-google-m2repository,extra-google-admob_ads_sdk,extra-google-analytics_sdk_v2,extra-google-gcm,extra-google-play_licensing,extra-google-google_play_services,extra-google-play_apk_expansion --no-ui --force --obsolete --all
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
expect "Do you accept the license *:"
send -- "y\r"
interact