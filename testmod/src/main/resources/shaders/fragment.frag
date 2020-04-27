#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform vec3 colour;

void main()
{
    fragColor = vec4(colour, 1);
}