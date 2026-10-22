package week01

// Day 3 — interfaces, sealed types, exhaustive when. Complete runnable example.
// Run: .\gradlew.bat day3

fun main() {
    val store: TaskStore = InMemoryTaskStore()
    store.add("Read the brief", null)
    store.add("Set up Android Studio", "  ")
    store.setCompleted(1, true)

    println(render(TaskListUiState.Loading))
    println("--")
    println(render(TaskListUiState.Success(store.all())))
    println("--")
    println(render(TaskListUiState.Error("Network unavailable", store.all())))
    println("--")

    try {
        store.add("   ", null)
    } catch (e: IllegalArgumentException) {
        println("rejected: ${e.message}")                   // rejected: Title is required
    }
    println("unknown id updated? ${store.setCompleted(99, true)}")   // false
    println("deleted 2? ${store.delete(2)}; remaining ${store.all().map { it.id }}") // true; [1]
}
