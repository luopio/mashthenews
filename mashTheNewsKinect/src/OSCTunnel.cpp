#include "OSCTunnel.h"

OSCTunnel::OSCTunnel()
{
    sender.setup( HOST, PORT );
}

OSCTunnel::~OSCTunnel()
{
    //dtor
}

void OSCTunnel::sendTestMessage() {
    ofxOscMessage m;
    m.setAddress( "/testmessage" );
	m.addIntArg( 1 );
	m.addFloatArg( 3.5f );
	m.addStringArg( "hello" );
	m.addFloatArg( ofGetElapsedTimef() );
	sender.sendMessage( m );
}

void OSCTunnel::sendCoordinates(vector<Coordinate> &coords)
{
    return;
}

void OSCTunnel::sendAttractionPoints(vector<Coordinate> &coords)
{
    ofxOscMessage m;
     m.setAddress( "/attractionpoints" );
    for (int i = 0; i < coords.size(); i++) {
        m.addFloatArg(coords[i].x);
        m.addFloatArg(coords[i].y);
        m.addFloatArg(coords[i].z);
    }
    sender.sendMessage(m);
    return;
}


