#!/bin/bash
# Execute only if branch is 'master' and a tag exists that starts with 'v'
if [[ $TRAVIS_BRANCH == "master" && -n "$TRAVIS_TAG" && "$TRAVIS_TAG" == v* ]]; then
    echo "Publishing version: $TRAVIS_TAG"
    echo "gradle.publish.key=$GRADLE_PUBLISH_KEY" >> gradle.properties
    echo "gradle.publish.secret=$GRADLE_PUBLISH_SECRET" >> gradle.properties
    ./gradlew bintrayUpload
    ./gradlew publishPlugins
fi