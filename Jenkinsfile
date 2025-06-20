pipeline {
    agent any

    tools {
        dockerTool 'Docker'
    }

    stages {
        stage('Build') {
            steps {
                bat "docker build -t rest-assured-cucumber ."
            }
        }

        stage('Test') {
            steps {
                script {
                    catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                        withCredentials([
                                usernamePassword(
                                        credentialsId: 'REST_BOOKER_CREDS',
                                        usernameVariable: 'RESTBOOKER_USERNAME',
                                        passwordVariable: 'RESTBOOKER_PASSWORD'
                                )
                        ]) {
                            bat """
                            docker run ^
                            -v %CD%\\allure-results:/app/allure-results ^
                            -e RESTBOOKER_USERNAME=%RESTBOOKER_USERNAME% ^
                            -e RESTBOOKER_PASSWORD=%RESTBOOKER_PASSWORD% ^
                            -e MAVEN_PROFILE=parallel ^
                            rest-assured-cucumber
                            """
                        }
                    }
                }
            }
        }

        stage('Report') {
            steps {
                script {
                    allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
                }
            }
        }
    }
}
