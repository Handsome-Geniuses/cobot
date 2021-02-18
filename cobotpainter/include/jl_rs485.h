#pragma once
#include <Arduino.h>
#include <SoftwareSerial.h>
#include "config.h"

class jl_rs485 : public SoftwareSerial{
    public:
        jl_rs485(byte pin_A, byte pin_B);
        void ReadWriteLoop();
    private:
};
extern jl_rs485 rs485;
