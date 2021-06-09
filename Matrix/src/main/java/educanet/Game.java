package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.stb.STBImage;

public class Game {

    private static final float[] vertices = {
            0.15f, 0.15f, 0.0f, // 0 -> Top right
            0.15f, -0.15f, 0.0f, // 1 -> Bottom right
            -0.15f, -0.15f, 0.0f, // 2 -> Bottom left
            -0.15f, 0.15f, 0.0f, // 3 -> Top left
    };
    ;

    private static final float[] colors = {
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
    };


    private static final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    private static final float[] textCoords = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,

    };

    private static int direction = 0; // default right sloped up
    private static int squareVaoId;
    private static int squareVboId;
    private static int squareEboId;
    private static int colorsId;
    private static int uniformMatrixLocation;
    private static int textureIndicesId;
    private static int textureId;
    private static float posX;
    private static float posY;

    private static Matrix4f matrix = new Matrix4f()
            .identity()
            .translate(0.25f, 0.25f, 0.25f);
    // 4x4 -> FloatBuffer of size 16

    private static FloatBuffer matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

    public static void init(long window) {

        // Setup shaders
        Shaders.initShaders();

        // Generate all the ids
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        colorsId = GL33.glGenBuffers();
        textureIndicesId = GL33.glGenBuffers();
        textureId = GL33.glGenTextures();

        // load texture to memory
        loadImage();

        // Get uniform location
        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        // Tell OpenGL we are currently using this object (vaoId)
        GL33.glBindVertexArray(squareVaoId);

        // Tell OpenGL we are currently writing to this buffer (eboId)
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        // Change to VBOs...


        // Tell OpenGL we are currently writing to this buffer (vboId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);

        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);


        // Change to Color...
        // Tell OpenGL we are currently writing to this buffer (colorsId)
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);

        FloatBuffer cb = BufferUtils.createFloatBuffer(colors.length)
                .put(colors)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);


        // loading texture indices to gpu buffer
        FloatBuffer tb = BufferUtils.createFloatBuffer(textCoords.length)
                .put(textCoords)
                .flip();

        // Send the buffer (positions) to the GPU
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(2);

        MemoryUtil.memFree(tb);

        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        matrix = matrix.translate(0.0f, 0.5f, 0f);
        posY += 0.5f;

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(cb);
        MemoryUtil.memFree(fb);
    }

    public static void render(long window) {
        GL33.glUseProgram(Shaders.shaderProgramId);
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);


        // Draw using the glDrawElements function
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);

    }

    public static void update(long window) {
        if (posX > 0.6f || posX < -1.1f || posY > 0.65f || posY < -1.1f) direction++;

        switch (direction % 4) {
            case 0 -> {
                matrix = matrix.translate(0.0002f, 0.0002f, 0f);
                posX += 0.0002f;
                posY += 0.0002f;
            }
            case 1 -> {
                matrix = matrix.translate(-0.0002f, 0.0002f, 0f);
                posX -= 0.0002f;
                posY += 0.0002f;
            }
            case 2 -> {
                matrix = matrix.translate(-0.0002f, -0.0002f, 0f);
                posX -= 0.0002f;
                posY -= 0.0002f;
            }
            case 3 -> {
                matrix = matrix.translate(0.0002f, -0.0002f, 0f);
                posX += 0.0002f;
                posY -= 0.0002f;
            }
        }
    }

    private static void loadImage() {
        textureId = GL33.glGenTextures();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer img = STBImage.stbi_load("resources/textures/texture.png", w, h, comp, 4);
            if (img != null) {
                img.flip();

                GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
                GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA, w.get(), h.get(), 0, GL33.GL_RGBA, GL33.GL_UNSIGNED_BYTE, img);
                GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);

                STBImage.stbi_image_free(img);
            } else {
                System.out.println("You vobrázekn't.");
            }
        }
    }
}
