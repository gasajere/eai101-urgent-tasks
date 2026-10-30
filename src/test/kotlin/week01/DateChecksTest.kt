package week01

import java.time.LocalDate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Thursday's date checks for "Book meeting room" (due Thu 22 Oct).
class DateChecksTest {

    private val bookRoom = Task(2, "Book meeting room", null, dueDate = LocalDate.of(2026, 10, 22))

    @Test
    fun bookRoomIsNotUrgentTheDayBefore() {
        assertFalse(bookRoom.isUrgent(LocalDate.of(2026, 10, 21)))
    }

    @Test
    fun bookRoomIsUrgentOnTheDay() {
        assertTrue(bookRoom.isUrgent(LocalDate.of(2026, 10, 22)))
    }
}
