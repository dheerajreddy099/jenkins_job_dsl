package com.dp.jenkins.job.ci.feedbackService

import com.dp.jenkins.utils.Constants
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Created by a573881 on 10/13/16.
 */
class FeedbackServiceStage {
    String jobName
    String label

    //  String projectName
    Boolean shouldDisable = false

    Job build(DslFactory dslFactory)  {
        dslFactory.job(jobName) {
            if (label) {
                'label' label
            }
            disabled(shouldDisable)
            logRotator(-1, 5, -1, -1)

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

                configure( { project ->
                    def sshNode =  project/builders/"org.jvnet.hudson.plugins.SSHBuilder"
                    sshNode/siteName("svvmas1@edplx2023.hq.target.com:22")
                    sshNode/command( shellscript)
                })
            }

        }

    }
    def shellscript = "cd orchestration\n" +
            "git pull\n" +
            "for docker_hosts in edplx2043.hq.target.com edplx2055.hq.target.com edplx2056.hq.target.com\n" +
            "do\n" +
            "    #docker-machine env \$docker_hosts\n" +
            "    eval \"\$(dm.sh \$docker_hosts)\"\n" +
            "    dp-deploy-feedback-service.sh stage\n" +
            "done"




}
