#version 330
//lichtberechnung hier nach gourad
uniform sampler2D smplr;
uniform sampler2D smplrN;
uniform vec3 lightPosition;


in vec3 position;
in vec3 color;
in vec3 normalIN;
in vec2 uvCordsO;

out vec3 coloroutf;

void main(){

    vec3 texel = texture(smplr, uvCordsO).rgb;
    vec3 normTex = texture(smplrN, uvCordsO).rgb;

    vec3 normal = normTex;//normalize(normalIN);
    vec3 licht = normalize(lightPosition-position);

    vec3 refelktionLicht = reflect(-licht,normal);

    vec3 cam = normalize(-1*position);

    float i = 0.1 + 0.7*(max(dot(licht,normal),0)*1.0+pow(max((dot(refelktionLicht,cam)),5),0)*1.0);

    coloroutf = texel*i;
}