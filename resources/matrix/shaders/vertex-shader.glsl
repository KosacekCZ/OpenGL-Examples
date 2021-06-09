#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aColor;
layout (location = 2) in vec2 aTextCoord;

uniform mat4 matrix;

out vec3 outColor;
out vec2 TextCoord;

void main()
{
    gl_Position = matrix * vec4(aPos.x, aPos.y, aPos.z, 1.0);
    outColor = aColor;
    TextCoord = aTextCoord;
}
