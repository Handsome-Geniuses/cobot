#pragma once
#include <Arduino.h>

class TIMER{
    public:
        TIMER() : time_set(0) {}
        TIMER(unsigned long int msec) : time_set(msec+50){}
        void Reset();
        void Start();
        void Set(unsigned long int msec);
        bool Done(bool auto_reset=true);
        unsigned long int GetTimePassed_msec();
        float GetTimePassed_sec();
    private:
        unsigned long int time_start = 0;
        unsigned long int time_set = 0;
        bool reset = true;
};