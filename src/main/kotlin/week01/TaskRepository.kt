package week01

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// Day 4 — the data layer as the ViewModel will see it: a Flow to observe, suspend functions to change.

interface TaskRepository {
    fun observeAll(): Flow<List<Task>>
    suspend fun add(title: String, description: String?)
    suspend fun setCompleted(id: Long, completed: Boolean)
    suspend fun delete(id: Long)
}

/**
 * Wraps the synchronous Day 3 store. `delay` stands in for real I/O so the suspension is visible.
 * Every change re-publishes the whole list through one MutableStateFlow.
 */
class InMemoryTaskRepository(
    private val store: TaskStore,
    private val simulatedLatencyMs: Long = 300
) : TaskRepository {
    private val tasks = MutableStateFlow(store.all())

    override fun observeAll(): Flow<List<Task>> = tasks

    override suspend fun add(title: String, description: String?) {
        delay(simulatedLatencyMs)
        store.add(title, description)      // may throw IllegalArgumentException; nothing is published then
        tasks.value = store.all()
    }

    override suspend fun setCompleted(id: Long, completed: Boolean) {
        delay(simulatedLatencyMs)
        store.setCompleted(id, completed)
        tasks.value = store.all()
    }

    override suspend fun delete(id: Long) {
        delay(simulatedLatencyMs)
        store.delete(id)
        tasks.value = store.all()
    }
}
