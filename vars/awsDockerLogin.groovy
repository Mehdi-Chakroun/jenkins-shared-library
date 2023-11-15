import com.example.Docker

def call(String registryURI, String region) {
    return new Docker(this).awsDockerLogin(registryURI, region)
}
