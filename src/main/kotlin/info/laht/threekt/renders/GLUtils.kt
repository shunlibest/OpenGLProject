package info.laht.threekt.renders

import info.laht.threekt.utils.FileUtils
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.io.File
import java.nio.ByteBuffer

object GLUtils {
    fun initWindow(): Long {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set()

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        check(GLFW.glfwInit()) { "Unable to initialize GLFW" }

        // 配置GLFW
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE) // 窗口默认不可见
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE) // 窗口可调整大小
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
        //        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
//
//
//        // Configure GLFW
//        glfwDefaultWindowHints(); // optional, the current window hints are already the default
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
//        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        val window = GLFW.glfwCreateWindow(300, 300, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL)
        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        GLFW.glfwSetKeyCallback(window) { _window: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) GLFW.glfwSetWindowShouldClose(
                _window,
                true
            ) // We will detect this in the rendering loop
        }
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1) // int*
            val pHeight = stack.mallocInt(1) // int*

            // Get the window size passed to glfwCreateWindow
            GLFW.glfwGetWindowSize(window, pWidth, pHeight)

            // Get the resolution of the primary monitor
            val vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())

            // Center the window
            GLFW.glfwSetWindowPos(
                window,
                (vidmode!!.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window)
        // Enable v-sync
        GLFW.glfwSwapInterval(1)

        // Make the window visible
        GLFW.glfwShowWindow(window)

        // 不加会崩溃
        GL.createCapabilities()
        GLFW.glfwMakeContextCurrent(window)
        return window
    }

    fun destroyWindow(window: Long) {
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(window)
        GLFW.glfwDestroyWindow(window)

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate()
        GLFW.glfwSetErrorCallback(null)!!.free()
    }

    fun initVertexShader(vertexShaderSource: String?): Int {
        // 创建顶点着色器
        val vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        // 设置着色器源码
        GL20.glShaderSource(vertexShader, vertexShaderSource)
        // 编译着色器
        GL20.glCompileShader(vertexShader)
        checkError()
        return vertexShader
    }

    fun initFragmentShader(fragmentShaderSource: String?): Int {
        // 创建片元着色器
        val fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        // 设置着色器源码
        GL20.glShaderSource(fragmentShader, fragmentShaderSource)
        // 编译着色器
        GL20.glCompileShader(fragmentShader)
        checkError()
        return fragmentShader
    }

    @JvmOverloads
    fun initProgram(
        vertexShaderFileName: String? = "shader/base.vert",
        fragmentShaderFileName: String? = "shader/base.frag"
    ): Int {
        val vertexShaderSource = FileUtils.readResourceFile(vertexShaderFileName)
        val fragmentShaderSource = FileUtils.readResourceFile(fragmentShaderFileName)
        return _initProgram(vertexShaderSource, fragmentShaderSource)
    }

     fun _initProgram(vertexShaderSource: String, fragmentShaderSource: String): Int {
        // 创建一个程序
        val program = GL20.glCreateProgram()
        // 附加顶点着色器
        GL20.glAttachShader(program, initVertexShader(vertexShaderSource))
        // 附加片元着色器
        GL20.glAttachShader(program, initFragmentShader(fragmentShaderSource))
        // 链接程序
        GL20.glLinkProgram(program)
        checkError()
        return program
    }

    fun loadTexture(fileName: String?): ImageData {
        val resource = ClassLoader.getSystemResource(fileName)
        val filePath = File(resource.path).absolutePath

        // Prepare buffers to receive texture data
        val width = BufferUtils.createIntBuffer(1)
        val height = BufferUtils.createIntBuffer(1)
        val channels = BufferUtils.createIntBuffer(1)

        // Load the texture using stb_image
        STBImage.stbi_set_flip_vertically_on_load(true)
        val imageData = STBImage.stbi_load(filePath, width, height, channels, 0)
            ?: throw RuntimeException(
                """
        Failed to load a texture file: $filePath
        ${STBImage.stbi_failure_reason()}
        """.trimIndent()
            )


//        // Upload the texture data to OpenGL
        val format = if (channels[0] == 3) GL11.GL_RGB else GL11.GL_RGBA
        //        glTexImage2D(GL_TEXTURE_2D, 0, format, width.get(0), height.get(0), 0, format, GL_UNSIGNED_BYTE, imageData);
//
////        // Generate MipMap if desired
//        glGenerateMipmap(GL_TEXTURE_2D);
////
////        // Unbind the texture
//        glBindTexture(GL_TEXTURE_2D, 0);
////
////        // Free the image memory
//        STBImage.stbi_image_free(imageData);

        // You can return the texture ID if you want to use this texture later
        // return textureId;
        return ImageData(format, width[0], height[0], format, imageData)
    }

    fun checkError() {
        val error = GL11.glGetError()
        if (error != GL11.GL_NO_ERROR) {
            throw RuntimeException("OpenGL error: $error")
        }
    }

    class ImageData(var format: Int, var width: Int, var height: Int, var channels: Int, var data: ByteBuffer)
}