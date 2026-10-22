package week01

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Day 4 — stand-in for a ViewModel.
//
// Lifetime: the holder OWNS `scope`. Everything it launches lives in that scope, and `close()`
// cancels the scope — the same contract as ViewModel.onCleared() cancelling viewModelScope.

class TaskStateHolder(
    private val repository: TaskRepository,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val state: StateFlow<TaskListUiState> = _state.asStateFlow()

    /** Kept so the lesson can show that it is cancelled by close(). */
    val collectorJob: Job = scope.launch {
        repository.observeAll()
            .catch { e -> _state.update { TaskListUiState.Error(e.message ?: "Unknown error", it.visibleTasks) } }
            .collect { tasks -> _state.value = TaskListUiState.Success(tasks) }
    }

    fun onAdd(title: String, description: String?) = runChange { repository.add(title, description) }
    fun onToggle(id: Long, completed: Boolean) = runChange { repository.setCompleted(id, completed) }
    fun onDelete(id: Long) = runChange { repository.delete(id) }

    private fun runChange(block: suspend () -> Unit) {
        scope.launch {
            try {
                block()
            } catch (e: CancellationException) {
                throw e                                   // never swallow cancellation
            } catch (e: IllegalArgumentException) {
                // keep whatever was on screen; only add the message
                _state.update { TaskListUiState.Error(e.message ?: "Invalid input", it.visibleTasks) }
            }
        }
    }

    /** Explicit shutdown. After this, no coroutine owned by the holder is running. */
    fun close() = scope.cancel()
}
