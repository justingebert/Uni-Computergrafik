#version 330

//vertex shader

layout(location = 0) in vec2 eckenAusJava;
layout(location = 1) in vec3 color;

out vec3 zeuch;

void main(){


    float x = eckenAusJava.x;
    float y = eckenAusJava.y;

    mat2 rota = mat2(
    cos(50),-sin(50),
    sin(50),cos(50)
    );

   /* rota[0] =  vec2 (cos(50),-sin(50));
    rota[1] =  vec2 (sin(50),cos(50));*/

/*
    float x1 = x*cos(50)-y*sin(50);
    float y2 = x*sin(50)+y*cos(50);*/


    vec2 eckenAusJava = eckenAusJava*rota;


  /*  zeuch = vec3 (x,y,0.0);*/

    zeuch = color;

    gl_Position = vec4(eckenAusJava,0.0,1.0);

}
