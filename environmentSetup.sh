function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle Properties does not exist"

        echo "Creating Gradle Properties file..."
        touch $GRADLE_PROPERTIES

        echo "Writing Flickr API keys to gradle.properties..."
        echo "flickrApiKey = $FLICKR_API_KEY" >> $GRADLE_PROPERTIES
        echo "flickrConsumerSecret = $FLICKR_CONSUMER_SECRET" >> $GRADLE_PROPERTIES
    fi
}
