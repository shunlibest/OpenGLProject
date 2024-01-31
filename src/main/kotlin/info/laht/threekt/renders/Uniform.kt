package info.laht.threekt.renders


class Uniform(
    var value: Any?,
    private val properties: MutableMap<String, Any?> = mutableMapOf()
) {

    internal var needsUpdate: Boolean? = null

    inline fun <reified T> value(): T? = value as T

    fun getProperty(key: String): Any {
        return properties[key] ?: throw IllegalArgumentException("No such key $key in ${properties.keys}")
    }

    override fun toString(): String {
        return "Uniform(value=$value, properties=$properties)"
    }

}
