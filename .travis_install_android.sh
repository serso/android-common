#!/usr/bin/expect -f
spawn android update sdk --filter platform-tools,extra-android-support,add-on,android-16,sysimg-16,extra-google-play_billing,extra-google-m2repository,extra-google-google_play_services --no-ui --force --all
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