import kotlinx.atomicfu.atomic
import java.util.EmptyStackException

class BaseStack<T> : StackInterface<T> {
    private val top = atomic<Node<T>?>(null)

    override fun push(x: T) {
        while (true) {
            val temp = top.value
            val head = Node(x, temp)
            if (top.compareAndSet(temp, head)) {
                return
            }
        }
    }

    override fun pop(): T {
        while (true) {
            val temp = top.value ?: throw EmptyStackException()
            if (top.compareAndSet(temp, temp.next)) {
                return temp.value
            }
        }
    }

    override fun top(): T? = top.value?.value
}