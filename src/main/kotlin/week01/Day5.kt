package week01

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// Day 5 — architecture-in-miniature checkpoint. Complete runnable example.
// Run: .\gradlew.bat day5        Tests: .\gradlew.bat test
//
// Layers, bottom to top, each in its own file:
//   Task.kt             domain model
//   TaskStore.kt        synchronous storage (data)
//   TaskRepository.kt   data layer as Flow + suspend
//   TaskStateHolder.kt  ViewModel stand-in
//   TaskListUiState.kt  UiState + render() (Compose stand-in)
//
// The checkpoint scenario below is the one the tests in src/test assert on.

fun main() = runBlocking {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val holder = TaskStateHolder(InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 50), scope)
    suspend fun step(label: String) {
        delay(150)
        println("[$label]"); println(render(holder.state.value)); println()
    }

    step("start")
    holder.onAdd("Write the README", null);          step("add #1")
    holder.onAdd("Run on a device", "USB debugging"); step("add #2")
    holder.onToggle(1, true);                         step("toggle #1")
    holder.onAdd("", null);                           step("blank add rejected")
    holder.onDelete(2);                               step("delete #2")
    holder.onDelete(42);                              step("delete unknown id (no change)")

    holder.close()
}
