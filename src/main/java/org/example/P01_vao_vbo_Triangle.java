package org.example;

import org.lwjgl.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


public class P01_vao_vbo_Triangle {
    public static void main(String[] args) {
        new P01_vao_vbo_Triangle().run();
//        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

    }

    // The window handle
    private long window;

    // step 1: 确定好要画的三角形的顶点
    static float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        window = GLUtils.initWindow();
        GL.createCapabilities();
        glfwMakeContextCurrent(window);
//
        int program = GLUtils.initProgram();

        int vao = glGenVertexArrays();
        // 首先绑定顶点数组对象，然后绑定和设置顶点缓冲区，然后配置顶点属性。
        glBindVertexArray(vao);
        // 顶点缓冲对象
        int vbo = glGenBuffers();
        // 绑定
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        // 上传数据
        // glBufferData是一个专门用来把用户定义的数据复制到当前绑定缓冲的函数。
        // 第一个参数是缓冲区的类型，第二个参数是缓冲区的大小，第三个参数是缓冲区的数据，第四个参数是缓冲区的使用模式。
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        //使用glVertexAttribPointer函数告诉OpenGL该如何解析顶点数据
        glVertexAttribPointer(0, vertices.length/3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);


        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // draw our first triangle
            glUseProgram(program);
            glBindVertexArray(vao); // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized
            glDrawArrays(GL_TRIANGLES, 0, 3);
            // glBindVertexArray(0); // no need to unbind it every time

            // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
            // -------------------------------------------------------------------------------
            glfwSwapBuffers(window);

            glfwPollEvents();
        }
//
//        GLUtils.destroyWindow(window);
    }


}