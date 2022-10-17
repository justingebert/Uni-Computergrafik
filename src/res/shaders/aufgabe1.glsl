#version 330

out vec3 pixelFarbe;
vec2 pixel = gl_FragCoord.xy;
vec2 CircleCenter = vec2(350,350);



void main() {
    //rechteck
    float x = gl_FragCoord.x;
    float y = gl_FragCoord.y;

    float x1 = x*cos(50)-y*sin(50);
    float y2 = x*sin(50)+y*cos(50);

    pixelFarbe = vec3(1.0, 1.0, 0.0);

    if(x<100 && x>50 && y<100 && y>50){
        pixelFarbe = vec3(1.0, 0.0, 0.0);
    }
    //kreis
    if(distance(pixel,CircleCenter)<50 ){
        pixelFarbe = vec3(1.0, 1.0, 1.0);
    }
    //rechteck rotiert
    if(x1<400 && x1>350 && y2<400 && y2>350){
        pixelFarbe = vec3(0.0, 0.0, 1.0);
    }
    //hintergrund


}
