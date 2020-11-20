#include "timer.h"

void TIMER::Reset(){
    reset = true;
}

void TIMER::Start(){
    time_start = millis();
}
 
void TIMER::Set(unsigned long int msec){
    time_set = msec+10;
}

bool TIMER::Done(bool auto_reset){
    if(reset){
        Start();
        reset = false;
        return false;
    }
    else if(millis() - time_start >= time_set){
        if(auto_reset) Reset();
        return true;
    }
    return false;
}

float TIMER::GetTimePassed_sec(){
    return (double)(millis() - time_start) / 1000;
}
unsigned long int TIMER::GetTimePassed_msec(){
    return (millis() - time_start);
}