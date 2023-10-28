
def call() {
    echo 'building Jar for branch ${BRANCH_NAME}...'
    sh 'mvn clean package'
}
