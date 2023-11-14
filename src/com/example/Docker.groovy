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
    def deleteUntaggedImage() {
        script.sh "UNTAGGED_IMAGES=\$(aws ecr list-images --repository-name workerservice --filter tagStatus=UNTAGGED --query 'imageIds[*].imageDigest' --output json)"
        script.sh "LATEST_IMAGE=\$(aws ecr list-images --repository-name workerservice --filter tagStatus=TAGGED --query 'imageIds[?imageTag==`latest`].imageDigest' --output json)"
        script.sh "aws ecr batch-delete-image --repository-name workerservice --image-ids $UNTAGGED_IMAGES $EXCLUDE_LATEST"
    }
}
