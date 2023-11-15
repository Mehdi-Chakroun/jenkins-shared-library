package com.example

class Docker implements Serializable {
    def script
    Docker(script) {
        this.script = script
    }
    def buildDockerImage(String imageName) {
        script.echo 'Building docker image...'
        script.sh "docker build -t ${imageName} ."
    }
    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "echo ${script.PASS} | docker login -u ${script.USER} --password-stdin"
        }
    }
    def dockerPush(String imageName) {
        script.sh "docker push ${imageName}"
    }
    def deleteLocalImage(String imageName) {
        script.sh "docker rmi ${imageName}"
    }
    def deleteUntaggedImage(String repositoryName) {
        def untaggedImages = script.sh(script: "aws ecr list-images --repository-name ${repositoryName} --filter tagStatus=UNTAGGED --query 'imageIds[*].imageDigest' --output json", returnStdout: true).trim()
        def latestImage = script.sh(script: "aws ecr list-images --repository-name ${repositoryName} --filter tagStatus=TAGGED --query 'imageIds[?imageTag==`latest`].imageDigest' --output json", returnStdout: true).trim()
        def excludeLatest = "--image-ids imageDigest=${latestImage}"
        script.sh "aws ecr batch-delete-image --repository-name ${repositoryName} --image-ids ${untaggedImages.split('\n').join(' ')} ${excludeLatest}"
    }
    def awsDockerLogin(String registryURI, String region) {
        script.sh "aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${registryURI}"
    }
}
