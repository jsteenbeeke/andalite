@Library(value = 'jenkins-shared-library@main', changelog = false)
import com.jeroensteenbeeke.hyperion.*

Hyperion hyperion = new Hyperion(this)
def pollInterval = hyperion.scmPollInterval()

pipeline {
    agent {
        docker {
            image 'registry.jeroensteenbeeke.nl/maven:latest'
            label 'docker'
        }
    }

    triggers {
       pollSCM(pollInterval)
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        disableConcurrentBuilds()
    }

    environment {
        MAVEN_DEPLOY_USER = credentials('MAVEN_DEPLOY_USER')
        MAVEN_DEPLOY_PASSWORD = credentials('MAVEN_DEPLOY_PASSWORD')
    }

    stages {
        stage('Maven') {
            steps {
                sh 'mvn -ntp -B -U clean verify package'
            }
        }
        stage('Coverage') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS' || currentBuild.result == null
                }
            }

            steps {
                jacoco changeBuildStatus: false, exclusionPattern: '**/*Test*.class,**/HelpMojo.class'
            }
        }
        stage('Deploy') {
            when {
                expression {
                    currentBuild.result == 'SUCCESS' || currentBuild.result == null
                }
            }

            steps {
                mavenDeploy deployUser: env.MAVEN_DEPLOY_USER,
                        deployPassword: env.MAVEN_DEPLOY_PASSWORD,
                        projects: [".", "core", "core-test", "forge", "java", "maven-plugin", "testbase", "xml"]
            }
        }
    }

    post {
        always {
            script {
                if (currentBuild.result == null) {
                    currentBuild.result = 'SUCCESS'
                }
            }
            sh 'rm -f /home/jenkins/.m2/settings.xml'

            step([$class                  : 'Mailer',
                  notifyEveryUnstableBuild: true,
                  sendToIndividuals       : true,
                  recipients              : 'j.steenbeeke@gmail.com'
            ])
            junit allowEmptyResults: true, testResults: '**/target/test-reports/*.xml'
        }
    }

}
