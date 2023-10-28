
def call(imageName) {
    echo 'Building docker image...'
    withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${imageName} ."
        sh "echo ${PASS} | docker login -u ${USER} --password-stdin"
        sh "docker push ${imageName}"
    }
}
