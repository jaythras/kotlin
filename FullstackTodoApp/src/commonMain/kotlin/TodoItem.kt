import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(val desc: String, val priority: Int) {
    val id: Int = desc.hashCode()
    val complete: Boolean = false
    companion object {
        const val path = "/TodoApp"
    }
}