//
//  main.h
//  project2
//
//  Created by Jonathan Cameron on 11/19/12.
//  Copyright (c) 2012 Jonathan Cameron. All rights reserved.
//

#ifndef project2_main_h
#define project2_main_h

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
#include <time.h>

#include <GLUT/GLUT.h>
#include <OpenGL/OpenGL.h>

#endif

static void draw_scene();
void set_projection();
static void idle();
static void passive_motion();
static void keyboard(unsigned char key, int x, int y);
static void display();
static void initGame();