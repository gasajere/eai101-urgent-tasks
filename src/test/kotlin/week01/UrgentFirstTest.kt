package week01


import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class UrgentFirstTest {

    private val thursday = LocalDate.of(2026, 10, 22)

    private val cards = listOf(
        Task(1, "Send report", null, dueDate = LocalDate.of(2026, 10, 21)),
        Task(2, "Book meeting room", null, dueDate = LocalDate.of(2026, 10, 22)),
        Task(3, "Pay supplier", null, isCompleted = true, dueDate = LocalDate.of(2026, 10, 20)),
        Task(4, "Renew licence", null, dueDate = LocalDate.of(2026, 10, 14)),
        Task(5, "Plan team lunch", null, dueDate = LocalDate.of(2026, 10, 26)),
        Task(6, "Call the airline", null),
        Task(7, "Order printer paper", null, dueDate = LocalDate.of(2026, 10, 23)),
        Task(8, "Update contact list", null, isCompleted = true, dueDate = LocalDate.of(2026, 10, 23)),
    )

    @Test
    fun urgentTasksComeFirstOnThursday() {
        assertEquals(
            listOf<Long>(1, 2, 4, 5, 6, 7, 3, 8),
            cards.urgentFirst(thursday).map { it.id }
        )
    }
}