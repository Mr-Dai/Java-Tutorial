[TOC]

# Introduction

JDK 5.0 introduces several new extensions to the Java programming language. One of these is the introduction of *generics*.

This trail is an introduction to generics. You may be familiar with similar constructs from other languages, most notably C++ templates. If so, you'll see that there are both similarities and important differences. If you are unfamiliar with look-a-alike constructs from elsewhere, all the better; you can start fresh, without having to unlearn any misconceptions.

Generics allow you to abstract over types. The most common examples are container types, such as those in the Collections hierarchy.

Here is a typical usage of that sort:

```java
List myIntList = new LinkedList(); // 1
myIntList.add(new Integer(0)); // 2
Integer x = (Integer) myIntList.iterator().next(); // 3        
```

The cast on line 3 is slightly annoying. Typically, the programmer knows what kind of data has been placed into a particular list. However, the cast is essential. The compiler can only guarantee that an `Object` will be returned by the iterator. To ensure the assignment to a variable of type `Integer` is type safe, the cast is required.

Of course, the cast not only introduces clutter. It also introduces the possibility of a run time error, since the programmer may be mistaken.

What if programmers could actually express their intent, and mark a list as being restricted to contain a particular data type? This is the core idea behind generics. Here is a version of the program fragment given above using generics:

```java
List<Integer> myIntList = new LinkedList<Integer>(); // 1'
myIntList.add(new Integer(0)); // 2'
Integer x = myIntList.iterator().next(); // 3'
```

Notice the type declaration for the variable `myIntList`. It specifies that this is not just an arbitrary `List`, but a `List` of `Integer`, written `List<Integer>`. We say that `List` is a generic interface that takes a *type parameter* -- in this case, `Integer`. We also specify a type parameter when creating the list object.

Note, too, that the cast on line 3' is gone.

Now, you might think that all we've accomplished is to move the clutter around. Instead of a cast to `Integer` on line 3, we have `Integer` as a type parameter on line 1'. However, there is a very big difference here. The compiler can now check the type correctness of the program at compile-time. When we say that `myIntList` is declared with type `List<Integer>`, this tells us something about the variable `myIntList`, which holds true wherever and whenever it is used, and the compiler will guarantee it. In contrast, the cast tells us something the programmer thinks is true at a single point in the code.

The net effect, especially in large programs, is improved readability and robustness.

# Defining Simple Generics

Here is a small excerpt from the definitions of the interfaces `List` and `Iterator` in package `java.util`:

```java
public interface List <E> {
    void add(E x);
    Iterator<E> iterator();
}

public interface Iterator<E> {
    E next();
    boolean hasNext();
}
```

This code should all be familiar, except for the stuff in angle brackets. Those are the declarations of the *formal type parameters* of the interfaces `List` and `Iterator`.

Type parameters can be used throughout the generic declaration, pretty much where you would use ordinary types (though there are some important restrictions; see the section *The Fine Print*.

In the introduction, we saw *invocations* of the generic type declaration `List`, such as `List<Integer>`. In the invocation (usually called a *parameterized type*), all occurrences of the formal type parameter (`E` in this case) are replaced by the *actual type argument* (in this case, `Integer`).

You might imagine that `List<Integer>` stands for a version of `List` where `E` has been uniformly replaced by `Integer`:

```java
public interface IntegerList {
    void add(Integer x);
    Iterator<Integer> iterator();
}
```

This intuition can be helpful, but it's also misleading.

It is helpful, because the parameterized type `List<Integer>` does indeed have methods that look just like this expansion.

It is misleading, because the declaration of a generic is never actually expanded in this way. There aren't multiple copies of the code -- not in source, not in binary, not on disk and not in memory. If you are a C++ programmer, you'll understand that this is very different than a C++ template.

A generic type declaration is compiled once and for all, and turned into a single class file, just like an ordinary class or interface declaration.

Type parameters are analogous to the ordinary parameters used in methods or constructors. Much like a method has *formal value parameters* that describe the kinds of values it operates on, a generic declaration has formal type parameters. When a method is invoked, *actual arguments* are substituted for the formal parameters, and the method body is evaluated. When a generic declaration is invoked, the actual type arguments are substituted for the formal type parameters.

A note on naming conventions. We recommend that you use pithy (single character if possible) yet evocative names for formal type parameters. It's best to avoid lower case characters in those names, making it easy to distinguish formal type parameters from ordinary classes and interfaces. Many container types use `E`, for element, as in the examples above. We'll see some additional conventions in later examples.

# Generics and Subtyping

Let's test your understanding of generics. Is the following code snippet legal?

```java
List<String> ls = new ArrayList<String>(); // 1
List<Object> lo = ls; // 2 
```

Line 1 is certainly legal. The trickier part of the question is line 2. This boils down to the question: is a `List` of `String` a `List` of `Object`. Most people instinctively answer, "Sure!"

Well, take a look at the next few lines:

```java
lo.add(new Object()); // 3
String s = ls.get(0); // 4: Attempts to assign an Object to a String!
```

Here we've aliased `ls` and `lo`. Accessing `ls`, a list of `String`, through the alias `lo`, we can insert arbitrary objects into it. As a result `ls` does not hold just `String`s anymore, and when we try and get something out of it, we get a rude surprise.

The Java compiler will prevent this from happening of course. Line 2 will cause a compile time error.

In general, if `Foo` is a subtype (subclass or subinterface) of `Bar`, and `G` is some generic type declaration, it is **not** the case that `G<Foo>` is a subtype of `G<Bar>`. This is probably the hardest thing you need to learn about generics, because it goes against our deeply held intuitions.

We should not assume that collections don't change. Our instinct may lead us to think of these things as immutable.

For example, if the department of motor vehicles supplies a list of drivers to the census bureau, this seems reasonable. We think that a `List<Driver>` is a `List<Person>`, assuming that `Driver` is a subtype of `Person`. In fact, what is being passed is a **copy** of the registry of drivers. Otherwise, the census bureau could add new people who are not drivers into the list, corrupting the DMV's records.

To cope with this sort of situation, it's useful to consider more flexible generic types. The rules we've seen so far are quite restrictive.

# Wildcards

Consider the problem of writing a routine that prints out all the elements in a collection. Here's how you might write it in an older version of the language (i.e., a pre-5.0 release):

```java
void printCollection(Collection c) {
    Iterator i = c.iterator();
    for (k = 0; k < c.size(); k++) {
        System.out.println(i.next());
    }
}
```

And here is a naive attempt at writing it using generics (and the new `for` loop syntax):

```java
void printCollection(Collection<Object> c) {
    for (Object e : c) {
        System.out.println(e);
    }
}
```

The problem is that this new version is much less useful than the old one. Whereas the old code could be called with any kind of collection as a parameter, the new code only takes `Collection<Object>`, which, as we've just demonstrated, is **not** a supertype of all kinds of collections!

So what **is** the supertype of all kinds of collections? It's written <code>Collection<&#63;></code> (pronounced "collection of unknown"), that is, a collection whose element type matches anything. It's called a **wildcard type** for obvious reasons. We can write:

<pre>
void printCollection(Collection<&#63;> c) {
    for (Object e : c) {
        System.out.println(e);
    }
}
</pre>

and now, we can call it with any type of collection. Notice that inside `printCollection()`, we can still read elements from `c` and give them type `Object`. This is always safe, since whatever the actual type of the collection, it does contain objects. It isn't safe to add arbitrary objects to it however:

<pre>
Collection<&#63;> c = new ArrayList&lt;String>();
c.add(new Object()); // Compile time error
</pre>

Since we don't know what the element type of `c` stands for, we cannot add objects to it. The `add()` method takes arguments of type `E`, the element type of the collection. When the actual type parameter is `?`, it stands for some unknown type. Any parameter we pass to add would have to be a subtype of this unknown type. Since we don't know what type that is, we cannot pass anything in. The sole exception is `null`, which is a member of every type.

On the other hand, given a <code>List<&#63;></code>, we can call `get()` and make use of the result. The result type is an unknown type, but we always know that it is an object. It is therefore safe to assign the result of `get()` to a variable of type `Object` or pass it as a parameter where the type `Object` is expected.

## Bounded Wildcards

Consider a simple drawing application that can draw shapes such as rectangles and circles. To represent these shapes within the program, you could define a class hierarchy such as this:

```java
public abstract class Shape {
    public abstract void draw(Canvas c);
}

public class Circle extends Shape {
    private int x, y, radius;
    public void draw(Canvas c) {
        ...
    }
}

public class Rectangle extends Shape {
    private int x, y, width, height;
    public void draw(Canvas c) {
        ...
    }
}
```

These classes can be drawn on a canvas:

```java
public class Canvas {
    public void draw(Shape s) {
        s.draw(this);
   }
}
```

Any drawing will typically contain a number of shapes. Assuming that they are represented as a list, it would be convenient to have a method in `Canvas` that draws them all:

```java
public void drawAll(List<Shape> shapes) {
    for (Shape s: shapes) {
        s.draw(this);
   }
}
```

Now, the type rules say that `drawAll()` can only be called on lists of exactly `Shape`: it cannot, for instance, be called on a `List<Circle>`. That is unfortunate, since all the method does is read shapes from the list, so it could just as well be called on a `List<Circle>`. What we really want is for the method to accept a list of **any** kind of shape:

<pre>
public void drawAll(List<&#63; extends Shape> shapes) {
    ...
}
</pre>

There is a small but very important difference here: we have replaced the type `List<Shape>` with <code>List<&#63; extends Shape></code>. Now `drawAll()` will accept lists of any subclass of `Shape`, so we can now call it on a `List<Circle>` if we want.

<code>List<&#63; extends Shape></code> is an example of a *bounded wildcard*. The `?` stands for an unknown type, just like the wildcards we saw earlier. However, in this case, we know that this unknown type is in fact a subtype of `Shape`. (Note: It could be `Shape` itself, or some subclass; it need not literally extend `Shape`.) We say that `Shape` is the *upper bound* of the wildcard.

There is, as usual, a price to be paid for the flexibility of using wildcards. That price is that it is now illegal to write into `shapes` in the body of the method. For instance, this is not allowed:

<pre>
public void addRectangle(List<&#63; extends Shape> shapes) {
    // Compile-time error!
    shapes.add(0, new Rectangle());
}
</pre>

You should be able to figure out why the code above is disallowed. The type of the second parameter to `shapes.add()` is `? extends Shape` -- an unknown subtype of `Shape`. Since we don't know what type it is, we don't know if it is a supertype of `Rectangle`; it might or might not be such a supertype, so it isn't safe to pass a `Rectangle` there.

Bounded wildcards are just what one needs to handle the example of the DMV passing its data to the census bureau. Our example assumes that the data is represented by mapping from names (represented as strings) to people (represented by reference types such as `Person` or its subtypes, such as `Driver`). `Map<K,V>` is an example of a generic type that takes two type arguments, representing the keys and values of the map.

Again, note the naming convention for formal type parameters -- `K` for keys and `V` for values.

<pre>
public class Census {
    public static void addRegistry(Map&lt;String, &#63; extends Person> registry) {
}
...

Map&lt;String, Driver> allDrivers = ... ;
Census.addRegistry(allDrivers);
</pre>

# Generic Methods

Consider writing a method that takes an array of objects and a collection and puts all objects in the array into the collection. Here's a first attempt:

<pre>
static void fromArrayToCollection(Object[] a, Collection<&#63;> c) {
    for (Object o : a) { 
        c.add(o); // compile-time error
    }
}
</pre>

By now, you will have learned to avoid the beginner's mistake of trying to use `Collection<Object>` as the type of the collection parameter. You may or may not have recognized that using <code>Collection<&#63;></code> isn't going to work either. Recall that you cannot just shove objects into a collection of unknown type.

The way to do deal with these problems is to use *generic methods*. Just like type declarations, method declarations can be generic -- that is, parameterized by one or more type parameters.

```java
static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
    for (T o : a) {
        c.add(o); // Correct
    }
}
```

We can call this method with any kind of collection whose element type is a supertype of the element type of the array.

```java
Object[] oa = new Object[100];
Collection<Object> co = new ArrayList<Object>();

// T inferred to be Object
fromArrayToCollection(oa, co); 

String[] sa = new String[100];
Collection<String> cs = new ArrayList<String>();

// T inferred to be String
fromArrayToCollection(sa, cs);

// T inferred to be Object
fromArrayToCollection(sa, co);

Integer[] ia = new Integer[100];
Float[] fa = new Float[100];
Number[] na = new Number[100];
Collection<Number> cn = new ArrayList<Number>();

// T inferred to be Number
fromArrayToCollection(ia, cn);

// T inferred to be Number
fromArrayToCollection(fa, cn);

// T inferred to be Number
fromArrayToCollection(na, cn);

// T inferred to be Object
fromArrayToCollection(na, co);

// compile-time error
fromArrayToCollection(na, cs);
```

Notice that we don't have to pass an actual type argument to a generic method. The compiler infers the type argument for us, based on the types of the actual arguments. It will generally infer the most specific type argument that will make the call type-correct.

One question that arises is: when should I use generic methods, and when should I use wildcard types? To understand the answer, let's examine a few methods from the `Collection` libraries.

<pre>
interface Collection&lt;E> {
    public boolean containsAll(Collection<&#63;> c);
    public boolean addAll(Collection<&#63; extends E> c);
}
</pre>

We could have used generic methods here instead:

```java
interface Collection<E> {
    public <T> boolean containsAll(Collection<T> c);
    public <T extends E> boolean addAll(Collection<T> c);
    // Hey, type variables can have bounds too!
}
```

However, in both `containsAll` and `addAll`, the type parameter `T` is used only once. The return type doesn't depend on the type parameter, nor does any other argument to the method (in this case, there simply is only one argument). This tells us that the type argument is being used for polymorphism; its only effect is to allow a variety of actual argument types to be used at different invocation sites. If that is the case, one should use wildcards. Wildcards are designed to support flexible subtyping, which is what we're trying to express here.

Generic methods allow type parameters to be used to express dependencies among the types of one or more arguments to a method and/or its return type. If there isn't such a dependency, a generic method should not be used.

It is possible to use both generic methods and wildcards in tandem. Here is the method `Collections.copy()`:

<pre>
class Collections {
    public static &lt;T> void copy(List&lt;T> dest, List<&#63; extends T> src) {
    ...
}
</pre>

Note the dependency between the types of the two parameters. Any object copied from the source list, `src`, must be assignable to the element type `T` of the destination list, `dst`. So the element type of `src` can be any subtype of `T` -- we don't care which. The signature of `copy` expresses the dependency using a type parameter, but uses a wildcard for the element type of the second parameter.

We could have written the signature for this method another way, without using wildcards at all:

```java
class Collections {
    public static <T, S extends T> void copy(List<T> dest, List<S> src) {
    ...
}
```

This is fine, but while the first type parameter is used both in the type of dst and in the bound of the second type parameter, `S`, `S` itself is only used once, in the type of `src` -- nothing else depends on it. This is a sign that we can replace `S` with a wildcard. Using wildcards is clearer and more concise than declaring explicit type parameters, and should therefore be preferred whenever possible.

Wildcards also have the advantage that they can be used outside of method signatures, as the types of fields, local variables and arrays. Here is an example.

Returning to our shape drawing problem, suppose we want to keep a history of drawing requests. We can maintain the history in a static variable inside class `Shape`, and have `drawAll()` store its incoming argument into the history field.

<pre>
static List&lt;List<&#63; extends Shape>> 
    history = new ArrayList&lt;List<&#63; extends Shape>>();

public void drawAll(List<&#63; extends Shape> shapes) {
    history.addLast(shapes);
    for (Shape s: shapes) {
        s.draw(this);
    }
}
</pre>

Finally, again let's take note of the naming convention used for the type parameters. We use `T` for type, whenever there isn't anything more specific about the type to distinguish it. This is often the case in generic methods. If there are multiple type parameters, we might use letters that neighbor `T` in the alphabet, such as `S`. If a generic method appears inside a generic class, it's a good idea to avoid using the same names for the type parameters of the method and class, to avoid confusion. The same applies to nested generic classes.
