package week01

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Day 4 — coroutines and Flow. Complete runnable example.
// Run: .\gradlew.bat day4
//
// Part A is the guided demonstration (suspend / launch / cancel / async).
// Part B is the essential exercise (state holder + StateFlow with an explicit lifetime).

suspend fun slowFetch(name: String, ms: Long): String {
    delay(ms)
    return "$name ready after ${ms}ms"
}

fun main() = runBlocking {
    println("== Part A: guided demonstration ==")

    // launch returns a Job; cancelling it stops the loop.
    val ticker = launch {
        var n = 0
        while (true) { delay(100); println("tick ${++n}") }
    }
    delay(350)
    ticker.cancel()
    ticker.join()
    println("ticker cancelled after 3 ticks")

    // async runs two slow calls concurrently; total time is ~ the slower one, not the sum.
    val started = System.currentTimeMillis()
    val a = async { slowFetch("A", 300) }
    val b = async { slowFetch("B", 200) }
    println(a.await()); println(b.await())
    println("both done in ~${System.currentTimeMillis() - started}ms (sequential would be ~500ms)")

    println()
    println("== Part B: essential exercise - state holder with explicit lifetime ==")

    // The holder gets its OWN scope. SupervisorJob: one failed child must not kill the collector.
    val holderScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val holder = TaskStateHolder(InMemoryTaskRepository(InMemoryTaskStore(), simulatedLatencyMs = 100), holderScope)

    // A printer that logs every state it observes. StateFlow conflates: a fast Loading -> Success
    // transition may already be over before this collector starts, so we do not assert on it.
    val printer = launch { holder.state.collect { println("PRINTER: " + render(it).replace("\n", " | ")) } }

    suspend fun settle() = delay(250)   // longer than the simulated latency

    settle()
    println("after start   -> ${holder.state.value::class.simpleName} ${holder.state.value.visibleTasks.map { it.id }}")

    holder.onAdd("Learn Flow", null); settle()
    println("after add     -> ${holder.state.value::class.simpleName} ${holder.state.value.visibleTasks.map { it.id }}")

    holder.onAdd("   ", null); settle()
    println("after bad add -> ${holder.state.value::class.simpleName} ${holder.state.value.visibleTasks.map { it.id }}  (task 1 still visible)")

    holder.onToggle(1, true); settle()
    println("after toggle  -> ${holder.state.value::class.simpleName} completed=${holder.state.value.visibleTasks.first().isCompleted}")

    holder.onDelete(1); settle()
    println("after delete  -> ${holder.state.value::class.simpleName} ${holder.state.value.visibleTasks.map { it.id }}")

    // Shut down in order: our printer first, then the holder and everything it owns.
    printer.cancel()
    holder.close()
    println("collectorJob.isActive after close() = ${holder.collectorJob.isActive}")   // false
    println("holderScope active = ${holderScope.coroutineContext[kotlinx.coroutines.Job]?.isActive}") // false
}
