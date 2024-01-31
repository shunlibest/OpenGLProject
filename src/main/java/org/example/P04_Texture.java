package org.example;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;


public class P04_Texture {
    public static void main(String[] args) {

        new P04_Texture().run();
    }

    // The window handle
    private long window;

    // 描述四个顶点的坐标
    private static final float vertices[] = {
            // positions          // colors           // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };

    // 描述四个顶点的索引, 使用EBO的方式
    private static final int indices[] = {
            // 注意索引从0开始!
            // 此例的索引(0,1,2,3)就是顶点数组vertices的下标，
            // 这样可以由下标代表顶点组合成矩形
            0, 1, 3,  // first Triangle
            1, 2, 3   // second Triangle
    };

    private static final float texCoords[] = {
            0.0f, 0.0f, // 左下角
            1.0f, 0.0f, // 右下角
            0.5f, 1.0f // 上中
    };

    public void run() {
        window = GLUtils.initWindow();
        int program = GLUtils.initProgram("shader/texture.vert", "shader/texture.frag");

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
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        // 绑定glsl程序中 layout==1 的属性值
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);


        GLUtils.checkError();


        // ---------------TBO 纹理数据----------------
        int texture1 = glGenTextures();
//        glActiveTexture(GL_TEXTURE0); // 在绑定纹理之前先激活纹理单元
        glBindTexture(GL_TEXTURE_2D, texture1);

        GLUtils.checkError();


        // 为当前绑定的纹理对象设置环绕、过滤方式
        // 设置s轴和t轴的重复方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // 设置过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);


        GLUtils.ImageData imageData = GLUtils.loadTexture("image/test.jpg");

        glTexImage2D(GL_TEXTURE_2D, 0, imageData.format, imageData.width, imageData.height, 0, imageData.format, GL_UNSIGNED_BYTE, imageData.data);
        glGenerateMipmap(GL_TEXTURE_2D);
        GLUtils.checkError();

        int texture2 = glGenTextures();
//        glActiveTexture(GL_TEXTURE0); // 在绑定纹理之前先激活纹理单元
        glBindTexture(GL_TEXTURE_2D, texture2);

        GLUtils.checkError();


        // 为当前绑定的纹理对象设置环绕、过滤方式
        // 设置s轴和t轴的重复方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // 设置过滤方式
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GLUtils.ImageData imageData2 = GLUtils.loadTexture("image/test2.png");
        glTexImage2D(GL_TEXTURE_2D, 0, imageData2.format, imageData2.width, imageData2.height, 0, imageData2.format, GL_UNSIGNED_BYTE, imageData2.data);
        glGenerateMipmap(GL_TEXTURE_2D);
        GLUtils.checkError();


        // 激活第一个纹理单元并绑定纹理
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture1);

        // 激活第二个纹理单元并绑定纹理
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, texture2);


        // 解绑数据
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, texture1);
//        glActiveTexture(GL_TEXTURE1);
//        glBindTexture(GL_TEXTURE_2D, texture2);
        GLUtils.checkError();

        glUseProgram(program);
        // 查询uniform变量的位置
        int vertexColorLocation = glGetUniformLocation(program, "ourColor");
        GLUtils.checkError();
//
        glUniform1i(glGetUniformLocation(program, "texture1"), 0);//
        glUniform1i(glGetUniformLocation(program, "texture2"), 1);
//        // or set it via the texture class
////        ourShader.setInt("texture2", 1);

        GLUtils.checkError();

        while (!glfwWindowShouldClose(window)) {
            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);


            glUseProgram(program);



            // 激活程序后, 使用glUniform函数设置uniform变量的值
            float red = (float) Math.abs(Math.sin(glfwGetTime()));

            glUniform4f(vertexColorLocation, red, 0f, 0f, 0f);

            glBindVertexArray(vao);

            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);


            GLUtils.checkError();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}