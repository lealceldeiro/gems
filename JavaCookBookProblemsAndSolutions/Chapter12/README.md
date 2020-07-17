# Chapter 12: Network Clients

## 12.1 HTTP/REST Web Client

### Problem

You need to read from a URL, for example, to connect to a RESTful web service or to download a web page or other resource over HTTP/HTTPS.

### Solution

Use the standard Java 11 `HttpClient` or the `URLConnection` class.

This technique applies anytime you need to read from a URL, not just a RESTful web service.
