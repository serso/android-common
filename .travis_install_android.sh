#!/usr/bin/expect
spawn android update sdk --filter platform-tools,extra-android-support,add-on,android-17,sysimg-17 --no-ui --force --all
expect "[y/n]:*"
send -- "y\r"
interact