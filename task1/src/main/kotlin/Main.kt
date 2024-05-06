fun main() {
    val stack = BaseStack<Int>()
    for (i in 0..100) {
        stack.push(i)
    }

    println(stack.top()) // 100

    for (i in 0..100) {
        stack.pop()
    }

    println(stack.top()) // null
}