package info.laht.threekt.renders



import kotlin.properties.Delegates


sealed class BufferAttribute(
    internal var itemSize: Int,
    internal var normalized: Boolean = false
) : Cloneable {

    var name = ""

    internal var version = 0

    abstract val size: Int

    internal val count: Int
        get() = size / itemSize

    var dynamic: Boolean = false
    internal var updateRange = UpdateRange(0, -1)

    internal var onUploadCallback: (() -> Unit)? = null

    var needsUpdate by Delegates.observable(false) { _, _, newValue ->
        if (newValue) version++
    }

    fun copy(source: BufferAttribute): BufferAttribute {

        this.name = source.name
        this.itemSize = source.itemSize
        this.normalized = source.normalized

        this.dynamic = source.dynamic

        return this

    }

    abstract override fun clone(): BufferAttribute

}

class IntBufferAttribute(
    internal var array: IntArray,
    itemSize: Int,
    normalized: Boolean = false
) : BufferAttribute(itemSize, normalized) {

    override val size: Int
        get() = array.size

    fun getX(index: Int): Int {
        return array[index * itemSize]
    }

    fun setX(index: Int, value: Int): IntBufferAttribute {
        array[index * itemSize] = value
        return this
    }

    fun copy(source: IntBufferAttribute): IntBufferAttribute {
        super.copy(this)
        array = source.array.clone()
        return this
    }

    @Suppress("NAME_SHADOWING")
    fun copyAt(index1: Int, attribute: IntBufferAttribute, index2: Int): IntBufferAttribute {

        val index1 = index1 * this.itemSize;
        val index2 = index2 * attribute.itemSize;

        for (i in 0 until itemSize) {
            array[index1 + i] = attribute.array[index2 + i];
        }

        return this
    }


    override fun clone(): IntBufferAttribute {
        return IntBufferAttribute(array.clone(), itemSize, normalized)
    }

}

class FloatBufferAttribute(
    internal var array: FloatArray,
    itemSize: Int,
    normalized: Boolean = false
) : BufferAttribute(itemSize, normalized) {

    override val size: Int
        get() = array.size

    fun getX(index: Int): Float {
        return array[index * itemSize]
    }

    fun setX(index: Int, value: Float): FloatBufferAttribute {
        array[index * itemSize] = value
        return this
    }

    fun copy(source: FloatBufferAttribute): FloatBufferAttribute {
        super.copy(this)
        array = source.array.clone()
        return this
    }

    @Suppress("NAME_SHADOWING")
    fun copyAt(index1: Int, attribute: FloatBufferAttribute, index2: Int): FloatBufferAttribute {

        val index1 = index1 * this.itemSize;
        val index2 = index2 * attribute.itemSize;

        for (i in 0 until itemSize) {
            array[index1 + i] = attribute.array[index2 + i];
        }

        return this
    }

    override fun clone(): FloatBufferAttribute {
        return FloatBufferAttribute(array.clone(), itemSize, normalized)
    }

}

class BufferAttributes : HashMap<String, BufferAttribute>() {

    val index get() = get("index") as IntBufferAttribute?
    val position get() = get("aPos") as FloatBufferAttribute?
    val normal get() = get("normal") as FloatBufferAttribute?
    val uv get() = get("uv") as IntBufferAttribute?
    val color get() = get("color") as FloatBufferAttribute?
    val tangent get() = get("tangent") as FloatBufferAttribute?

}



class UpdateRange(
    var offset: Int,
    var count: Int
)