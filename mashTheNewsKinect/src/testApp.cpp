#include "testApp.h"


//--------------------------------------------------------------
void testApp::setup() {
	//kinect.init(true);  //shows infrared image
	kinect.init();
	kinect.setVerbose(true);
	kinect.open();

	colorImg.allocate(kinect.width, kinect.height);
	grayImage.allocate(kinect.width, kinect.height);
	ofSetFrameRate(60);

	// zero the tilt on startup
	kinect.setCameraTiltAngle(0);
}

//--------------------------------------------------------------
void testApp::update() {
	ofBackground(100, 100, 0);
	kinect.update();

	if(kinect.isFrameNew())	{

		grayImage.setFromPixels(kinect.getDepthPixels(), kinect.width, kinect.height);

		//we do two thresholds - one for the far plane and one for the near plane
		//we then do a cvAnd to get the pixels which are a union of the two thresholds.
        /*
        grayThreshFar = grayImage;
        grayThresh = grayImage;
        grayThresh.threshold(nearThreshold, true);
        grayThreshFar.threshold(farThreshold);
        cvAnd(grayThresh.getCvImage(), grayThreshFar.getCvImage(), grayImage.getCvImage(), NULL);

		//update the cv image
		grayImage.flagImageChanged();
		*/


		// find contours which are between the size of 20 pixels and 1/3 the w*h pixels.
    	// also, find holes is set to true so we will get interior contours as well....
    	// contourFinder.findContours(grayImage, 10, (kinect.width*kinect.height)/2, 20, false);
	}
}

//--------------------------------------------------------------
void testApp::draw() {
	ofSetColor(255, 255, 255);

    kinect.drawDepth(10, 10, 400, 300);
    kinect.draw(420, 10, 400, 300);

    // grayImage.draw(10, 320, 400, 300);

    // contourFinder.draw(10, 320, 400, 300);

	ofSetColor(255, 255, 255);
}

//--------------------------------------------------------------
void testApp::exit() {
	kinect.setCameraTiltAngle(0); // zero the tilt on exit
	kinect.close();
}

//--------------------------------------------------------------
void testApp::keyPressed (int key) {
	switch (key) {
		case ' ':
			break;
	}
}

//--------------------------------------------------------------
void testApp::mouseMoved(int x, int y) {
}

//--------------------------------------------------------------
void testApp::mouseDragged(int x, int y, int button)
{}

//--------------------------------------------------------------
void testApp::mousePressed(int x, int y, int button)
{}

//--------------------------------------------------------------
void testApp::mouseReleased(int x, int y, int button)
{}

//--------------------------------------------------------------
void testApp::windowResized(int w, int h)
{}

