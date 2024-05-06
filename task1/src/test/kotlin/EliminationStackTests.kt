package concurrentStack

import EliminationStack
import StackForTest
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Test
fun test2() {
    val stack = EliminationStack<Int>()
    for (i in 1..10) {
        stack.push(i)
    }

    assertEquals(stack.top(), 10)

    for (i in 1..10) {
        stack.pop()
    }
    assertEquals(stack.top(), null)
}

class ConcurrentEliminationStackStressTest {

    private val stack = EliminationStack<Int>()

    @Operation fun push(x: Int) = stack.push(x)

    @Operation fun pop() = stack.pop()

    @Operation fun top() = stack.top()

    @Test
    fun stressTest() =
            StressOptions()
                    .sequentialSpecification(StackForTest::class.java)
                    .check(this::class.java)

    @Test
    fun modelTest() =
            ModelCheckingOptions()
                    .checkObstructionFreedom()
                    .sequentialSpecification(StackForTest::class.java)
                    .check(this::class.java)
}
