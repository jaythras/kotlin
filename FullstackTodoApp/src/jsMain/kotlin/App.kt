import react.*
import react.dom.*
import kotlinext.js.*
import kotlinx.html.js.*
import kotlinx.coroutines.*
import kotlinx.html.classes

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (todoList, setTodoList) = useState(emptyList<TodoItem>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setTodoList(getTodoList())
        }
    }

    h1 {
        +"Full-Stack Todo List"
    }
    h3 {
        +"* Append exclamation points to the end of your task to set the priority."
    }
    ul {
        todoList.sortedByDescending(TodoItem::priority).forEach { item ->
            li {
                key = item.toString()
                if (item.complete){
                    attrs.classes = mutableSetOf("complete")
                } else {
                    attrs.classes = mutableSetOf()
                }
                +"Priority ${item.priority}: ${item.desc} "

                button {
                    i { attrs.classes = mutableSetOf("far fa-trash-alt")}
                    attrs.classes = mutableSetOf("delete")
                    attrs.onClickFunction = {
                        scope.launch {
                            deleteTodoListItem(item)
                            setTodoList(getTodoList())
                        }

                    }
                }
                attrs.onClickFunction = {
                    scope.launch {
                        //toggle complete
                        toggleComplete(item)
                        setTodoList(getTodoList())
                    }
                }
            }
        }
    }
    child(
            InputComponent,
            props = jsObject {
                onSubmit = { input ->
                    val todoItem = TodoItem(input.replace("!", ""), input.count { it == '!' })
                    scope.launch {
                        addTodoListItem(todoItem)
                        setTodoList(getTodoList())
                    }
                }
            }
    )
}