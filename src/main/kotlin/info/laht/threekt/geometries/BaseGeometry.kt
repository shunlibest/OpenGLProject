package info.laht.threekt.geometries

import info.laht.threekt.renders.BufferAttribute
import info.laht.threekt.renders.BufferAttributes
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseGeometry {
    internal val id = geometryIdCount.getAndAdd(2)

    val attributes = BufferAttributes()

    var needUpdateVertexAttribPointer = true


    // 添加一个Attribute属性值
    fun addAttribute(
        name: String, attribute: BufferAttribute
    ): BaseGeometry {
        attributes[name] = attribute
        return this
    }


    private companion object {
        val geometryIdCount = AtomicInteger(1) // BufferGeometry uses odd numbers as Id
    }
}