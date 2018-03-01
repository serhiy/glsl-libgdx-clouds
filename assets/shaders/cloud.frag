// The simplexNoise2 function was taken from the example presented at 
// <http://www.geeks3d.com/20110317/shader-library-simplex-noise-glsl-opengl/>, 
// all the credits go to the original author of the respective algorithm.

#ifdef GL_ES
#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

uniform float u_time;
uniform vec2 u_size;
uniform vec2 u_position;

const float initialPersisence = 0.5;
const float initialScale = 0.01;
const float density = 0.5;
const float fadeSpeed = 0.025;
const float innerSpeed = 5.0;
const vec2 amplitude = vec2(-0.4, 1.0);

const vec4 pParam = vec4(17.0*17.0, 34.0, 1.0, 7.0);

vec3 permute(vec3 x0,vec3 p) { 
	vec3 x1 = mod(x0 * p.y, p.x);
	return floor(mod((x1 + p.z) * x0, p.x));
}

float taylorInvSqrt(float r) { 
	return (0.83666002653408 + 0.7*0.85373472095314 - 0.85373472095314 * r);
}

float simplexNoise2(vec2 v) {
	const vec2 C = vec2(0.211324865405187134, 0.366025403784438597);
	const vec3 D = vec3(0.0, 0.5, 2.0) * 3.14159265358979312;
	
	// First corner
	vec2 i = floor(v + dot(v, C.yy));
	vec2 x0 = v - i + dot(i, C.xx);

	// Other corners
	vec2 i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);

	vec2 x1 = x0 - i1 + 1.0 * C.xx ;
	vec2 x2 = x0 - 1.0 + 2.0 * C.xx ;

	// Permutations
	i = mod(i, pParam.x);
	vec3 p = permute(permute(i.y + vec3(0.0, i1.y, 1.0), pParam.xyz) + i.x + vec3(0.0, i1.x, 1.0), pParam.xyz);

	vec3 x = fract(p / pParam.w) ;
	vec3 h = 0.5 - abs(x) ;

	vec3 sx = vec3(lessThan(x, D.xxx)) * 2.0 - 1.0;
	vec3 sh = vec3(lessThan(h, D.xxx));

	vec3 a0 = x + sx * sh;
	vec2 p0 = vec2(a0.x, h.x);
	vec2 p1 = vec2(a0.y, h.y);
	vec2 p2 = vec2(a0.z, h.z);

	p0 *= taylorInvSqrt(dot(p0, p0));
	p1 *= taylorInvSqrt(dot(p1, p1));
	p2 *= taylorInvSqrt(dot(p2, p2));
  
	vec3 g = 2.0 * vec3(dot(p0, x0), dot(p1, x1), dot(p2, x2));
	vec3 m = max(0.5 - vec3(dot(x0, x0), dot(x1, x1), dot(x2, x2)), 0.0);
	m = m * m ;
	
	return 1.66666 * 70.0 * dot(m * m, g);
}

float combine(float iterations, vec2 position, float persistence, float scale, float lowAmp, float highAmp) {
    float sigTotal = 0.0;
    float sigCurr = 1.0;
    float freq = scale;
    float noise = 0.0;

    for(float i = 0.0; i < iterations; ++i) {
        noise += simplexNoise2(position * freq) * sigCurr;
        sigTotal += sigCurr;
        sigCurr *= persistence;
        freq *= 2.0;
    }

    return (noise / sigTotal) * (highAmp - lowAmp) / 2.0 + (highAmp + lowAmp) / 2.0;
}

float displacement(float dx) {
	return sin((dx + u_time) * 0.15) + sin((dx + u_time * 5.45) * 0.025);
}

float gradient(float noise) {
	float dx = gl_FragCoord.x - (u_position.x + u_size.x / 2.0);
	float dy = gl_FragCoord.y - (u_position.y + u_size.y / 2.0);
	float dxn = abs(dx) / u_size.x;
	float dyn = abs(dy) / u_size.y; 
	float d = dxn * dxn + dyn * dyn;
	
	float md = (0.48 + 0.02 * displacement(dx)) * 0.9;
	float g = d / (md * md);
	
	return noise * max(0.0, 1.0 - g);
}

float fade() {
	float t1 = mod(u_time * fadeSpeed, 2.0);
	float t2 = step(1.0, t1) * 2.0;
	return 1.0 - abs(t2 - t1);
}

void main() {	
	float noise = combine(4.0, vec2(gl_FragCoord.x + innerSpeed * u_time, gl_FragCoord.y), initialPersisence, initialScale, amplitude.x + fade() * density, amplitude.y);

	gl_FragColor = vec4(vec3(1.0), gradient(noise));
}