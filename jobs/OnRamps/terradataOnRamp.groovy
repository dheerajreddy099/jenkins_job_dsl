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
        jobName: basePath + '/' + "TerradataIngest-Onramp",
        githubOwnerAndProject: "DataPortal/teradata-ingest",
        pollScmSchedule: "H 14 * * *",
        shellScript: buildShellScript()
).build(this)

private String buildShellScript(){
    String shellScript = "./gradlew clean test\n" +
            "#./gradlew clean bootRun -Dspring.profiles.active=STAGE -Dteradata.dbproda.crawler.type=full\n" +
            "./gradlew clean bootRun -Dspring.profiles.active=PROD -Dteradata.dbproda.crawler.type=full"
    shellScript
}
