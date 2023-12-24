package presentation.model

data class OutputModel(
    private val message: String,
) {
    override fun toString(): String {
        return message
    }
}