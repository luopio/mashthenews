#ifndef OSCTUNNEL_H
#define OSCTUNNEL_H

#include <vector>
using namespace std;

struct Coordinate
{
    int x;
    int y;
    int z;
};

class OSCTunnel
{
    public:
        OSCTunnel();
        virtual ~OSCTunnel();

        void sendCoordinates(vector<Coordinate> *coords);
    protected:
    private:
};

#endif // OSCTUNNEL_H
