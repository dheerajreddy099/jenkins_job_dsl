

job('seed') {
    scm {
        git 'https://git.target.com/DineshReddy/jenkins.git'
    }
    triggers {
        scm 'H/5 * * * *'
    }
    steps {
        dsl {
            external 'jobs/**/*Jobs.groovy'
            additionalClasspath 'src/main/groovy'
        }
    }
}
