package com.dp.jenkins.job.ci.ingest

import com.dp.jenkins.utils.Constants
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Created by a573881 on 10/12/16.
 */
class IngestApiBuild {
    String jobName
    String gitBranch = 'master'
    String githubOwnerAndProject
    String label
    String pollScmSchedule
    Boolean shouldDisable = false
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
            triggers {
                scm pollScmSchedule
                githubPush()
            }
            triggers {
                githubPush()
            }
            steps {
                shell """
                    cd ingest-api
                    ./gradlew clean dependencies build
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
                downstream(downStreamJobNames)
                wsCleanup({})
            }

        }

    }
}
