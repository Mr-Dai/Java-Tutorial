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

`URL`s and `URLConnection`s provide a relatively high-level mechanism for accessing resources on the Internet. Sometimes your programs require lower-level network communication, for example, when you want to write a client-server application.

In client-server applications, the server provides some service, such as processing database queries or sending out current stock prices. The client uses the service provided by the server, either displaying database query results to the user or making stock purchase recommendations to an investor. The communication that occurs between the client and the server must be reliable. That is, no data can be dropped and it must arrive on the client side in the same order in which the server sent it.

TCP provides a reliable, point-to-point communication channel that client-server applications on the Internet use to communicate with each other. To communicate over TCP, a client program and a server program establish a connection to one another. Each program binds a socket to its end of the connection. To communicate, the client and the server each reads from and writes to the socket bound to the connection.

### What Is a Socket?

Normally, a server runs on a specific computer and has a socket that is bound to a specific port number. The server just waits, listening to the socket for a client to make a connection request.

On the client-side: The client knows the hostname of the machine on which the server is running and the port number on which the server is listening. To make a connection request, the client tries to rendezvous with the server on the server's machine and port. The client also needs to identify itself to the server so it binds to a local port number that it will use during this connection. This is usually assigned by the system.

![](http://docs.oracle.com/javase/tutorial/figures/networking/5connect.gif)

If everything goes well, the server accepts the connection. Upon acceptance, the server gets a new socket bound to the same local port and also has its remote endpoint set to the address and port of the client. It needs a new socket so that it can continue to listen to the original socket for connection requests while tending to the needs of the connected client.

![](http://docs.oracle.com/javase/tutorial/figures/networking/6connect.gif)

On the client side, if the connection is accepted, a socket is successfully created and the client can use the socket to communicate with the server.

The client and server can now communicate by writing to or reading from their sockets.

> **Definition:**
>
> A *socket* is one endpoint of a two-way communication link between two programs running on the network. A socket is bound to a port number so that the TCP layer can identify the application that data is destined to be sent to.

An endpoint is a combination of an IP address and a port number. Every TCP connection can be uniquely identified by its two endpoints. That way you can have multiple connections between your host and the server.

The `java.net` package in the Java platform provides a class, `Socket`, that implements one side of a two-way connection between your Java program and another program on the network. The `Socket` class sits on top of a platform-dependent implementation, hiding the details of any particular system from your Java program. By using the `java.net.Socket` class instead of relying on native code, your Java programs can communicate over the network in a platform-independent fashion.

Additionally, `java.net` includes the `ServerSocket` class, which implements a socket that servers can use to listen for and accept connections to clients. This lesson shows you how to use the `Socket` and `ServerSocket` classes.

If you are trying to connect to the Web, the `URL` class and related classes (`URLConnection`, `URLEncoder`) are probably more appropriate than the socket classes. In fact, URLs are a relatively high-level connection to the Web and use sockets as part of the underlying implementation. See [Working with URLs](#working-with-urls) for information about connecting to the Web via URLs.

### Reading from and Writing to a Socket

Let's look at a simple example that illustrates how a program can establish a connection to a server program using the `Socket` class and then, how the client can send data to and receive data from the server through the socket.

The example program implements a client, [EchoClient](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoClient.java), that connects to an echo server. The echo server receives data from its client and echoes it back. The example [EchoServer](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoServer.java) implements an echo server. (Alternatively, the client can connect to any host that supports the Echo Protocol.)

The `EchoClient` example creates a socket, thereby getting a connection to the echo server. It reads input from the user on the standard input stream, and then forwards that text to the echo server by writing the text to the socket. The server echoes the input back through the socket to the client. The client program reads and displays the data passed back to it from the server.

Note that the `EchoClient` example both writes to and reads from its socket, thereby sending data to and receiving data from the echo server.

Let's walk through the program and investigate the interesting parts. The following statements in the [try-with-resources](http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html) statement in the `EchoClient` example are critical. These lines establish the socket connection between the client and the server and open a `PrintWriter` and a `BufferedReader` on the socket:

```java
String hostName = args[0];
int portNumber = Integer.parseInt(args[1]);

try (
    Socket echoSocket = new Socket(hostName, portNumber);
    PrintWriter out =
        new PrintWriter(echoSocket.getOutputStream(), true);
    BufferedReader in =
        new BufferedReader(
            new InputStreamReader(echoSocket.getInputStream()));
    BufferedReader stdIn =
        new BufferedReader(
            new InputStreamReader(System.in))
)
```

The first statement in the try-with resources statement creates a new `Socket` object and names it `echoSocket`. The `Socket` constructor used here requires the name of the computer and the port number to which you want to connect. The example program uses the first [command-line argument](http://docs.oracle.com/javase/tutorial/essential/environment/cmdLineArgs.html) as the name of the computer (the host name) and the second command line argument as the port number. When you run this program on your computer, make sure that the host name you use is the fully qualified IP name of the computer to which you want to connect. For example, if your echo server is running on the computer `echoserver.example.com` and it is listening on port number 7, first run the following command from the computer `echoserver.example.com` if you want to use the `EchoServer` example as your echo server:

```
java EchoServer 7
```

Afterward, run the `EchoClient` example with the following command:

```
java EchoClient echoserver.example.com 7
```

The second statement in the try-with resources statement gets the socket's output stream and opens a `PrintWriter` on it. Similarly, the third statement gets the socket's input stream and opens a BufferedReader on it. The example uses readers and writers so that it can write Unicode characters over the socket.

To send data through the socket to the server, the `EchoClient` example needs to write to the `PrintWriter`. To get the server's response, `EchoClient` reads from the `BufferedReader` object `stdIn`, which is created in the fourth statement in the try-with resources statement. If you are not yet familiar with the Java platform's I/O classes, you may wish to read [Basic I/O](https://github.com/Mr-Dai/Java-Tutorial/blob/master/io/io.md).

The next interesting part of the program is the `while` loop. The loop reads a line at a time from the standard input stream and immediately sends it to the server by writing it to the `PrintWriter` connected to the socket:

```java
String userInput;
while ((userInput = stdIn.readLine()) != null) {
    out.println(userInput);
    System.out.println("echo: " + in.readLine());
}
```

The last statement in the `while` loop reads a line of information from the `BufferedReader` connected to the socket. The `readLine` method waits until the server echoes the information back to `EchoClient`. When `readline` returns, `EchoClient` prints the information to the standard output.

The `while` loop continues until the user types an end-of-input character. That is, the `EchoClient` example reads input from the user, sends it to the Echo server, gets a response from the server, and displays it, until it reaches the end-of-input. (You can type an end-of-input character by pressing **Ctrl-C**.) The `while` loop then terminates, and the Java runtime automatically closes the readers and writers connected to the socket and to the standard input stream, and it closes the socket connection to the server. The Java runtime closes these resources automatically because they were created in the try-with-resources statement. The Java runtime closes these resources in reverse order that they were created. (This is good because streams connected to a socket should be closed before the socket itself is closed.)

This client program is straightforward and simple because the echo server implements a simple protocol. The client sends text to the server, and the server echoes it back. When your client programs are talking to a more complicated server such as an HTTP server, your client program will also be more complicated. However, the basics are much the same as they are in this program:

1. Open a socket.
2. Open an input stream and output stream to the socket.
3. Read from and write to the stream according to the server's protocol.
4. Close the streams.
5. Close the socket.

Only step 3 differs from client to client, depending on the server. The other steps remain largely the same.

This section shows you how to write a server and the client that goes with it. The server in the client/server pair serves up Knock Knock jokes. Knock Knock jokes are favored by children and are usually vehicles for bad puns. They go like this:

**Server**: "Knock knock!"
**Client**: "Who's there?"
**Server**: "Dexter."
**Client**: "Dexter who?"
**Server**: "Dexter halls with boughs of holly."
**Client**: "Groan."

The example consists of two independently running Java programs: the client program and the server program. The client program is implemented by a single class, [KnockKnockClient](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockClient.java), and is very similar to the [EchoClient](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/EchoClient.java) example from the previous section. The server program is implemented by two classes: [KnockKnockServer](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockServer.java) and [KnockKnockProtocol](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockProtocol.java). `KnockKnockServer`, which is similar to `EchoServer`, contains the main method for the server program and performs the work of listening to the port, establishing connections, and reading from and writing to the socket. The class `KnockKnockProtocol` serves up the jokes. It keeps track of the current joke, the current state (sent knock knock, sent clue, and so on), and returns the various text pieces of the joke depending on the current state. This object implements the protocol—the language that the client and server have agreed to use to communicate.

The following section looks in detail at each class in both the client and the server and then shows you how to run them.

**The Knock Knock Server**

This section walks through the code that implements the Knock Knock server program, `KnockKnockServer`.

The server program begins by creating a new `ServerSocket` object to listen on a specific port (see the statement in bold in the following code segment). When running this server, choose a port that is not already dedicated to some other service. For example, this command starts the server program `KnockKnockServer` so that it listens on port 4444:

```
java KnockKnockServer 4444
```

The server program creates the `ServerSocket` object in a try-with-resources statement:

```java
int portNumber = Integer.parseInt(args[0]);

try ( 
    ServerSocket serverSocket = new ServerSocket(portNumber);
    Socket clientSocket = serverSocket.accept();
    PrintWriter out =
        new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
) {
```

`ServerSocket` is a `java.net` class that provides a system-independent implementation of the server side of a client/server socket connection. The constructor for `ServerSocket` throws an exception if it can't listen on the specified port (for example, the port is already being used). In this case, the `KnockKnockServer` has no choice but to exit.

If the server successfully binds to its port, then the `ServerSocket` object is successfully created and the server continues to the next step—accepting a connection from a client (the next statement in the try-with-resources statement):

```java
clientSocket = serverSocket.accept();
```

The `accept` method waits until a client starts up and requests a connection on the host and port of this server. (Let's assume that you ran the server program `KnockKnockServer` on the computer named `knockknockserver.example.com`.) In this example, the server is running on the port number specified by the first command-line argument. When a connection is requested and successfully established, the accept method returns a new `Socket` object which is bound to the same local port and has its remote address and remote port set to that of the client. The server can communicate with the client over this new `Socket` and continue to listen for client connection requests on the original `ServerSocket` This particular version of the program doesn't listen for more client connection requests. However, a modified version of the program is provided in [Supporting Multiple Clients](#supporting-multiple-clients).

After the server successfully establishes a connection with a client, it communicates with the client using this code:

```java
try (
    // ...
    PrintWriter out =
        new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));
) {
    String inputLine, outputLine;
            
    // Initiate conversation with client
    KnockKnockProtocol kkp = new KnockKnockProtocol();
    outputLine = kkp.processInput(null);
    out.println(outputLine);

    while ((inputLine = in.readLine()) != null) {
        outputLine = kkp.processInput(inputLine);
        out.println(outputLine);
        if (outputLine.equals("Bye."))
            break;
    }
```

This code does the following:

1. Gets the socket's input and output stream and opens readers and writers on them.
2. Initiates communication with the client by writing to the socket (shown in bold).
3. Communicates with the client by reading from and writing to the socket (the `while` loop).

Step 1 is already familiar. Step 2 is shown in bold and is worth a few comments. The bold statements in the code segment above initiate the conversation with the client. The code creates a `KnockKnockProtocol` object—the object that keeps track of the current joke, the current state within the joke, and so on.

After the `KnockKnockProtocol` is created, the code calls `KnockKnockProtocol`'s `processInput` method to get the first message that the server sends to the client. For this example, the first thing that the server says is "Knock! Knock!" Next, the server writes the information to the `PrintWriter` connected to the client socket, thereby sending the message to the client.

Step 3 is encoded in the `while` loop. As long as the client and server still have something to say to each other, the server reads from and writes to the socket, sending messages back and forth between the client and the server.

The server initiated the conversation with a "Knock! Knock!" so afterwards the server must wait for the client to say "Who's there?" As a result, the while loop iterates on a read from the input stream. The `readLine` method waits until the client responds by writing something to its output stream (the server's input stream). When the client responds, the server passes the client's response to the `KnockKnockProtocol` object and asks the `KnockKnockProtocol` object for a suitable reply. The server immediately sends the reply to the client via the output stream connected to the socket, using a call to println. If the server's response generated from the `KnockKnockServer` object is "Bye." this indicates that the client doesn't want any more jokes and the loop quits.

The Java runtime automatically closes the input and output streams, the client socket, and the server socket because they have been created in the try-with-resources statement.

**The Knock Knock Protocol**

The [KnockKnockProtocol](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockProtocol.java) class implements the protocol that the client and server use to communicate. This class keeps track of where the client and the server are in their conversation and serves up the server's response to the client's statements. The `KnockKnockProtocol` object contains the text of all the jokes and makes sure that the client gives the proper response to the server's statements. It wouldn't do to have the client say "Dexter who?" when the server says "Knock! Knock!"

All client/server pairs must have some protocol by which they speak to each other; otherwise, the data that passes back and forth would be meaningless. The protocol that your own clients and servers use depends entirely on the communication required by them to accomplish the task.

**The Knock Knock Client**

The [KnockKnockClient](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KnockKnockClient.java) class implements the client program that speaks to the `KnockKnockServer`. `KnockKnockClient` is based on the `EchoClient` program in the previous section, [Reading from and Writing to a Socket](#reading-from-and-writing-to-a-socket) and should be somewhat familiar to you. But we'll go over the program anyway and look at what's happening in the client in the context of what's going on in the server.

When you start the client program, the server should already be running and listening to the port, waiting for a client to request a connection. So, the first thing the client program does is to open a socket that is connected to the server running on the specified host name and port:

```java
String hostName = args[0];
int portNumber = Integer.parseInt(args[1]);

try (
    Socket kkSocket = new Socket(hostName, portNumber);
    PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
    BufferedReader in = new BufferedReader(
        new InputStreamReader(kkSocket.getInputStream()));
)
```

When creating its socket, the `KnockKnockClient` example uses the host name of the first command-line argument, the name of the computer on your network that is running the server program `KnockKnockServer`.

The `KnockKnockClient` example uses the second command-line argument as the port number when creating its socket. This is a *remote port number*—the number of a port on the server computer—and is the port to which `KnockKnockServer` is listening. For example, the following command runs the `KnockKnockClient` example with `knockknockserver.example.com` as the name of the computer that is running the server program `KnockKnockServer` and 4444 as the remote port number:

```
java KnockKnockClient knockknockserver.example.com 4444
```

The client's socket is bound to any available local port—a port on the client computer. Remember that the server gets a new socket as well. If you run the `KnockKnockClient` example with the command-line arguments in the previous example, then this socket is bound to local port number 4444 on the computer from which you ran the `KnockKnockClient` example. The server's socket and the client's socket are connected.

Next comes the `while` loop that implements the communication between the client and the server. The server speaks first, so the client must listen first. The client does this by reading from the input stream attached to the socket. If the server does speak, it says "Bye." and the client exits the loop. Otherwise, the client displays the text to the standard output and then reads the response from the user, who types into the standard input. After the user types a carriage return, the client sends the text to the server through the output stream attached to the socket.

```java
while ((fromServer = in.readLine()) != null) {
    System.out.println("Server: " + fromServer);
    if (fromServer.equals("Bye."))
        break;

    fromUser = stdIn.readLine();
    if (fromUser != null) {
        System.out.println("Client: " + fromUser);
        out.println(fromUser);
    }
}
```

The communication ends when the server asks if the client wishes to hear another joke, the client says no, and the server says "Bye."

The client automatically closes its input and output streams and the socket because they were created in the try-with-resources statement.

**Running the Programs**

You must start the server program first. To do this, run the server program using the Java interpreter, just as you would any other Java application. Specify as a command-line argument the port number on which the server program listens:

```
java KnockKnockServer 4444
```

Next, run the client program. Note that you can run the client on any computer on your network; it does not have to run on the same computer as the server. Specify as command-line arguments the host name and the port number of the computer running the KnockKnockServer server program:

```
java KnockKnockClient knockknockserver.example.com 4444
```

If you are too quick, you might start the client before the server has a chance to initialize itself and begin listening on the port. If this happens, you will see a stack trace from the client. If this happens, just restart the client.

If you try to start a second client while the first client is connected to the server, the second client just hangs. The next section, [Supporting Multiple Clients](#supporting-multiple-clients), talks about supporting multiple clients.

When you successfully get a connection between the client and server, you will see the following text displayed on your screen:

```
Server: Knock! Knock!
```

Now, you must respond with:

```
Who's there?
```

The client echoes what you type and sends the text to the server. The server responds with the first line of one of the many Knock Knock jokes in its repertoire. Now your screen should contain this:

```
Server: Knock! Knock!
Who's there?
Client: Who's there?
Server: Turnip
```

Now, you respond with:

```
Turnip who?
```

Again, the client echoes what you type and sends the text to the server. The server responds with the punch line. Now your screen should contain this:

```
Server: Knock! Knock!
Who's there?
Client: Who's there?
Server: Turnip
Turnip who?
Client: Turnip who?
Server: Turnip the heat, it's cold in here! Want another? (y/n)   
```

If you want to hear another joke, type `y`; if not, type `n`. If you type `y`, the server begins again with "Knock! Knock!" If you type n, the server says "Bye." thus causing both the client and the server to exit.

If at any point you make a typing mistake, the `KnockKnockServer` object catches it and the server responds with a message similar to this:

```
Server: You're supposed to say "Who's there?"!
```

The server then starts the joke over again:

```
Server: Try again. Knock! Knock!
```

Note that the `KnockKnockProtocol` object is particular about spelling and punctuation but not about capitalization.

**Supporting Multiple Clients**

To keep the `KnockKnockServer` example simple, we designed it to listen for and handle a single connection request. However, multiple client requests can come into the same port and, consequently, into the same `ServerSocket`. Client connection requests are queued at the port, so the server must accept the connections sequentially. However, the server can service them simultaneously through the use of threads—one thread per each client connection.

The basic flow of logic in such a server is this:

```
while (true) {
    accept a connection;
    create a thread to deal with the client;
}
```

The thread reads from and writes to the client connection as necessary.

> **Try This:**
>
> Modify the `KnockKnockServer` so that it can service multiple clients at the same time. Two classes compose our solution: [KKMultiServer](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KKMultiServer.java) and [KKMultiServerThread](http://docs.oracle.com/javase/tutorial/networking/sockets/examples/KKMultiServer.java). KKMultiServer loops forever, listening for client connection requests on a ServerSocket. When a request comes in, KKMultiServer accepts the connection, creates a new KKMultiServerThread object to process it, hands it the socket returned from accept, and starts the thread. Then the server goes back to listening for connection requests. The KKMultiServerThread object communicates to the client by reading from and writing to the socket. Run the new Knock Knock server KKMultiServer and then run several clients in succession.

## All About Datagrams

Some applications that you write to communicate over the network will not require the reliable, point-to-point channel provided by TCP. Rather, your applications might benefit from a mode of communication that delivers independent packages of information whose arrival and order of arrival are not guaranteed.

The UDP protocol provides a mode of network communication whereby applications send packets of data, called datagrams, to one another. A datagram is an independent, self-contained message sent over the network whose arrival, arrival time, and content are not guaranteed. The `DatagramPacket` and `DatagramSocket` classes in the java.net package implement system-independent datagram communication using UDP.

> **Note:**
>
> Many firewalls and routers are configured not to allow UDP packets. If you have trouble connecting to a service outside your firewall, or if clients have trouble connecting to your service, ask your system administrator if UDP is permitted.

### What Is a Datagram?

Clients and servers that communicate via a reliable channel, such as a TCP socket, have a dedicated point-to-point channel between themselves, or at least the illusion of one. To communicate, they establish a connection, transmit the data, and then close the connection. All data sent over the channel is received in the same order in which it was sent. This is guaranteed by the channel.

In contrast, applications that communicate via datagrams send and receive completely independent packets of information. These clients and servers do not have and do not need a dedicated point-to-point channel. The delivery of datagrams to their destinations is not guaranteed. Nor is the order of their arrival.

> **Definition:**
>
> A *datagram* is an independent, self-contained message sent over the network whose arrival, arrival time, and content are not guaranteed.

The `java.net` package contains three classes to help you write Java programs that use datagrams to send and receive packets over the network: `DatagramSocket`, `DatagramPacket`, and `MulticastSocket`. An application can send and receive `DatagramPacket`s through a `DatagramSocket`. In addition, `DatagramPacket`s can be broadcast to multiple recipients all listening to a `MulticastSocket`.

### Writing a Datagram Client and Server

The example featured in this section consists of two applications: a client and a server. The server continuously receives datagram packets over a datagram socket. Each datagram packet received by the server indicates a client request for a quotation. When the server receives a datagram, it replies by sending a datagram packet that contains a one-line "quote of the moment" back to the client.

The client application in this example is fairly simple. It sends a single datagram packet to the server indicating that the client would like to receive a quote of the moment. The client then waits for the server to send a datagram packet in response.

Two classes implement the server application: `QuoteServer` and `QuoteServerThread`. A single class implements the client application: `QuoteClient`.

Let's investigate these classes, starting with the class that contains the `main` method for the server application. [Working With a Server-Side Application](http://docs.oracle.com/javase/tutorial/deployment/applet/server.html) contains an applet version of the `QuoteClient` class.

**The QuoteServer Class**

The [QuoteServer](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteServer.java) class, shown here in its entirety, contains a single method: the `main` method for the quote server application. The main method simply creates a new `QuoteServerThread` object and starts it:

```java
import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}
```

The `QuoteServerThread` class implements the main logic of the quote server.

**The QuoteServerThread Class**

When created, the [QuoteServerThread](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteServerThread.java) creates a `DatagramSocket` on port 4445 (arbitrarily chosen). This is the `DatagramSocket` through which the server communicates with all of its clients.

```java
public QuoteServerThread() throws IOException {
    this("QuoteServer");
}

public QuoteServerThread(String name) throws IOException {
    super(name);
    socket = new DatagramSocket(4445);

    try {
        in = new BufferedReader(new FileReader("one-liners.txt"));
    }   
    catch (FileNotFoundException e){
        System.err.println("Couldn't open quote file.  Serving time instead.");
    }
}
```

Remember that certain ports are dedicated to well-known services and you cannot use them. If you specify a port that is in use, the creation of the `DatagramSocket` will fail.

The constructor also opens a `BufferedReader` on a file named [one-liners.txt](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/one-liners.txt) which contains a list of quotes. Each quote in the file is on a line by itself.

Now for the interesting part of the `QuoteServerThread`: its `run` method. The `run` method overrides `run` in the `Thread` class and provides the implementation for the thread. For information about threads, see [Defining and Starting a Thread](https://github.com/Mr-Dai/Java-Tutorial/blob/master/concurrency/concurrency.md#defining-and-starting-a-thread).

The `run` method contains a `while` loop that continues as long as there are more quotes in the file. During each iteration of the loop, the thread waits for a `DatagramPacket` to arrive over the `DatagramSocket`. The packet indicates a request from a client. In response to the client's request, the `QuoteServerThread` gets a quote from the file, puts it in a `DatagramPacket` and sends it over the `DatagramSocket` to the client that asked for it.

Let's look first at the section that receives the requests from clients:

```java
byte[] buf = new byte[256];
DatagramPacket packet = new DatagramPacket(buf, buf.length);
socket.receive(packet);
```

The first statement creates an array of bytes which is then used to create a `DatagramPacket`. The `DatagramPacket` will be used to receive a datagram from the socket because of the constructor used to create it. This constructor requires only two arguments: a byte array that contains client-specific data and the length of the byte array. When constructing a `DatagramPacket` to send over the `DatagramSocket`, you also must supply the Internet address and port number of the packet's destination. You'll see this later when we discuss how the server responds to a client request.

The last statement in the previous code snippet receives a datagram from the socket (the information received from the client gets copied into the packet). The receive method waits forever until a packet is received. If no packet is received, the server makes no further progress and just waits.

Now assume that, the server has received a request from a client for a quote. Now the server must respond. This section of code in the run method constructs the response:

```java
String dString = null;
if (in == null)
    dString = new Date().toString();
else
    dString = getNextQuote();
buf = dString.getBytes();
```

If the quote file did not get opened for some reason, then in equals null. If this is the case, the quote server serves up the time of day instead. Otherwise, the quote server gets the next quote from the already opened file. Finally, the code converts the string to an array of bytes.

Now, the `run` method sends the response to the client over the `DatagramSocket` with this code:

```java
InetAddress address = packet.getAddress();
int port = packet.getPort();
packet = new DatagramPacket(buf, buf.length, address, port);
socket.send(packet);
```

The first two statements in this code segment get the Internet address and the port number, respectively, from the datagram packet received from the client. The Internet address and port number indicate where the datagram packet came from. This is where the server must send its response. In this example, the byte array of the datagram packet contains no relevant information. The arrival of the packet itself indicates a request from a client that can be found at the Internet address and port number indicated in the datagram packet.

The third statement creates a new `DatagramPacket` object intended for sending a datagram message over the datagram socket. You can tell that the new `DatagramPacket` is intended to send data over the socket because of the constructor used to create it. This constructor requires four arguments. The first two arguments are the same required by the constructor used to create receiving datagrams: a byte array containing the message from the sender to the receiver and the length of this array. The next two arguments are different: an Internet address and a port number. These two arguments are the complete address of the destination of the datagram packet and must be supplied by the sender of the datagram. The last line of code sends the `DatagramPacket` on its way.

When the server has read all the quotes from the quote file, the `while` loop terminates and the `run` method cleans up:

```java
socket.close();
```

**The QuoteClient Class**

The [QuoteClient](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/QuoteClient.java) class implements a client application for the `QuoteServer`. This application sends a request to the `QuoteServer`, waits for the response, and, when the response is received, displays it to the standard output. Let's look at the code in detail.

The `QuoteClient` class contains one method, the `main` method for the client application. The top of the `main` method declares several local variables for its use:

```java
int port;
InetAddress address;
DatagramSocket socket = null;
DatagramPacket packet;
byte[] sendBuf = new byte[256];
```

First, the `main` method processes the command-line arguments used to invoke the `QuoteClient` application:

```java
if (args.length != 1) {
    System.out.println("Usage: java QuoteClient <hostname>");
    return;
}
```

The `QuoteClient` application requires one command-line arguments: the name of the machine on which the `QuoteServer` is running.

Next, the `main` method creates a `DatagramSocket`:

```java
DatagramSocket socket = new DatagramSocket();
```

The client uses a constructor that does not require a port number. This constructor just binds the `DatagramSocket` to any available local port. It doesn't matter what port the client is bound to because the `DatagramPackets` contain the addressing information. The server gets the port number from the `DatagramPackets` and send its response to that port.

Next, the `QuoteClient` program sends a request to the server:

```java
byte[] buf = new byte[256];
InetAddress address = InetAddress.getByName(args[0]);
DatagramPacket packet = new DatagramPacket(buf, buf.length, 
                                address, 4445);
socket.send(packet);
```

The code segment gets the Internet address for the host named on the command line (presumably the name of the machine on which the server is running). This `InetAddress` and the port number 4445 (the port number that the server used to create its `DatagramSocket`) are then used to create `DatagramPacket` destined for that Internet address and port number. Therefore the `DatagramPacket` will be delivered to the quote server.

Note that the code creates a `DatagramPacket` with an empty byte array. The byte array is empty because this datagram packet is simply a request to the server for information. All the server needs to know to send a response--the address and port number to which reply--is automatically part of the packet.

Next, the client gets a response from the server and displays it:

```java
packet = new DatagramPacket(buf, buf.length);
socket.receive(packet);
String received = new String(packet.getData(), 0, packet.getLength());
System.out.println("Quote of the Moment: " + received);
```

To get a response from the server, the client creates a "receive" packet and uses the `DatagramSocket` receive method to receive the reply from the server. The receive method waits until a datagram packet destined for the client comes through the socket. Note that if the server's reply is somehow lost, the client will wait forever because of the no-guarantee policy of the datagram model. Normally, a client sets a timer so that it doesn't wait forever for a reply; if no reply arrives, the timer goes off and the client retransmits.

When the client receives a reply from the server, the client uses the getData method to retrieve that data from the packet. The client then converts the data to a string and displays it.

**Running the Server and Client**

After you've successfully compiled the server and the client programs, you run them. You have to run the server program first. Just use the Java interpreter and specify the `QuoteServer` class name.

Once the server has started, you can run the client program. Remember to run the client program with one command-line argument: the name of the host on which the `QuoteServer` is running.

After the client sends a request and receives a response from the server, you should see output similar to this:

```
Quote of the Moment:
Good programming is 99% sweat and 1% coffee.
```

### Broadcasting to Multiple Recipients

In addition to `DatagramSocket`, which lets programs send packets to one another, `java.net` includes a class called `MulticastSocket`. This kind of socket is used on the client-side to listen for packets that the server broadcasts to multiple clients.

Let's rewrite the quote server so that it broadcasts DatagramPackets to multiple recipients. Instead of sending quotes to a specific client that makes a request, the new server now needs to broadcast quotes at a regular interval. The client needs to be modified so that it passively listens for quotes and does so on a `MulticastSocket`.

This example is comprised of three classes which are modifications of the three classes from the previous example: [MulticastServer](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/MulticastServer.java), [MulticastServerThread](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/MulticastServerThread.java), and [MulticastClient](http://docs.oracle.com/javase/tutorial/networking/datagrams/examples/MulticastClient.java). This discussion highlights the interesting parts of these classes.

Here is the new version of the server's main program. The differences between this code and the previous version, `QuoteServer`, are shown in bold:

```java
import java.io.*;

public class MulticastServer {
    public static void main(String[] args) throws IOException {
        new MulticastServerThread().start();
    }
}
```

Basically, the server got a new name and creates a `MulticastServerThread` instead of a `QuoteServerThread`. Now let's look at the `MulticastServerThread` which contains the heart of the server. Here's its class declaration:

```java
public class MulticastServerThread extends QuoteServerThread {
    // ...
}
```

We've made this class a subclass of `QuoteServerThread` so that it can use the constructor, and inherit some member variable and the `getNextQuote` method. Recall that `QuoteServerThread` creates a `DatagramSocket` bound to port 4445 and opens the quote file. The `DatagramSocket`'s port number doesn't actually matter in this example because the client never send anything to the server.

The only method explicitly implemented in `MulticastServerThread` is its `run` method. The differences between this `run` method and the one in `QuoteServerThread` are shown in bold:

```java
public void run() {
    while (moreQuotes) {
        try {
            byte[] buf = new byte[256];
            // don't wait for request...just send a quote

            String dString = null;
            if (in == null)
                dString = new Date().toString();
            else
                dString = getNextQuote();
            buf = dString.getBytes();

            InetAddress group = InetAddress.getByName("203.0.113.0");
            DatagramPacket packet;
            packet = new DatagramPacket(buf, buf.length, group, 4446);
            socket.send(packet);

            try {
                sleep((long)Math.random() * FIVE_SECONDS);
            } 
            catch (InterruptedException e) { }
        }
        catch (IOException e) {
            e.printStackTrace();
            moreQuotes = false;
        }
    }
    socket.close();
}
```

The interesting change is how the `DatagramPacket` is constructed, in particular, the `InetAddress` and port used to construct the `DatagramPacket`. Recall that the previous example retrieved the `InetAddress` and port number from the packet sent to the server from the client. This was because the server needed to reply directly to the client. Now, the server needs to address multiple clients. So this time both the `InetAddress` and the port number are hard-coded.

The hard-coded port number is 4446 (the client must have a `MulticastSocket` bound to this port). The hard-coded `InetAddress` of the `DatagramPacket` is "203.0.113.0" and is a group identifier (rather than the Internet address of the machine on which a single client is running). This particular address was arbitrarily chosen from the reserved for this purpose.

Created in this way, the `DatagramPacket` is destined for all clients listening to port number 4446 who are member of the "203.0.113.0" group.

To listen to port number 4446, the new client program just created its `MulticastSocket` with that port number. To become a member of the "203.0.113.0" group, the client calls the `MulticastSocket`'s `joinGroup` method with the `InetAddress` that identifies the group. Now, the client is set up to receive `DatagramPacket`s destined for the port and group specified. Here's the relevant code from the new client program (which was also rewritten to passively receive quotes rather than actively request them). The bold statements are the ones that interact with the `MulticastSocket`:

```java
MulticastSocket socket = new MulticastSocket(4446);
InetAddress group = InetAddress.getByName("203.0.113.0");
socket.joinGroup(group);

DatagramPacket packet;
for (int i = 0; i < 5; i++) {
    byte[] buf = new byte[256];
    packet = new DatagramPacket(buf, buf.length);
    socket.receive(packet);

    String received = new String(packet.getData());
    System.out.println("Quote of the Moment: " + received);
}

socket.leaveGroup(group);
socket.close();
```

Notice that the server uses a `DatagramSocket` to broadcast packet received by the client over a `MulticastSocket`. Alternatively, it could have used a `MulticastSocket`. The socket used by the server to send the `DatagramPacket` is not important. What's important when broadcasting packets is the addressing information contained in the `DatagramPacket`, and the socket used by the client to listen for it.

> **Try this:**
>
> Run the `MulticastServer` and several clients. Watch how the clients all get the same quotes.

## Programmatic Access to Network Parameters

Systems often run with multiple active network connections, such as wired Ethernet, `802.11 b/g` (wireless), and bluetooth. Some applications might need to access this information to perform the particular network activity on a specific connection.

The `java.net.NetworkInterface` class provides access to this information.

This lesson guides you through some of the more common uses of this class and provides examples that list all the network interfaces on a machine as well as their IP addresses and status.

### What Is a Network Interface?

A *network interface* is the point of interconnection between a computer and a private or public network. A network interface is generally a network interface card (NIC), but does not have to have a physical form. Instead, the network interface can be implemented in software. For example, the loopback interface (<tt>127.0.0.1</tt> for IPv4 and <tt>::1</tt> for IPv6) is not a physical device but a piece of software simulating a network interface. The loopback interface is commonly used in test environments.

The `java.net.NetworkInterface` class represents both types of interfaces.

`NetworkInterface` is useful for a multi-homed system, which is a system with multiple NICs. Using `NetworkInterface`, you can specify which NIC to use for a particular network activity.

For example, assume you have a machine with two configured NICs, and you want to send data to a server. You create a socket like this:

```java
Socket soc = new java.net.Socket();
soc.connect(new InetSocketAddress(address, port));
```

To send the data, the system determines which interface is used. However, if you have a preference or otherwise need to specify which NIC to use, you can query the system for the appropriate interfaces and find an address on the interface you want to use. When you create the socket and bind it to that address, the system uses the associated interface. For example:

```java
NetworkInterface nif = NetworkInterface.getByName("bge0");
Enumeration<InetAddress> nifAddresses = nif.getInetAddresses();

Socket soc = new java.net.Socket();
soc.bind(new InetSocketAddress(nifAddresses.nextElement(), 0));
soc.connect(new InetSocketAddress(address, port));
```

You can also use `NetworkInterface` to identify the local interface on which a multicast group is to be joined. For example:

```java
NetworkInterface nif = NetworkInterface.getByName("bge0");
MulticastSocket ms = new MulticastSocket();
ms.joinGroup(new InetSocketAddress(hostname, port), nif);
```

`NetworkInterface` can be used with Java APIs in many other ways beyond the two uses described here.

### Retrieving Network Interfaces

The `NetworkInterface` class has no public constructor. Therefore, you cannot just create a new instance of this class with the `new` operator. Instead, the following static methods are available so that you can retrieve the interface details from the system: `getByInetAddress()`, `getByName()`, and `getNetworkInterfaces()`. The first two methods are used when you already know the IP address or the name of the particular interface. The third method, `getNetworkInterfaces()` returns the complete list of interfaces on the machine.

Network interfaces can be hierarchically organized. The `NetworkInterface` class includes two methods, `getParent()` and `getSubInterfaces()`, that are pertinent to a network interface hierarchy. The `getParent()` method returns the parent `NetworkInterface` of an interface. If a network interface is a subinterface, `getParent()` returns a non-null value. The getSubInterfaces() method returns all the subinterfaces of a network interface.

The following example program lists the name of all the network interfaces and subinterfaces (if any exist) on a machine.

```java
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

public class ListNIFs 
{
    public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        
        for (NetworkInterface netIf : Collections.list(nets)) {
            out.printf("Display name: %s\n", netIf.getDisplayName());
            out.printf("Name: %s\n", netIf.getName());
            displaySubInterfaces(netIf);
            out.printf("\n");
        }
    }

    static void displaySubInterfaces(NetworkInterface netIf) throws SocketException {
        Enumeration<NetworkInterface> subIfs = netIf.getSubInterfaces();
        
        for (NetworkInterface subIf : Collections.list(subIfs)) {
            out.printf("\tSub Interface Display name: %s\n", subIf.getDisplayName());
            out.printf("\tSub Interface Name: %s\n", subIf.getName());
        }
     }
}
```

The following is sample output from the example program:

```
Display name: bge0
Name: bge0
    Sub Interface Display name: bge0:3
    Sub Interface Name: bge0:3
    Sub Interface Display name: bge0:2
    Sub Interface Name: bge0:2
    Sub Interface Display name: bge0:1
    Sub Interface Name: bge0:1

Display name: lo0
Name: lo0
```

### Listing Network Interface Addresses

One of the most useful pieces of information you can get from a network interface is the list of IP addresses that are assigned to it. You can obtain this information from a `NetworkInterface` instance by using one of two methods. The first method, `getInetAddresses()`, returns an `Enumeration` of `InetAddress`. The other method, `getInterfaceAddresses()`, returns a list of `java.net.InterfaceAddress` instances. This method is used when you need more information about an interface address beyond its IP address. For example, you might need additional information about the subnet mask and broadcast address when the address is an IPv4 address, and a network prefix length in the case of an IPv6 address.

The following example program lists all the network interfaces and their addresses on a machine:

```java
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

public class ListNets {

    public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
     }
}  
```

The following is sample output from the example program:

```
Display name: TCP Loopback interface
Name: lo
InetAddress: /127.0.0.1

Display name: Wireless Network Connection
Name: eth0
InetAddress: /192.0.2.0
```

### Network Interface Parameters

You can access network parameters about a network interface beyond the name and IP addresses assigned to it

You can discover if a network interface is “up” (that is, running) with the `isUP()` method. The following methods indicate the network interface type:

- `isLoopback()` indicates if the network interface is a loopback interface.
- `isPointToPoint()` indicates if the interface is a point-to-point interface.
- `isVirtual()` indicates if the interface is a virtual interface.

The `supportsMulticast()` method indicates whether the network interface supports multicasting. The `getHardwareAddress()` method returns the network interface's physical hardware address, usually called MAC address, when it is available. The `getMTU()` method returns the Maximum Transmission Unit (MTU), which is the largest packet size.

The following example expands on the example in [Listing Network Interface Addresses](#listing-network-interface-addresses) by adding the additional network parameters described on this page:

```java
import java.io.*;
import java.net.*;
import java.util.*;
import static java.lang.System.out;

public class ListNetsEx {

    public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
       
        out.printf("Up? %s\n", netint.isUp());
        out.printf("Loopback? %s\n", netint.isLoopback());
        out.printf("PointToPoint? %s\n", netint.isPointToPoint());
        out.printf("Supports multicast? %s\n", netint.supportsMulticast());
        out.printf("Virtual? %s\n", netint.isVirtual());
        out.printf("Hardware address: %s\n",
                    Arrays.toString(netint.getHardwareAddress()));
        out.printf("MTU: %s\n", netint.getMTU());
        out.printf("\n");
    }
}
```

The following is sample output from the example program:

```
Display name: bge0
Name: bge0
InetAddress: /fe80:0:0:0:203:baff:fef2:e99d%2
InetAddress: /129.156.225.59
Up? true
Loopback? false
PointToPoint? false
Supports multicast? false
Virtual? false
Hardware address: [0, 3, 4, 5, 6, 7]
MTU: 1500

Display name: lo0
Name: lo0
InetAddress: /0:0:0:0:0:0:0:1%1
InetAddress: /127.0.0.1
Up? true
Loopback? true
PointToPoint? false
Supports multicast? false
Virtual? false
Hardware address: null
MTU: 8232
```

## Working With Cookies

Though you are probably already familiar with cookies, you might not know how to take advantage of them in your Java application. This lesson guides you through the concept of cookies and explains how to set a cookie handler so that your HTTP URL connections will use it.

Java SE provides one main class for this functionality, `java.net.CookieHandler`, and the following supporting classes and interfaces: `java.net.CookieManager`, `java.net.CookiePolicy`, `java.net.CookieStore`, and `java.net.HttpCookie`.

### HTTP State Management With Cookies

The HTTP state management mechanism specifies a way to create a stateful session with HTTP requests and responses.

Generally, HTTP request/response pairs are independent of each other. However, the state management mechanism enables clients and servers that can exchange state information to put these pairs in a larger context, which is called a *session*. The state information used to create and maintain the session is called a *cookie*.

A cookie is a piece of data that can be stored in a browser's cache. If you visit a web site and then revisit it, the cookie data can be used to identify you as a return visitor. Cookies enable state information, such as an online shopping cart, to be remembered. A cookie can be short term, holding data for a single web session, that is, until you close the browser, or a cookie can be longer term, holding data for a week or a year.

For more information about HTTP state management, see [RFC 2965: HTTP State Management Mechanism](http://www.ietf.org/rfc/rfc2965.txt).

### CookieHandler Callback Mechanism

HTTP state management is implemented in Java SE through the `java.net.CookieHandler` class. A `CookieHandler` object provides a callback mechanism to provide an HTTP state management policy implementation in the HTTP protocol handler. That is, URLs that use HTTP as the protocol, `new URL("http://example.com")` for example, will use the HTTP protocol handler. This protocol handler calls back to the `CookieHandler` object, if set, to handle the state management.

The `CookieHandler` class is an abstract class that has two pairs of related methods. The first pair, `getDefault()` and `setDefault(cookieHandler)`, are static methods that enable you to discover the current handler that is installed and to install your own handler.

No default handler is installed, and installing a handler is done on a system-wide basis. For applications running within a secure environment, that is, they have a security manager installed, you must have special permission to get and set the handler. For more information, see `java.net.CookieHandler.getDefault`.

The second pair of related methods, `put(uri, responseHeaders)` and `get(uri, requestHeaders)`, enable you to set and get all the applicable cookies to and from a cookie cache for the specified URI in the response/request headers, respectively. These methods are abstract, and a concrete implementation of a `CookieHandler` must provide the implementation.

Java Web Start and Java Plug-in have a default `CookieHandler` installed. However, if you are running a stand-alone application and want to enable HTTP state management, you must set a system-wide handler. The next two pages in this lesson show you how to do so.

### Default CookieManager

`java.net.CookieManager` provides a concrete implementation of a `CookieHandler` and for most users is sufficient for handling HTTP state management. `CookieManager` separates the storage of cookies from the policy surrounding, accepting, and rejecting them. A `CookieManager` is initialized with a `java.net.CookieStore` and a `java.net.CookiePolicy`. `CookieStore` manages the storage of the cookies. `CookiePolicy` makes policy decisions on cookie acceptance and rejection.

The following code shows how to create and set a system-wide CookieManager:

```java
java.net.CookieManager cm = new java.net.CookieManager();
java.net.CookieHandler.setDefault(cm);
```

The first line calls the default CookieManager constructor to create the instance. The second line calls the static `setDefault` method of `CookieHandler` to set the system-wide handler.

The default `CookieManager` constructor creates a new `CookieManager` instance with a default cookie store and accept policy. `CookieStore` is the place where any accepted HTTP cookie is stored. If not specified when created, a `CookieManager` instance will use an internal in-memory implementation. This implementation is not persistent and only lives for the lifetime of the Java Virtual Machine. Users requiring a persistent store must implement their own store.

The default cookie policy used by `CookieManager` is `CookiePolicy.ACCEPT_ORIGINAL_SERVER`, which only accepts cookies from the original server. So, the `Set-Cookie` response from the server must have a “domain” attribute set, and it must match the domain of the host in the URL. For more information, see `java.net.HttpCookie.domainMatches`. Users requiring a different policy must implement the `CookiePolicy` interface and pass it to the `CookieManager` constructor or set it to an already constructed `CookieManager` instance by using the `setCookiePolicy(cookiePolicy)` method.

When retrieving cookies from the cookie store, `CookieManager` also enforces the path-match rule from section 3.3.4 of [RFC 2965](http://www.ietf.org/rfc/rfc2965.txt). So, a cookie must also have its “path” attribute set so that the path-match rule can be applied before the cookie is retrieved from the cookie store.

In summary, CookieManager provides the framework for handling cookies and provides a good default implementation for `CookieStore`. `CookieManager` is highly customizable by enabling you to set your own `CookieStore`, `CookiePolicy`, or both.

### Custom CookieManager

Two aspects of the `CookieManager` class can be customized, the `CookiePolicy` and the `CookieStore`.

**CookiePolicy**

For convenience, `CookiePolicy` defines the following pre-defined policies for accepting cookies:

- `CookiePolicy.ACCEPT_ORIGINAL_SERVER` only accepts cookies from the original server.
- `CookiePolicy.ACCEPT_ALL` accepts all cookies.
- `CookiePolicy.ACCEPT_NONE` accepts no cookies.

You can also define your own cookie policy by implementing the `shouldAccept` method of `CookiePolicy`. You can then use this `CookiePolicy` by passing it to the multi-argument `CookieManager` constructor or by calling the `setCookiePolicy(cookiePolicy)` method to change an already existing cookie manager.

The following is an example of a cookie policy that rejects cookies from domains that are on a blacklist, before applying the `CookiePolicy.ACCEPT_ORIGINAL_SERVER` policy:

```java
import java.net.*;

public class BlacklistCookiePolicy implements CookiePolicy {
    String[] blacklist;

    public BlacklistCookiePolicy(String[] list) {
        blacklist = list;
    }

    public boolean shouldAccept(URI uri, HttpCookie cookie)  {
        String host;
        try {
            host =  InetAddress.getByName(uri.getHost()).getCanonicalHostName();
        } catch (UnknownHostException e) {
            host = uri.getHost();
        }

        for (int i = 0; i<blacklist.length; i++) {
	    if (HttpCookie.domainMatches(blacklist[i], host)) {
                return false;
            }
        }

        return CookiePolicy.ACCEPT_ORIGINAL_SERVER.shouldAccept(uri, cookie);
    }
}
```

When you create a `BlacklistCookiePolicy` instance, you pass it an array of strings representing the domains that you do not want to accept cookies from. Then, you set this `BlacklistCookiePolicy` instance as the cookie policy for your `CookieManager`. For example:

```java
String[] list = new String[]{ ".example.com" };
CookieManager cm = new CookieManager(null, new BlacklistCookiePolicy(list));
CookieHandler.setDefault(cm);
```

The sample code will not accept cookies from hosts such as the following:

```
host.example.com
domain.example.com
```

However, this sample code will accept cookies from hosts such as the following:

```
example.com
example.org
myhost.example.org
```

**CookieStore**

A `CookieStore` is an interface that represents a storage area for cookies. `CookieManager` adds the cookies to the `CookieStore` for every HTTP response and retrieves cookies from the `CookieStore` for every HTTP request.

You can implement this interface to provide your own `CookieStore` and pass it to the `CookieManager` during creation. You cannot set the `CookieStore` after the `CookieManager` instance has been created. However, you can get a reference to the cookie store by calling `CookieManager.getCookieStore()`. Doing so can be useful as it enables you to leverage the default in-memory `CookieStore` implementation that is provided by Java SE and complement its functionality.

For example, you might want to create a persistent cookie store that would save cookies so that they can be used even if the Java Virtual Machine is restarted. Your implementation would work similar to the following:

1. Any cookies that were previously saved are read in.
2. During runtime, cookies are stored and retrieved from memory.
3. Cookies are written out to persistent storage before exiting.

The following is an incomplete example of this cookie store. This example shows you how to leverage the Java SE default in-memory cookie store and how you might extend its functionality.

```java
import java.net.*;
import java.util.*;

public class PersistentCookieStore implements CookieStore, Runnable {
    CookieStore store;

    public PersistentCookieStore() {
        // get the default in memory cookie store
        store = new CookieManager().getCookieStore();

        // todo: read in cookies from persistant storage
        // and add them store

        // add a shutdown hook to write out the in memory cookies
        Runtime.getRuntime().addShutdownHook(new Thread(this)); 
    }

    public void run() {
        // todo: write cookies in store to persistent storage
    }

    public void	add(URI uri, HttpCookie cookie) {
        store.add(uri, cookie);
    }

    public List<HttpCookie> get(URI uri) {
        return store.get(uri);
    }

    public List<HttpCookie> getCookies() {
        return store.getCookies();
    }
    
    public List<URI> getURIs() {
        return store.getURIs();
    }

    public boolean remove(URI uri, HttpCookie cookie) {
        return store.remove(uri, cookie);
    }

    public boolean removeAll()  {
        return store.removeAll();
    }
}
```

## End of Trail

You have reached the end of the "Custom Networking" trail.

If you have comments or suggestions about this trail, use our [feedback page](https://docs.oracle.com/javase/feedback.html) to tell us about it.

- [RMI](http://docs.oracle.com/javase/tutorial/rmi/index.html): RMI is a Java-to-Java alternative to IDL for writing client/server programs.
- [Security Features in Java SE](http://docs.oracle.com/javase/tutorial/security/index.html): When writing programs that communicate, you need to understand the security implications.
- [JDBC Database Access](http://docs.oracle.com/javase/tutorial/jdbc/index.html): This trail shows you how to access a database from Java programs.
