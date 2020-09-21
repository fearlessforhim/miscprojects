//
//  camera.h
//  project2
//
//  Created by Jonathan Cameron on 11/19/12.
//  Copyright (c) 2012 Jonathan Cameron. All rights reserved.
//

#ifndef project2_cameron_h
#define project2_cameron_h



#endif
#ifndef _camera_h_
#define _camera_h_

// sets camera parameters
void camera_set(const double* eye, const double* cen, const double* up);

// returns camera parameters
void camera_get(double* eye_ret, double* cen_ret, double* up_ret);

// call from display func, does gluLookAt() for camera pos
void camera_lookat();

// call from keyboard func, returns true if camera key used
int camera_keyboard(int key);

// call from glut mouse func, returns true if mouse input used
int camera_mouse(int button, int state, int x, int y);

// call from glut motion func, returns true if mouse input used
int camera_motion(int x, int y);

// util function, sets perspective projection (optional, not needed for camera functionality)
void camera_set_projection();

// joystick input
int camera_joystick(int axis, int value);

// needed to update joystick input
void camera_tick();

#endif
