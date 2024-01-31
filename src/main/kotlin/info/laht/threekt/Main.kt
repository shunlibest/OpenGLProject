package info.laht.threekt

import info.laht.threekt.cameras.Camera
import info.laht.threekt.geometries.BaseGeometry
import info.laht.threekt.geometries.BoxBufferGeometry
import info.laht.threekt.geometries.BoxBufferGeometry2
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.maths.Color
import info.laht.threekt.objects.Mesh
import info.laht.threekt.objects.Object3D
import info.laht.threekt.renders.GLProgram
import info.laht.threekt.renders.GLRenderer
import info.laht.threekt.renders.GLUtils
import info.laht.threekt.scenes.ColorBackground
import info.laht.threekt.scenes.Scene
import info.laht.threekt.shaders.ShaderLib
import info.laht.threekt.vao.VaoUtils
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays

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
        -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f
    )

    fun run() {


        window = GLUtils.initWindow()
//        GL.createCapabilities()
//        GLFW.glfwMakeContextCurrent(window)


//        val vao = GL30.glGenVertexArrays()
        // 首先绑定顶点数组对象，然后绑定和设置顶点缓冲区，然后配置顶点属性。
//        GL30.glBindVertexArray(vao)
        // 顶点缓冲对象
        // Vertex data
//        val vertices = floatArrayOf(
//            -0.5f, -0.5f, 0.0f,
//            0.5f, -0.5f, 0.0f,
//            0.0f, 0.5f, 0.0f
//        )
        val vaoId = VaoUtils.createVao()
        VaoUtils.activateVao(vaoId)


        // Create a VBO and copy the vertex data
//        val vboId = GL15.glGenBuffers()
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
//        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW)
//        GLUtils.checkError()

//        var geometry: BaseGeometry = BoxBufferGeometry()
//        var material: Material = MeshBasicMaterial()
//
        val object3D = Mesh(BoxBufferGeometry(), MeshBasicMaterial().apply { color = Color.red })

        val object3D2 = Mesh(BoxBufferGeometry2(), MeshBasicMaterial().apply { color = Color.yellow })
        val scene = Scene().apply {
            background = ColorBackground(Color.aliceblue)
        }
        scene.children.add(object3D)
        scene.children.add(object3D2)

        val glRenderer = GLRenderer()
        val camera = Camera()
//
//        glRenderer.render(scene, camera)
        val shaderProgram = glRenderer.currentProgramId

        // Create the shader program

        // Specify the layout of the vertex data
//        val posAttrib = GL20.glGetAttribLocation(shaderProgram, "aPos")
//        GL20.glEnableVertexAttribArray(posAttrib)
//        GLUtils.checkError()
//
//        GL20.glVertexAttribPointer(posAttrib, 3, GL11.GL_FLOAT, false, 0, 0)
//        GLUtils.checkError()

        // Unbind the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)


        GLUtils.checkError()


//        GLProgram.deleteProgram(shaderProgram)


        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)


            // draw our first triangle
//            GL20.glUseProgram(shaderProgram)
//
//            GL30.glBindVertexArray(vaoId) // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
//            //使用glVertexAttribPointer函数告诉OpenGL该如何解析顶点数据
//
////            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
////            GL20.glEnableVertexAttribArray(0)
//            GLUtils.checkError()
////            GL20.glVertexAttribPointer(0, vertices.size / 3, GL11.GL_FLOAT, false, 0, 0)
//            GLUtils.checkError()
//
//
//            glRenderer.drawOperate()

            glRenderer.render(scene, camera)


//            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3)
//             glBindVertexArray(0); // no need to unbind it every time

            // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
            // -------------------------------------------------------------------------------
            GLFW.glfwSwapBuffers(window)
            GLFW.glfwPollEvents()
            GLUtils.checkError()
        }
//
//        GLUtils.destroyWindow(window);
    }

}