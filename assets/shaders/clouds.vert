attribute vec4 a_position;
attribute vec4 a_color;

uniform mat4 u_projTrans;

void main() {
    gl_Position =  u_projTrans * a_position;
}