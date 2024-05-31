package org.example.trees

import kotlinx.coroutines.sync.Mutex

class OptimalNode<T : Comparable<T>>(
    var value: T,
    var left: OptimalNode<T>? = null,
    var right: OptimalNode<T>? = null,
    private val mutex: Mutex = Mutex()
) {
    suspend fun lock() {
        mutex.lock()
    }

    fun unlock() {
        mutex.unlock()
    }
}
