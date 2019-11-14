package feedbackService

import com.dp.jenkins.job.ci.feedbackService.FeedbackServiceBuild
import com.dp.jenkins.job.ci.feedbackService.FeedbackServiceDockerImageBuilder
import com.dp.jenkins.job.ci.feedbackService.FeedbackServiceStage


String basePath = 'feedback-Service'

folder(basePath) {
    description 'This example shows basic folder/job creation.'
}


new FeedbackServiceStage(
        jobName: basePath + '/' + "FeedbackService-stage-deployment"
).build(this)


new FeedbackServiceDockerImageBuilder(
        jobName: basePath + '/' + "FeedbackServiceDockerImage" ,
        githubOwnerAndProject: "DataPortal/feedback-service",
        pollScmSchedule: "H/5 * * * *",
        dockerImageName: "ingest-api",
        downStreamJobNames: ["FeedbackService-stage-deployment"]
).build(this)

new FeedbackServiceBuild(
        jobName: basePath + '/' + "Feedback-Service",
        githubOwnerAndProject: "DataPortal/feedback-service",
        pollScmSchedule: "H/5 * * * *",
        downStreamJobNames: ["FeedbackServiceDockerImage"]
).build(this)
