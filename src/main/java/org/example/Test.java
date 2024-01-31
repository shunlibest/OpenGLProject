package org.example;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Test {
    // The window handle
    private long window;

    // Shader programs
    private int vertexShader;
    private int fragmentShader;
    private int shaderProgram;

    // Vertex Array Object
    private int vao;
    // Vertex Buffer Object
    private int vbo;
    // Element Buffer Object
    private int ebo;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Create the window
        window = GLUtils.initWindow();
        // Link shaders
        shaderProgram = GLUtils.initProgram();


        float[] vertices = {
                -0.5f, 0.5f, 0.0f,  // Top-left
                0.5f, 0.5f, 0.0f,  // Top-right
                0.5f, -0.5f, 0.0f,  // Bottom-right
                -0.5f, -0.5f, 0.6f   // Bottom-left
        };

        int[] indices = {
                0, 1, 2,  // First triangle (top-left -> top-right -> bottom-right)
                0, 2, 3   // Second triangle (top-left -> bottom-right -> bottom-left)
        };

        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        ebo = glGenBuffers();

        glBindVertexArray(vao);

        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Unbind the VBO/VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // Unbind the EBO after unbinding the VAO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            // Use our shader program
            glUseProgram(shaderProgram);
            glBindVertexArray(vao); // Bind the VAO (it was already setup)

            // Draw the triangle using the EBO
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            // Unbind the VAO to prevent accidental modification
            glBindVertexArray(0);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void checkCompileErrors(int shader, String type) {
        IntBuffer success = BufferUtils.createIntBuffer(1);
        if ("PROGRAM".equals(type)) {
            glGetProgramiv(shader, GL_LINK_STATUS, success);
            if (success.get(0) == GL_FALSE) {
                System.out.println("ERROR::SHADER_LINKING_ERROR of type: " + type);
                System.out.println(glGetProgramInfoLog(shader));
            }
        } else {
            glGetShaderiv(shader, GL_COMPILE_STATUS, success);
            if (success.get(0) == GL_FALSE) {
                System.out.println("ERROR::SHADER_COMPILATION_ERROR of type: " + type);
                System.out.println(glGetShaderInfoLog(shader));
            }
        }
    }

    public static void main(String[] args) {
        new Test().run();
    }
}
