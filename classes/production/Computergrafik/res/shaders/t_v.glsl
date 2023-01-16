#version 330

layout(location=0)in vec3 vertecies;

layout(location=1) in vec3 vertexNormals;

layout(location=2) in vec2 uvCords;

layout(location=3) in vec3 vertexColors;


uniform mat4 transformationsMatrix;
uniform mat4 projectionMatrix;

out vec3 position;
out vec3 normalIN;
out vec3 uvCordsO;
out vec3 colorP;


void main(){

    colorP = vertexColors;
    //normals according to tranformations
    vec4 normals = vec4(vertexNormals,1.0);
    mat4 normalMatrix = inverse(transpose((transformationsMatrix)));


    vec4 transEcken = vec4(vertecies,0);
    normalIN = vec3(normalMatrix*transEcken);
    //Transformation
    transEcken = transformationsMatrix*transEcken;
    position = vec3(transEcken);
    // - var mit 2D-Koordinaten
    // - Z-Koordinate
    gl_Position = projectionMatrix*transEcken;
    //licht berechnung hier ist flat
}