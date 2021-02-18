#include <Arduino.h>
#include "jl_rs485.h"

void setup() {
    Serial.begin(9600);
    while(!Serial)
    Serial.setTimeout(200);
    rs485.begin(9600);
    while(!rs485);
}

void loop() {
    rs485.ReadWriteLoop();
}