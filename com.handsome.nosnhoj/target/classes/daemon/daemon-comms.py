#!/usr/bin/env python

import time
import sys
import serial
import serial.tools.list_ports
import atexit

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

#sends message to arduino
def send_message(s="default message"):
    if(ser.is_open):
        try:
            ser.write(s)
            return "sent: " + s
        except:
            return "Could not write"
    else: 
        return "Port not opened"

#reads from arduino. using '\n' to indicate message over.
#problem with this is readline is blocking
#think this will be okay if function is ran through a thread?
def read_message():
    if(ser.is_open):
        try:
            s = ser.readline()
            #-1 to remove newline
            s=s[:-1]
            return str(s)
        except:
            return "Could not read"
    else:
        return "Port not opened"

def msg_dump():
    if(ser.is_open):
        try:
            ser.reset_input_buffer()
            return "input buffer cleared"
        except:
            return "Could not dump"
    else:
        return "Port not opened"

#opens port given port
def port_open(port='/dev/ttyUSB0', baud=9600):
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

#closes port. will auto close on exit as well
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

# if __name__=="__main__":
#     import threading
#     stop = threading.Event()
#     def threadtest():
#         while(True):
#             print(str(read_message()))
#             if(stop.is_set()):
#                 break
#     print(port_open())
#     print(msg_dump())
#     th = threading.Thread(target=threadtest)
#     th.start()
#     try:
#         while(1):
#             time.sleep(2)
#             print("hello")
#             pass
#     finally:
#         stop.set()

from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

sys.stdout.write(">>>>>MyDaemon daemon started")
sys.stderr.write(">>>>>MyDaemon daemon started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

#below sets up the rpc server. register functions so server can use them
server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(send_message, "send_message")
server.register_function(read_message, "read_message")
server.register_function(msg_dump, "msg_dump")
server.register_function(get_ports, "get_ports")
server.register_function(port_open, "port_open")
server.register_function(port_close, "port_close")
server.serve_forever()
