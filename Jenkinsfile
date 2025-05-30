pipeline {
    agent any
    
    environment {
        DOCKER_HUB_CREDS = credentials('ijaeyoung')
        DOCKER_IMAGE = "ijaeyoung/weebeebackend"
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                // GitHub에서 소스코드 체크아웃
                checkout scm
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Docker 이미지 빌드
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:latest ."
            }
        }
        
        stage('Push Docker Image') {
            steps {
                // Docker Hub에 이미지 푸시
                sh "echo ${DOCKER_HUB_CREDS_PSW} | docker login -u ${DOCKER_HUB_CREDS_USR} --password-stdin"
                sh "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                sh "docker push ${DOCKER_IMAGE}:latest"
                sh "docker logout"
            }
        }
        
        stage('Deploy to EC2') {
            steps {
                // EC2 서버에 SSH로 접속하여 배포
                sshagent(['ec2-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@52.78.4.114 '
                            docker pull ${DOCKER_IMAGE}:latest && 
                            docker stop weebee-app || true && 
                            docker rm weebee-app || true && 
                            docker run -d -p 8085:8085 \\
                            --restart=always \\
                            --name weebee-app \\
                            --env-file /home/ubuntu/weebeeBackend/.env \\
                            -v /home/ubuntu/weebeeBackend/logs:/app/logs \\
                            ${DOCKER_IMAGE}:latest
                        '
                    """
                }
            }
        }
        
        stage('Cleanup') {
            steps {
                // 로컬 이미지 정리
                sh "docker rmi ${DOCKER_IMAGE}:${DOCKER_TAG} || true"
                sh "docker rmi ${DOCKER_IMAGE}:latest || true"
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
