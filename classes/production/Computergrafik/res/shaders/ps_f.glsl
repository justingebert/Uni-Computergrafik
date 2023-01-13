#version 330
//lichtberechnung hier nach gourad

uniform sampler2D smplrP;
uniform sampler2D smplrP2;
uniform vec3 lightPosition;


in vec3 position;
in vec3 normalIN;
in vec2 uvCordsO;

out vec3 coloroutf;

void main(){

    vec3 texel = texture(smplrP, uvCordsO).rgb;

    vec3 normal = normalize(normalIN);
    vec3 licht = normalize(lightPosition-position);

    vec3 refelktionLicht = reflect(-licht,normal);

    vec3 cam = normalize(-1*position);

    float i = 0.1 + 0.7*(max(dot(licht,normal),0)*1.0+pow(max((dot(refelktionLicht,cam)),5),0)*1.0);

    //coloroutf = color*i;
    coloroutf = texel*i;
}