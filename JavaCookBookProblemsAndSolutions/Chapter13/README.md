# Chapter 13: Server Side: Java

These techniques are required only for those who need or want to build their own server from the ground up.

## 13.1 Opening a Server Socket for Business

### Problem

You need to write a socket-based server.

### Solution

Create a ServerSocket for the given port number.

## 13.2 Finding Network Interfaces

### Problem

You wish to find out about the computer’s networking arrangements.

### Solution

Use the `NetworkInterface` class.

## 13.3 Returning a Response (String or Binary)

### Problem

You need to write a string or binary data to the client.

### Solution

Use the `InputStream` and `OutputStream` from the socket.

## 13.4 Returning Object Information Across a Network Connection

### Problem

You need to return an object across a network connection.

### Solution

Create the object you need, and write it using an `ObjectOutputStream` created on top of the socket’s output stream.
