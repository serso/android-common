#!/usr/bin/expect -f
spawn android update sdk --filter platform-tools,extra-android-support,add-on,android-17,sysimg-17 --no-ui --force --all
expect "Do you accept the license *:"
send -- "y\r"
interact