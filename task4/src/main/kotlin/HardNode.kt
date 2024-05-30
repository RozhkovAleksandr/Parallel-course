
import kotlinx.coroutines.sync.Mutex

data class HardNode<K : Comparable<K>, V>(
    var key: K,
    var value: V
)
{
    var left: HardNode<K, V>? = null
    var right: HardNode<K, V>? = null
}

