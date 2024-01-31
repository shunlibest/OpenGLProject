package org.example;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


public class P02_EBO_Rectangle {
    public static void main(String[] args) {

        new P02_EBO_Rectangle().run();
    }

    // The window handle
    private long window;

    // 描述四个顶点的坐标
    private static final float vertices[] = {
            0.5f, 0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f, 0.5f, 0.0f   // top left
    };

    // 描述四个顶点的索引, 使用EBO的方式
    private static final int indices[] = {
            // 注意索引从0开始!
            // 此例的索引(0,1,2,3)就是顶点数组vertices的下标，
            // 这样可以由下标代表顶点组合成矩形
            0, 1, 3,  // first Triangle
            1, 2, 3   // second Triangle
    };

    public void run() {
        window = GLUtils.initWindow();

//
        int program = GLUtils.initProgram();

        int vao = glGenVertexArrays();
        // 首先绑定顶点数组对象，然后绑定和设置顶点缓冲区，然后配置顶点属性。
        glBindVertexArray(vao);

        //--------------VBO 数据----------------
        // 顶点缓冲对象
        int vbo = glGenBuffers();
        // 绑定
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        // 上传数据
        // glBufferData是一个专门用来把用户定义的数据复制到当前绑定缓冲的函数。
        // 第一个参数是缓冲区的类型，第二个参数是缓冲区的大小，第三个参数是缓冲区的数据，第四个参数是缓冲区的使用模式。
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        //--------------EBO 数据----------------
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //使用glVertexAttribPointer函数告诉OpenGL该如何解析顶点数据
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        // 绑定glsl程序中 layout==1 的属性值
        glEnableVertexAttribArray(0);

        // 解绑数据
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // draw our first triangle
            glUseProgram(program);
            glBindVertexArray(vao); // seeing as we only have a single VAO there's no need to bind it every time, but we'll do so to keep things a bit more organized

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

//             glBindVertexArray(0); // no need to unbind it every time

            // glfw: swap buffers and poll IO events (keys pressed/released, mouse moved etc.)
            // -------------------------------------------------------------------------------
            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }
}