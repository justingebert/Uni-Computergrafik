#version 330

layout(location=0)in vec3 vertecies;

layout(location=1) in vec3 vertexNormals;

layout(location=2) in vec2 uvCords;

//layout(location=3) in vec3 vertexColors;

uniform mat4 transformationsMatrix;
uniform mat4 projectionMatrix;

out vec3 position;
out vec3 normalIN;
out vec2 uvCordsO;


void main(){

    uvCordsO = uvCords;
    //normals according to tranformations
    mat3 normalMatrix = inverse(transpose(mat3(transformationsMatrix)));
    normalIN = normalMatrix*vertexNormals;

    vec4 transEcken = vec4(vertecies,1.0);
    //Transformation
    transEcken = transformationsMatrix*transEcken;
    position = vec3(transEcken);
    // - var mit 2D-Koordinaten
    // - Z-Koordinate
    gl_Position = projectionMatrix*transEcken;
    //licht berechnung hier ist flat
}