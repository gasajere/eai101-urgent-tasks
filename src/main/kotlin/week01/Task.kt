package week01

import java.time.LocalDate
import java.time.temporal.ChronoUnit

// Day 2 — the domain model used for the rest of the course.

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean = false,
    val dueDate: LocalDate? = null
)

fun Task.descriptionOrFallback(): String =
    description?.takeIf { it.isNotBlank() } ?: "No description"

/** Returns a new list; the receiver is never modified. */
fun List<Task>.toggle(id: Long): List<Task> =
    map { if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it }

fun List<Task>.pendingFirst(): List<Task> = sortedBy { it.isCompleted }

/** Urgent = not done, and overdue or due today (client rules, W2D2). */
fun Task.isUrgent(today: LocalDate): Boolean {
    if (isCompleted) return false
    val due = dueDate ?: return false
    val daysLeft = ChronoUnit.DAYS.between(today, due).toInt()
    return daysLeft <= 0
}
