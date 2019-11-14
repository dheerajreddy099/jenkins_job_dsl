import com.dp.jenkins.job.ci.searchService.SearchServiceBuild
import com.dp.jenkins.job.ci.searchService.SearchServiceDockerImageBuilder
import com.dp.jenkins.job.ci.searchService.SearchServiceStage

String basePath = 'Search-Service'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}


new SearchServiceStage(
        jobName: basePath + '/' + "search-service-stage-deployment"
).build(this)


new SearchServiceDockerImageBuilder(
        jobName: basePath + '/' + "Search-Service-Docker-Image" ,
        githubOwnerAndProject: "DataPortal/search-service",
        pollScmSchedule: "H/5 * * * *",
        dockerImageName: "search-service",
        downStreamJobNames: ["search-service-stage-deployment"]
).build(this)

new SearchServiceBuild(
        jobName: basePath + '/' + "Search-service",
        githubOwnerAndProject: "DataPortal/search-service",
        pollScmSchedule: "H/5 * * * *",
        downStreamJobNames: ["Search-Service-Docker-Image"]
).build(this)