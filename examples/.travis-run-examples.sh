#!/bin/bash

function cleanup {
  cd $PLUGIN_BUILD_DIR
}
trap cleanup EXIT

# Run the examples
cd $TRAVIS_BUILD_DIR/examples
./gradlew build
