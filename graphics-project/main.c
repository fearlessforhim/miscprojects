// main.c	: opengl 'hello world' using SDL

#include "object_loader.h"
#include "main.h"

static void draw_scene(){
    
}

void set_projection() {
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	float l = 0;
	float r = 100;
	float b = 0;
	float t = 100;
	float n = -1;
	float f = 1;
	glOrtho(l, r, b, t, n, f);
	glMatrixMode(GL_MODELVIEW);
}

static void idle(){
    
}

//mouse detection
static void passive_motion(){
    
}

static void keyboard(unsigned char key, int x, int y){
    
}

static void display() {
//	glClear(GL_COLOR_BUFFER_BIT);
//	glLoadIdentity();
//    
//	draw_scene();
//    
//	glFlush();
    
    glLoadIdentity();
    glClear(GL_COLOR_BUFFER_BIT);
    
    GLfloat vertices[3][3] = {{0.0, 0.0, 0.0}, {50.0, 100.0,0.0}, {100.0, 0.0, 0.0}};
    
    GLfloat p[3] = {0.0, 0.0, 0.0};
    
    int j,k;
    srand((unsigned)time(NULL));
    float red, green, blue;
    
    glBegin(GL_POINTS);
    for (k=0; k<100000; k++) {
        j=rand()%3;
        
        p[0] = (p[0] + vertices[j][0])/2;
        p[1] = (p[1] + vertices[j][1])/2;
        
        red = p[0]/5;
        green = p[1]/10;
        blue = 1.0;
        
        glColor3f(red, green, blue);
        glVertex3fv(p);
    }
    glEnd();
    glFlush();
	
}

static void initGame(){
    
}

int main(int argc, char * argv[])
{
    glutInit(&argc, argv);
    glutCreateWindow("BlockBuster");
    glutReshapeWindow(glutGet(GLUT_SCREEN_WIDTH)/1.5, glutGet(GLUT_SCREEN_HEIGHT)/1.5);
    
    //init game state
    initGame();
    
    printf("running");
    
    glutDisplayFunc(display);
    glutIdleFunc(idle);
    glutPassiveMotionFunc(passive_motion);
    glutKeyboardFunc(keyboard);
    
    set_projection();
    glutMainLoop();
}