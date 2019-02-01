# Examples

This folder contains examples on how to use Lombok with this Gradle plugin.

## hello-world

Simple project demonstrating `@Data`, `@SneakyThrows` and `val`.

Run it with:

    ./gradlew :hello-world:build

## delombok-gradle-groovy

Demonstrates how the delombok task can be used to generate JavaDoc with Groovy as the Gradle script language.

Run it with:

    ./gradlew :delombok-gradle-groovy:javadoc

And open up [Greeting.html](delombok-gradle-groovy/build/docs/javadoc/com/example/Greeting.html) in your browser.
