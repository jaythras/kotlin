import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window

val endpoint = window.location.origin // only needed until https://github.com/ktorio/ktor/issues/1695 is resolved

val jsonClient = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun getTodoList(): List<TodoItem> {
    return jsonClient.get(endpoint + TodoItem.path)
}

suspend fun addTodoListItem(todoItem: TodoItem) {
    jsonClient.post<Unit>(endpoint + TodoItem.path) {
        contentType(ContentType.Application.Json)
        body = todoItem
    }
}

suspend fun toggleComplete(todoItem: TodoItem) {
    jsonClient.put<Unit>(endpoint + TodoItem.path ) {
        contentType(ContentType.Application.Json)
        body = todoItem
    }
}

suspend fun deleteTodoListItem(todoItem: TodoItem) {
    jsonClient.delete<Unit>(endpoint + TodoItem.path + "/${todoItem.id}")
}