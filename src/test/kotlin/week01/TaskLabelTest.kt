package week01

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskLabelTest {

    private val today = LocalDate.of(2026, 10, 31)

    @Test
    fun overdueTaskShowsOverdue() {
        val task = Task(1, "Task", null, dueDate = LocalDate.of(2026, 10, 30))
        assertEquals("Overdue", task.label(today))
    }

    @Test
    fun taskDueTodayShowsToday() {
        val task = Task(2, "Task", null, dueDate = LocalDate.of(2026, 10, 31))
        assertEquals("Today", task.label(today))
    }

    @Test
    fun taskDueTomorrowShowsTomorrowAcrossMonthEnd() {
        val task = Task(3, "Task", null, dueDate = LocalDate.of(2026, 11, 1))
        assertEquals("Tomorrow", task.label(today))
    }

    @Test
    fun taskDueInThreeDaysShowsSoon() {
        val task = Task(4, "Task", null, dueDate = LocalDate.of(2026, 11, 3))
        assertEquals("Soon", task.label(today))
    }

    @Test
    fun taskDueInFourDaysShowsLater() {
        val task = Task(5, "Task", null, dueDate = LocalDate.of(2026, 11, 4))
        assertEquals("Later", task.label(today))
    }

    @Test
    fun completedOverdueTaskShowsDone() {
        val task = Task(6, "Task", null, isCompleted = true, dueDate = LocalDate.of(2026, 10, 30))
        assertEquals("Done", task.label(today))
    }

    @Test
    fun pendingTaskWithoutDueDateShowsNoDate() {
        val task = Task(7, "Task", null)
        assertEquals("No date", task.label(today))
    }

    @Test
    fun completedTaskWithoutDueDateShowsDone() {
        val task = Task(8, "Task", null, isCompleted = true)
        assertEquals("Done", task.label(today))
    }
}
