#!/usr/bin/env python

import time
import sys
import serial
import serial.tools.list_ports
import atexit

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

# ser = serial.Serial(port='/dev/ttyUSB0', baudrate=4800)
ser = serial.Serial()

#returns a string. have java convert to string array
def get_ports():
    ports = []
    comports = serial.tools.list_ports.comports()
    for p in comports:
        ports.append(str(p.device))  
    
    s = str(ports)
    # s=s.replace("'", '"')
    s=s.replace("'", '')
    s=s.replace(' ', '')
    s=s[1:-1]
    return s

def send_message(s="default message"):
    if(ser.is_open):
        try:
            ser.write(s)
            return "sent: " + s
        except:
            return "Could not write"
    else: 
        return "Port not opened"

def port_open(port='/dev/ttyUSB0', baud=4800):
    if(ser.is_open == True):
        port_close()
        time.sleep(0.5)
    ser.baudrate=baud
    ser.setPort(port)
    try:
        ser.open()
        time.sleep(1)
        return str(port)+" is opened"
    except:
        sys.stdout.write(">>>>> ERROR: Could not open port")
        return "Could not open port."

@atexit.register
def port_close():
    sys.stdout.write("Shutting down.\n")
    if(ser.is_open):
        try:
            ser.write("$Goodbye!\n$")
            ser.close()
            return "closed"
        except:
            return "could not close properly"

# print(get_ports())
# print(port_open())
# print(send_message())
# print(get_ports())

sys.stdout.write("MyDaemon daemon started")
sys.stderr.write("MyDaemon daemon started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass


#below sets up the rpc server. register functions so server can use them
server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(send_message, "send_message")
server.register_function(get_ports, "get_ports")
server.register_function(port_open, "port_open")
server.register_function(port_close, "port_close")
server.serve_forever()
