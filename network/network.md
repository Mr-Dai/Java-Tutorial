# Custom Networking

The Java platform is highly regarded in part because of its suitability for writing programs that use and interact with the resources on the Internet and the World Wide Web. In fact, Java-compatible browsers use this ability of the Java platform to the extreme to transport and run applets over the Internet.

This trail walks you through the complexities of writing Java applications and applets that can be used on the Internet.

[Overview of Networking](#overview-of-networking) has two sections. The first describes the networking capabilities of the Java platform that you may already be using without realizing that you are using the network. The second provides a brief overview of networking to familiarize you with terms and concepts that you should understand before reading how to use URLs, sockets, and datagrams.

[Working With URLs](#working-with-urls) discusses how your Java programs can use URLs to access information on the Internet. A URL (Uniform Resource Locator) is the address of a resource on the Internet. Your Java programs can use URLs to connect to and retrieve information over a network. This lesson provides a more complete definition of a URL and shows you how to create and parse a URL, how to open a connection to a URL, and how to read from and write to that connection.

[All About Sockets](#all-about-sockets) explains how to use sockets so that your programs can communicate with other programs on the network. A socket is one endpoint of a two-way communication link between two programs running on the network. This lesson shows you how a client can connect to a standard server, the Echo server, and communicate with it via a socket. It then walks you through the details of a complete client/server example, which shows you how to implement both the client side and the server side of a client/server pair.

[All About Datagrams](#all-about-datagrams) takes you step by step through a simple client/server example that uses datagrams to communicate. It then challenges you to rewrite the example using multicast socket instead.

[Programmatic Access to Network Parameters](#programmatic-access-to-network-parameters) explains why you might want to access network interface parameters and how to do so. It gives examples of how to list all the IP addresses assigned to the machine as well as other useful information such as whether the interface is running.

[Working With Cookies](#working-with-cookies) discusses how cookies are used to create a session between a client and server, and how you can take advantage of cookies in your HTTP URL connections.

> **Security considerations:**
>
> Note that communications over the network are subject to approval by the current security manager. [The Security Manager](http://docs.oracle.com/javase/tutorial/essential/environment/security.html) describes what a security manager is and how it impacts your applications. For general information about the security features provided by the JDK, refer to [Security Features in Java SE](http://docs.oracle.com/javase/tutorial/security/index.html).
>
> The example programs in the following lessons that cover URLs, sockets, and datagrams are standalone applications, which, by default, have no security manager. If you convert these applications to applets, they may be unable to communicate over the network, depending on the browser or viewer in which they are running. See [What Applets Can and Cannot Do](http://docs.oracle.com/javase/tutorial/deployment/applet/security.html) for information about the security restrictions placed on applets.

## Overview of Networking

Before plowing through the examples in the next several lessons, you should have an understanding of some networking basics. Also, to boost your confidence, we've included a section that reviews what you may already know about networking in Java without even realizing it.

**What You May Already Know About Networking in Java**

If you've been working on the other trails in this tutorial, you've probably loaded an applet over the Internet by now, and you've likely loaded images from the network into applets running over the network. All of this is using the network and you already know how to do it. This page highlights the networking features that are covered in other trails and lessons of this tutorial--features that you might already be familiar with--and provides links to the pages that discuss those features.

**Networking Basics**

You'll learn what you need to know about TCP, UDP, sockets, datagrams, and ports to get the most out of the remaining lessons in this trail. If you are already familiar with these concepts, feel free to skip this section.

### What You May Already Know About Networking in Java

The word *networking* strikes fear in the hearts of many programmers. Fear not! Using the networking capabilities provided in the Java environment is quite easy. In fact, you may be using the network already without even realizing it!

**Loading Applets from the Network**

If you have access to a Java-enabled browser, you have undoubtedly already executed many applets. The applets you've run are referenced by a special tag in an HTML file — the `<APPLET>` tag. Applets can be located anywhere, whether on your local machine or somewhere out on the Internet. The location of the applet is completely invisible to you, the user. However, the location of the applet is encoded within the `<APPLET>` tag. The browser decodes this information, locates the applet, and runs it. If the applet is on some machine other than your own, the browser must download the applet before it can be run.

This is the highest level of access that you have to the Internet from the Java development environment. Someone else has taken the time to write a browser that does all of the grunt work of connecting to the network and getting data from it, thereby enabling you to run applets from anywhere in the world.

**For more information:**
[The "Hello World!" Application](http://docs.oracle.com/javase/tutorial/getStarted/cupojava/index.html) shows you how to write your first applet and run it.
The [Java Applets](http://docs.oracle.com/javase/tutorial/deployment/applet/index.html) trail describes how to write Java applets from A to Z.

**Loading Images from URLs**

If you've ventured into writing your own Java applets and applications, you may have run into a class in the java.net package called URL. This class represents a Uniform Resource Locator and is the address of some resource on the network. Your applets and applications can use a URL to reference and even connect to resources out on the network. For example, to load an image from the network, your Java program must first create a URL that contains the address to the image.

This is the next highest level of interaction you can have with the Internet — your Java program gets an address of something it wants, creates a URL for it, and then uses some existing function in the Java development environment that does the grunt work of connecting to the network and retrieving the resource.

**For more information:**
[How to Use Icons](http://docs.oracle.com/javase/tutorial/uiswing/components/icon.html) shows you how to load an image into your Java program (whether applets or applications) when you have its URL. Before you can load the image you must create a URL object with the address of the resource in it.
[Working with URLs](http://docs.oracle.com/javase/tutorial/networking/urls/index.html), the next lesson in this trail, provides a complete discussion about URLs, including how your programs can connect to them and read from and write to that connection.

### Networking Basics

Computers running on the Internet communicate to each other using either the Transmission Control Protocol (TCP) or the User Datagram Protocol (UDP), as this diagram illustrates:

![](http://docs.oracle.com/javase/tutorial/figures/networking/1netw.gif)

When you write Java programs that communicate over the network, you are programming at the application layer. Typically, you don't need to concern yourself with the TCP and UDP layers. Instead, you can use the classes in the `java.net` package. These classes provide system-independent network communication. However, to decide which Java classes your programs should use, you do need to understand how TCP and UDP differ.

**TCP**

When two applications want to communicate to each other reliably, they establish a connection and send data back and forth over that connection. This is analogous to making a telephone call. If you want to speak to Aunt Beatrice in Kentucky, a connection is established when you dial her phone number and she answers. You send data back and forth over the connection by speaking to one another over the phone lines. Like the phone company, TCP guarantees that data sent from one end of the connection actually gets to the other end and in the same order it was sent. Otherwise, an error is reported.

TCP provides a point-to-point channel for applications that require reliable communications. The Hypertext Transfer Protocol (HTTP), File Transfer Protocol (FTP), and Telnet are all examples of applications that require a reliable communication channel. The order in which the data is sent and received over the network is critical to the success of these applications. When HTTP is used to read from a URL, the data must be received in the order in which it was sent. Otherwise, you end up with a jumbled HTML file, a corrupt zip file, or some other invalid information.

> **Definition:**
>
> *TCP* (*Transmission Control Protocol*) is a connection-based protocol that provides a reliable flow of data between two computers.

**UDP**

The UDP protocol provides for communication that is not guaranteed between two applications on the network. UDP is not connection-based like TCP. Rather, it sends independent packets of data, called datagrams, from one application to another. Sending datagrams is much like sending a letter through the postal service: The order of delivery is not important and is not guaranteed, and each message is independent of any other.

> **Definition:**
>
> *UDP* (*User Datagram Protocol*) is a protocol that sends independent packets of data, called datagrams, from one computer to another with no guarantees about arrival. UDP is not connection-based like TCP.

For many applications, the guarantee of reliability is critical to the success of the transfer of information from one end of the connection to the other. However, other forms of communication don't require such strict standards. In fact, they may be slowed down by the extra overhead or the reliable connection may invalidate the service altogether.

Consider, for example, a clock server that sends the current time to its client when requested to do so. If the client misses a packet, it doesn't really make sense to resend it because the time will be incorrect when the client receives it on the second try. If the client makes two requests and receives packets from the server out of order, it doesn't really matter because the client can figure out that the packets are out of order and make another request. The reliability of TCP is unnecessary in this instance because it causes performance degradation and may hinder the usefulness of the service.

Another example of a service that doesn't need the guarantee of a reliable channel is the ping command. The purpose of the ping command is to test the communication between two programs over the network. In fact, ping needs to know about dropped or out-of-order packets to determine how good or bad the connection is. A reliable channel would invalidate this service altogether.

The UDP protocol provides for communication that is not guaranteed between two applications on the network. UDP is not connection-based like TCP. Rather, it sends independent packets of data from one application to another. Sending datagrams is much like sending a letter through the mail service: The order of delivery is not important and is not guaranteed, and each message is independent of any others.

> **Note:**
>
> Many firewalls and routers have been configured not to allow UDP packets. If you're having trouble connecting to a service outside your firewall, or if clients are having trouble connecting to your service, ask your system administrator if UDP is permitted.

**Understanding Ports**

Generally speaking, a computer has a single physical connection to the network. All data destined for a particular computer arrives through that connection. However, the data may be intended for different applications running on the computer. So how does the computer know to which application to forward the data? Through the use of *ports*.

Data transmitted over the Internet is accompanied by addressing information that identifies the computer and the port for which it is destined. The computer is identified by its 32-bit IP address, which IP uses to deliver data to the right computer on the network. Ports are identified by a 16-bit number, which TCP and UDP use to deliver the data to the right application.

In connection-based communication such as TCP, a server application binds a socket to a specific port number. This has the effect of registering the server with the system to receive all data destined for that port. A client can then rendezvous with the server at the server's port, as illustrated here:

![](http://docs.oracle.com/javase/tutorial/figures/networking/2tcp.gif)

> **Definition:**
>
> The TCP and UDP protocols use ports to map incoming data to a particular process running on a computer.

In datagram-based communication such as UDP, the datagram packet contains the port number of its destination and UDP routes the packet to the appropriate application, as illustrated in this figure:

![](http://docs.oracle.com/javase/tutorial/figures/networking/3tcpudp.gif)

Port numbers range from 0 to 65,535 because ports are represented by 16-bit numbers. The port numbers ranging from 0 - 1023 are restricted; they are reserved for use by well-known services such as HTTP and FTP and other system services. These ports are called *well-known* ports. Your applications should not attempt to bind to them.

**Networking Classes in the JDK**

Through the classes in `java.net`, Java programs can use TCP or UDP to communicate over the Internet. The `URL`, `URLConnection`, `Socket`, and `ServerSocket` classes all use TCP to communicate over the network. The `DatagramPacket`, `DatagramSocket`, and `MulticastSocket` classes are for use with UDP.

## Working with URLs

URL is the acronym for Uniform Resource Locator. It is a reference (an address) to a resource on the Internet. You provide URLs to your favorite Web browser so that it can locate files on the Internet in the same way that you provide addresses on letters so that the post office can locate your correspondents.

Java programs that interact with the Internet also may use URLs to find the resources on the Internet they wish to access. Java programs can use a class called `URL` in the `java.net` package to represent a URL address.

> **Terminology Note:**
>
> The term *URL* can be ambiguous. It can refer to an Internet address or a `URL` object in a Java program. Where the meaning of URL needs to be specific, this text uses "URL address" to mean an Internet address and "`URL` object" to refer to an instance of the URL class in a program.

### What Is a URL?

If you've been surfing the Web, you have undoubtedly heard the term URL and have used URLs to access HTML pages from the Web.

It's often easiest, although not entirely accurate, to think of a URL as the name of a file on the World Wide Web because most URLs refer to a file on some machine on the network. However, remember that URLs also can point to other resources on the network, such as database queries and command output.

> **Definition:**
>
> URL is an acronym for *Uniform Resource Locator* and is a reference (an address) to a resource on the Internet.

A URL has two main components:

- Protocol identifier: For the URL `http://example.com`, the protocol identifier is `http`.
- Resource name: For the URL `http://example.com`, the resource name is `example.com`.

Note that the protocol identifier and the resource name are separated by a colon and two forward slashes. The protocol identifier indicates the name of the protocol to be used to fetch the resource. The example uses the Hypertext Transfer Protocol (HTTP), which is typically used to serve up hypertext documents. HTTP is just one of many different protocols used to access different types of resources on the net. Other protocols include File Transfer Protocol (FTP), Gopher, File, and News.

The resource name is the complete address to the resource. The format of the resource name depends entirely on the protocol used, but for many protocols, including HTTP, the resource name contains one or more of the following components:

<dl>
<dt><strong>Host Name</strong></dt>
<dd>The name of the machine on which the resource lives.</dd>
<dt><strong>Filename</strong></dt>
<dd>The pathname to the file on the machine.</dd>
<dt><strong>Port Number</strong></dt>
<dd>The port number to which to connect (typically optional).</dd>
<dt><strong>Reference</strong></dt>
<dd>A reference to a named anchor within a resource that usually identifies a specific location within a file (typically optional).</dd>
</dl>

For many protocols, the host name and the filename are required, while the port number and reference are optional. For example, the resource name for an HTTP URL must specify a server on the network (Host Name) and the path to the document on that machine (Filename); it also can specify a port number and a reference.

### Creating a URL

The easiest way to create a `URL` object is from a `String` that represents the human-readable form of the URL address. This is typically the form that another person will use for a URL. In your Java program, you can use a `String` containing this text to create a `URL` object:

```java
URL myURL = new URL("http://example.com/");
```

The `URL` object created above represents an *absolute URL*. An absolute URL contains all of the information necessary to reach the resource in question. You can also create `URL` objects from a *relative URL* address.

**Creating a URL Relative to Another**

A relative URL contains only enough information to reach the resource relative to (or in the context of) another URL.

Relative URL specifications are often used within HTML files. For example, suppose you write an HTML file called `JoesHomePage.html`. Within this page, are links to other pages, `PicturesOfMe.html` and `MyKids.html`, that are on the same machine and in the same directory as `JoesHomePage.html`. The links to `PicturesOfMe.html` and `MyKids.html` from `JoesHomePage.html` could be specified just as filenames, like this:

```html
<a href="PicturesOfMe.html">Pictures of Me</a>
<a href="MyKids.html">Pictures of My Kids</a>
```

These URL addresses are *relative URLs*. That is, the URLs are specified relative to the file in which they are contained — `JoesHomePage.html`.

In your Java programs, you can create a `URL` object from a relative URL specification. For example, suppose you know two URLs at the site `example.com`:

```
http://example.com/pages/page1.html
http://example.com/pages/page2.html
```

You can create `URL` objects for these pages relative to their common base URL: `http://example.com/pages/` like this:

```java
URL myURL = new URL("http://example.com/pages/");
URL page1URL = new URL(myURL, "page1.html");
URL page2URL = new URL(myURL, "page2.html");
```

This code snippet uses the URL constructor that lets you create a `URL` object from another URL object (the base) and a relative URL specification. The general form of this constructor is:

```
URL(URL baseURL, String relativeURL)
```

The first argument is a `URL` object that specifies the base of the new `URL`. The second argument is a `String` that specifies the rest of the resource name relative to the base. If `baseURL` is `null`, then this constructor treats `relativeURL` like an absolute URL specification. Conversely, if `relativeURL` is an absolute URL specification, then the constructor ignores `baseURL`.

This constructor is also useful for creating `URL` objects for named anchors (also called references) within a file. For example, suppose the `page1.html` file has a named anchor called `BOTTOM` at the bottom of the file. You can use the relative URL constructor to create a `URL` object for it like this:

```java
URL page1BottomURL = new URL(page1URL,"#BOTTOM");
```

**Other URL Constructors**

The `URL` class provides two additional constructors for creating a `URL` object. These constructors are useful when you are working with URLs, such as HTTP URLs, that have host name, filename, port number, and reference components in the resource name portion of the URL. These two constructors are useful when you do not have a String containing the complete URL specification, but you do know various components of the URL.

For example, suppose you design a network browsing panel similar to a file browsing panel that allows users to choose the protocol, host name, port number, and filename. You can construct a `URL` from the panel's components. The first constructor creates a `URL` object from a protocol, host name, and filename. The following code snippet creates a `URL` to the `page1.html` file at the `example.com` site:

```java
new URL("http", "example.com", "/pages/page1.html");
```

This is equivalent to

```java
new URL("http://example.com/pages/page1.html");
```

The first argument is the protocol, the second is the host name, and the last is the pathname of the file. Note that the filename contains a forward slash at the beginning. This indicates that the filename is specified from the root of the host.

The final `URL` constructor adds the port number to the list of arguments used in the previous constructor:

```java
URL gamelan = new URL("http", "example.com", 80, "pages/page1.html"); 
```

This creates a `URL` object for the following URL:

```
http://example.com:80/pages/page1.html
```

If you construct a `URL` object using one of these constructors, you can get a `String` containing the complete URL address by using the `URL` object's `toString` method or the equivalent `toExternalForm` method.

**URL addresses with Special characters**

Some URL addresses contain special characters, for example the space character. Like this:

```
http://example.com/hello world/
```

To make these characters legal they need to be encoded before passing them to the URL constructor.

```java
URL url = new URL("http://example.com/hello%20world");
```

Encoding the special character(s) in this example is easy as there is only one character that needs encoding, but for URL addresses that have several of these characters or if you are unsure when writing your code what URL addresses you will need to access, you can use the multi-argument constructors of the `java.net.URI` class to automatically take care of the encoding for you.

```java
URI uri = new URI("http", "example.com", "/hello world/", "");
```

And then convert the URI to a URL.

```java
URL url = uri.toURL();
```

**MalformedURLException**

Each of the four `URL` constructors throws a `MalformedURLException` if the arguments to the constructor refer to a `null` or unknown protocol. Typically, you want to catch and handle this exception by embedding your URL constructor statements in a `try/catch` pair, like this:

```java
try {
    URL myURL = new URL(...);
} 
catch (MalformedURLException e) {
    // exception handler code here
    // ...
}
```

See [Exceptions](http://docs.oracle.com/javase/tutorial/essential/exceptions/index.html) for information about handling exceptions.

> **Note:**
>
> `URL`s are "write-once" objects. Once you've created a URL object, you cannot change any of its attributes (protocol, host name, filename, or port number).

### Parsing a URL

The `URL` class provides several methods that let you query `URL` objects. You can get the protocol, authority, host name, port number, path, query, filename, and reference from a URL using these accessor methods:

<dl>
<dt><strong><code>getProtocol</code></strong></dt>
<dd>Returns the protocol identifier component of the URL.</dd>
<dt><strong><code>getAuthority</code></strong></dt>
<dd>Returns the authority component of the URL.</dd>
<dt><strong><code>getHost</code></strong></dt>
<dd>Returns the host name component of the URL.</dd>
<dt><strong><code>getPort</code></strong></dt>
<dd>Returns the port number component of the URL. The <code>getPort</code> method returns an integer that is the port number. If the port is not set, <code>getPort</code> returns -1.</dd>
<dt><strong><code>getPath</code></strong></dt>
<dd>Returns the path component of this URL.</dd>
<dt><strong><code>getQuery</code></strong></dt>
<dd>Returns the query component of this URL.</dd>
<dt><strong><code>getFile</code></strong></dt>
<dd>Returns the filename component of the URL. The <code>getFile</code> method returns the same as <code>getPath</code>, plus the concatenation of the value of <code>getQuery</code>, if any.</dd>
<dt><strong><code>getRef</code></strong></dt>
<dd>Returns the reference component of the URL.</dd>
</dl>

> **Note:**
>
> Remember that not all URL addresses contain these components. The URL class provides these methods because HTTP URLs do contain these components and are perhaps the most commonly used URLs. The URL class is somewhat HTTP-centric.

You can use these `getXXX` methods to get information about the URL regardless of the constructor that you used to create the URL object.

The URL class, along with these accessor methods, frees you from ever having to parse URLs again! Given any string specification of a URL, just create a new URL object and call any of the accessor methods for the information you need. This small example program creates a URL from a string specification and then uses the URL object's accessor methods to parse the URL:

```java
import java.net.*;
import java.io.*;

public class ParseURL {
    public static void main(String[] args) throws Exception {

        URL aURL = new URL("http://example.com:80/docs/books/tutorial"
                           + "/index.html?name=networking#DOWNLOADING");

        System.out.println("protocol = " + aURL.getProtocol());
        System.out.println("authority = " + aURL.getAuthority());
        System.out.println("host = " + aURL.getHost());
        System.out.println("port = " + aURL.getPort());
        System.out.println("path = " + aURL.getPath());
        System.out.println("query = " + aURL.getQuery());
        System.out.println("filename = " + aURL.getFile());
        System.out.println("ref = " + aURL.getRef());
    }
}
```

Here is the output displayed by the program:

```
protocol = http
authority = example.com:80
host = example.com
port = 80
path = /docs/books/tutorial/index.html
query = name=networking
filename = /docs/books/tutorial/index.html?name=networking
ref = DOWNLOADING
```

### Reading Directly from a URL

After you've successfully created a `URL`, you can call the `URL`'s `openStream()` method to get a stream from which you can read the contents of the URL. The `openStream()` method returns a `java.io.InputStream` object, so reading from a `URL` is as easy as reading from an input stream.

The following small Java program uses `openStream()` to get an input stream on the URL `http://www.oracle.com/`. It then opens a `BufferedReader` on the input stream and reads from the `BufferedReader` thereby reading from the URL. Everything read is copied to the standard output stream:

```java
import java.net.*;
import java.io.*;

public class URLReader {
    public static void main(String[] args) throws Exception {

        URL oracle = new URL("http://www.oracle.com/");
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }
}
```

When you run the program, you should see, scrolling by in your command window, the HTML commands and textual content from the HTML file located at `http://www.oracle.com/`. Alternatively, the program might hang or you might see an exception stack trace. If either of the latter two events occurs, you may have to [set the proxy host](http://docs.oracle.com/javase/tutorial/networking/urls/_setProxy.html) so that the program can find the Oracle server.

### Connecting to a URL

After you've successfully created a `URL` object, you can call the `URL` object's `openConnection` method to get a `URLConnection` object, or one of its protocol specific subclasses, e.g. `java.net.HttpURLConnection`.

You can use this `URLConnection` object to setup parameters and general request properties that you may need before connecting. Connection to the remote object represented by the URL is only initiated when the `URLConnection.connect` method is called. When you do this you are initializing a communication link between your Java program and the URL over the network. For example, the following code opens a connection to the site `example.com`:

```java
try {
    URL myURL = new URL("http://example.com/");
    URLConnection myURLConnection = myURL.openConnection();
    myURLConnection.connect();
} 
catch (MalformedURLException e) { 
    // new URL() failed
    // ...
} 
catch (IOException e) {   
    // openConnection() failed
    // ...
}
```

A new `URLConnection` object is created every time by calling the `openConnection` method of the protocol handler for this URL.

You are not always required to explicitly call the `connect` method to initiate the connection. Operations that depend on being connected, like `getInputStream`, `getOutputStream`, etc, will implicitly perform the connection, if necessary.

Now that you've successfully connected to your URL, you can use the `URLConnection` object to perform actions such as reading from or writing to the connection. The next section shows you how.

### Reading from and Writing to a URLConnection

The `URLConnection` class contains many methods that let you communicate with the URL over the network. `URLConnection` is an HTTP-centric class; that is, many of its methods are useful only when you are working with HTTP URLs. However, most URL protocols allow you to read from and write to the connection. This section describes both functions.

**Reading from a URLConnection**

The following program performs the same function as the `URLReader` program shown in [Reading Directly from a URL](#reading-directly-from-a-url).

However, rather than getting an input stream directly from the URL, this program explicitly retrieves a `URLConnection` object and gets an input stream from the connection. The connection is opened implicitly by calling `getInputStream`. Then, like `URLReader`, this program creates a `BufferedReader` on the input stream and reads from it. The bold statements highlight the differences between this example and the previous:

```java
import java.net.*;
import java.io.*;

public class URLConnectionReader {
    public static void main(String[] args) throws Exception {
        URL oracle = new URL("http://www.oracle.com/");
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                    yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
    }
}
```

The output from this program is identical to the output from the program that opens a stream directly from the URL. You can use either way to read from a URL. However, reading from a `URLConnection` instead of reading directly from a URL might be more useful. This is because you can use the `URLConnection` object for other tasks (like writing to the URL) at the same time.

Again, if the program hangs or you see an error message, you may have to set the proxy host so that the program can find the Oracle server.

**Writing to a URLConnection**

Many HTML pages contain *forms* — text fields and other GUI objects that let you enter data to send to the server. After you type in the required information and initiate the query by clicking a button, your Web browser writes the data to the URL over the network. At the other end the server receives the data, processes it, and then sends you a response, usually in the form of a new HTML page.

Many of these HTML forms use the HTTP POST METHOD to send data to the server. Thus writing to a URL is often called *posting to a URL*. The server recognizes the POST request and reads the data sent from the client.

For a Java program to interact with a server-side process it simply must be able to write to a URL, thus providing data to the server. It can do this by following these steps:

1. Create a `URL`.
2. Retrieve the `URLConnection` object.
3. Set output capability on the `URLConnection`.
4. Open a connection to the resource.
5. Get an output stream from the connection.
6. Write to the output stream.
7. Close the output stream.

Here is a small `servlet` named [ReverseServlet](http://docs.oracle.com/javase/tutorial/networking/urls/examples/ReverseServlet.java) (or if you prefer a [cgi-bin](http://docs.oracle.com/javase/tutorial/networking/urls/examples/backwards) script). You can use this servlet to test the following example program.

The servlet running in a container reads from its InputStream, reverses the string, and writes it to its OutputStream. The servlet requires input of the form `string=string_to_reverse`, where `string_to_reverse` is the string whose characters you want displayed in reverse order.

Here's an example program that runs the `ReverseServlet` over the network through a `URLConnection`:

```java
import java.io.*;
import java.net.*;

public class Reverse {
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage:  java Reverse "
                + "http://<location of your servlet/script>"
                + " string_to_reverse");
            System.exit(1);
        }

        String stringToReverse = URLEncoder.encode(args[1], "UTF-8");

        URL url = new URL(args[0]);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(
                                         connection.getOutputStream());
        out.write("string=" + stringToReverse);
        out.close();

        BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                    connection.getInputStream()));
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            System.out.println(decodedString);
        }
        in.close();
    }
}
```

Let's examine the program and see how it works. First, the program processes its command-line arguments:

```java
if (args.length != 2) {
    System.err.println("Usage:  java Reverse "
        + "http://<location of your servlet/script>"
        + " string_to_reverse");
    System.exit(1);
}       

String stringToReverse = URLEncoder.encode(args[1], "UTF-8");
```

These statements ensure that the user provides two and only two command-line arguments to the program. The command-line arguments are the location of the `ReverseServlet` and the string that will be reversed. It may contain spaces or other non-alphanumeric characters. These characters must be encoded because the string is processed on its way to the server. The `URLEncoder` class methods encode the characters.

Next, the program creates the `URL` object, and sets the connection so that it can write to it:

```java
URL url = new URL(args[0]);
URLConnection connection = url.openConnection();
connection.setDoOutput(true);
```

The program then creates an output stream on the connection and opens an `OutputStreamWriter` on it:

```java
OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
```

If the URL does not support output, `getOutputStream` method throws an `UnknownServiceException`. If the URL does support output, then this method returns an output stream that is connected to the input stream of the URL on the server side — the client's output is the server's input.

Next, the program writes the required information to the output stream and closes the stream:

```java
out.write("string=" + stringToReverse);
out.close();
```

This code writes to the output stream using the `write` method. So you can see that writing data to a URL is as easy as writing data to a stream. The data written to the output stream on the client side is the input for the servlet on the server side. The `Reverse` program constructs the input in the form required by the script by prepending `string=` to the encoded string to be reversed.

The servlet reads the information you write, performs a reverse operation on the string value, and then sends this back to you. You now need to read the string the server has sent back. The `Reverse` program does it like this:

```java
BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                            connection.getInputStream()));
String decodedString;
while ((decodedString = in.readLine()) != null) {
    System.out.println(decodedString);
}
in.close();
```

If your `ReverseServlet` is located at `http://example.com/servlet/ReverseServlet`, then when you run the `Reverse` program using

```
http://example.com/servlet/ReverseServlet "Reverse Me"
```

as the argument (including the double quote marks), you should see this output:

```
Reverse Me
 reversed is: 
eM esreveR
```

## All About Sockets
