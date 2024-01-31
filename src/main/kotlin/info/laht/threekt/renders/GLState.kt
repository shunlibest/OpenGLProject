package info.laht.threekt.renders


import info.laht.threekt.*
import org.lwjgl.opengl.*
import java.nio.ByteBuffer
import kotlin.math.roundToInt


class GLState internal constructor() {

    var currentProgram: Int? = null

    val maxVertexAttributes = GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS)
    val newAttributes = IntArray(maxVertexAttributes)
    val enabledAttributes = IntArray(maxVertexAttributes)


    init {

    }

    fun useProgram(program: Int): Boolean {
        if (currentProgram != program) {
            GL20.glUseProgram(program);
            currentProgram = program;
            return true;
        }
        return false;
    }

    fun initAttributes() {
        for (i in 0 until newAttributes.size) {
            newAttributes[i] = 0
        }
    }

    fun enableAttribute(attribute: Int) {
        enableAttributeAndDivisor(attribute, 0);
    }

    fun enableAttributeAndDivisor(attribute: Int, meshPerAttribute: Int) {
        newAttributes[attribute] = 1;
//        if (enabledAttributes[attribute] == 0) {
            GL20.glEnableVertexAttribArray(attribute);
//            enabledAttributes[attribute] = 1;
//        }
    }


}
