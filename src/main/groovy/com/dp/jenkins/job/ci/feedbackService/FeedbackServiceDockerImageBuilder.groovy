package com.dp.jenkins.job.ci.feedbackService

import com.dp.jenkins.utils.Constants
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Created by a573881 on 10/13/16.
 */
class FeedbackServiceDockerImageBuilder {
    String jobName
    String gitBranch = 'master'
    String githubOwnerAndProject
    //  String folderUrl
    //  String gitUrl
    String label
    //  String projectName
    String pollScmSchedule
    Boolean shouldDisable = false
    String dockerImageName
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
                    export DOCKER_HOST=tcp://localhost:2376
                    ./gradlew clean build buildDockerImage
                    docker images
                    docker push edplx2023.hq.target.com:5000/datastrategy/${dockerImageName}:latest
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
