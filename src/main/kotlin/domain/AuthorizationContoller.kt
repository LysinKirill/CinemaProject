package domain

interface AuthorizationContoller {
    //fun generateSalt(): ByteArray

    fun hashPassword(password: String): String

    fun bytesToString(bytes: ByteArray): String

    fun registerUser(username: String, password: String)

    fun login(username: String, password: String): Boolean
}