package info.laht.threekt.renders


import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import java.util.*

class GLAttributes internal constructor() {

    private val buffers = WeakHashMap<BufferAttribute, Buffer>()

    private fun createBuffer(attribute: BufferAttribute, bufferType: Int): Buffer {

        val usage = if (attribute.dynamic) GL15.GL_DYNAMIC_DRAW else GL15.GL_STATIC_DRAW

        val buffer = GL15.glGenBuffers()
        GL15.glBindBuffer(bufferType, buffer)

        val (type, bytesPerElement) = when (attribute) {
            is IntBufferAttribute -> {
                GL15.glBufferData(bufferType, attribute.array, usage)
                GL11.GL_UNSIGNED_INT to 3
            }
            is FloatBufferAttribute -> {
                GL15.glBufferData(bufferType, attribute.array, usage)
                GL11.GL_FLOAT to 4
            }
        }

        return Buffer(buffer, type, bytesPerElement, attribute.version)

    }

    private fun updateBuffer(buffer: Int, attribute: BufferAttribute, bufferType: Int, bytesPerElement: Int) {

        val updateRange = attribute.updateRange;

        if (updateRange.count == 0) {
            println("GLObjects.updateBuffer: dynamic BufferAttribute marked as needsUpdate but updateRange.count is 0, ensure you are using set methods or updating manually.")
            return
        }

        GL15.glBindBuffer(bufferType, buffer)

        when (attribute) {
            is IntBufferAttribute -> {
                if (!attribute.dynamic) GL15.glBufferData(bufferType, attribute.array, GL15.GL_STATIC_DRAW)
                else if (updateRange.count == -1) GL15.glBufferSubData(bufferType, 0, attribute.array)
                else if (updateRange.count == 0) println("GLObjects.updateBuffer: dynamic THREE.BufferAttribute marked as needsUpdate but updateRange.count is 0, ensure you are using set methods or updating manually.")
                else {
                    val sub = attribute.array.copyOfRange(updateRange.offset, updateRange.offset + updateRange.count)
                    GL15.glBufferSubData(bufferType, (updateRange.offset + bytesPerElement).toLong(), sub)
                    updateRange.count = -1
                }
            }
            is FloatBufferAttribute -> {
                if (!attribute.dynamic) GL15.glBufferData(bufferType, attribute.array, GL15.GL_STATIC_DRAW)
                else if (updateRange.count == -1) GL15.glBufferSubData(bufferType, 0, attribute.array)
                else if (updateRange.count == 0) println("GLObjects.updateBuffer: dynamic THREE.BufferAttribute marked as needsUpdate but updateRange.count is 0, ensure you are using set methods or updating manually.")
                else {
                    val sub = attribute.array.copyOfRange(updateRange.offset, updateRange.offset + updateRange.count)
                    GL15.glBufferSubData(bufferType, (updateRange.offset + bytesPerElement).toLong(), sub)
                    updateRange.count = -1
                }
            }
        }

    }

    fun get(attribute: BufferAttribute): Buffer {
        return buffers[attribute] ?: throw IllegalStateException("")
    }

    fun remove(attribute: BufferAttribute) {
        buffers.remove(attribute)?.also {
            GL15.glDeleteBuffers(it.buffer)
        }
    }

    fun update(attribute: BufferAttribute, bufferType: Int) {
        val data = buffers[attribute]
        if (data == null) {
            buffers[attribute] = createBuffer(attribute, bufferType)
        } else if (data.version < attribute.version) {
            updateBuffer(data.buffer, attribute, bufferType, data.bytesPerElement)
            data.version = attribute.version
        }
    }

    companion object {
        fun getAttributeLocations(program: Int): Map<String, Int> {
            val attributes = mutableMapOf<String, Int>()
            val n = GL20.glGetProgrami(program, GL20.GL_ACTIVE_ATTRIBUTES)
            for (i in 0 until n) {
                val sizeBuffer = BufferUtils.createIntBuffer(1)
                val typeBuffer = BufferUtils.createIntBuffer(1)
                val name = GL20.glGetActiveAttrib(program, i, sizeBuffer, typeBuffer)
                attributes[name] = GL20.glGetAttribLocation(program, name)
            }
            return attributes
        }
    }




    class Buffer(
        val buffer: Int,
        val type: Int,
        val bytesPerElement: Int,
        var version: Int
    )

}