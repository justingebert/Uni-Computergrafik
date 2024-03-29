#version 330

layout(location=0)in vec3 vertecies;

layout(location=1) in vec3 vertexNormals;

layout(location=2) in vec2 uvCords;


uniform mat4 transformationsMatrix;
uniform mat4 projectionMatrix;

out vec3 position;
//out vec3 color;
out vec3 normalIN;
out vec2 uvCordsO;


void main(){

    //color = vertecies;
    uvCordsO = uvCords;
    //normals according to tranformations
    vec3 normals = vertexNormals;
    mat3 normalMatrix = mat3(transpose(inverse(transformationsMatrix)));
    //inverse has issues --> values inconsistent

    vec4 transEcken = vec4(vertecies,1.0);
    normalIN = normalMatrix*normals;

    //Transformation
    transEcken = transformationsMatrix*transEcken;
    //transEcken = projektionsMatrix*transEcken;
    position = vec3(transEcken);
    // - var mit 2D-Koordinaten
    // - Z-Koordinate
    gl_Position = projectionMatrix*transEcken;
    //licht berechnung hier ist flat
}