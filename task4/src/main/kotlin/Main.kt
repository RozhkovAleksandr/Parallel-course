package org.example
import kotlinx.coroutines.runBlocking

import org.example.trees.HardTree
import org.example.trees.TonkayaTree
import org.example.trees.TonkayaNode
import org.example.trees.OptimalTree
import org.example.trees.OptimalNode

fun main() = runBlocking {
    val hard = OptimalTree<Int>()

    hard.insert(20)
    // hard.insert(21, "Value evdscve0")
    // hard.insert(10, "Value 10")
    // hard.insert(30, "Value 30")
    hard.insert(5)
    hard.insert(15)
    hard.delete(5)
    // hard.insert(25, "Value 25")
    // hard.insert(40, "Value 40")
    // hard.insert(35, "Value 35")
    // hard.insert(37, "Value 37")

    // println("Before Delete:")
    // hard.printInOrder()
    // hard.delete(20)

    // println(hard.search(20))

    // println("After Delete:")
    // hard.printInOrder()
}