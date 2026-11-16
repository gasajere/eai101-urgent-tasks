package week01

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class UrgentFirstReportTest {
    private val payRent = Task(1, "Pay rent", null, dueDate = LocalDate.of(2026, 11, 3))
    private val callBank = Task(2, "Call bank", null, dueDate = LocalDate.of(2026, 11, 1))
    private val cards = listOf<Task>(payRent, callBank)
    @Test
    fun taskDueTomorrowComesFirst(){
        val today = LocalDate.of(2026, 10, 31)
        assertEquals(
            listOf<Long>(2,1),
            cards.urgentFirst(today).map { it.id }
        )
    }
}
