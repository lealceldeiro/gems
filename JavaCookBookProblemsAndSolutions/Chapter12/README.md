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
