package com.dp.jenkins.job.ci.ingest

import com.dp.jenkins.utils.Constants
import groovy.transform.Canonical
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


// import com.tgt.esv.jenkins.Constants
@Canonical
class IngestDockerImageBuilder {

    String jobName
    String gitBranch = 'master'
    String githubOwnerAndProject
  //  String folderUrl
  //  String gitUrl
    String label
  //  String projectName
    String pollScmSchedule
    Boolean shouldDisable = false
    String dockerImageName = "ingest-api"
    List downStreamJobNames


    Job build(DslFactory dslFactory)  {
        dslFactory.job(jobName) {
            if (label) {
                'label' label
            }
            disabled(shouldDisable)
            logRotator(-1, 5, -1, -1)
            scm {
                git {
                    remote {
                        github(githubOwnerAndProject,"https",Constants.GHE_HOST) //githubOwnerAndProject
                        credentials(Constants.CREDENTIAL_ID)
                    }
                    branch gitBranch
                }
            }
            steps {
                shell """
                    cd ingest-api/
                    git clone git@git.target.com:EDABI-Core/Core.git
                    cd Core
                    sbt clean package publishM2
                    cd ../
                    export DOCKER_HOST=tcp://localhost:2376
                    ./gradlew clean build buildDockerImage
                    docker images
                    docker push edplx2023.hq.target.com:5000/datastrategy/${dockerImageName}:\$BUILD_NUMBER
                    docker rmi \$(docker images -q)
                """.stripIndent().trim()

            }


            publishers {
                extendedEmail({
                    recipientList(Constants.RECEPIENT_LIST as String[])
                    defaultSubject('Build Is Broken For:'+jobName)
                    defaultContent('Build Is Broken For Job :'+jobName)
                    contentType('text/html')
                    attachBuildLog(true)
                    triggers {
                        beforeBuild()
                        stillUnstable {
                            subject('Build Is Broken For Job :'+jobName)
                            content('<h1>Build Is Broken For Job :'+jobName+"</h1>")
                        }
                    }
                })
                        //"SIA-EDABI-SG-OPs@Target.com"

                wsCleanup({})
                downstream(downStreamJobNames)

                configure { project ->
                    def sshNode =  project/builders/"org.jvnet.hudson.plugins.SSHBuilder"
                    sshNode/siteName("svvmas1@edplx2023.hq.target.com:22")
                    sshNode/command( "echo 'just for test'")
                }
            }

        }

    }
}
