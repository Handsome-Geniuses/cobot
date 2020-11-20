#include "LockZoneControl.h"

/***********************************************************************************************
 * @brief           Class handles leds for different zones
 * @param pin_r     pin for red
 * @param pin_g     pin for green
 * @param pin_b     pin for blue
***********************************************************************************************/
_ZONE::_ZONE(byte pin_r, byte pin_g, byte pin_b):
    pins_led{pin_r, pin_g, pin_b}{
}

/***********************************************************************************************
 * @brief           Initialize pins and sets all to off
***********************************************************************************************/
bool _ZONE::Init() {
    for(unsigned int i=0; i<sizeof(pins_led)/sizeof(pins_led[0]); i++) pinMode(pins_led[i], OUTPUT);
    AllOff();
    return true;
}

/***********************************************************************************************
 * @brief           True to turn on. False to turn off
 * @param r         0-9 red brightness
 * @param g         0-9 green brightness
 * @param b         0-9 blue brightness
***********************************************************************************************/
void _ZONE::LEDControl(byte r, byte g, byte b) {
    if(r>9) r=9;
    if(g>9) g=9;
    if(b>9) b=9;
    pinWrite(pins_led[0], B(r));
    pinWrite(pins_led[1], B(g));
    pinWrite(pins_led[2], B(b));
    prev[0]=curr[0];
    prev[1]=curr[1];
    prev[2]=curr[2];
    curr[0]=r;
    curr[1]=g;
    curr[2]=b;
}

/***********************************************************************************************
 * @brief           Turns all pins off
***********************************************************************************************/
void _ZONE::AllOff() {
    LEDControl(0,0,0);
}

/***********************************************************************************************
 * @brief           Toggles led based on led state.
 * @param r         0-9 red brightness
 * @param g         0-9 green brightness
 * @param b         0-9 blue brightness
***********************************************************************************************/
void _ZONE::Tog(byte r, byte g, byte b){
    LEDControl( curr[0]>prev[0]?prev[0]:r,
                curr[1]>prev[1]?prev[1]:g,
                curr[2]>prev[2]?prev[2]:b);
}

/***********************************************************************************************
 * @brief           Toggles led based on led state and delay.
 * @param r         0-9 red brightness
 * @param g         0-9 green brightness
 * @param b         0-9 blue brightness
 * @param FlashOn   0-9 How long led stays on. seconds=(1+FlashOn)*200. 4=1second.
 * @param FlashOff  0-9 How long led stays off. seconds=(1+FlashOn)*200. 4=1second.
***********************************************************************************************/
void _ZONE::Run(byte r, byte g, byte b, byte FlashOn, byte FlashOff){
    static bool flip;
    if(FlashOn>9) FlashOn=9;
    if(FlashOff>9) FlashOff=9;
    if(!isRunning){
        timer.Reset();
        timer.Start();
        AllOff();
        isRunning=true;
        flip=false;
    }
    timer.Set((unsigned long)(flip?(1+FlashOn)*200:(1+FlashOff)*200));
    if(timer.Done(true)){
        Tog(r,g,b);
        flip=!flip;
    } 
}

void _ZONE::Stop(){
    isRunning = false;
}