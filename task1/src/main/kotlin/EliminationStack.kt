import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import java.util.EmptyStackException
import kotlin.random.Random


class EliminationStack<T> : StackInterface<T> {
    private val Size = 5
    private val Wait = 30
    private val top = atomic<Node<T>?>(null)
    private val array = atomicArrayOfNulls<T>(Size)

    private fun tryPush(x: T): Boolean {
        val temp = top.value
        val head = Node(x, temp)
        return top.compareAndSet(temp, head)
    }

    private fun tryPop(): T? {
        val temp = top.value ?: throw EmptyStackException()
        if (top.compareAndSet(temp, temp.next)) {
            return temp.value
        }
        return null
    }

    override fun top(): T? = top.value?.value

    override fun push(x: T) {
        if (tryPush(x)) {
            return
        }
        val pos = Random.nextInt(Size)
        val randElement = array[pos].value
        if (randElement == null) {
            if (array[pos].compareAndSet(null, x)) {
                repeat(Wait) {}
                if (array[pos].compareAndSet(x, null)) {
                    while (true) {
                        val temp = top.value
                        val head = Node(x, temp)
                        if (top.compareAndSet(temp, head)) {
                            return
                        }
                    }
                }
            } else {
                while (true) {
                    val temp = top.value
                    val head = Node(x, temp)
                    if (top.compareAndSet(temp, head)) {
                        return
                    }
                }
            }
        } else {
            while (true) {
                val temp = top.value
                val head = Node(x, temp)
                if (top.compareAndSet(temp, head)) {
                    return
                }
            }
        }
    }

    override fun pop(): T {
        val value = tryPop()
        if (value != null) {
            return value
        }
        val pos = Random.nextInt(Size)
        for (i in 1..30) {
            val randElement = array[pos].value
            if (randElement != null) {
                if (array[pos].compareAndSet(randElement, null)) {
                    return randElement
                }
            }
        }
        while (true) {
            val temp = top.value ?: throw EmptyStackException()
            if (top.compareAndSet(temp, temp.next)) {
                return temp.value
            }
        }
    }
}