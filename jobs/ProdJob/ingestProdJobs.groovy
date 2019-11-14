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
        jobName: basePath + '/' + "Ingest-Prod",
        hostnames:["edplx1045.hq.target.com","edplx1041.hq.target.com", "edplx1042.hq.target.com", "edplx1043.hq.target.com"],
        scriptname: "dp-deploy-ingest-api.sh"
).build(this)

