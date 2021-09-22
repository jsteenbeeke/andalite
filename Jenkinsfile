def quiet_sh(cmd) {
    sh('#!/bin/sh -e\n' + cmd)
}

pipeline {
    agent {
        docker {
            image 'maven:3.8-openjdk-17'
            label 'docker'
        }
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
                sh 'mvn -B -U clean verify package -P-disable-slow-tests'
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
                script {
                    if (env.MAVEN_DEPLOY_USER == null || env.MAVEN_DEPLOY_USER.isEmpty()) {
                        error('Deployment user not set')
                    } else if (env.MAVEN_DEPLOY_PASSWORD == null || env.MAVEN_DEPLOY_PASSWORD.isEmpty()) {
                        error('Deployment password not set')
                    }
                }
                quiet_sh 'mkdir -p /root/.m2'
                quiet_sh 'echo "<settings><servers><server><id>repo-jeroensteenbeeke-releases</id><username>'+ env.MAVEN_DEPLOY_USER +'</username><password>'+ env.MAVEN_DEPLOY_PASSWORD +'</password></server><server><id>repo-jeroensteenbeeke-snapshots</id><username>'+ env.MAVEN_DEPLOY_USER + '</username><password>'+ env.MAVEN_DEPLOY_PASSWORD +'</password></server></servers></settings>" > /root/.m2/settings.xml'
                sh 'mvn deploy -s /root/.m2/settings.xml -DskipTests=true'
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
            sh 'rm $MAVEN_CONFIG/settings.xml'

            step([$class                  : 'Mailer',
                  notifyEveryUnstableBuild: true,
                  sendToIndividuals       : true,
                  recipients              : 'j.steenbeeke@gmail.com'
            ])
            junit allowEmptyResults: true, testResults: '**/target/test-reports/*.xml'
        }
    }

}
