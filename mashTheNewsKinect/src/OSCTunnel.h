#ifndef OSCTUNNEL_H
#define OSCTUNNEL_H

#include <vector>
#include "ofxOsc.h"

#define HOST "192.168.10.12"
#define PORT 7000

using namespace std;

struct Coordinate
{
    float x;
    float y;
    float z;
};

class OSCTunnel
{
    public:
        OSCTunnel();
        virtual ~OSCTunnel();

        void sendCoordinates(vector<Coordinate> &coords);
        void sendAttractionPoints(vector<Coordinate> &coords);
        void sendTestMessage();
    protected:
    private:
        ofxOscSender sender;

};

#endif // OSCTUNNEL_H
