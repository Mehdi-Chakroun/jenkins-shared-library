package com.example

class Docker implements Serializable {
    def script
    Docker(script) {
        this.script = script
    }
    def buildDockerImage(String imageName) {
        script.echo 'Building docker image...'
        script.sh "docker build -t ${script.imageName} ."
    }
    def dockerLogin() {
        script.withCredentials([script.usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "echo ${script.PASS} | docker login -u ${script.USER} --password-stdin"
        }
    }
    def dockerPush(String imageName) {
        script.sh "docker push ${script.imageName}"
    }
}
