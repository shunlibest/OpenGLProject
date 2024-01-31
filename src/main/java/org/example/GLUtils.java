package org.example;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GLUtils {


    public static long initWindow() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // 配置GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // 窗口默认不可见
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // 窗口可调整大小
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
//        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
//
//
//        // Configure GLFW
//        glfwDefaultWindowHints(); // optional, the current window hints are already the default
//        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
//        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        long window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (_window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(_window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // 不加会崩溃
        GL.createCapabilities();
        glfwMakeContextCurrent(window);
        return window;
    }

    public static void destroyWindow(long window) {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    public static int initVertexShader(String vertexShaderSource) {
        // 创建顶点着色器
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        // 设置着色器源码
        glShaderSource(vertexShader, vertexShaderSource);
        // 编译着色器
        glCompileShader(vertexShader);

        checkError();
        return vertexShader;
    }

    public static int initFragmentShader(String fragmentShaderSource) {
        // 创建片元着色器
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        // 设置着色器源码
        glShaderSource(fragmentShader, fragmentShaderSource);
        // 编译着色器
        glCompileShader(fragmentShader);
        checkError();
        return fragmentShader;
    }


    public static int initProgram() {
        return initProgram("shader/base.vert", "shader/base.frag");
    }

    public static int initProgram(String vertexShaderFileName, String fragmentShaderFileName) {

        String vertexShaderSource = FileUtils.readResourceFile(vertexShaderFileName);
        String fragmentShaderSource = FileUtils.readResourceFile(fragmentShaderFileName);
        return _initProgram(vertexShaderSource, fragmentShaderSource);
    }

    private static int _initProgram(String vertexShaderSource, String fragmentShaderSource) {
        // 创建一个程序
        int program = glCreateProgram();
        // 附加顶点着色器
        glAttachShader(program, initVertexShader(vertexShaderSource));
        // 附加片元着色器
        glAttachShader(program, initFragmentShader(fragmentShaderSource));
        // 链接程序
        glLinkProgram(program);
        checkError();
        return program;
    }


    public static ImageData loadTexture(String fileName) {
        URL resource = ClassLoader.getSystemResource(fileName);
        String filePath = new File(resource.getPath()).getAbsolutePath();

        // Prepare buffers to receive texture data
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Load the texture using stb_image
        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer imageData = STBImage.stbi_load(filePath, width, height, channels, 0);
        if (imageData == null) {
            throw new RuntimeException("Failed to load a texture file: " + filePath + "\n" +
                    STBImage.stbi_failure_reason());
        }


//        // Upload the texture data to OpenGL
        int format = channels.get(0) == 3 ? GL_RGB : GL_RGBA;
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
        return new ImageData(format, width.get(0), height.get(0), format, imageData);
    }


    public static void checkError() {
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            throw new RuntimeException("OpenGL error: " + error);
        }
    }


    public static class ImageData {
        public int width;
        public int height;
        public int format;
        public int channels;
        public ByteBuffer data;

        public ImageData(int format, int width, int height, int channels, ByteBuffer data) {
            this.width = width;
            this.height = height;
            this.format = format;
            this.channels = channels;
            this.data = data;
        }

    }
}

