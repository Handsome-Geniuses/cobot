#!/usr/bin/env python

import time
import sys
import serial


from SimpleXMLRPCServer import SimpleXMLRPCServer
from SocketServer import ThreadingMixIn

title = "My default title"

def set_title(s):
    global title
    title = s
    return title

def get_title():
    tmp = ""
    if str(title):
        tmp = title
    else:
		tmp = "No title set"
    return tmp + " (Python)"

def test_message():
    ser = serial.Serial(port='/dev/ttyACM0', baudrate=9600)
    try:
        ser.isOpen()
        time.sleep(2)
        ser.write("Hello ")
        time.sleep(2)
        ser.write("World\n")
    finally:
        ser.close()
    return "Hello World"

def send_message(s):
    ser = serial.Serial(port='/dev/ttyACM0', baudrate=9600)
    try:
        ser.isOpen()
        time.sleep(2)
        ser.write(s)
        time.sleep(2)
        ser.write(s)
    finally:
        ser.close()
    return "sent: " + s

test_message()
# send_message("yes mabye?")

sys.stdout.write("hello-daemon started")
sys.stderr.write("hello-daemon started")

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("127.0.0.1", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(set_title, "set_title")
server.register_function(get_title, "get_title")
# server.register_function(get_message, "get_message")
server.register_function(test_message, "test_message")
server.register_function(send_message, "send_message")
server.serve_forever()


