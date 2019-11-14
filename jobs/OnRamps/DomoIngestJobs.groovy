package OnRamps

import com.dp.jenkins.job.onRamps.OnRamps

/**
 * Created by a573881 on 10/14/16.
 */
String basePath = 'OnRamps'



folder(basePath) {
    description 'This example shows basic folder/job creation.'
}

new OnRamps(
        jobName: basePath + '/' + "Domo-Ingest-Onramp",
        githubOwnerAndProject: "DataPortal/domo-ingest",
        pollScmSchedule: "H 6 * * *",
        shellScript: buildShellScript()
).build(this)

private String buildShellScript(){
    String shellScript = "./gradlew clean build\n" +
            "./gradlew clean test\n" +
            "./gradlew clean bootRun -Dspring.profiles.active=STAGE\n" +
            "./gradlew clean bootRun -Dspring.profiles.active=PROD"
    shellScript
}
