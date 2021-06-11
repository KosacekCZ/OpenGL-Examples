#version 330 core

out vec4 FragColor;

in vec3 outColor;
in vec2 TextCoord;

uniform sampler2D aTexture;

void main()
{
    FragColor = texture(aTexture, TextCoord);
    // FragColor = vec4(outColor, 1.0f);
}
