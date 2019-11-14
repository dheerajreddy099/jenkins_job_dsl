/*
package ProdJob

import com.dp.jenkins.job.ci.ProdMSDeployment.ProdDeploymentJob
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.JobParent


class TestJobs {

    public static void main(String[] args) {
        try{
            DslFactory dslFactory;
            ProdDeploymentJob prodJob = new ProdDeploymentJob(
                    jobName: "Test" + '/' + "Search-Service-Prod",
                    hostnames:["edplx1015.hq.target.com", "edplx1016.hq.target.com", "edplx1017.hq.target.com"],
                    scriptname: "dp-deploy-search-service.sh"
            ).build(dslFactory)
        }catch(Exception e){
          e.printStackTrace()
        }
    }
}
*/
