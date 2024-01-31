package org.example

import org.lwjgl.Version
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.*

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Hello World!")
        Main.run()
    }


    // The window handle
    private var window: Long = 0

    // step 1: 确定好要画的三角形的顶点
    var vertices = floatArrayOf(
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.0f, 0.5f, 0.0f
    )

    fun run() {
        println("Hello LWJGL " + Version.getVersion() + "!")
        window = GLUtils.initWindow()
        GL.createCapabilities()
        GLFW.glfwMakeContextCurrent(window)
        //
        val program: Int = GLUtils.initProgram()
        val vao = GL30.glGenVertexArrays()
        // 首先绑定顶点数组对象，然后绑定和设置顶点缓冲区，然后配置顶点属性。
        GL30.glBindVertexArray(vao)
        // 顶点缓冲对象
        val vbo = GL15.glGenBuffers()
        // 绑定
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
        // 上传数据
        // glBufferData是一个专门用来把用户定义的数据复制到当前绑定缓冲的函数。
        // 第一个参数是缓冲区的类型，第二个参数是缓冲区的大小，第三个参数是缓冲区的数据，第四个参数是缓冲区的使用模式。
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)

        //使用glVertexAttribPointer函数告诉OpenGL该如何解析顶点数据
        GL20.glVertexAttribPointer(0, vertices.size / 3, GL11.GL_FLOAT, false, 0, 0)
        GL20.glEnableVertexAttribArray(0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL30.glBindVertexArray(0)
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

            // draw our first triangle
            GL20.glUseProgram(program)
            GL30.glBindVertexArray(vao) // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
            // glBindVertexArray(0); // no need to unbind it every time

            // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
            // -------------------------------------------------------------------------------
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
        }
//
//        GLUtils.destroyWindow(window);
    }

}