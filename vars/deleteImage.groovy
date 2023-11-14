import com.example.Docker

def call(String imageName) {
    return new Docker(this).deleteImage(imageName)

}
