pipeline {
    agent any

    environment {
        TOMCAT_URL       = 'http://localhost:8080'
        TOMCAT_USER      = 'admin'
        TOMCAT_PASSWORD  = 'admin'
        APP_NAME         = 'student-portal'
        WAR_FILE         = "target/${APP_NAME}.war"
        DEPLOY_PATH      = "/opt/tomcat/webapps/${APP_NAME}.war"
    }

    tools {
        maven 'Maven 3.6'
        jdk   'JDK8'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project with Maven...'
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Code Quality') {
            steps {
                echo 'Running static analysis (placeholder — plug in SonarQube here)...'
                sh 'mvn verify -DskipTests'
            }
        }

        stage('Deploy to Tomcat') {
            when {
                branch 'main'
            }
            steps {
                echo "Deploying ${WAR_FILE} to Tomcat at ${TOMCAT_URL}..."
                sh """
                    curl -v \\
                         --user ${TOMCAT_USER}:${TOMCAT_PASSWORD} \\
                         --upload-file ${WAR_FILE} \\
                         "${TOMCAT_URL}/manager/text/deploy?path=/${APP_NAME}&update=true"
                """
            }
        }
    }

    post {
        success {
            echo "Build and deployment successful! Application available at ${TOMCAT_URL}/${APP_NAME}/"
        }
        failure {
            echo 'Build or deployment failed. Check the logs above for details.'
        }
        always {
            cleanWs()
        }
    }
}
