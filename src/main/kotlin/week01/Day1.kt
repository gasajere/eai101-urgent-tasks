package week01

// Day 1 — values, functions, control flow. Complete runnable example.
// Run: .\gradlew.bat day1

fun describeTask(title: String, isCompleted: Boolean = false): String {
    val status = if (isCompleted) "done" else "pending"
    return "$title ($status)"
}

fun priorityLabel(daysLeft: Int): String = when {
    daysLeft < 0 -> "Overdue"
    daysLeft == 0 -> "Today"
    daysLeft in 1..3 -> "Soon"
    else -> "Later"
}

fun main() {
    println(describeTask("Write notes"))           // Write notes (pending)
    println(describeTask("Write notes", true))     // Write notes (done)
    for (d in -1..5) println("$d -> ${priorityLabel(d)}")
}
