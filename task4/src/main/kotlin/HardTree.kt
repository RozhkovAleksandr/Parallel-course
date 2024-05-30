package org.example.trees

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import HardNode

class HardTree<K : Comparable<K>, V> {
    private var root: HardNode<K, V>? = null
    private val mutex = Mutex()

    suspend fun insert(key: K, value: V) {
        mutex.withLock {
            root = insert(root, key, value)
        }
    }

    private fun insert(HardNode: HardNode<K, V>?, key: K, value: V): HardNode<K, V> {
        if (HardNode == null) {
            return HardNode(key, value)
        }

        if (key < HardNode.key) {
            HardNode.left = insert(HardNode.left, key, value)
        } else if (key > HardNode.key) {
            HardNode.right = insert(HardNode.right, key, value)
        } else {
            HardNode.value = value
        }
        return HardNode
    }

    suspend fun search(key: K): V? {
        mutex.withLock {
            return search(root, key)
        }
    }

    private fun search(HardNode: HardNode<K, V>?, key: K): V? {
        if (HardNode == null) return null
        return when {
            key == HardNode.key -> HardNode.value
            key < HardNode.key -> search(HardNode.left, key)
            else -> search(HardNode.right, key)
        }
    }

    suspend fun delete(key: K) {
        mutex.withLock {
            root = delete(root, key)
        }
    }

    private fun delete(HardNode: HardNode<K, V>?, key: K): HardNode<K, V>? {
        if (HardNode == null) return null

        when { // keyy = 25, Node = 30
            key < HardNode.key -> HardNode.left = delete(HardNode.left, key)
            key > HardNode.key -> HardNode.right = delete(HardNode.right, key)
            else -> {
                if (HardNode.left == null) return HardNode.right
                if (HardNode.right == null) return HardNode.left

                val minRight = findMin(HardNode.right!!)
                HardNode.key = minRight.key
                HardNode.value = minRight.value
                HardNode.right = delete(HardNode.right, HardNode.key)
            }
        }
        return HardNode
    }

    private fun findMin(HardNode: HardNode<K, V>): HardNode<K, V> {
        var current = HardNode
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    fun printInOrder() {
        printInOrder(root)
        println()
    }

    private fun printInOrder(node: HardNode<K, V>?) {
        if (node == null) return

        printInOrder(node.left)
        print("(${node.key}, ${node.value}) ")
        printInOrder(node.right)
    }
}