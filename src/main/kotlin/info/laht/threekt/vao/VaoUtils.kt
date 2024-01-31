package info.laht.threekt.vao

import org.lwjgl.opengl.GL30

object VaoUtils {

    fun createVao(): VAO {
        val vaoId: Int = GL30.glGenVertexArrays()
        return VAO(vaoId)
    }

    fun activateVao(vao: VAO) {
        GL30.glBindVertexArray(vao.vaoId)
    }

}