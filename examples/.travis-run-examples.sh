#!/bin/bash

function cleanup {
  cd $PLUGIN_BUILD_DIR
}
trap cleanup EXIT

# Install JDK 11
cd $TRAVIS_BUILD_DIR
wget -q https://github.com/sormuras/bach/raw/master/install-jdk.sh && chmod +x install-jdk.sh
JAVA_HOME=~/jdk-11 && ./install-jdk.sh --feature 11 --target $JAVA_HOME && PATH=$JAVA_HOME/bin:$PATH

# Run the examples
cd examples
./gradlew build
