interface StackInterface<T> {
    fun push(x: T) 
    fun pop() : T
    fun top() : T?
}