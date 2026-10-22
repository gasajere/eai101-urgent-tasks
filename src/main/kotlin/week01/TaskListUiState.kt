package week01

// Day 3 — sealed UI state and a render() that stands in for Compose.
//
// Error carries the last tasks that were on screen so a failed operation does not blank the list.

sealed interface TaskListUiState {
    data object Loading : TaskListUiState
    data class Success(val tasks: List<Task>) : TaskListUiState
    data class Error(val message: String, val tasks: List<Task> = emptyList()) : TaskListUiState
}

/** Tasks currently visible in any state (none while loading). */
val TaskListUiState.visibleTasks: List<Task>
    get() = when (this) {
        TaskListUiState.Loading -> emptyList()
        is TaskListUiState.Success -> tasks
        is TaskListUiState.Error -> tasks
    }

fun render(state: TaskListUiState): String = when (state) {
    TaskListUiState.Loading -> "Loading..."
    is TaskListUiState.Success ->
        if (state.tasks.isEmpty()) "No tasks yet"
        else state.tasks.joinToString("\n") { describeTask(it.title, it.isCompleted) }
    is TaskListUiState.Error ->
        "Error: ${state.message}" +
            if (state.tasks.isEmpty()) "" else "\n" + state.tasks.joinToString("\n") { describeTask(it.title, it.isCompleted) }
}
