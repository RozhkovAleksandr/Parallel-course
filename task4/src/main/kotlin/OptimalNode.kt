package org.example.trees

import java.util.concurrent.locks.ReentrantLock

class TonkayaNode<K : Comparable<K>, V>(var key: K, var value: V) {
    var left: TonkayaNode<K, V>? = null
    var right: TonkayaNode<K, V>? = null
    val lock = ReentrantLock()

    fun lock() {
        lock.lock()
    }

    fun unlock() {
        lock.unlock()
    }
}