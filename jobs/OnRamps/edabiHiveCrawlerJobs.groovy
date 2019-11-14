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
        jobName: basePath + '/' + "edabiHive-Crawler-Onramp",
        githubOwnerAndProject: "DataPortal/edabiHive-crawler",
        pollScmSchedule: "H 18 * * *",
        shellScript: buildShellScript()
).build(this)

private String buildShellScript(){
    String shellScript = "./gradlew clean test bootRun -Dspring.profiles.active=prod"
    shellScript
}
