{\rtf1\ansi\ansicpg1252\cocoartf2761
\cocoatextscaling0\cocoaplatform0{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww11520\viewh8400\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 // vertex.glsl\
attribute vec4 a_position;\
attribute vec4 a_color;\
attribute vec2 a_texCoord0;\
\
uniform mat4 u_projTrans;\
\
varying vec4 v_color;\
varying vec2 v_texCoords;\
\
void main() \{\
    v_color = a_color;\
    v_texCoords = a_texCoord0;\
    gl_Position = u_projTrans * a_position;\
\}\
\
}