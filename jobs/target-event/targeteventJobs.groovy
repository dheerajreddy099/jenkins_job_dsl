String basePath = 'target-event'
String repo = 'https://git.target.com/DineshReddy/target-event.git'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}

job("$basePath/target-event") {
    logRotator {
        numToKeep 5
    }
    parameters {
      stringParam 'LanId'
    }
    scm {
        git repo
    }
    steps {
        shell '''
        cat <<EOT >> gradle.properties
        artifactBuildNumber=$BUILD_NUMBER
        artifactory_password=$Password
        artifactory_user=$LanId

EOT

        ./gradlew artifactoryPublish
        '''.stripIndent().trim()
    }
}
