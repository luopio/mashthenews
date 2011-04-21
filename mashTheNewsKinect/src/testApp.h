#pragma once

#include "ofMain.h"
#include "ofxOpenCv.h"
#include "ofxKinect.h"
#include "OSCTunnel.h"
#include "ofxOsc.h"

#define HOST "192.168.10.12"
#define PORT 12000

class testApp : public ofBaseApp {
	public:

		void setup();
		void update();
		void draw();
		void exit();

		void drawPointCloud();

		void keyPressed  (int key);
		void mouseMoved(int x, int y );
		void mouseDragged(int x, int y, int button);
		void mousePressed(int x, int y, int button);
		void mouseReleased(int x, int y, int button);
		void windowResized(int w, int h);

		ofxKinect kinect;

		ofxCvColorImage		    colorImg;
		ofxCvGrayscaleImage 	grayImage;
		ofxCvGrayscaleImage 	scaleImage;
		ofxCvGrayscaleImage 	grayThresh;
		ofxCvGrayscaleImage 	grayThreshFar;
		ofxCvContourFinder 	    contourFinder;

        OSCTunnel * tunnel;
        vector<OSCTunnel*> osctunnels;
        vector<Coordinate> points;


		int nearThreshold;
		int farThreshold;

    private:
        //ofxOscSender sender;
};
