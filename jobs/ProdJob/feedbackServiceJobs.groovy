package ProdJob

import com.dp.jenkins.job.ci.ProdMSDeployment.ProdDeploymentJob

/**
 * Created by a573881 on 10/13/16.
 */
String basePath = 'Prod'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}

new ProdDeploymentJob(
        jobName: basePath + '/' + "Feedback-Service-Prod",
        hostnames:["edplx2044.hq.target.com","edplx1065.hq.target.com", "edplx1066.hq.target.com"],
        scriptname: "dp-deploy-feedback-service.sh"
).build(this)
