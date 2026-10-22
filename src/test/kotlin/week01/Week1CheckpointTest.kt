package week01

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// Day 5 checkpoint, expressed as tests. These are the "verify and explain" items from the handbook.

@OptIn(ExperimentalCoroutinesApi::class)   // StandardTestDispatcher / advanceUntilIdle
class Week1CheckpointTest {

    // ---- Day 1–2: pure functions ----

    @Test
    fun priorityLabelBoundaries() {
        assertEquals("Overdue", priorityLabel(-1))
        assertEquals("Today", priorityLabel(0))
        assertEquals("Soon", priorityLabel(1))
        assertEquals("Soon", priorityLabel(3))
        assertEquals("Later", priorityLabel(4))
    }

    @Test
    fun toggleReturnsNewListAndLeavesSourceUntouched() {
        val tasks = listOf(Task(1, "a", null), Task(2, "b", null))
        val toggled = tasks.toggle(2)
        assertTrue(toggled[1].isCompleted)
        assertFalse(tasks[1].isCompleted)
        assertFalse(toggled[0].isCompleted)
    }

    @Test
    fun nullAndBlankDescriptionsUseFallback() {
        assertEquals("No description", Task(1, "a", null).descriptionOrFallback())
        assertEquals("No description", Task(1, "a", "   ").descriptionOrFallback())
        assertEquals("real", Task(1, "a", "real").descriptionOrFallback())
    }

    // ---- Day 3: store rules ----

    @Test
    fun storeRejectsBlankTitleAndKeepsExistingTasks() {
        val store = InMemoryTaskStore()
        store.add("keep me", null)
        try {
            store.add("   ", null)
            throw AssertionError("blank title should be rejected")
        } catch (expected: IllegalArgumentException) {
            assertEquals(1, store.all().size)
        }
    }

    @Test
    fun storeUnknownIdChangesNothing() {
        val store = InMemoryTaskStore()
        store.add("a", null)
        assertFalse(store.setCompleted(99, true))
        assertFalse(store.delete(99))
        assertEquals(listOf(1L), store.all().map { it.id })
    }

    @Test
    fun duplicateTitlesGetDistinctIds() {
        val store = InMemoryTaskStore()
        val first = store.add("same", null)
        val second = store.add("same", null)
        assertTrue(first.id != second.id)
    }

    // ---- Day 4–5: repository + state holder with explicit lifetime ----

    @Test
    fun holderAddToggleDeleteFlowThroughAllLayers() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(SupervisorJob() + dispatcher)
        val holder = TaskStateHolder(InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 10), scope)

        advanceUntilIdle()
        assertEquals(TaskListUiState.Success(emptyList()), holder.state.value)

        holder.onAdd("one", null); advanceUntilIdle()
        assertEquals(listOf("one"), holder.state.value.visibleTasks.map { it.title })

        holder.onToggle(1, true); advanceUntilIdle()
        assertTrue(holder.state.value.visibleTasks.single().isCompleted)

        holder.onDelete(1); advanceUntilIdle()
        assertEquals(TaskListUiState.Success(emptyList()), holder.state.value)

        holder.close()
    }

    @Test
    fun failedAddShowsErrorButKeepsEarlierTasksVisible() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(SupervisorJob() + dispatcher)
        val holder = TaskStateHolder(InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 10), scope)

        holder.onAdd("first", null); advanceUntilIdle()
        holder.onAdd("   ", null); advanceUntilIdle()

        val state = holder.state.value
        assertTrue(state is TaskListUiState.Error)
        assertEquals("Title is required", (state as TaskListUiState.Error).message)
        assertEquals(listOf("first"), state.tasks.map { it.title })

        holder.close()
    }

    @Test
    fun closeCancelsTheCollectorAndEverythingInTheScope() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val scope = CoroutineScope(SupervisorJob() + dispatcher)
        val holder = TaskStateHolder(InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 10), scope)
        advanceUntilIdle()
        assertTrue(holder.collectorJob.isActive)

        holder.close()
        advanceUntilIdle()

        assertFalse(holder.collectorJob.isActive)
        assertFalse(scope.coroutineContext[kotlinx.coroutines.Job]!!.isActive)
    }

    @Test
    fun repositoryFlowEmitsCurrentListToANewCollector() = runTest {
        val repo = InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 0)
        repo.add("x", null)
        assertEquals(listOf("x"), repo.observeAll().first().map { it.title })
        assertNull(repo.observeAll().first().firstOrNull { it.title == "y" })
    }
}
