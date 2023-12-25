package presentation.model

data class OutputModel(
    val message: String,
) {
    override fun toString(): String {
        return message
    }
}