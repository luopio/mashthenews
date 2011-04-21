#ifndef OSCTUNNEL_H
#define OSCTUNNEL_H

#include <vector>
#include "ofxOsc.h"

#define HOST "192.168.1.101"
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
        OSCTunnel(char * ip);
        virtual ~OSCTunnel();

        void sendCoordinates(vector<Coordinate> &coords);
        void sendAttractionPoints(vector<Coordinate> &coords);
        void sendImageData(vector<Coordinate> & coords);
        void sendTestMessage();
    protected:
    private:
        ofxOscSender sender;
        void sendStartMessage();
        void sendStopMessage();

};

#endif // OSCTUNNEL_H
