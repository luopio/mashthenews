#include "testApp.h"
#include "ofMain.h"

using namespace std;

//--------------------------------------------------------------
void testApp::setup() {
	//kinect.init(true);  //shows infrared image
	kinect.init();
	kinect.setVerbose(true);
	kinect.open();

	colorImg.allocate(kinect.width, kinect.height);
	grayImage.allocate(kinect.width, kinect.height);
	grayThresh.allocate(kinect.width, kinect.height);
	grayThreshFar.allocate(kinect.width, kinect.height);
	ofSetFrameRate(60);
    nearThreshold = 230;
    farThreshold = 200;

	// zero the tilt on startup
	kinect.setCameraTiltAngle(0);
    ofBackground(0, 0, 0);

    //sender.setup( HOST, PORT );

    tunnel.sendTestMessage();
    vector<Coordinate> v;

    Coordinate c;
    c.x = 120;
    c.y = 230;
    c.z = 250;

    v.push_back(c);

    tunnel.sendAttractionPoints(v);

    //cout << "halleluja!" << endl;

}

//--------------------------------------------------------------
void testApp::update() {
	kinect.update();

	if(kinect.isFrameNew())	{

		grayImage.setFromPixels(kinect.getDepthPixels(), kinect.width, kinect.height);

		//we do two thresholds - one for the far plane and one for the near plane
		//we then do a cvAnd to get the pixels which are a union of the two thresholds.
        grayThreshFar = grayImage;
        grayThresh = grayImage;
        grayThresh.threshold(nearThreshold, true);
        grayThreshFar.threshold(farThreshold);
        cvAnd(grayThresh.getCvImage(), grayThreshFar.getCvImage(), grayImage.getCvImage(), NULL);

        /*unsigned char * pix = grayImage.getPixels();
        for(int i = 0; i < grayImage.getWidth() * grayImage.getHeight(); i++){
            if(pix[i] < 200 && pix[i] > 100) {
                pix[i] = 255;
            } else {
                pix[i] = 0;
            }
        }*/

        grayImage.flagImageChanged();

    	contourFinder.findContours(grayImage, 2000, (kinect.width*kinect.height)/2, 20, false);

    	points.clear();

    	for(int i = 0; i < contourFinder.blobs.size(); i++) {
            Coordinate tc, c, bc, l, r;
            ofxCvBlob blob = contourFinder.blobs[i];
            // top center
            tc.x = blob.centroid.x*1.0/grayImage.width;
            tc.y = blob.boundingRect.y*1.0/grayImage.height;

            tc.z = 0;//(int)grayImage.getPixels()[blob.centroid.x+(int)(blob.centroid.y*grayImage.width)];
            c.x = blob.centroid.x*1.0/grayImage.width;
            c.y = blob.centroid.y*1.0/grayImage.height;
            c.z = 0;
            bc.x = blob.centroid.x*1.0/grayImage.width;
            bc.y = (blob.boundingRect.y + blob.boundingRect.height)*1.0/grayImage.height;
            bc.z = 0;
            l.x = blob.boundingRect.x*1.0/grayImage.width;
            l.y = blob.centroid.y*1.0/grayImage.height;
            l.z = 0;
            r.x = (blob.boundingRect.x + blob.boundingRect.width)*1.0/grayImage.width;
            r.y = blob.centroid.y*1.0/grayImage.height;
            r.z = 0;
            points.push_back(tc);
            points.push_back(c);
            points.push_back(bc);
            points.push_back(l);
            points.push_back(r);
    	}
    	tunnel.sendAttractionPoints(points);
	}
}

//--------------------------------------------------------------
void testApp::draw() {
	ofSetColor(255, 255, 255);

    // kinect.drawDepth(10, 10, 400, 300);
    // kinect.draw(420, 10, 400, 300);

    grayImage.draw(10, 320, 400, 300);
    // contourFinder.draw(420, 320, 400, 300);

    for(int i = 0; i < points.size(); i++) {
        ofSetColor(255, 0, 0);
        //ofCircle(points[i].x, points[i].y, 20);
        ofCircle(points[i].x*grayImage.width, points[i].y*grayImage.height, 20);
    }

	ofSetColor(255, 255, 255);
	ofDrawBitmapString(ofToString(nearThreshold) + " - " + ofToString(farThreshold),
                    20, ofGetHeight() - 50);
}

//--------------------------------------------------------------
void testApp::exit() {
	kinect.setCameraTiltAngle(0); // zero the tilt on exit
	kinect.close();
}

//--------------------------------------------------------------
void testApp::keyPressed (int key) {
	switch (key) {
		case OF_KEY_DOWN:
            farThreshold++;
            nearThreshold++;
			break;
        case OF_KEY_UP:
            farThreshold--;
            nearThreshold--;
			break;
        case OF_KEY_LEFT:
            farThreshold++;
            nearThreshold--;
			break;
        case OF_KEY_RIGHT:
            farThreshold--;
            nearThreshold++;
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

