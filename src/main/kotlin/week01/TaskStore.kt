package week01

// Day 3 — interface + in-memory implementation. Synchronous; no coroutines yet.

interface TaskStore {
    fun all(): List<Task>
    fun add(title: String, description: String?): Task
    fun setCompleted(id: Long, completed: Boolean): Boolean
    fun delete(id: Long): Boolean
}

class InMemoryTaskStore : TaskStore {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1L

    override fun all(): List<Task> = tasks.toList()

    override fun add(title: String, description: String?): Task {
        val clean = title.trim()
        require(clean.isNotEmpty()) { "Title is required" }
        val task = Task(nextId++, clean, description?.trim()?.takeIf { it.isNotEmpty() })
        tasks += task
        return task
    }

    override fun setCompleted(id: Long, completed: Boolean): Boolean {
        val i = tasks.indexOfFirst { it.id == id }
        if (i == -1) return false
        tasks[i] = tasks[i].copy(isCompleted = completed)
        return true
    }

    override fun delete(id: Long): Boolean = tasks.removeIf { it.id == id }
}
