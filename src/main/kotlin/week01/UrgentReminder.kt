package week01

import java.time.LocalDate

/** Reminder message for the urgent tasks (the client asked for it on the call). */
fun List<Task>.urgentReminder(today: LocalDate): String? {
    val urgent = filter { it.isUrgent(today) }
    if (urgent.isEmpty()) return null
    return "Reminder: ${urgent.size} urgent task(s): " + urgent.joinToString { it.title }
}
