#!/bin/bash

wget https://projectlombok.org/downloads/lombok.jar

version=$( java -jar lombok.jar --version | sed -n 's/v\([0-9\.]*\).*/\1/p' )
hash=$( shasum -a 256 lombok.jar | cut -d ' ' -f 1 )
echo "Updating to Lombok $version with hash: $hash"

sed -i '' -e "s/\(DEFAULT_VERSION = \"\)[0-9\.]*/\1$version/g" src/main/groovy/io/franzbecker/gradle/lombok/LombokPluginExtension.groovy
sed -i '' -e "s/\(DEFAULT_VERSION_HASH = \"\)[^\"]*/\1$hash/g" src/main/groovy/io/franzbecker/gradle/lombok/LombokPluginExtension.groovy
sed -i '' -e "s/\(version = \"\)[0-9\.]*/\1$version/g" ../README.md
sed -i '' -e "s/\(sha256 = \"\)[^\"][^\"]*/\1$hash/g" ../README.md

git add src/main/groovy/io/franzbecker/gradle/lombok/LombokPluginExtension.groovy
git add ../README.md

git commit -m "Update the default Lombok version to $version"
rm lombok.jar
