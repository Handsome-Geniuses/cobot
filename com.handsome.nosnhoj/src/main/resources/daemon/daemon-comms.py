#!/usr/bin/env python
# -*- coding: utf-8 -*-

import time
import sys
import wget
import serial
import string
import serial.tools.list_ports
import atexit
import threading

ser = serial.Serial()
read_string_buffer = "~"
stopper = threading.Event()

urcaps_url = "https://github.com/Handsome-Geniuses/cobot/raw/main/com.handsome.nosnhoj/target/nosnhoj-1.0-SNAPSHOT.urcap"
urcaps_fp = "/programs/jl/urcaps_download/nosnhoj.urcap"

def Urcaps_Update():
    return "downloaded" + wget.download(urcaps_url, urcaps_fp)

def get_time(): #returns float
    return time.time()

def string_contains(main_string, sub_string):
    if main_string.find(sub_string) != -1:
        return True
    else:
        return False

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

#running calls to this function in a thread doesn't work.
#the rpc is stuck here and can't execute other functions.
#store message in buffer. reset the buffer, return stored
#this way, only received once
def get_message():
    global read_string_buffer
    s = read_string_buffer
    read_string_buffer = "~"
    return s

def read_message():
    if(ser.is_open):
        try:
            s = ser.readline()
            #-1 to remove newline
            s=s[:-1]
            # s = ''.join(filter(lambda x: x in string.printable, s))
            s = ''.join(x for x in s if x in string.printable)
            return str(s)
        except:
            return "could not read for whatever reason"
    else:
        return "Port not opened doiiiii"

def msg_dump():
    if(ser.is_open):
        try:
            ser.flush()
            ser.reset_input_buffer()
            ser.reset_output_buffer() #out too arduino. don't want to accidently clear.
            get_message() #reset read_string_buffer
            get_message() #extra for sanity
            return "reset input, output, flush"
        except:
            return "Could not dump"
    else:
        return "Port not opened"

#checks if a port is open
def port_check():
    if(ser.is_open):
        return "opened"
    else:
        return "closed"


#opens port given port
def port_open(port='/dev/ttyUSB0', baud=9600):
    if(ser.is_open == True):
        # port_close()
        # time.sleep(0.5)
        return "Port already open. Close first to change."
    ser.baudrate=baud
    # ser.timeout=0.2
    ser.setPort(port)
    try:
        ser.open()
        time.sleep(1)
        return str(port)+" is opened!"
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
            return "nothing to close properly"

@atexit.register
def Kill_Thread():
    stopper.set()

def Thread_Reader():
    global read_string_buffer
    #wait till port has been opened
    while(ser.is_open == False):
        #may need to reset read_string_buffer to ~
        time.sleep(0.1)
    #main thread here
    while(not stopper.is_set()):
        #wait till someone grabs the message before grabbing more
        #buffer set to ~ when message is grabbed
        while(not stopper.is_set() and read_string_buffer!="~"):
            time.sleep(0.1)
        read_string_buffer=read_message()   #read till newline
       
        
thread_start = threading.Thread(target=Thread_Reader)
thread_start.start()

# ##print(port_open())
# try:
#     msg_dump()
#     while(True):
#         time.sleep(5)
#         print("connected" if port_check()=="t" else s"nope")
#         # msg = get_message()
#         # if(msg is not "~"):
#         #     if(msg == "hi"):
#         #         print("whatsup")
#         #     else:
#         #         print(msg)
# finally:
#     stopper.set()


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
server.register_function(get_message, "get_message")
server.register_function(msg_dump, "msg_dump")
server.register_function(get_ports, "get_ports")
server.register_function(port_check, "port_check")
server.register_function(port_open, "port_open")
server.register_function(port_close, "port_close")
server.register_function(string_contains, "string_contains")
server.register_function(get_time, "get_time")
server.register_function(Urcaps_Update, "Urcaps_Update")
server.serve_forever()


