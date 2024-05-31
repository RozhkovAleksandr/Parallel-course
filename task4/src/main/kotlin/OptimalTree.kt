package org.example.trees

import java.util.concurrent.locks.ReentrantLock
import org.example.trees.TonkayaNode


class TonkayaTree < K: Comparable < K > , V > {
    private
    var root: TonkayaNode < K, V > ? = null
    var treeLock = ReentrantLock()

    /** Вспомогательный поиск */
    private fun findWithParent(key: K): Pair < TonkayaNode < K, V > ? , TonkayaNode < K, V > ? > {
        treeLock.lock()
        var parent: TonkayaNode < K, V > ? = null
        var current = root
        var grandParent: TonkayaNode < K, V > ? = null

        try {
            while (current != null) {
                current.lock()
                if (grandParent != null) {
                    grandParent.unlock()
                } else {
                    if (treeLock.isHeldByCurrentThread()) {
                        treeLock.unlock()
                    }
                }

                val cmp = key.compareTo(current.key)
                if (cmp == 0) {
                    return Pair(current, parent)
                }

                grandParent = parent
                parent = current
                current =
                    if (cmp < 0) current.left
                else current.right
            }
        } finally {
            if (current == null) {
                parent?.let {
                    if (it.lock.isHeldByCurrentThread()) {
                        it.unlock()
                    }
                }
                if (grandParent != null) {
                    grandParent.unlock()
                } else {
                    if (treeLock.isHeldByCurrentThread()) {
                        treeLock.unlock()
                    }
                }
            }
        }

        return Pair(null, parent)
    }


    /** Поиск значения по ключу */
    fun search(key: K) : V ? {
        val(node, parent) = findWithParent(key)
        try {
            return node?.value
        } finally {
            node?.unlock()
            parent?.let {
                if (it.lock.isHeldByCurrentThread()) {
                    it.unlock()
                }
            }
            if (parent == null) {
                if (treeLock.isHeldByCurrentThread()) {
                    treeLock.unlock()
                }
            }
        }
    }

    fun insert(key: K, value: V) {
        val(node, parent) = findWithParent(key)

        if (node != null) {
            try {
                node.value = value
            } finally {
                node.unlock()
                parent?.let {
                    if (it.lock.isHeldByCurrentThread()) {
                        it.unlock()
                    }
                }
            }
            return
        }
        val newNode = TonkayaNode(key, value)
        try {
            if (parent == null) {
                if (root == null) {
                    root = newNode
                } else {
                    if (treeLock.isHeldByCurrentThread()) {
                        treeLock.unlock()
                    }
                    insert(key, value)
                }
            } else {
                if (key < parent.key) {
                    parent.left = newNode
                } else {
                    parent.right = newNode
                }
            }
        } finally {

            parent?.let {
                if (it.lock.isHeldByCurrentThread()) {
                    it.unlock()
                }
            }
            if (parent == null) {
                if (treeLock.isHeldByCurrentThread()) {
                    treeLock.unlock()
                }
            }
        }
    }

    fun delete(key: K) {
        val(node, parent) = findWithParent(key)
        if (node == null) {
            parent?.let {
                if (it.lock.isHeldByCurrentThread()) {
                    it.unlock()
                }
            }
            if (parent == null) {
                if (treeLock.isHeldByCurrentThread()) {
                    treeLock.unlock()
                }
            }
            return
        }

        try {
            if (node.left != null && node.right != null) {
                var minParent = node
                var min = node.right

                while (min!!.left != null) {
                    minParent.lock()
                    if (min != node.right) {
                        minParent.unlock()
                    }
                    min = min.left
                }

                node.key = min.key
                node.value = min.value

                if (minParent === node) {
                    node.right = min.right
                } else {
                    minParent.left = min.right
                }

                min.right?.lock()
                min.unlock()
                min.right?.unlock()
                minParent.unlock()
            } else {
                val child = node.left ?: node.right

                if (parent == null) {
                    root = child
                } else {
                    if (parent.left === node) {
                        parent.left = child
                    } else {
                        parent.right = child
                    }
                }
            }
        } finally {
            node.unlock()
            parent?.let {
                if (it.lock.isHeldByCurrentThread()) {
                    it.unlock()
                }
            }
            if (parent == null) {
                if (treeLock.isHeldByCurrentThread()) {
                    treeLock.unlock()
                }
            }
        }
    }
}