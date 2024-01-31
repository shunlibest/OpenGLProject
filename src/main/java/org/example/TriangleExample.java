package org.example;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TriangleExample {

    // 窗口句柄
    private long window;


    // 三角形的顶点
    private float[] vertices = {
            -0.5f, -0.5f, 0.0f, // 左下角
            0.5f, -0.5f, 0.0f, // 右下角
            0.0f,  0.5f, 0.0f  // 顶部
    };

    // 顶点数组对象, 顶点缓冲对象, 着色器程序
    private int VAO, VBO, shaderProgram;

    public void run() {
        System.out.println("LWJGL版本：" + Version.getVersion() + "!");

        init();
        loop();

        // 清理资源
        glDeleteVertexArrays(VAO);
        glDeleteBuffers(VBO);
        glDeleteProgram(shaderProgram);

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // 终止GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {

        window = GLUtils.initWindow();

    }

    private void loop() {
        // 创建OpenGL功能指针
        GL.createCapabilities();


        // 创建着色器程序
        shaderProgram = GLUtils.initProgram();

        // 创建VAO、VBO
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // 设置清屏颜色
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // 运行渲染循环，直到用户要求关闭窗口
        while (!glfwWindowShouldClose(window)) {
            // 清屏
            glClear(GL_COLOR_BUFFER_BIT);

            // 绘制三角形
            glUseProgram(shaderProgram);
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 3);

            // 交换颜色缓冲
            glfwSwapBuffers(window);

            // 轮询事件
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new TriangleExample().run();
    }
}
