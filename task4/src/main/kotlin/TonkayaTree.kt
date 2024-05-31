package org.example.trees

import kotlinx.coroutines.sync.Mutex
import org.example.trees.OptimalNode

class OptimalTree<T : Comparable<T>> {
    private var root: OptimalNode<T>? = null
    private val mutex: Mutex = Mutex()

    suspend fun lock() {
        mutex.lock()
    }

    fun unlock() {
        mutex.unlock()
    }

    suspend fun find_helper(value: T): Pair<OptimalNode<T>?, OptimalNode<T>?> {
    while (true) {
        lock()
        if (root == null) {
            return null to null
        }
        var current = root
        var parent: OptimalNode<T>? = null
        while (current != null && current.value != value) {
            val grandparent = parent
            parent = current
            if (value < current.value) {
                current = current.left
            } else {
                current = current.right
            }
            if (grandparent == null) {
                unlock()
            }
        }
        parent?.lock()
        current?.lock()
        var curr = root
        var prev: OptimalNode<T>? = null
        while (curr != null && curr.value != value && curr != current) {
            prev = curr
            if (value < curr.value) {
                curr = curr.left
            } else {
                curr = curr.right
            }
        }
        if (curr == current && prev == parent) {
            return current to parent
        }
        current?.unlock()
        parent?.unlock()
    }
}

    suspend fun search(value: T): Boolean {
        var node: OptimalNode<T>? 
        var parent: OptimalNode<T>? 
        find_helper(value).let { node = it.first; parent = it.second }
        if (node == null) {
            return false
        }
        val tmp = node
        tmp?.unlock()
        parent?.unlock()

        return true
    }

    suspend fun insert(value: T) {
        var node: OptimalNode<T>?
        var parent: OptimalNode<T>?
        find_helper(value).let { node = it.first; parent = it.second }
        if (root == null) {
            root = OptimalNode(value)
            unlock()
            return
        }
        if (node == null) {
            if (value < parent!!.value) {
                parent?.left = OptimalNode(value)
            } else {
                parent?.right = OptimalNode(value)
            }
        }
        parent?.unlock()
    }

    suspend fun delete(value: T) {
        val (node, parent) = find_helper(value)
        parent?.unlock()
        if (node == null) {
            return
        }

        if (node.left == null && node.right == null) {
            if (node == root) {
                root = null
            } else if (value < parent!!.value) {
                parent.left = null
            } else {
                parent.right = null
            }
            return
        }

        if (node.left == null) {
            if (node == root) {
                root = node.right
            } else if (value < parent!!.value) {
                parent.left = node.right
            } else {
                parent.right = node.right
            }
            return
        }

        if (node.right == null) {
            if (node == root) {
                root = node.left
            } else if (value < parent!!.value) {
                parent.left = node.left
            } else {
                parent.right = node.left
            }
            return
        }
        node.unlock()
        node.right!!.lock()
        var succParent = node
        var succ = node.right
        while (succ!!.left!= null) {
            val succGrandparent = succParent
            succParent = succ
            succ.left!!.lock()
            succ = succ.left
            if (succGrandparent!= null && succGrandparent!= node) {
                succGrandparent.unlock()
            }
        }
        if (succParent != node) {
            succParent?.unlock()
            succParent?.left = succ.right
        } else {
            succParent.right = succ.right
        }
        node.value = succ.value
    }
}