package week01

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UrgentReminderTest {

    private val thursday = LocalDate.of(2026, 10, 22)

    @Test
    fun reminderNamesTheUrgentTasks() {
        val tasks = listOf(
            Task(1, "Send report", null, dueDate = LocalDate.of(2026, 10, 21)),
            Task(5, "Plan team lunch", null, dueDate = LocalDate.of(2026, 10, 26))
        )
        assertEquals("Reminder: 1 urgent task(s): Send report", tasks.urgentReminder(thursday))
    }

    @Test
    fun noReminderWhenNothingIsUrgent() {
        assertNull(emptyList<Task>().urgentReminder(thursday))
    }
}
