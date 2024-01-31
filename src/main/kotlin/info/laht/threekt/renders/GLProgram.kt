package info.laht.threekt.renders

import info.laht.threekt.shaders.ShaderLib
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.util.regex.Pattern

sealed class _GLProgram {
    abstract val id: Int
}

object GLProgramDefault : _GLProgram() {
    override val id = -1

}

class GLProgram constructor(
    val code: String, shader: ShaderLib.Shader
) : _GLProgram() {

    override val id = programIdCount++


    var usedTimes = 1

    var program: Int = -1

    var glVertexShader = -1
        private set

    var glFragmentShader = -1
        private set


    private lateinit var cachedUniforms: GLUniforms
    private lateinit var cachedAttributes: Map<String, Int>

    init {
        val vertexShader = shader.vertexShader
        val fragmentShader = shader.fragmentShader
        program = GLUtils._initProgram(vertexShader, fragmentShader)
    }

    // 获取所有的uniform变量
    fun getUniforms(): GLUniforms {
        if (!::cachedUniforms.isInitialized) {
            cachedUniforms = GLUniforms(program)
        }
        return cachedUniforms
    }

    // 获取所有的attribute变量
    fun getAttributes(): Map<String, Int> {
        if (!::cachedAttributes.isInitialized) {
            cachedAttributes = GLAttributes.getAttributeLocations(program)
        }
//        println("激活的attribute变量：")
//        cachedAttributes.forEach { (value, index) ->
//            print("Attributes(index=$index , value=$value)")
//        }
        return cachedAttributes
    }

    fun destroy() {
        GL20.glDeleteProgram(program)
        program = -1
    }

    private companion object {
        var programIdCount = 0
    }

}
