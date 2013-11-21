#!/bin/sh

# Scripts installs Android SDK artifacts in local Maven repository

git clone git://github.com/serso/maven-android-sdk-deployer.git

# install platform
cd ./maven-android-sdk-deployer/
git checkout tags/api-19-mvn3.1
mvn install -P4.2

# install admob
cd ./extras/admob
mvn install
cd ../..

#exit
cd ..