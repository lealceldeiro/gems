# Chapter 12: Network Clients

## 12.1 HTTP/REST Web Client

### Problem

You need to read from a URL, for example, to connect to a RESTful web service or to download a web page or other resource over HTTP/HTTPS.

### Solution

Use the standard Java 11 `HttpClient` or the `URLConnection` class.

This technique applies anytime you need to read from a URL, not just a RESTful web service.

## 12.2 Contacting a Socket Server

### Problem

You need to contact a server using TCP/IP.

### Solution

Just create a `java.net.Socket`, passing the hostname and port number into the constructor.

## 12.3 Finding and Reporting Network Addresses

### Problem

You want to look up a host’s address name or number or get the address at the other end of a network connection.

### Solution

Get an `InetAddress` object.

## 12.5 Reading and Writing Textual Data

### Problem

Having connected, you wish to transfer textual data.

### Solution

Construct a `BufferedReader` or `PrintWriter` from the socket’s `getInputStream()` or `getOutputStream()`.

Example:

```java
BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
```

## 12.6 Reading and Writing Binary or Serialized Data

### Problem

Having connected, you wish to transfer binary data, either raw binary data or serialized Java objects.

### Solution

For plain binary data, construct a `DataInputStream` or `DataOutputStream` from the socket’s `getInputStream()` or `getOutputStream()`. For serialized Java object data, construct an `ObjectInputStream` or `ObjectOutputStream`.

If the volume of data might be large, insert a buffered stream for efficiency:

```java
DataInputStream is = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
DataOutputStream is = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
```

## 12.7 UDP Datagrams

### Problem

You need to use a datagram connection (UDP) instead of a stream connection (TCP).

### Solution

Use `DatagramSocket` and `DatagramPacket`.
