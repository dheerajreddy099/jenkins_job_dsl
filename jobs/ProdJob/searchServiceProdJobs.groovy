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
        jobName: basePath + '/' + "Search-Service-Prod",
        hostnames:["edplx1015.hq.target.com", "edplx1016.hq.target.com", "edplx1017.hq.target.com"],
        scriptname: "dp-deploy-search-service.sh"
).build(this)