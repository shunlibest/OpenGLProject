package info.laht.threekt.renders

import info.laht.threekt.maths.Color
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20

internal interface Container {
    val seq: MutableList<UniformObject>
    val map: MutableMap<String, UniformObject>
}

class GLUniforms internal constructor(
    program: Int
) : Container {

    override val seq = mutableListOf<UniformObject>()
    override val map = mutableMapOf<String, UniformObject>()

    init {
        // 获取GPU Program中激活的Uniform个数
        val n = GL20.glGetProgrami(program, GL20.GL_ACTIVE_UNIFORMS)
        // 遍历每一个激活的Uniform, 获取其信息, 并记录下来, 用于之后的设置值
        for (i in 0 until n) {
            val info = ActiveUniformInfo(program, i)
            val addr = GL20.glGetUniformLocation(program, info.name)
            val uniformObject = parseUniform(info, addr)
            seq.add(uniformObject)
            map[uniformObject.id] = uniformObject
        }
        println("激活的Uniform:${seq.size}")
        seq.forEach {
            println(it.toString())
        }
    }


    private fun parseUniform(activeInfo: ActiveUniformInfo, addr: Int): UniformObject {
        val name = activeInfo.name
        val pathLength = name.length
        // 只处理了单个Uniform变量的情况, 如:uniform mat4 u_MVPMatrix;
        // 不支持数组Uniform的情况, 如: uniform mat4 u_BoneTransforms[MAX_BONES];
        return SingleUniform(name, activeInfo, addr)
    }

    fun setValue(name: String, value: Any) {
        val u = map[name]
        u?.setValue(value)
    }

    companion object {


    }

}

private class ActiveUniformInfo(
    program: Int, index: Int
) {

    val name: String
    val size: Int
    val type: Int

    init {
        val sizeBuffer = BufferUtils.createIntBuffer(1)
        val typeBuffer = BufferUtils.createIntBuffer(1)
        name = GL20.glGetActiveUniform(program, index, sizeBuffer, typeBuffer)
        size = sizeBuffer.get()
        type = typeBuffer.get()
    }

    override fun toString(): String {
        return "ActiveUniformInfo(name='$name', size=$size, type=$type)"
    }

}

sealed class UniformObject(
    val id: String
) {
    abstract fun setValue(v: Any)
}

private class SingleUniform(
    id: String, activeInfo: ActiveUniformInfo, private val addr: Int
) : UniformObject(id) {

    // 根据Uniform类型, 选择合适的设置方法
    private val setValue = getSingularSetter(activeInfo.type)

    private val type = getUniformTypeString(activeInfo.type)

    override fun setValue(v: Any) {
        setValue.invoke(v)
    }

    private fun getSingularSetter(type: Int): (Any) -> Unit {
        return when (type) {
            // float 类型
            GL11.GL_FLOAT -> { v -> setValueV1f(v) }
            GL20.GL_FLOAT_VEC2 -> { v -> setValueV2f(v) }
            GL20.GL_FLOAT_VEC3 -> { v -> setValueV3f(v) }
            GL20.GL_FLOAT_VEC4 -> { v -> setValueV4f(v) }

            // INT和BOOL类型
            GL11.GL_INT, GL20.GL_BOOL -> { v -> setValueV1i(v) }

            // Vector类型
            GL20.GL_INT_VEC2, GL20.GL_BOOL_VEC2 -> { v -> setValueV2i(v) } // _VEC2
            GL20.GL_INT_VEC3, GL20.GL_BOOL_VEC3 -> { v -> setValueV3i(v) } // _VEC3
            GL20.GL_INT_VEC4, GL20.GL_BOOL_VEC4 -> { v -> setValueV4i(v) } // _VEC4


            // mat 类型
            GL20.GL_FLOAT_MAT2 -> throw UnsupportedOperationException() // _MAT2
            GL20.GL_FLOAT_MAT3 -> { v -> setValueM3(v) } // _MAT3
            GL20.GL_FLOAT_MAT4 -> { v -> setValueM4(v) } // _MAT4

            //             0x8b5e,0x8d66 -> setValueT1; // SAMPLER_2D, SAMPLER_EXTERNAL_OES
            //             0x8b5f, return setValueT3D1; // SAMPLER_3D
            //             0x8b60, return setValueT6; // SAMPLER_CUBE
            //             0x8DC1, return setValueT2DArray1; // SAMPLER_2D_ARRAY
            else -> throw UnsupportedOperationException()
        }
    }

    private fun getUniformTypeString(type: Int): String {
        return when (type) {
            GL11.GL_FLOAT -> "GL11.GL_FLOAT"
            GL20.GL_FLOAT_VEC2 -> "GL11.GL_FLOAT"
            GL20.GL_FLOAT_VEC3 -> "GL20.GL_FLOAT_VEC2"
            GL20.GL_FLOAT_VEC4 -> "GL20.GL_FLOAT_VEC3"
            GL11.GL_INT, GL20.GL_BOOL -> "GL11.GL_INT"
            GL20.GL_INT_VEC2, GL20.GL_BOOL_VEC2 -> "GL20.GL_INT_VEC2"
            GL20.GL_INT_VEC3, GL20.GL_BOOL_VEC3 -> "GL20.GL_INT_VEC3"
            GL20.GL_INT_VEC4, GL20.GL_BOOL_VEC4 -> "GL20.GL_INT_VEC4"

            // mat 类型
            GL20.GL_FLOAT_MAT2 -> "GL20.GL_FLOAT_MAT2"
            GL20.GL_FLOAT_MAT3 -> "GL20.GL_FLOAT_MAT3"
            GL20.GL_FLOAT_MAT4 -> "GL20.GL_FLOAT_MAT4"
            else -> throw UnsupportedOperationException()
        }
    }

    private fun setValueV1i(v: Any) {
        when (v) {
            is Int -> GL20.glUniform1i(addr, v)
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV2i(v: Any) {
        when (v) {
            is IntArray -> GL20.glUniform2iv(addr, v)
//            is Vector2 -> GL20.glUniform2i(addr, v.x.roundToInt(), v.y.roundToInt())
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV3i(v: Any) {
        when (v) {
            is IntArray -> GL20.glUniform3iv(addr, v)
//            is Vector3 -> GL20.glUniform3i(addr, v.x.roundToInt(), v.y.roundToInt(), v.z.roundToInt())
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV4i(v: Any) {
        when (v) {
            is IntArray -> GL20.glUniform4iv(addr, v)
//            is Vector4 -> GL20.glUniform4i(addr, v.x.roundToInt(), v.y.roundToInt(), v.z.roundToInt(), v.w.roundToInt())
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV1f(v: Any) {
        when (v) {
            is Float -> GL20.glUniform1f(addr, v)
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV2f(v: Any) {
        when (v) {
            is FloatArray -> GL20.glUniform2fv(addr, v)
//            is Vector2 -> GL20.glUniform2f(addr, v.x, v.y)
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV3f(v: Any) {
        when (v) {
            is FloatArray -> GL20.glUniform3fv(addr, v)
//            is Vector3 -> GL20.glUniform3f(addr, v.x, v.y, v.z)
            is Color -> GL20.glUniform3f(addr, v.r, v.g, v.b)
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueV4f(v: Any) {
        when (v) {
            is FloatArray -> GL20.glUniform4fv(addr, v)
//            is Vector4 -> GL20.glUniform4f(addr, v.x, v.y, v.z, v.w)
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueM3(v: Any) {
        when (v) {
//            is Matrix3 -> GL20.glUniformMatrix3fv(addr, false, v.toArray(mat3array))
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    private fun setValueM4(v: Any) {
        when (v) {
//            is Matrix4 -> GL20.glUniformMatrix4fv(addr, false, v.toArray(mat4array))
            else -> throw IllegalArgumentException("Illegal type encountered: $v")
        }
    }

    override fun toString(): String {
        return "SingleUniform(id=$id, addr=$addr, type=$type)"
    }
}