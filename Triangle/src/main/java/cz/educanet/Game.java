package cz.educanet;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Game {

    private static final float[] vertices = {
            -0.5f, -0.5f, 0.0f, //0
            0.5f, -0.5f, 0.0f,  //1
            0.5f, 0.5f, 0.0f,   //2
            -0.5f, -0.5f, 0.0f, //3
            -0.5f, 0.5f, 0.0f,  //4
            0.5f, 0.5f, 0.0f    //5
    };

    private static final float[] vertices2 = {
            -1.0f, -1.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, 0.0f, 0.0f
    };

    private static final float[] colors = {
            1.0f, 0.0f, 0.0f, 1.0f,
            0.5f, 1.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 1.0f, 0.8f, 1.0f
    };

    private static int triangleVaoId;
    private static int triangleVboId;
    private static int triangleVaoId2;
    private static int triangleVboId2;

    public static void init(long window) {      // inicializace informací a instrukcí se shadery
        // Inicializace shadaerů
        Shaders.initShaders();

        // Generate all the ids
        triangleVaoId = GL33.glGenVertexArrays();
        triangleVboId = GL33.glGenBuffers();
        triangleVaoId2 = GL33.glGenVertexArrays();
        triangleVboId2 = GL33.glGenBuffers();

    }

    public static void draw(long window) {

        GL33.glBindVertexArray(triangleVaoId);                      // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, triangleVboId);     // Tell OpenGL we are currently writing to this buffer (vboId)

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length) // Positions (buffer);
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);


        MemoryUtil.memFree(fb);
    }

    public static void render(long window) {
        GL33.glUseProgram(Shaders.shaderProgramId);
        GL33.glBindVertexArray(triangleVaoId);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices.length / 3);
        GL33.glBindVertexArray(triangleVaoId2);
        GL33.glDrawArrays(GL33.GL_TRIANGLES, 0, vertices2.length / 3);

    }

    public static void update(long window) {
    }

}
