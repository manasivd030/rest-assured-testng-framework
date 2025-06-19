pipeline{
    agent any

    tools {
        jdk 'jdk-23'
        maven 'M2_HOME'
    }

    stages {
        stage('Build') {
            steps {
                bat "mvn clean install -DskipTests"
            }
        }

        stage('Test') {
            steps {
                script{
                    catchError(buildResult:'UNSTABLE',stageResult:'FAILURE') {
                    withCredentials([usernamePassword(
                            credentialsId:'REST_BOOKER_CREDS',
                            usernameVariable:'RESTBOOKER_USERNAME',
                            passwordVariable:'RESTBOOKER_PASSWORD'

                    )]) {

                            bat "mvn test -P${profile}"
                        }
                    }
                }
            }
        }

        stage('Report') {
            steps{
            script
                    {
                        allure includeProperties: false, jdk: '', results: [[path: 'allure-results']]
                    }
            }

        }

    }
}