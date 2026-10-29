package week01

import java.time.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UrgentTasksTest {

    private val thursday = LocalDate.of(2026, 10, 22)
    private val wednesday = LocalDate.of(2026, 10, 21)

    private val sendReport = Task(1, "Send report", null, dueDate = LocalDate.of(2026, 10, 21))
    private val bookRoom = Task(2, "Book meeting room", null, dueDate = LocalDate.of(2026, 10, 22))
    private val paySupplier = Task(3, "Pay supplier", null, isCompleted = true, dueDate = LocalDate.of(2026, 10, 20))
    private val renewLicence = Task(4, "Renew licence", null, dueDate = LocalDate.of(2026, 10, 14))

    @Test
    fun urgentTasksOnThursday() {
        assertTrue(sendReport.isUrgent(thursday))
        assertTrue(bookRoom.isUrgent(thursday))
        assertTrue(renewLicence.isUrgent(thursday))
    }

    @Test
    fun completedTaskIsNeverUrgent() {
        assertFalse(paySupplier.isUrgent(thursday))
    }

    @Test
    fun taskWithNoDueDateIsNotUrgent() {
        assertFalse(bookRoom.copy(dueDate = null).isUrgent(thursday))
    }

    @Test
    fun taskDueInTwoDaysIsNotUrgent() {
        val tuesday = LocalDate.of(2026, 10, 20)
        assertFalse(bookRoom.isUrgent(tuesday))
    }
    @Test
    fun taskDueTomorrowIsUrgent(){
        assertTrue(bookRoom.isUrgent(wednesday))
    }
}
