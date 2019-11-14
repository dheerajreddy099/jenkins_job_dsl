import com.dp.jenkins.job.ci.ingest.IngestApiBuild
import com.dp.jenkins.job.ci.ingest.IngestDockerImageBuilder
import com.dp.jenkins.job.ci.ingest.IngestStage

String basePath = 'ingest'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}


new IngestStage(
        jobName: basePath + '/' + "ingest-stage-deployment"
).build(this)


new IngestDockerImageBuilder(
        jobName: basePath + '/' + "ingestDockerImage" ,
        githubOwnerAndProject: "DataPortal/ingest",
        pollScmSchedule: "H/5 * * * *",
        dockerImageName: "ingest-api",
        downStreamJobNames: ["ingest-stage-deployment"]
).build(this)

new IngestApiBuild(
        jobName: basePath + '/' + "ingest-api",
        githubOwnerAndProject: "DataPortal/ingest",
        pollScmSchedule: "H/5 * * * *",
        downStreamJobNames: ["ingestDockerImage"]
).build(this)

