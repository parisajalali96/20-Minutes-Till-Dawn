{\rtf1\ansi\ansicpg1252\cocoartf2761
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 // fragment.glsl\
#ifdef GL_ES\
precision mediump float;\
#endif\
\
varying vec4 v_color;\
varying vec2 v_texCoords;\
uniform sampler2D u_texture;\
\
void main() \{\
    vec4 color = texture2D(u_texture, v_texCoords);\
    float gray = dot(color.rgb, vec3(0.299, 0.587, 0.114)); // luminance formula\
    gl_FragColor = vec4(vec3(gray), color.a) * v_color;\
\}\
}