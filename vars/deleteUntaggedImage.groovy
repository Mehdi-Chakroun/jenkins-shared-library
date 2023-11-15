import com.example.Docker

def call(String repositoryName) {
    return new Docker(this).deleteUntaggedImage(repositoryName)
}
