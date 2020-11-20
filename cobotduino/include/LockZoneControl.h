#pragma once
#include <Arduino.h>
#include "timer.h"

#define ACTIVE_HIGH     //comment out if active low
#define USING_PWM       //comment out if using digital signals

#ifdef USING_PWM
    #undef USING_PWM
    #define MAX_BRIGHTNESS 255
    #define MIN_BRIGHTNESS 0
    #define pinWrite(pin, state) analogWrite(pin, state)
    #define pinRead(pin) analogRead(pin)
#else
    #define MAX_BRIGHTNESS HIGH
    #define MIN_BRIGHTNESS LOW
    #define pinWrite(pin, state) digitalWrite(pin, state)
    #define pinRead(pin) digitalRead(pin)
#endif
#ifdef ACTIVE_HIGH
    #undef ACTIVE_HIGH
    #define ON MAX_BRIGHTNESS
    #define OFF MIN_BRIGHTNESS
    #define Brightness(x) int((MAX_BRIGHTNESS*x)/9)
#else
    #define ON MIN_BRIGHTNESS
    #define OFF MAX_BRIGHTNESS
    #define Brightness(x) int((MIN_BRIGHTNESS*x)/9)
#endif

#define KEY_MSG '$'
#define KEY_CMD '@'
#define KEY_NAN '~'
#define KEY_TERMINATOR ';'

#define B(x) Brightness(x)
#define strcontains(s, c) s.indexOf(c)<0 ? false:true

enum LED_COLOR{LED_RED, LED_GREEN, LED_BLUE};
/***********************************************************************************************
 * Class containing functions for each zone. 
***********************************************************************************************/
class _ZONE{
    public:
        _ZONE(byte pin_r, byte pin_g, byte pin_b);
        bool Init();
        void LEDControl(byte r, byte g, byte b);
        void AllOff();
        void Tog(byte r, byte g, byte b);

        //handles flashing without blocking code. Run stop to reset
        //Mostly for testing. Don't actually use this with urcaps
        //have urcaps create thread that uses tog
        void Run(byte r, byte g, byte b, byte FlashOn=4, byte FlashOff=4);
        void Stop();
    private:
        TIMER timer; 
        bool isRunning = false;
        
        byte pins_led[3];
        byte prev[3] = {0,0,0};
        byte curr[3] = {0,0,0};
};

