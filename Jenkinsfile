pipeline {
    agent any // 빌드를 실행할 Jenkins 에이전트 (any는 아무 에이전트나 사용)

    environment {
        // Docker 이미지 이름 설정
        DOCKER_IMAGE_NAME = "test-app-spring"
        // Docker Hub 사용자 이름 (선택 사항, Docker Hub에 푸시할 경우 필요)
        // DOCKER_HUB_USERNAME = "your-dockerhub-username"
    }

    stages {
        stage('Checkout Code') {
            steps {
                // GitHub에서 소스 코드 체크아웃
                git branch: 'main',  url: 'https://github.com/K1ng1nsu/jenkins-test-spring.git'
//                 git branch: 'main', credentialsId: 'your-github-credentials-id', url: 'https://github.com/K1ng1nsu/jenkins-test-spring.git'
                // 'your-github-credentials-id'는 Jenkins에 등록된 GitHub 자격증명의 ID입니다.
                // CredentialsId는 Jenkins > Jenkins 관리 > Credentials > System > Global credentials 에서 확인할 수 있습니다.
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Dockerfile이 있는 디렉토리로 이동 (보통 프로젝트 루트)
                    // 현재 디렉토리에서 Docker 이미지를 빌드합니다.
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${BUILD_NUMBER} ."
                    // ${BUILD_NUMBER}는 Jenkins 빌드 번호를 사용하여 이미지 태그를 유니크하게 만듭니다.
                    // 필요에 따라 'latest' 태그도 추가할 수 있습니다.
                    sh "docker tag ${DOCKER_IMAGE_NAME}:${BUILD_NUMBER} ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Stop & Remove Old Container') {
            steps {
                script {
                    // 기존에 실행 중인 컨테이너가 있다면 중지하고 제거합니다.
                    // 컨테이너 이름은 자유롭게 지정할 수 있습니다.
                    def containerId = sh(returnStdout: true, script: "docker ps -aq --filter name=^test-app-spring\$").trim()
                    if (containerId) {
                        sh "docker stop ${containerId}"
                        sh "docker rm ${containerId}"
                    } else {
                        echo "No existing container 'my-app-container' found."
                    }
                }
            }
        }

        stage('Run New Docker Container') {
            steps {
                script {
                    // 새로운 Docker 컨테이너를 실행합니다.
                    // -p 80:8080 : 호스트의 80번 포트를 컨테이너의 8080번 포트에 매핑
                    // --name my-app-container : 컨테이너 이름 지정
                    sh "docker run -d -p 8080:8080 --name test-app-spring ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        // 선택 사항: Docker Hub에 이미지 푸시
        // stage('Push Docker Image to Docker Hub') {
        //     steps {
        //         script {
        //             // Docker Hub 로그인 (Jenkins Credentials에 Docker Hub 자격증명 등록 필요)
        //             // Docker Hub 자격증명 ID는 'docker-hub-credentials'로 가정합니다.
        //             // sh "echo ${env.DOCKER_HUB_PASSWORD} | docker login -u ${env.DOCKER_HUB_USERNAME} --password-stdin" // 보안상 env 사용 권장
        //             withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_HUB_PASSWORD', usernameVariable: 'DOCKER_HUB_USERNAME')]) {
        //                 sh "docker login -u ${DOCKER_HUB_USERNAME} -p ${DOCKER_HUB_PASSWORD}"
        //                 sh "docker push ${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:${BUILD_NUMBER}"
        //                 sh "docker push ${DOCKER_HUB_USERNAME}/${DOCKER_IMAGE_NAME}:latest"
        //             }
        //         }
        //     }
        // }
    }

    post {
        always {
            // 빌드 완료 후 항상 실행되는 블록 (성공/실패 여부와 관계없이)
            echo 'Pipeline finished.'
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}