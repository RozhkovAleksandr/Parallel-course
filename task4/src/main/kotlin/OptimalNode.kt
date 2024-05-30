
import kotlinx.coroutines.sync.Mutex

data class OptimalNode<K : Comparable<K>, V>(
    var key: K, var value: V)
{
    var left: OptimalNode<K, V>? = null
    var right: OptimalNode<K, V>? = null
}

