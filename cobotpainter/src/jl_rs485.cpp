#include "jl_rs485.h"

jl_rs485 rs485(PIN_RS485_A, PIN_RS485_B);

jl_rs485::jl_rs485(byte pin_A, byte pin_B):SoftwareSerial(pin_A, pin_B){
}

void jl_rs485::ReadWriteLoop(){
    if(rs485.available()) Serial.write(rs485.read());
    if(Serial.available()) rs485.write(Serial.read());
}
