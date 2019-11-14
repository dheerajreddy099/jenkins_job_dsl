package com.dp.jenkins.job.ci.ProdMSDeployment

import com.dp.jenkins.utils.Constants
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Created by a573881 on 10/13/16.
 */
class ProdDeploymentJob {

    String jobName
    String label
    List hostnames
    String scriptname
    //String computedHostNames = ""
           //hostnames.join(" ")

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
                    defaultSubject('Build Is Broken')
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

                String shellScript = buildShellScript(hostnames.join(" "))
                configure( { project ->
                    def sshNode =  project/builders/"org.jvnet.hudson.plugins.SSHBuilder"
                    sshNode/siteName("svvmas1@edplx2023.hq.target.com:22")
                    sshNode/command(shellScript)
                })
            }

        }

    }

    /**
     * Method to build the shell script for given host names.
     *
     * @param computedHostName
     * @return String
     */
    private String buildShellScript(String computedHostName){
        String shellScript = "cd orchestration\n" +
                "git pull\n" +
                "for docker_hosts in ${computedHostName}\n" +
                "do\n" +
                "    docker-machine env \$docker_hosts\n" +
                "    eval \"\$(docker-machine env \$docker_hosts)\"\n" +
                "    ${scriptname} prod\n" +
                "done"

        shellScript
    }

}
