#include <Arduino.h>
#include <SoftwareSerial.h>
#include "LockZoneControl.h"

#define PIN_LED 13
#define PIN_BTN 45
#define PIN_BTN2 A2
#define print(s) Serial.print(s)
#define println(s) Serial.println(s)
#define debounce_btn 500

SoftwareSerial rs485(11,10);

void ReadWrite();
void DelmitMe();
void BtnLedTest();
void TypeSend();

TIMER btn_debouncer(debounce_btn);

enum MODES{MODE_MSG, MODE_CMD};
MODES mode=MODE_MSG;

enum ZONES{z1,z2,z3};
ZONES zone=z1;

_ZONE zones[2]{
    _ZONE{7,6,5},
    _ZONE{4,3,2}
};

void setup() {
    Serial.begin(9600);
    while(!Serial);
    Serial.setTimeout(200);
    // Serial.setTimeout(200);
    rs485.begin(9600);
    while(!rs485);
    // rs485.setTimeout(200);

    pinMode(PIN_LED, OUTPUT);                                 
    digitalWrite(PIN_LED, LOW);

    pinMode(PIN_BTN, INPUT_PULLUP);
    pinMode(PIN_BTN2, INPUT_PULLUP);

    zones[z1].Init();
    zones[z1].LEDControl(0,0,0);
    println();
    btn_debouncer.Reset();
    btn_debouncer.Start();
}

void loop() {
    // zones[z1].Run(0,0,1,4,4);
    DelmitMe();
    BtnLedTest();
    TypeSend();
}

void ReadWrite(){
    if(rs485.available()) Serial.write(rs485.read());
    if(Serial.available()) rs485.write(Serial.read());
}
void TypeSend(){
    if(Serial.available()){
        char c = Serial.read();
        rs485.write(c);
        print(c);
    } 
}
void BtnLedTest(){
    if(digitalRead(PIN_BTN)==HIGH){
        digitalWrite(PIN_LED, LOW);
    }
    else if(btn_debouncer.Done(true)){
        // rs485.write(String("b"+String(PIN_BTN)+"\n").c_str());
        // rs485.write("hi\n");
        rs485.write("b45\n");
        digitalWrite(PIN_LED, HIGH);
        println("pressed!");
        btn_debouncer.Reset();
        btn_debouncer.Start();
    }
    if(digitalRead(PIN_BTN2)==LOW && btn_debouncer.Done(true)){
        rs485.write("bA2\n");
        btn_debouncer.Reset();
        btn_debouncer.Start();
    }   
}

void DelmitMe(){
    String s;
    // if(Serial.available()){
    //     s=Serial.readStringUntil(KEY_TERMINATOR);
    if(rs485.available()){
        s=rs485.readStringUntil(KEY_TERMINATOR);
        if(s[0]==KEY_MSG){
            mode = MODE_MSG;
            s=s.substring(1);
        }
        else if(s[0]==KEY_CMD){
            mode = MODE_CMD;
            s=s.substring(1);
        }

        if(mode==MODE_MSG){
            println(s);
        }
        else if(mode==MODE_CMD){
            switch (s[0])
            {
            case 'Z':
                if(s[1]<'1' || s[1]>'3' || s[1]==zone+'0'+1) return;
                zone = static_cast<ZONES>(s[1]-'0'-1);
                println("Zone switch: "+String(zone+1));
                break;
            case 'L':   //LRGB;
                if(s.length()==4) zones[zone].LEDControl(s[1]-'0', s[2]-'0', s[3]-'0');
                break;
            case 'T':   //TRGB;
                if(s.length()==4) zones[zone].Tog(s[1]-'0', s[2]-'0', s[3]-'0');
                break;
            case 'X':   //X;
                if(s.length()==1) zones[zone].AllOff();
            default:
                break;
            }
        }
    } 
}