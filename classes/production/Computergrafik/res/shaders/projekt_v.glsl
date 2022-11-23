#version 330

layout(location=0)
in vec3 vertecies;
uniform mat4 transformationsMatrix;

layout(location=1)
in vec3 vertexColors;
out vec3 color;

void main(){
    color = vertexColors;

    vec4 transEcken = vec4(vertecies,1.0);

    //Transformation
    transEcken = transformationsMatrix*transEcken;

    // - var mit 2D-Koordinaten
    // - Z-Koordinate
    gl_Position = transEcken;
}