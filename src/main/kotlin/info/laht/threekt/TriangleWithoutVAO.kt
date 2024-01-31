package info.laht.threekt

import info.laht.threekt.renders.GLProgram
import info.laht.threekt.renders.GLUtils
import info.laht.threekt.shaders.ShaderLib
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.system.MemoryUtil

class TriangleWithoutVAO {
    private var window: Long = 0
    private var vboId = 0
    private var shaderProgram = 0
    fun run() {
        init()
        loop()

        // Cleanup
        cleanup()
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }

    private fun init() {
//        GLFWErrorCallback.createPrint(System.err).set()
//        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }
//
//        // Configure GLFW for Compatibility Profile
//        GLFW.glfwDefaultWindowHints()
//        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
//        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2)
//        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE)

        // Create the window
        window = GLUtils.initWindow()
        if (window == MemoryUtil.NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }


        // Vertex data
        val vertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
        )

        // Create a FloatBuffer of vertices
        val vertexBuffer = BufferUtils.createFloatBuffer(vertices.size)
        vertexBuffer.put(vertices).flip()

        // Create a VBO and copy the vertex data
        vboId = GL15.glGenBuffers()
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
        GLUtils.checkError()


        // Create the shader program
        shaderProgram = GLProgram("cacheCode", ShaderLib.cube).program
        // Specify the layout of the vertex data
        val posAttrib = GL20.glGetAttribLocation(shaderProgram, "aPos")
        GL20.glEnableVertexAttribArray(posAttrib)
        GL20.glVertexAttribPointer(posAttrib, 3, GL11.GL_FLOAT, false, 0, 0)

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    private fun loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

            // Use the shader program
            GL20.glUseProgram(shaderProgram)

            // Bind the VBO
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
            GL20.glEnableVertexAttribArray(0) // Position attribute
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0)

            // Draw the triangle
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)

            // Unbind the VBO
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
            GL20.glDisableVertexAttribArray(0)

            // Unbind the shader program
            GL20.glUseProgram(0)
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
        }
    }

    private fun cleanup() {
        GL15.glDeleteBuffers(vboId)
        GL20.glDeleteProgram(shaderProgram)
        GLFW.glfwDestroyWindow(window)
    }

    private fun checkShaderError(shader: Int, type: String) {
        val success = BufferUtils.createIntBuffer(1)
        GL20.glGetShaderiv(shader, GL20.GL_COMPILE_STATUS, success)
        if (success[0] != GL11.GL_TRUE) {
            System.err.println(type + " compilation failed: " + GL20.glGetShaderInfoLog(shader))
        }
    }

    private fun checkProgramError(program: Int, type: String) {
        val success = BufferUtils.createIntBuffer(1)
        GL20.glGetProgramiv(program, GL20.GL_LINK_STATUS, success)
        if (success[0] != GL11.GL_TRUE) {
            System.err.println(type + " link failed: " + GL20.glGetProgramInfoLog(program))
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TriangleWithoutVAO().run()
        }
    }
}