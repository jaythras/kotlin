import com.mongodb.client.model.UpdateOptions
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue


val client = KMongo.createClient().coroutine
val database = client.getDatabase("todoList")
val collection = database.getCollection<TodoItem>()

fun main() {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Put)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                call.respondText(
                        this::class.java.classLoader.getResource("index.html")!!.readText(),
                        ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route(TodoItem.path){
                get {
                    call.respond(collection.find().toList())
                }
                post {
                    collection.insertOne(call.receive<TodoItem>())
                    call.respond(HttpStatusCode.OK)
                }
                put {
                    val item = call.receive<TodoItem>()
                    collection.updateOne(TodoItem:: id eq item.id, setValue(TodoItem::complete, !item.complete))
                    call.respond(HttpStatusCode.OK)
                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    collection.deleteOne(TodoItem::id eq id)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }.start(wait = true)
}