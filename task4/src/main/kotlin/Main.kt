package org.example
import kotlinx.coroutines.runBlocking

import org.example.trees.HardTree

fun main() = runBlocking {
    val hard = HardTree<Int, String>()

    // Insert elements
    hard.insert(20, "Value 20")
    hard.insert(10, "Value 10")
    hard.insert(30, "Value 30")
    hard.insert(5, "Value 5")
    hard.insert(15, "Value 15")
    hard.insert(25, "Value 25")
    hard.insert(40, "Value 40")
    hard.insert(35, "Value 35")
    hard.insert(37, "Value 37")

    println("Before Delete:")
    hard.printInOrder()

    hard.delete(20)

    println("After Delete:")
    hard.printInOrder()
}