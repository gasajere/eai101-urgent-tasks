package week01

// Day 2 — data classes, null safety, collections. Complete runnable example.
// Run: .\gradlew.bat day2

fun main() {
    val tasks = listOf(
        Task(1, "Read Compose docs", null),
        Task(2, "Run the app", "On a real device"),
        Task(3, "Write a test", "   ", isCompleted = true)
    )

    val pending = tasks.filter { !it.isCompleted }
    val titles = tasks.map { it.title }
    val second = tasks.find { it.id == 2L }
    val missing = tasks.find { it.id == 99L }
    val toggled = tasks.toggle(1)

    println("pending: ${pending.size}")                     // 2
    println("titles: $titles")                              // 3 strings
    println("second: ${second?.title}")                     // Run the app
    println("missing: $missing")                            // null
    println("blank description -> ${tasks[2].descriptionOrFallback()}")   // No description
    println("null description  -> ${tasks[0].descriptionOrFallback()}")   // No description
    println("toggled[0].isCompleted = ${toggled[0].isCompleted}")         // true
    println("tasks[0].isCompleted   = ${tasks[0].isCompleted}")           // false (source unchanged)
    println("pendingFirst ids: ${tasks.pendingFirst().map { it.id }}")    // [1, 2, 3]
}
