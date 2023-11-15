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
        script.sh "aws ecr batch-delete-image --repository-name ${repositoryName} --image-ids \$(aws ecr describe-images --repository-name ${repositoryName} --query 'imageDetails[?imageTags==null].imageDigest' --output json)"
    }
    def awsDockerLogin(String registryURI, String region) {
        script.sh "aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${registryURI}"
    }
}
