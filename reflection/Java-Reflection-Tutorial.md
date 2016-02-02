[TOC]

# Introduction

## Uses of Reflection

Reflection is commonly used by programs which require the ability to examine or modify the runtime behavior of applications running in the Java virtual machine. This is a relatively advanced feature and should be used only by developers who have a strong grasp of the fundamentals of the language. With that caveat in mind, reflection is a powerful technique and can enable applications to perform operations which would otherwise be impossible.

<dl>
	<dt style="font-weight: bold">Extensibility Features</dt>
	<dd>An application may make use of external, user-defined classes by creating instances of extensibility objects using their fully-qualified names.</dd>
	<dt style="font-weight: bold">Class Browsers and Visual Development Environments</dt>
	<dd>A class browser needs to be able to enumerate the members of classes. Visual development environments can benefit from making use of type information available in reflection to aid the developer in writing correct code.</dd>
	<dt style="font-weight: bold">Debuggers and Test Tools</dt>
	<dd>Debuggers need to be able to examine private members on classes. Test harnesses can make use of reflection to systematically call a discoverable set APIs defined on a class, to insure a high level of code coverage in a test suite.</dd>
</dl>

### Drawbacks of Reflection

Reflection is powerful, but should not be used indiscriminately. If it is possible to perform an operation without using reflection, then it is preferable to avoid using it. The following concerns should be kept in mind when accessing code via reflection.

<dl>
	<dt style="font-weight: bold">Performance Overhead</dt>
	<dd>Because reflection involves types that are dynamically resolved, certain Java virtual machine optimizations can not be performed. Consequently, reflective operations have slower performance than their non-reflective counterparts, and should be avoided in sections of code which are called frequently in performance-sensitive applications.</dd>
	<dt style="font-weight: bold">Security Restrictions</dt>
	<dd>Reflection requires a runtime permission which may not be present when running under a security manager. This is in an important consideration for code which has to run in a restricted security context, such as in an Applet.</dd>
	<dt style="font-weight: bold">Exposure of Internals</dt>
	<dd>Since reflection allows code to perform operations that would be illegal in non-reflective code, such as accessing `private` fields and methods, the use of reflection can result in unexpected side-effects, which may render code dysfunctional and may destroy portability. Reflective code breaks abstractions and therefore may change behavior with upgrades of the platform.</dd>
</dl>

### Trail Lessons

This trail covers common uses of reflection for accessing and manipulating classes, fields, methods, and constructors. Each lesson contains code examples, tips, and troubleshooting information.

<dl>
	<dt>Classes</dt>
	<dd>This lesson shows the various ways to obtain a `Class` object and use it to examine properties of a class, including its declaration and contents.</dd>
	<dt>Members</dt>
	<dd>This lesson describes how to use the Reflection APIs to find the fields, methods, and constructors of a class. Examples are provided for setting and getting field values, invoking methods, and creating new instances of objects using specific constructors.</dd>
	<dt>Arrays and Enumerated Types</dt>
	<dd>This lesson introduces two special types of classes: arrays, which are generated at runtime, and `enum` types, which define unique named object instances. Sample code shows how to retrieve the component type for an array and how to set and get fields with array or `enum` types.</dd>
</dl>

---

**Note:** 
The examples in this trail are designed for experimenting with the Reflection APIs. The handling of exceptions therefore is not the same as would be used in production code. In particular, in production code it is not recommended to dump stack traces that are visible to the user.

---

# Classes

Every object is either a reference or primitive type. Reference types all inherit from [`java.lang.Object`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html). Classes, enums, arrays, and interfaces are all reference types. There is a fixed set of primitive types: `boolean`, `byte`, `short`, `int`, `long`, `char`, `float`, and `double`. Examples of reference types include [`java.lang.String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), all of the wrapper classes for primitive types such as [`java.lang.Double`](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html), the interface [`java.io.Serializable`](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html), and the enum [`javax.swing.SortOrder`](https://docs.oracle.com/javase/8/docs/api/javax/swing/SortOrder.html).

For every type of object, the Java virtual machine instantiates an immutable instance of [`java.lang.Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) which provides methods to examine the runtime properties of the object including its members and type information. [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) also provides the ability to create new classes and objects. Most importantly, it is the entry point for all of the Reflection APIs. This lesson covers the most commonly used reflection operations involving classes:

*   **Retrieving Class Objects** describes the ways to get a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)
*   **Examining Class Modifiers and Types** shows how to access the class declaration information
*   **Discovering Class Members** illustrates how to list the constructors, fields, methods, and nested classes in a class
*   **Troubleshooting** describes common errors encountered when using [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)

## Retrieving Class Objects

The entry point for all reflection operations is [`java.lang.Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). With the exception of [`java.lang.reflect.ReflectPermission`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/ReflectPermission.html), none of the classes in [`java.lang.reflect`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/package-summary.html) have public constructors. To get to these classes, it is necessary to invoke appropriate methods on [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). There are several ways to get a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) depending on whether the code has access to an object, the name of class, a type, or an existing [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html).

### Object.getClass()

If an instance of an object is available, then the simplest way to get its [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) is to invoke [`Object.getClass()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--). Of course, this only works for reference types which all inherit from [`Object`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html). Some examples follow.

```java
Class c = "foo".getClass();
```

Returns the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) for [`String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)

```java
Class c = System.console().getClass();
```

here is a unique console associated with the virtual machine which is returned by the `static` method [`System.console()`](https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#console--). The value returned by [`getClass()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--) is the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to [`java.io.Console`](https://docs.oracle.com/javase/8/docs/api/java/io/Console.html).

```java
enum E { A, B }
Class c = A.getClass();
```

`A` is is an instance of the enum `E`; thus [`getClass()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--) returns the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to the enumeration type `E`.

```java
byte[] bytes = new byte[1024];
Class c = bytes.getClass();
```

Since arrays are [`Objects`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html), it is also possible to invoke [`getClass()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--) on an instance of an array. The returned [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponds to an array with component type `byte`.

```java
import java.util.HashSet;
import java.util.Set;

Set<String> s = new HashSet<String>();
Class c = s.getClass();
```

In this case, [`java.util.Set`](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html) is an interface to an object of type [`java.util.HashSet`](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html). The value returned by [`getClass()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#getClass--) is the class corresponding to [`java.util.HashSet`](https://docs.oracle.com/javase/8/docs/api/java/util/HashSet.html).

### The .class Syntax

If the type is available but there is no instance then it is possible to obtain a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) by appending `.class` to the name of the type. This is also the easiest way to obtain the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) for a primitive type.

```java
boolean b;
Class c = b.getClass();   // compile-time error

Class c = boolean.class;  // correct
```

Note that the statement `boolean.getClass()` would produce a compile-time error because a `boolean` is a primitive type and cannot be dereferenced. The `.class` syntax returns the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to the type `boolean`.

```java
Class c = java.io.PrintStream.class;
```

The variable `c` will be the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to the type [`java.io.PrintStream`](https://docs.oracle.com/javase/8/docs/api/java/io/PrintStream.html).

```java
Class c = int[][][].class;
```

The `.class` syntax may be used to retrieve a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to a multi-dimensional array of a given type.

### Class.forName()

If the fully-qualified name of a class is available, it is possible to get the corresponding [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) using the static method [`Class.forName()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#forName-java.lang.String-). This cannot be used for primitive types. The syntax for names of array classes is described by [`Class.getName()`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html#getName--). This syntax is applicable to references and primitive types.

```java
Class c = Class.forName("com.duke.MyLocaleServiceProvider");
```

This statement will create a class from the given fully-qualified name.

```java
Class cDoubleArray = Class.forName("[D");

Class cStringArray = Class.forName("[[Ljava.lang.String;");
```

The variable `cDoubleArray` will contain the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to an array of primitive type `double` (i.e. the same as `double[].class`). The `cStringArray` variable will contain the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) corresponding to a two-dimensional array of [`String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) (i.e. identical to `String[][].class`).

### TYPE Field for Primitive Type Wrappers

The `.class` syntax is a more convenient and the preferred way to obtain the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) for a primitive type; however there is another way to acquire the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). Each of the primitive types and `void` has a wrapper class in [`java.lang`](https://docs.oracle.com/javase/8/docs/api/java/lang/package-summary.html) that is used for boxing of primitive types to reference types. Each wrapper class contains a field named `TYPE` which is equal to the [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) for the primitive type being wrapped.

```java
Class c = Double.TYPE;
```

There is a class [`java.lang.Double`](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html) which is used to wrap the primitive type `double` whenever an [`Object`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html) is required. The value of [`Double.TYPE`](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html#TYPE) is identical to that of `double.class`.

```java
Class c = Void.TYPE;
```

</div>

[`Void.TYPE`](https://docs.oracle.com/javase/8/docs/api/java/lang/Void.html#TYPE) is identical to `void.class`.

### Methods that Return Classes

There are several Reflection APIs which return classes but these may only be accessed if a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) has already been obtained either directly or indirectly.

<dl>
	<dt>Class.getSuperclass()</dt>
	<dd>
		Returns the super class for the given class.
		<pre>Class c = javax.swing.JButton.class.getSuperclass();</pre>
		The super class of <code>javax.swing.JButton</code> is <code>javax.swing.AbstractButton</code>.
	</dd>
</dl>
<dl>
	<dt>Class.getClasses()</dt>
	<dd>
		Returns all the public classes, interfaces, and enums that are members of the class including inherited members.
		<pre>Class<&#63;>[] c = Character.class.getClasses();</pre>
		<code>Character</code> contains two member classes <code>Character.Subset</code> and <code>Character.UnicodeBlock</code>.</dd>
</dl>
<dl>
	<dt>Class.getDeclaredClasses()</dt>
	<dd>
		Returns all of the classes interfaces, and enums that are explicitly declared in this class.
		<pre>Class<&#63;>[] c = Character.class.getDeclaredClasses();</pre>
		<code>Character</code> contains two public member classes <code>Character.Subset</code> and <code>Character.UnicodeBlock</code> and one private class <code>Character.CharacterCache</code>.
	</dd>
</dl>
<dl>
	<dt>
		Class.getDeclaringClass(), Field.getDeclaringClass(), Method.getDeclaringClass(), Constructor.getDeclaringClass()
	</dt>
	<dd>
		Returns the <code>Class</code> in which these members were declared. Anonymous Class Declarations will not have a declaring class but will have an enclosing class.
		<pre>
import java.lang.reflect.Field;

Field f = System.class.getField("out");
Class c = f.getDeclaringClass();
</pre>
		The field <code>out</code> is declared in <code>System</code>.
		<pre>
public class MyClass {
    static Object o = new Object() {
        public void m() {} 
    };
    static Class&lt;c> = o.getClass().getEnclosingClass();
}
</pre>
		The declaring class of the anonymous class defined by `o` is `null`.
	</dd>
</dl>

<dl>
	<dt>Class.getEnclosingClass()</dt>
	<dd>
		Returns the immediately enclosing class of the class.
		<pre>Class c = Thread.State.class().getEnclosingClass();</pre>
		The enclosing class of the enum <code>Thread.State</code> is <code>Thread</code>.
		<pre>
public class MyClass {
    static Object o = new Object() { 
        public void m() {} 
    };
    static Class&lt;c> = o.getClass().getEnclosingClass();
}
</pre>
		The anonymous class defined by <code>o</code> is enclosed by <code>MyClass</code>.
	</dd>
</dl>

## Examining Class Modifiers and Types

A class may be declared with one or more modifiers which affect its runtime behavior:

- Access modifiers: `public`, `protected`, and `private`
- Modifier requiring override: `abstract`
- Modifier restricting to one instance: `static`
- Modifier prohibiting value modification: `final`
- Modifier forcing strict floating point behavior: `strictfp`
- Annotations

Not all modifiers are allowed on all classes, for example an interface cannot be `final` and an enum cannot be `abstract`. `java.lang.reflect.Modifier` contains declarations for all possible modifiers. It also contains methods which may be used to decode the set of modifiers returned by `Class.getModifiers()`.

The `ClassDeclarationSpy` example shows how to obtain the declaration components of a class including the modifiers, generic type parameters, implemented interfaces, and the inheritance path. Since `Class` implements the `java.lang.reflect.AnnotatedElement` interface it is also possible to query the runtime annotations.

```java
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

public class ClassDeclarationSpy {
    public static void main(String... args) {
		try {
	    	Class<?> c = Class.forName(args[0]);
	    	out.format("Class:%n  %s%n%n", c.getCanonicalName());
	    	out.format("Modifiers:%n  %s%n%n",
		    	Modifier.toString(c.getModifiers()));

	    	out.format("Type Parameters:%n");
	    	TypeVariable[] tv = c.getTypeParameters();
	    	if (tv.length != 0) {
				out.format("  ");
				for (TypeVariable t : tv)
			    	out.format("%s ", t.getName());
				out.format("%n%n");
	    	} else {
				out.format("  -- No Type Parameters --%n%n");
	    	}

	    	out.format("Implemented Interfaces:%n");
	    	Type[] intfs = c.getGenericInterfaces();
	    	if (intfs.length != 0) {
				for (Type intf : intfs)
		 			out.format("  %s%n", intf.toString());
				out.format("%n");
	    	} else {
				out.format("  -- No Implemented Interfaces --%n%n");
	    	}

	    	out.format("Inheritance Path:%n");
	    	List<Class> l = new ArrayList<Class>();
	    	printAncestor(c, l);
	    	if (l.size() != 0) {
				for (Class<?> cl : l)
		 			out.format("  %s%n", cl.getCanonicalName());
				out.format("%n");
	    	} else {
				out.format("  -- No Super Classes --%n%n");
	    	}

	    	out.format("Annotations:%n");
	    	Annotation[] ann = c.getAnnotations();
	    	if (ann.length != 0) {
				for (Annotation a : ann)
		   			out.format("  %s%n", a.toString());
				out.format("%n");
	    	} else {
				out.format("  -- No Annotations --%n%n");
	    	}

        	// production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
	    	x.printStackTrace();
		}
    }

    private static void printAncestor(Class<?> c, List<Class> l) {
		Class<?> ancestor = c.getSuperclass();
 		if (ancestor != null) {
	    	l.add(ancestor);
	    	printAncestor(ancestor, l);
 		}
    }
}
```

A few samples of the output follows. User input is in italics.

<pre>
$ <em>java ClassDeclarationSpy java.util.concurrent.ConcurrentNavigableMap</em>
Class:
  java.util.concurrent.ConcurrentNavigableMap

Modifiers:
  public abstract interface

Type Parameters:
  K V

Implemented Interfaces:
  java.util.concurrent.ConcurrentMap<K, V>
  java.util.NavigableMap<K, V>

Inheritance Path:
  -- No Super Classes --

Annotations:
  -- No Annotations --
</pre>

This is the actual declaration for `java.util.concurrent.ConcurrentNavigableMap` in the source code:

```java
public interface ConcurrentNavigableMap<K,V>
    extends ConcurrentMap<K,V>, NavigableMap<K,V>
```

Note that since this is an interface, it is implicitly `abstract`. The compiler adds this modifier for every interface. Also, this declaration contains two generic type parameters, `K` and `V`. The example code simply prints the names of these parameters, but is it possible to retrieve additional information about them using methods in `java.lang.reflect.TypeVariable`. Interfaces may also implement other interfaces as shown above.

<pre>
$ <em>java ClassDeclarationSpy "[Ljava.lang.String;"</em>
Class:
  java.lang.String[]

Modifiers:
  public abstract final

Type Parameters:
  -- No Type Parameters --

Implemented Interfaces:
  interface java.lang.Cloneable
  interface java.io.Serializable

Inheritance Path:
  java.lang.Object

Annotations:
  -- No Annotations --
</pre>

Since arrays are runtime objects, all of the type information is defined by the Java virtual machine. In particular, arrays implement `Cloneable` and `java.io.Serializable` and their direct superclass is always `Object`.

<pre>
$ <em>java ClassDeclarationSpy java.io.InterruptedIOException</em>
Class:
  java.io.InterruptedIOException

Modifiers:
  public

Type Parameters:
  -- No Type Parameters --

Implemented Interfaces:
  -- No Implemented Interfaces --

Inheritance Path:
  java.io.IOException
  java.lang.Exception
  java.lang.Throwable
  java.lang.Object

Annotations:
  -- No Annotations --
</pre>

From the inheritance path, it may be deduced that `java.io.InterruptedIOException` is a checked exception because `RuntimeException` is not present.

<pre>
$ <em>java ClassDeclarationSpy java.security.Identity</em>
Class:
  java.security.Identity

Modifiers:
  public abstract

Type Parameters:
  -- No Type Parameters --

Implemented Interfaces:
  interface java.security.Principal
  interface java.io.Serializable

Inheritance Path:
  java.lang.Object

Annotations:
  @java.lang.Deprecated()
</pre>

This output shows that `java.security.Identity`, a deprecated API, possesses the annotation `java.lang.Deprecated`. This may be used by reflective code to detect deprecated APIs.

---

**Note:** Not all annotations are available via reflection. Only those which have a `java.lang.annotation.RetentionPolicy` of `RUNTIME` are accessible. Of the three annotations pre-defined in the language `@Deprecated`, `@Override`, and `@SuppressWarnings` only `@Deprecated` is available at runtime.

---

## Discovering Class Members

There are two categories of methods provided in `Class` for accessing fields, methods, and constructors: methods which enumerate these members and methods which search for particular members. Also there are distinct methods for accessing members declared directly on the class versus methods which search the superinterfaces and superclasses for inherited members. The following tables provide a summary of all the member-locating methods and their characteristics.

<table summary="Class Methods for Locating Fields">
	<caption>Class Methods for Locating Fields</caption>
	<tr>
		<th><code>Class</code> API</th>
		<th>List of members?</th>
		<th>Inherited members?</th>
		<th>Private members?</th>
	</tr>
	<tr>
		<td><code>getDeclaredField()</a></td>
		<td align="center">no</td>
		<td align="center">no</td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getField()</a></td>
		<td align="center">no</td>
		<td align="center">yes</td>
		<td align="center">no</td>
	</tr>
	<tr>
		<td><code>getDeclaredFields()</code></td>
		<td align="center">yes</td>
		<td align="center">no</td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getFields()</code></td>
		<td align="center">yes</td>
		<td align="center">yes</td>
		<td align="center">no</td>
	</tr>
</table>

<table summary="Class Methods for Locating Methods">
	<caption>Class Methods for Locating Methods</caption>
	<tr>
		<th><code>Class</code> API</th>
		<th>List of members?</th>
		<th>Inherited members?</th>
		<th>Private members?</th>
	</tr>
	<tr>
		<td><code>getDeclaredMethod()</code></td>
		<td align="center">no</td>
		<td align="center">no</td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getMethod()</code></td>
		<td align="center">no</td>
		<td align="center">yes</td>
		<td align="center">no</td>
	</tr>
	<tr>
		<td><code>getDeclaredMethods()</code></td>
		<td align="center">yes</td>
		<td align="center">no</td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getMethods()</code></td>
		<td align="center">yes</td>
		<td align="center">yes</td>
		<td align="center">no</td>
	</tr>
</table>

<table summary="Class Methods for Locating Constructors">
	<caption>Class Methods for Locating Constructors</caption>
	<tr>
		<th><code>Class</code> API</th>
		<th>List of members?</th>
		<th>Inherited members?</th>
		<th>Private members?</th>
	</tr>
	<tr>
		<td><code>getDeclaredConstructor()</code></td>
		<td align="center">no</td>
		<td align="center">N/A<sup>1</sup></td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getConstructor()</code></td>
		<td align="center">no</td>
		<td align="center">N/A<sup>1</sup></td>
		<td align="center">no</td>
	</tr>
	<tr>
		<td><code>getDeclaredConstructors()</code></td>
		<td align="center">yes</td>
		<td align="center">N/A<sup>1</sup></td>
		<td align="center">yes</td>
	</tr>
	<tr>
		<td><code>getConstructors()</code></td>
		<td align="center">yes</td>
		<td align="center">N/A<sup>1</sup></td>
		<td align="center">no</td>
	</tr>
</table>
<p><sup>1</sup> Constructors are not inherited.</p>

Given a class name and an indication of which members are of interest, the `ClassSpy` example uses the `get*s()` methods to determine the list of all public elements, including any which are inherited.

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import static java.lang.System.out;

enum ClassMember { CONSTRUCTOR, FIELD, METHOD, CLASS, ALL }

public class ClassSpy {
    public static void main(String... args) {
		try {
	    	Class<?> c = Class.forName(args[0]);
	    	out.format("Class:%n  %s%n%n", c.getCanonicalName());

	    	Package p = c.getPackage();
	    	out.format("Package:%n  %s%n%n",
		            (p != null ? p.getName() : "-- No Package --"));

	    	for (int i = 1; i < args.length; i++) {
				switch (ClassMember.valueOf(args[i])) {
				case CONSTRUCTOR:
		    		printMembers(c.getConstructors(), "Constructor");
		    		break;
				case FIELD:
		    		printMembers(c.getFields(), "Fields");
		    		break;
				case METHOD:
		    		printMembers(c.getMethods(), "Methods");
		    		break;
				case CLASS:
		    		printClasses(c);
		    		break;
				case ALL:
		    		printMembers(c.getConstructors(), "Constuctors");
		    		printMembers(c.getFields(), "Fields");
		    		printMembers(c.getMethods(), "Methods");
		    		printClasses(c);
		    		break;
				default:
		    		assert false;
				}
	    	}

        	// production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
	    	x.printStackTrace();
		}
    }

    private static void printMembers(Member[] mbrs, String s) {
		out.format("%s:%n", s);
		for (Member mbr : mbrs) {
	    	if (mbr instanceof Field)
				out.format("  %s%n", ((Field)mbr).toGenericString());
	    	else if (mbr instanceof Constructor)
				out.format("  %s%n", ((Constructor)mbr).toGenericString());
	    	else if (mbr instanceof Method)
				out.format("  %s%n", ((Method)mbr).toGenericString());
		}
		if (mbrs.length == 0)
	    	out.format("  -- No %s --%n", s);
		out.format("%n");
    }

    private static void printClasses(Class<?> c) {
		out.format("Classes:%n");
		Class<?>[] clss = c.getClasses();
		for (Class<?> cls : clss)
	    	out.format("  %s%n", cls.getCanonicalName());
		if (clss.length == 0)
	    	out.format("  -- No member interfaces, classes, or enums --%n");
		out.format("%n");
    }
}
```

This example is relatively compact; however the `printMembers()` method is slightly awkward due to the fact that the `java.lang.reflect.Member` interface has existed since the earliest implementations of reflection and it could not be modified to include the more useful `getGenericString()` method when generics were introduced. The only alternatives are to test and cast as shown, replace this method with `printConstructors()`, `printFields()`, and `printMethods()`, or to be satisfied with the relatively spare results of `Member.getName()`.

Samples of the output and their interpretation follows. User input is in italics.

<pre>
$ <em>java ClassSpy java.lang.ClassCastException CONSTRUCTOR</em>
Class:
  java.lang.ClassCastException

Package:
  java.lang

Constructor:
  public java.lang.ClassCastException()
  public java.lang.ClassCastException(java.lang.String)
</pre>

Since constructors are not inherited, the exception chaining mechanism constructors (those with a `Throwable` parameter) which are defined in the immediate super class `RuntimeException` and other super classes are not found.

<pre>
$ <em>java ClassSpy java.nio.channels.ReadableByteChannel METHOD</em>
Class:
  java.nio.channels.ReadableByteChannel

Package:
  java.nio.channels

Methods:
  public abstract int java.nio.channels.ReadableByteChannel.read
    (java.nio.ByteBuffer) throws java.io.IOException
  public abstract void java.nio.channels.Channel.close() throws
    java.io.IOException
  public abstract boolean java.nio.channels.Channel.isOpen()
</pre>

The interface `java.nio.channels.ReadableByteChannel` defines `read()`. The remaining methods are inherited from a super interface. This code could easily be modified to list only those methods that are actually declared in the class by replacing `get*s()` with `getDeclared*s()`.

<pre>
$ <em>java ClassSpy ClassMember FIELD METHOD</em>
Class:
  ClassMember

Package:
  -- No Package --

Fields:
  public static final ClassMember ClassMember.CONSTRUCTOR
  public static final ClassMember ClassMember.FIELD
  public static final ClassMember ClassMember.METHOD
  public static final ClassMember ClassMember.CLASS
  public static final ClassMember ClassMember.ALL

Methods:
  public static ClassMember ClassMember.valueOf(java.lang.String)
  public static ClassMember[] ClassMember.values()
  public final int java.lang.Enum.hashCode()
  public final int java.lang.Enum.compareTo(E)
  public int java.lang.Enum.compareTo(java.lang.Object)
  public final java.lang.String java.lang.Enum.name()
  public final boolean java.lang.Enum.equals(java.lang.Object)
  public java.lang.String java.lang.Enum.toString()
  public static &lt;T> T java.lang.Enum.valueOf
    (java.lang.Class&lt;T>,java.lang.String)
  public final java.lang.Class&lt;E> java.lang.Enum.getDeclaringClass()
  public final int java.lang.Enum.ordinal()
  public final native java.lang.Class<&#63;> java.lang.Object.getClass()
  public final native void java.lang.Object.wait(long) throws
    java.lang.InterruptedException
  public final void java.lang.Object.wait(long,int) throws
    java.lang.InterruptedException
  public final void java.lang.Object.wait() hrows java.lang.InterruptedException
  public final native void java.lang.Object.notify()
  public final native void java.lang.Object.notifyAll()
</pre>

In the fields portion of these results, enum constants are listed. While these are technically fields, it might be useful to distinguish them from other fields. This example could be modified to use `java.lang.reflect.Field`\.isEnumConstant()` for this purpose. The `EnumSpy` example in a later section of this trail, **Examining Enums**, contains a possible implementation.

In the methods section of the output, observe that the method name includes the name of the declaring class. Thus, the `toString()` method is implemented by `Enum`, not inherited from `Object`. The code could be amended to make this more obvious by using `Field.getDeclaringClass()`. The following fragment illustrates part of a potential solution.

```java
if (mbr instanceof Field) {
    Field f = (Field)mbr;
    out.format("  %s%n", f.toGenericString());
    out.format("  -- declared in: %s%n", f.getDeclaringClass());
}
```

## Troubleshooting

The following examples show typical errors which may be encountered when reflecting on classes.

### Compiler Warning: "Note: ... uses unchecked or unsafe operations"

When a method is invoked, the types of the argument values are checked and possibly converted. `ClassWarning` invokes `getMethod()` to cause a typical unchecked conversion warning:

```java
import java.lang.reflect.Method;

public class ClassWarning {
    void m() {
	try {
	    Class c = ClassWarning.class;
	    Method m = c.getMethod("m");  // warning

        // production code should handle this exception more gracefully
	} catch (NoSuchMethodException x) {
    	    x.printStackTrace();
    	}
    }
}
```

<pre>
$ <em>javac ClassWarning.java</em>
Note: ClassWarning.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
$ <em>javac -Xlint:unchecked ClassWarning.java</em>
ClassWarning.java:6: warning: [unchecked] unchecked call to getMethod
  (String,Class<&#63;>...) as a member of the raw type Class
Method m = c.getMethod("m");  // warning
                      ^
1 warning
</pre>

Many library methods have been retrofitted with generic declarations including several in `Class`. Since `c` is declared as a *raw* type (has no type parameters) and the corresponding parameter of `getMethod()` is a parameterized type, an unchecked conversion occurs. The compiler is required to generate a warning. (See *The Java Language Specification, Java SE 7 Edition*, sections *Unchecked Conversion* and *Method Invocation Conversion*.)

There are two possible solutions. The more preferable it to modify the declaration of `c` to include an appropriate generic type. In this case, the declaration should be:

```java
Class<?> c = warn.getClass();
```

Alternatively, the warning could be explicitly suppressed using the predefined annotation `@SuppressWarnings` preceding the problematic statement.

```java
Class c = ClassWarning.class;
@SuppressWarnings("unchecked")
Method m = c.getMethod("m");  
// warning gone
```

---

**Tip:** As a general principle, warnings should not be ignored as they may indicate the presence of a bug. Parameterized declarations should be used as appropriate. If that is not possible (perhaps because an application must interact with a library vendor's code), annotate the offending line using `@SuppressWarnings.`

---

### InstantiationException when the Constructor is Not Accessible

`Class.newInstance()` will throw an `InstantiationException` if an attempt is made to create a new instance of the class and the zero-argument constructor is not visible. The `ClassTrouble` example illustrates the resulting stack trace.

```java
class Cls {
    private Cls() {}
}

public class ClassTrouble {
    public static void main(String... args) {
		try {
	    	Class<?> c = Class.forName("Cls");
	    	c.newInstance();  // InstantiationException

        	// production code should handle these exceptions more gracefully
		} catch (InstantiationException x) {
	    	x.printStackTrace();
		} catch (IllegalAccessException x) {
	    	x.printStackTrace();
		} catch (ClassNotFoundException x) {
	   		x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java ClassTrouble</em>
java.lang.IllegalAccessException: Class ClassTrouble can not access a member of
  class Cls with modifiers "private"
        at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:65)
        at java.lang.Class.newInstance0(Class.java:349)
        at java.lang.Class.newInstance(Class.java:308)
        at ClassTrouble.main(ClassTrouble.java:9)
</pre>

`Class.newInstance()` behaves very much like the `new` keyword and will fail for the same reasons new would fail. The typical solution in reflection is to take advantage of the `java.lang.reflect.AccessibleObject` class which provides the ability to suppress access control checks; however, this approach will not work because `java.lang.Class` does not extend `AccessibleObject`. The only solution is to modify the code to use `Constructor.newInstance()` which does extend `AccessibleObject`.

---

**Tip:** In general, it is preferable to use `Constructor.newInstance()` for the reasons described in the *Creating New Class Instances* section in the *Members* lesson.

---

Additional examples of potential problems using `Constructor.newInstance()` may be found in the *Constructor Troubleshooting* section of the *Members* lesson.

# Members

Reflection defines an interface `java.lang.reflect.Member` which is implemented by `java.lang.reflect.Field`, `java.lang.reflect.Method`, and `java.lang.reflect.Constructor`. These objects will be discussed in this lesson. For each member, the lesson will describe the associated APIs to retrieve declaration and type information, any operations unique to the member (for example, setting the value of a field or invoking a method), and commonly encountered errors. Each concept will be illustrated with code samples and related output which approximate some expected reflection uses.

---

**Note:** According to *The Java Language Specification, Java SE 7 Edition*, the members of a class are the inherited components of the class body including fields, methods, nested classes, interfaces, and enumerated types. Since constructors are not inherited, they are not members. This differs from the implementing classes of `java.lang.reflect.Member`.

## Fields

A *field* is a class, interface, or enum with an associated value. Methods in the `java.lang.reflect.Field` class can retrieve information about the field, such as its name, type, modifiers, and annotations. (The section *Examining Class Modifiers and Types* in the *Classes* lesson describes how to retrieve annotations.) There are also methods which enable dynamic access and modification of the value of the field. These tasks are covered in the following sections:

- *Obtaining Field Types* describes how to get the declared and generic types of a field
- *Retrieving and Parsing Field Modifiers* shows how to get portions of the field declaration such as `public` or `transient`
- *Getting and Setting Field Values* illustrates how to access field values
- *Troubleshooting* describes some common coding errors which may cause confusion

When writing an application such as a class browser, it might be useful to find out which fields belong to a particular class. A class's fields are identified by invoking `Class.getFields()`. The `getFields()` method returns an array of `Field` objects containing one object per accessible public field.

A public field is accessible if it is a member of either:

- this class
- a superclass of this class
- an interface implemented by this class
- an interface extended from an interface implemented by this class

A field may be a class (instance) field, such as `java.io.Reader.lock`, a static field, such as `java.lang.Integer.MAX_VALUE`, or an enum constant, such as `java.lang.Thread.State.WAITING`.

### Obtaining Field Types

A field may be either of primitive or reference type. There are eight primitive types: `boolean`, `byte`, `short`, `int`, `long`, `char`, `float`, and `double`. A reference type is anything that is a direct or indirect subclass of `java.lang.Object` including interfaces, arrays, and enumerated types.

The `FieldSpy` example prints the field's type and generic type given a fully-qualified binary class name and field name.

```java
import java.lang.reflect.Field;
import java.util.List;

public class FieldSpy<T> {
    public boolean[][] b = {{ false, false }, { true, true } };
    public String name  = "Alice";
    public List<Integer> list;
    public T val;

    public static void main(String... args) {
		try {
	    	Class<?> c = Class.forName(args[0]);
	    	Field f = c.getField(args[1]);
	    	System.out.format("Type: %s%n", f.getType());
	    	System.out.format("GenericType: %s%n", f.getGenericType());

        	// production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
	    	x.printStackTrace();
		} catch (NoSuchFieldException x) {
	    	x.printStackTrace();
		}
    }
}
```

Sample output to retrieve the type of the three public fields in this class (`b`, `name`, and the parameterized type `list`), follows. User input is in italics.

<pre>
$ <em>java FieldSpy FieldSpy b</em>
Type: class [[Z
GenericType: class [[Z
$ <em>java FieldSpy FieldSpy name</em>
Type: class java.lang.String
GenericType: class java.lang.String
$ <em>java FieldSpy FieldSpy list</em>
Type: interface java.util.List
GenericType: java.util.List&lt;java.lang.Integer>
$ <em>java FieldSpy FieldSpy val</em>
Type: class java.lang.Object
GenericType: T
</pre>

The type for the field `b` is two-dimensional array of boolean. The syntax for the type name is described in `Class.getName()`.

The type for the field `val` is reported as `java.lang.Object` because generics are implemented via *type erasure* which removes all information regarding generic types during compilation. Thus `T` is replaced by the upper bound of the type variable, in this case, `java.lang.Object`.

`Field.getGenericType()` will consult the Signature Attribute in the class file if it's present. If the attribute isn't available, it falls back on `Field.getType()` which was not changed by the introduction of generics. The other methods in reflection with name `getGenericFoo` for some value of *Foo* are implemented similarly.

### Retrieving and Parsing Field Modifiers

There are several modifiers that may be part of a field declaration:

- Access modifiers: `public`, `protected`, and `private`
- Field-specific modifiers governing runtime behavior: `transient` and `volatile`
- Modifier restricting to one instance: `static`
- Modifier prohibiting value modification: `final`
- Annotations

The method `Field.getModifiers()` can be used to return the integer representing the set of declared modifiers for the field. The bits representing the modifiers in this integer are defined in `java.lang.reflect.Modifier`.

The `FieldModifierSpy` example illustrates how to search for fields with a given modifier. It also determines whether the located field is synthetic (compiler-generated) or is an enum constant by invoking `Field.isSynthetic()` and `Field.isEnumCostant()` respectively.

```java
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static java.lang.System.out;

enum Spy { BLACK , WHITE }

public class FieldModifierSpy {
	volatile int share;
	int instance;
	class Inner {}

	public static void main(String... args) {
		try {
			Class<?> c = Class.forName(args[0]);
			int searchMods = 0x0;
			for (int i = 1; i < args.length; i++) {
				searchMods |= modifierFromString(args[i]);
	    	}

			Field[] flds = c.getDeclaredFields();
			out.format("Fields in Class '%s' containing modifiers:  %s%n",
				c.getName(),
				Modifier.toString(searchMods));
			boolean found = false;
			for (Field f : flds) {
				int foundMods = f.getModifiers();
				// Require all of the requested modifiers to be present
				if ((foundMods & searchMods) == searchMods) {
					out.format("%-8s [ synthetic=%-5b enum_constant=%-5b ]%n",
						f.getName(), f.isSynthetic(),
						f.isEnumConstant());
					found = true;
				}
			}

			if (!found) {
				out.format("No matching fields%n");
			}

				// production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
			x.printStackTrace();
		}
    }

    private static int modifierFromString(String s) {
		int m = 0x0;
		if ("public".equals(s))           m |= Modifier.PUBLIC;
		else if ("protected".equals(s))   m |= Modifier.PROTECTED;
		else if ("private".equals(s))     m |= Modifier.PRIVATE;
		else if ("static".equals(s))      m |= Modifier.STATIC;
		else if ("final".equals(s))       m |= Modifier.FINAL;
		else if ("transient".equals(s))   m |= Modifier.TRANSIENT;
		else if ("volatile".equals(s))    m |= Modifier.VOLATILE;
		return m;
    }
}
```

Sample output follows:

<pre>
$ <em>java FieldModifierSpy FieldModifierSpy volatile</em>
Fields in Class 'FieldModifierSpy' containing modifiers:  volatile
share    [ synthetic=false enum_constant=false ]

$ <em>java FieldModifierSpy Spy public</em>
Fields in Class 'Spy' containing modifiers:  public
BLACK    [ synthetic=false enum_constant=true  ]
WHITE    [ synthetic=false enum_constant=true  ]

$ <em>java FieldModifierSpy FieldModifierSpy\$Inner final</em>
Fields in Class 'FieldModifierSpy$Inner' containing modifiers:  final
this$0   [ synthetic=true  enum_constant=false ]

$ <em>java FieldModifierSpy Spy private static final</em>
Fields in Class 'Spy' containing modifiers:  private static final
$VALUES  [ synthetic=true  enum_constant=false ]
</pre>

Notice that some fields are reported even though they are not declared in the original code. This is because the compiler will generate some *synthetic fields* which are needed during runtime. To test whether a field is synthetic, the example invokes `Field.isSynthetic()`. The set of synthetic fields is compiler-dependent; however commonly used fields include `this$0` for inner classes (i.e. nested classes that are not static member classes) to reference the outermost enclosing class and `$VALUES` used by enums to implement the implicitly defined static method `values()`. The names of synthetic class members are not specified and may not be the same in all compiler implementations or releases. These and other synthetic fields will be included in the array returned by `Class.getDeclaredFields()` but not identified by `Class.getField()` since synthetic members are not typically `public`.

Because `Field` implements the interface `java.lang.reflect.AnnotatedElement`, it is possible to retrieve any runtime annotation with `java.lang.annotation.RetentionPolicy.RUNTIME`. For an example of obtaining annotations see the section *Examining Class Modifiers and Types*.

### Getting and Setting Field Values

Given an instance of a class, it is possible to use reflection to set the values of fields in that class. This is typically done only in special circumstances when setting the values in the usual way is not possible. Because such access usually violates the design intentions of the class, it should be used with the utmost discretion.

The `Book` class illustrates how to set the values for long, array, and enum field types. Methods for getting and setting other primitive types are described in `Field`.

```java
import java.lang.reflect.Field;
import java.util.Arrays;
import static java.lang.System.out;

enum Tweedle { DEE, DUM }

public class Book {
    public long chapters = 0;
    public String[] characters = { "Alice", "White Rabbit" };
    public Tweedle twin = Tweedle.DEE;

    public static void main(String... args) {
		Book book = new Book();
		String fmt = "%6S:  %-12s = %s%n";

		try {
		    Class<?> c = book.getClass();

		    Field chap = c.getDeclaredField("chapters");
		    out.format(fmt, "before", "chapters", book.chapters);
	  	    chap.setLong(book, 12);
		    out.format(fmt, "after", "chapters", chap.getLong(book));

		    Field chars = c.getDeclaredField("characters");
		    out.format(fmt, "before", "characters",
			       Arrays.asList(book.characters));
		    String[] newChars = { "Queen", "King" };
		    chars.set(book, newChars);
		    out.format(fmt, "after", "characters",
			       Arrays.asList(book.characters));

		    Field t = c.getDeclaredField("twin");
		    out.format(fmt, "before", "twin", book.twin);
		    t.set(book, Tweedle.DUM);
		    out.format(fmt, "after", "twin", t.get(book));

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
```

This is the corresponding output:

<pre>
$ <em>java Book</em>
BEFORE:  chapters     = 0
 AFTER:  chapters     = 12
BEFORE:  characters   = [Alice, White Rabbit]
 AFTER:  characters   = [Queen, King]
BEFORE:  twin         = DEE
 AFTER:  twin         = DUM
</pre>

---

**Note:** Setting a field's value via reflection has a certain amount of performance overhead because various operations must occur such as validating access permissions. From the runtime's point of view, the effects are the same, and the operation is as atomic as if the value was changed in the class code directly.

Use of reflection can cause some runtime optimizations to be lost. For example, the following code is highly likely be optimized by a Java virtual machine:

<pre>
int x = 1;
x = 2;
x = 3;
</pre>

Equivalent code using `Field.set*()` may not.

---

### Troubleshooting

Here are a few common problems encountered by developers with explanations for why the occur and how to resolve them.

#### IllegalArgumentException due to Inconvertible Types

The `FieldTrouble` example will generate an `IllegalArgumentException`. `Field.setInt()` is invoked to set a field that is of the reference type `Integer` with a value of primitive type. In the non-reflection equivalent `Integer val = 42`, the compiler would convert (or *box*) the primitive type `42` to a reference type as `new Integer(42)` so that its type checking will accept the statement. When using reflection, type checking only occurs at runtime so there is no opportunity to box the value.

```java
import java.lang.reflect.Field;

public class FieldTrouble {
    public Integer val;

    public static void main(String... args) {
		FieldTrouble ft = new FieldTrouble();
		try {
		    Class<?> c = ft.getClass();
		    Field f = c.getDeclaredField("val");
	  	    f.setInt(ft, 42);               // IllegalArgumentException

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
	 	} catch (IllegalAccessException x) {
	 	    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java FieldTrouble</em>
Exception in thread "main" java.lang.IllegalArgumentException: Can not set
  java.lang.Object field FieldTrouble.val to (long)42
        at sun.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException
          (UnsafeFieldAccessorImpl.java:146)
        at sun.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException
          (UnsafeFieldAccessorImpl.java:174)
        at sun.reflect.UnsafeObjectFieldAccessorImpl.setLong
          (UnsafeObjectFieldAccessorImpl.java:102)
        at java.lang.reflect.Field.setLong(Field.java:831)
        at FieldTrouble.main(FieldTrouble.java:11)
</pre>

To eliminate this exception, the problematic line should be replaced by the following invocation of `Field.set(Object obj, Object value)`:

```java
f.set(ft, new Integer(43));
```

---

**Tip:** When using reflection to set or get a field, the compiler does not have an opportunity to perform boxing. It can only convert types that are related as described by the specification for `Class.isAssignableFrom()`. The example is expected to fail because `isAssignableFrom()` will return `false` in this test which can be used programmatically to verify whether a particular conversion is possible:

```java
Integer.class.isAssignableFrom(int.class) == false
```

Similarly, automatic conversion from primitive to reference type is also impossible in reflection.

```java
int.class.isAssignableFrom(Integer.class) == false
```

---

#### NoSuchFieldException for Non-Public Fields

The astute reader may notice that if the `FieldSpy` example shown earlier is used to get information on a non-public field, it will fail:

<pre>
$ <em>java FieldSpy java.lang.String count</em>
java.lang.NoSuchFieldException: count
        at java.lang.Class.getField(Class.java:1519)
        at FieldSpy.main(FieldSpy.java:12)
</pre>

---

**Tip:** The `Class.getField()` and `Class.getFields()` methods return the *public* member field(s) of the class, enum, or interface represented by the `Class` object. To retrieve all fields declared (but not inherited) in the `Class`, use the `Class.getDeclaredFields()` method.

---

#### IllegalAccessException when Modifying Final Fields

An `IllegalAccessException` may be thrown if an attempt is made to get or set the value of a `private` or otherwise inaccessible field or to set the value of a `final` field (regardless of its access modifiers).

The `FieldTroubleToo` example illustrates the type of stack trace which results from attempting to set a final field.

```java
import java.lang.reflect.Field;

public class FieldTroubleToo {
    public final boolean b = true;

    public static void main(String... args) {
		FieldTroubleToo ft = new FieldTroubleToo();
		try {
		    Class<?> c = ft.getClass();
		    Field f = c.getDeclaredField("b");
	// 	    f.setAccessible(true);  // solution
		    f.setBoolean(ft, Boolean.FALSE);   // IllegalAccessException

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		} catch (IllegalArgumentException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java FieldTroubleToo</em>
java.lang.IllegalAccessException: Can not set final boolean field
  FieldTroubleToo.b to (boolean)false
        at sun.reflect.UnsafeFieldAccessorImpl.
          throwFinalFieldIllegalAccessException(UnsafeFieldAccessorImpl.java:55)
        at sun.reflect.UnsafeFieldAccessorImpl.
          throwFinalFieldIllegalAccessException(UnsafeFieldAccessorImpl.java:63)
        at sun.reflect.UnsafeQualifiedBooleanFieldAccessorImpl.setBoolean
          (UnsafeQualifiedBooleanFieldAccessorImpl.java:78)
        at java.lang.reflect.Field.setBoolean(Field.java:686)
        at FieldTroubleToo.main(FieldTroubleToo.java:12)
</pre>

---

**Tip:** An access restriction exists which prevents `final` fields from being set after initialization of the class. However, `Field` is declared to extend `AccessibleObject` which provides the ability to suppress this check.

If `AccessibleObject.setAccessible()` succeeds, then subsequent operations on this field value will not fail do to this problem. This may have unexpected side-effects; for example, sometimes the original value will continue to be used by some sections of the application even though the value has been modified. `AccessibleObject.setAccessible()` will only succeed if the operation is allowed by the security context.

---

## Methods

A *method* contains executable code which may be invoked. Methods are inherited and in non-reflective code behaviors such as overloading, overriding, and hiding are enforced by the compiler. In contrast, reflective code makes it possible for method selection to be restricted to a specific class without considering its superclasses. Superclass methods may be accessed but it is possible to determine their declaring class; this is impossible to discover programmatically without reflection and is the source of many subtle bugs.

The `java.lang.reflect.Method` class provides APIs to access information about a method's modifiers, return type, parameters, annotations, and thrown exceptions. It also be used to invoke methods. These topics are covered by the following sections:

- **Obtaining Method Type Information** shows how to enumerate methods declared in a class and obtains type information
- **Obtaining Names of Method Parameters** shows how to retrieve names and other information of a method or constructor's parameters
- **Retrieving and Parsing Method Modifiers** describes how to access and decode modifiers and other information associated with the method
- **Invoking Methods** illustrates how to execute a method and obtain its return value
- **Troubleshooting** covers common errors encountered when finding or invoking methods

### Obtaining Method Type Information

A method declaration includes the name, modifiers, parameters, return type, and list of throwable exceptions. The `java.lang.reflect.Method` class provides a way to obtain this information.

The `MethodSpy` example illustrates how to enumerate all of the declared methods in a given class and retrieve the return, parameter, and exception types for all the methods of the given name.

```java
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import static java.lang.System.out;

public class MethodSpy {
    private static final String  fmt = "%24s: %s%n";

    // for the morbidly curious
    <E extends RuntimeException> void genericThrow() throws E {}

    public static void main(String... args) {
		try {
		    Class<?> c = Class.forName(args[0]);
		    Method[] allMethods = c.getDeclaredMethods();
		    for (Method m : allMethods) {
				if (!m.getName().equals(args[1])) {
				    continue;
				}
				out.format("%s%n", m.toGenericString());

				out.format(fmt, "ReturnType", m.getReturnType());
				out.format(fmt, "GenericReturnType", m.getGenericReturnType());

				Class<?>[] pType  = m.getParameterTypes();
				Type[] gpType = m.getGenericParameterTypes();
				for (int i = 0; i < pType.length; i++) {
				    out.format(fmt,"ParameterType", pType[i]);
				    out.format(fmt,"GenericParameterType", gpType[i]);
				}

				Class<?>[] xType  = m.getExceptionTypes();
				Type[] gxType = m.getGenericExceptionTypes();
				for (int i = 0; i < xType.length; i++) {
				    out.format(fmt,"ExceptionType", xType[i]);
				    out.format(fmt,"GenericExceptionType", gxType[i]);
				}
		    }

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
```

Here is the output for `Class.getConstructor()` which is an example of a method with parameterized types and a variable number of parameters.

<pre>
$ <em>java MethodSpy java.lang.Class getConstructor</em>
public java.lang.reflect.Constructor&lt;T> java.lang.Class.getConstructor
  (java.lang.Class&lt;&#63;>[]) throws java.lang.NoSuchMethodException,
  java.lang.SecurityException
              ReturnType: class java.lang.reflect.Constructor
       GenericReturnType: java.lang.reflect.Constructor&lt;T>
           ParameterType: class [Ljava.lang.Class;
    GenericParameterType: java.lang.Class<&#63;>[]
           ExceptionType: class java.lang.NoSuchMethodException
    GenericExceptionType: class java.lang.NoSuchMethodException
           ExceptionType: class java.lang.SecurityException
    GenericExceptionType: class java.lang.SecurityException
</pre>

This is the actual declaration of the method in source code:

<pre>
public Constructor&lt;T> getConstructor(Class<&#63;>... parameterTypes)
</pre>

First note that the return and parameter types are generic. `Method.getGenericReturnType()` will consult the Signature Attribute in the class file if it's present. If the attribute isn't available, it falls back on `Method.getReturnType()` which was not changed by the introduction of generics. The other methods with name `getGenericFoo()` for some value of Foo in reflection are implemented similarly.

Next, notice that the last (and only) parameter, `parameterType`, is of variable arity (has a variable number of parameters) of type `java.lang.Class`. It is represented as a single-dimension array of type `java.lang.Class`. This can be distinguished from a parameter that is explicitly an array of `java.lang.Class` by invoking `Method.isVarArgs()`. The syntax for the returned values of `Method.get*Types()` is described in `Class.getName()`.

The following example illustrates a method with a generic return type.

<pre>
$ <em>java MethodSpy java.lang.Class cast</em>
public T java.lang.Class.cast(java.lang.Object)
              ReturnType: class java.lang.Object
       GenericReturnType: T
           ParameterType: class java.lang.Object
    GenericParameterType: class java.lang.Object
</pre>

The generic return type for the method `Class.cast()` is reported as `java.lang.Object` because generics are implemented via type erasure which removes all information regarding generic types during compilation. The erasure of `T` is defined by the declaration of `Class`:

```java
public final class Class<T> implements ...
```

Thus `T` is replaced by the upper bound of the type variable, in this case, `java.lang.Object`.

The last example illustrates the output for a method with multiple overloads.

<pre>
$ <em>java MethodSpy java.io.PrintStream format</em>
public java.io.PrintStream java.io.PrintStream.format
  (java.util.Locale,java.lang.String,java.lang.Object[])
              ReturnType: class java.io.PrintStream
       GenericReturnType: class java.io.PrintStream
           ParameterType: class java.util.Locale
    GenericParameterType: class java.util.Locale
           ParameterType: class java.lang.String
    GenericParameterType: class java.lang.String
           ParameterType: class [Ljava.lang.Object;
    GenericParameterType: class [Ljava.lang.Object;
public java.io.PrintStream java.io.PrintStream.format
  (java.lang.String,java.lang.Object[])
              ReturnType: class java.io.PrintStream
       GenericReturnType: class java.io.PrintStream
           ParameterType: class java.lang.String
    GenericParameterType: class java.lang.String
           ParameterType: class [Ljava.lang.Object;
    GenericParameterType: class [Ljava.lang.Object;
</pre>

If multiple overloads of the same method name are discovered, they are all returned by `Class.getDeclaredMethods()`. Since `format()` has two overloads (with with a `Locale` and one without), both are shown by `MethodSpy`.

---

**Note:** `Method.getGenericExceptionTypes()` exists because it is actually possible to declare a method with a generic exception type. However this is rarely used since it is not possible to catch a generic exception type.

---

### Obtaining Names of Method Parameters

You can obtain the names of the formal parameters of any method or constructor with the method `java.lang.reflect.Executable.getParameters`. (The classes `Method` and `Constructor` extend the class `Executable` and therefore inherit the method `Executable.getParameters`.) However, `.class` files do not store formal parameter names by default. This is because many tools that produce and consume class files may not expect the larger static and dynamic footprint of `.class` files that contain parameter names. In particular, these tools would have to handle larger `.class` files, and the Java Virtual Machine (JVM) would use more memory. In addition, some parameter names, such as `secret` or `password`, may expose information about security-sensitive methods.

To store formal parameter names in a particular `.class` file, and thus enable the Reflection API to retrieve formal parameter names, compile the source file with the `-parameters` option to the `javac` compiler.

The `MethodParameterSpy` example illustrates how to retrieve the names of the formal parameters of all constructors and methods of a given class. The example also prints other information about each parameter.

The following command prints the formal parameter names of the constructors and methods of the class `ExampleMethods`. **Note**: Remember to compile the example `ExampleMethods` with the `-parameters` compiler option:

<pre>
	<em>java MethodParameterSpy ExampleMethods</em>
</pre>

This command prints the following:

```
Number of constructors: 1

Constructor #1
public ExampleMethods()

Number of declared constructors: 1

Declared constructor #1
public ExampleMethods()

Number of methods: 4

Method #1
public boolean ExampleMethods.simpleMethod(java.lang.String,int)
             Return type: boolean
     Generic return type: boolean
         Parameter class: class java.lang.String
          Parameter name: stringParam
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false
         Parameter class: int
          Parameter name: intParam
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false

Method #2
public int ExampleMethods.varArgsMethod(java.lang.String...)
             Return type: int
     Generic return type: int
         Parameter class: class [Ljava.lang.String;
          Parameter name: manyStrings
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false

Method #3
public boolean ExampleMethods.methodWithList(java.util.List<java.lang.String>)
             Return type: boolean
     Generic return type: boolean
         Parameter class: interface java.util.List
          Parameter name: listParam
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false

Method #4
public <T> void ExampleMethods.genericMethod(T[],java.util.Collection<T>)
             Return type: void
     Generic return type: void
         Parameter class: class [Ljava.lang.Object;
          Parameter name: a
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false
         Parameter class: interface java.util.Collection
          Parameter name: c
               Modifiers: 0
            Is implicit?: false
        Is name present?: true
           Is synthetic?: false
```

The `MethodParameterSpy` example uses the following methods from the `Parameter` class:

<ul>
<li><code>getType</code>: Returns a <code>Class</code> object that identifies the declared type for the parameter.
<li>
	<p>
		<code>getName</code>: Returns the name of the parameter. If the parameter's name is present, then this method returns the name provided by the <code>.class</code> file. Otherwise, this method synthesizes a name of the form <code>argN</code>, where <code>N</code> is the index of the parameter in the descriptor of the method that declares the parameter.
	</p>
	<p>
		For example, suppose you compiled the class <code>ExampleMethods</code> without specifying the <code>-parameters</code> compiler option. The example <code>MethodParameterSpy</code> would print the following for the method <code>ExampleMethods.simpleMethod</code>:
	</p>
	<pre>
public boolean ExampleMethods.simpleMethod(java.lang.String,int)
     		 Return type: boolean
     Generic return type: boolean
         Parameter class: class java.lang.String
          Parameter name: arg0
               Modifiers: 0
            Is implicit?: false
        Is name present?: false
           Is synthetic?: false
         Parameter class: int
          Parameter name: arg1
               Modifiers: 0
            Is implicit?: false
        Is name present?: false
           Is synthetic?: false
	</pre>
</li>
<li>
	<code>getModifiers</code>: Returns an integer that represents various characteristics that the formal parameter possesses. This value is the sum of the following values, if applicable to the formal parameter:

	<table>
		<tr>
			<th>Value (in decimal)</th>
			<th>Value (in hexadecimal)</th>
			<th>Description</th>
		</tr>
		<tr>
			<td>16</td>
			<td>0x0010</td>
			<td>The formal parameter is declared <code>final</code></td>
		</tr>
		<tr>
			<td>4096</td>
			<td>0x1000</td>
			<td>The formal parameter is synthetic. Alternatively, you can invoke the method <code>isSynthetic</code>.</td>
		</tr>
		<tr>
			<td>32768</td>
			<td>0x8000</td>
			<td>The parameter is implicitly declared in source code. Alternatively, you can invoke the method <code>isImplicit</code></td>
		</tr>
	</table>
</li>
<li>
	<code>isImplicit</code>: Returns <code>true</code> if this parameter is implicitly declared in source code. See the section <b>Implicit and Synthetic Parameters</b> for more information.
</li>
<li>
	<code>isNamePresent</code>: Returns <code>true</code> if the parameter has a name according to the <code>.class</code> file.
</li>
<li>
	<code>isSynthetic</code>: Returns <code>true</code> if this parameter is neither implicitly nor explicitly declared in source code. See the section <b>Implicit and Synthetic Parameters</b> for more information.
</li>
</ul>

#### Implicit and Synthetic Parameters

Certain constructs are implicitly declared in the source code if they have not been written explicitly. For example, the `ExampleMethods` example does not contain a constructor. A default constructor is implicitly declared for it. The `MethodParameterSpy` example prints information about the implicitly declared constructor of `ExampleMethods`:

```
Number of declared constructors: 1
public ExampleMethods()
```

Consider the following excerpt from `MethodParameterExamples`:

```java
public class MethodParameterExamples {
    public class InnerClass { }
}
```

The class `InnerClass` is a non-static nested class or inner class. A constructor for inner classes is also implicitly declared. However, this constructor will contain a parameter. When the Java compiler compiles `InnerClass`, it creates a `.class` file that represents code similar to the following:

```java
public class MethodParameterExamples {
    public class InnerClass {
        final MethodParameterExamples parent;
        InnerClass(final MethodParameterExamples this$0) {
            parent = this$0; 
        }
    }
}
```

The `InnerClass` constructor contains a parameter whose type is the class that encloses `InnerClass`, which is `MethodParameterExamples`. Consequently, the example `MethodParameterExamples` prints the following:

```
public MethodParameterExamples$InnerClass(MethodParameterExamples)
         Parameter class: class MethodParameterExamples
          Parameter name: this$0
               Modifiers: 32784
            Is implicit?: true
        Is name present?: true
           Is synthetic?: false
```

Because the constructor of the class `InnerClass` is implicitly declared, its parameter is implicit as well.

**Note**:

- The Java compiler creates a formal parameter for the constructor of an inner class to enable the compiler to pass a reference (representing the immediately enclosing instance) from the creation expression to the member class's constructor.
- The value 32784 means that the parameter of the `InnerClass` constructor is both final (16) and implicit (32768).
- The Java programming language allows variable names with dollar signs (`$`); however, by convention, dollar signs are not used in variable names.

Constructs emitted by a Java compiler are marked as *synthetic* if they do not correspond to a construct declared explicitly or implicitly in source code, unless they are class initialization methods. Synthetic constructs are artifacts generated by compilers that vary among different implementations. Consider the following excerpt from `MethodParameterExamples`:

```java
public class MethodParameterExamples {
    enum Colors {
        RED, WHITE;
    }
}
```

When the Java compiler encounters an `enum` construct, it creates several methods that are compatible with the `.class` file structure and provide the expected functionality of the `enum` construct. For example, the Java compiler would create a `.class` file for the `enum` construct `Colors` that represents code similar to the following:

```java
final class Colors extends java.lang.Enum<Colors> {
    public final static Colors RED = new Colors("RED", 0);
    public final static Colors BLUE = new Colors("WHITE", 1);
 
    private final static values = new Colors[]{ RED, BLUE };
 
    private Colors(String name, int ordinal) {
        super(name, ordinal);
    }
 
    public static Colors[] values(){
        return values;
    }
 
    public static Colors valueOf(String name){
        return (Colors)java.lang.Enum.valueOf(Colors.class, name);
    }
}
```

The Java compiler creates three constructors and methods for this `enum` construct: `Colors(String name, int ordinal)`, `Colors[] values()`, and `Colors valueOf(String name)`. The methods `values` and `valueOf` are implicitly declared. Consequently, their formal parameter names are implicitly declared as well.

The `enum` constructor `Colors(String name, int ordinal)` is a default constructor and it is implicitly declared. However, the formal parameters of this constructor (`name` and `ordinal`) are *not* implicitly declared. Because these formal parameters are neither explicitly or implicitly declared, they are synthetic. (The formal parameters for the default constructor of an `enum` construct are not implicitly declared because different compilers need not agree on the form of this constructor; another Java compiler might specify different formal parameters for it. When compilers compile expressions that use `enum` constants, they rely only on the public static fields of the `enum` construct, which are implicitly declared, and not on their constructors or how these constants are initialized.)

Consequently, the example `MethodParameterExample` prints the following about the `enum` construct `Colors`:

```
enum Colors:

Number of constructors: 0

Number of declared constructors: 1

Declared constructor #1
private MethodParameterExamples$Colors()
         Parameter class: class java.lang.String
          Parameter name: $enum$name
               Modifiers: 4096
            Is implicit?: false
        Is name present?: true
           Is synthetic?: true
         Parameter class: int
          Parameter name: $enum$ordinal
               Modifiers: 4096
            Is implicit?: false
        Is name present?: true
           Is synthetic?: true

Number of methods: 2

Method #1
public static MethodParameterExamples$Colors[]
    MethodParameterExamples$Colors.values()
             Return type: class [LMethodParameterExamples$Colors;
     Generic return type: class [LMethodParameterExamples$Colors;

Method #2
public static MethodParameterExamples$Colors
    MethodParameterExamples$Colors.valueOf(java.lang.String)
             Return type: class MethodParameterExamples$Colors
     Generic return type: class MethodParameterExamples$Colors
         Parameter class: class java.lang.String
          Parameter name: name
               Modifiers: 32768
            Is implicit?: true
        Is name present?: true
           Is synthetic?: false
```

Refer to the *Java Language Specification* for more information about implicitly declared constructs, including parameters that appear as implicit in the Reflection API.

### Retrieving and Parsing Method Modifiers

There a several modifiers that may be part of a method declaration:

- Access modifiers: `public`, `protected`, and `private`
- Modifier restricting to one instance: `static`
- Modifier prohibiting value modification: `final`
- Modifier requiring override: `abstract`
- Modifier preventing reentrancy: `synchronized`
- Modifier indicating implementation in another programming language: `ative`
- Modifier forcing strict floating point behavior: `strictfp`
- Annotations

The `MethodModifierSpy` example lists the modifiers of a method with a given name. It also displays whether the method is synthetic (compiler-generated), of variable arity, or a bridge method (compiler-generated to support generic interfaces).

<pre>
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static java.lang.System.out;

public class MethodModifierSpy {

    private static int count;
    private static synchronized void inc() { count++; }
    private static synchronized int cnt() { return count; }

    public static void main(String... args) {
		try {
		    Class<&#63;> c = Class.forName(args[0]);
		    Method[] allMethods = c.getDeclaredMethods();
		    for (Method m : allMethods) {
				if (!m.getName().equals(args[1])) {
				    continue;
				}
				out.format("%s%n", m.toGenericString());
				out.format("  Modifiers:  %s%n",
					   Modifier.toString(m.getModifiers()));
				out.format("  [ synthetic=%-5b var_args=%-5b bridge=%-5b ]%n",
					   m.isSynthetic(), m.isVarArgs(), m.isBridge());
				inc();
		    }
		    out.format("%d matching overload%s found%n", cnt(),
			       (cnt() == 1 ? "" : "s"));

	        // production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

A few examples of the output `MethodModifierSpy` produces follow.

<pre>
$ <em>java MethodModifierSpy java.lang.Object wait</em>
public final void java.lang.Object.wait() throws java.lang.InterruptedException
  Modifiers:  public final
  [ synthetic=false var_args=false bridge=false ]
public final void java.lang.Object.wait(long,int)
  throws java.lang.InterruptedException
  Modifiers:  public final
  [ synthetic=false var_args=false bridge=false ]
public final native void java.lang.Object.wait(long)
  throws java.lang.InterruptedException
  Modifiers:  public final native
  [ synthetic=false var_args=false bridge=false ]
3 matching overloads found
</pre>
<pre>
$ <em>java MethodModifierSpy java.lang.StrictMath toRadians</em>
public static double java.lang.StrictMath.toRadians(double)
  Modifiers:  public static strictfp
  [ synthetic=false var_args=false bridge=false ]
1 matching overload found
</pre>
<pre>
$ <em>java MethodModifierSpy MethodModifierSpy inc</em>
private synchronized void MethodModifierSpy.inc()
  Modifiers: private synchronized
  [ synthetic=false var_args=false bridge=false ]
1 matching overload found
</pre>
<pre>
$ <em>java MethodModifierSpy java.lang.Class getConstructor</em>
public java.lang.reflect.Constructor&lt;T> java.lang.Class.getConstructor
  (java.lang.Class&lt;T>[]) throws java.lang.NoSuchMethodException,
  java.lang.SecurityException
  Modifiers: public transient
  [ synthetic=false var_args=true bridge=false ]
1 matching overload found
</pre>
<pre>
$ <em>java MethodModifierSpy java.lang.String compareTo</em>
public int java.lang.String.compareTo(java.lang.String)
  Modifiers: public
  [ synthetic=false var_args=false bridge=false ]
public int java.lang.String.compareTo(java.lang.Object)
  Modifiers: public volatile
  [ synthetic=true  var_args=false bridge=true  ]
2 matching overloads found
</pre>

Note that `Method.isVarArgs()` returns `true` for `Class.getConstructor()`. This indicates that the method declaration looks like this:
<pre>public Constructor&lt;T> getConstructor(Class<&#63;>... parameterTypes)</pre>
not like this:
<pre>public Constructor&lt;T> getConstructor(Class<&#63;>[] parameterTypes)</pre>

Notice that the output for `String.compareTo()` contains two methods. The method declared in `String.java`:
```java
public int compareTo(String anotherString);
```
and a second *synthetic* or compiler-generated *bridge* method. This occurs because `String` implements the parameterized interface `Comparable`. During type erasure, the argument type of the inherited method `Comparable.compareTo()` is changed from `java.lang.Object` to `java.lang.String`. Since the parameter types for the `compareTo` methods in `Comparable` and `String` no longer match after erasure, overriding can not occur. In all other circumstances, this would produce a compile-time error because the interface is not implemented. The addition of the bridge method avoids this problem.

`Method` implements `java.lang.reflect.AnnotatedElement`. Thus any runtime annotations with `java.lang.annotation.RetentionPolicy.RUNTIME` may be retrieved. For an example of obtaining annotations see the section **Examining Class Modifiers and Types**.

### Invoking Methods

Reflection provides a means for invoking methods on a class. Typically, this would only be necessary if it is not possible to cast an instance of the class to the desired type in non-reflective code. Methods are invoked with `java.lang.reflect.Method.invoke()`. The first argument is the object instance on which this particular method is to be invoked. (If the method is `static`, the first argument should be `null`.) Subsequent arguments are the method's parameters. If the underlying method throws an exception, it will be wrapped by an `java.lang.reflect.InvocationTargetException`. The method's original exception may be retrieved using the exception chaining mechanism's `InvocationTargetException.getCause()` method.

#### Finding and Invoking a Method with a Specific Declaration

Consider a test suite which uses reflection to invoke private test methods in a given class. The `Deet` example searches for `public` methods in a class which begin with the string `test`, have a boolean return type, and a single `Locale` parameter. It then invokes each matching method.

<pre>
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Locale;
import static java.lang.System.out;
import static java.lang.System.err;

public class Deet&lt;T> {
    private boolean testDeet(Locale l) {
	// getISO3Language() may throw a MissingResourceException
	out.format("Locale = %s, ISO Language Code = %s%n", l.getDisplayName(), l.getISO3Language());
	return true;
    }

    private int testFoo(Locale l) { return 0; }
    private boolean testBar() { return true; }

    public static void main(String... args) {
		if (args.length != 4) {
		    err.format("Usage: java Deet &lt;classname> &lt;langauge> &lt;country> &lt;variant>%n");
		    return;
		}

		try {
		    Class<&#63;> c = Class.forName(args[0]);
		    Object t = c.newInstance();

		    Method[] allMethods = c.getDeclaredMethods();
		    for (Method m : allMethods) {
				String mname = m.getName();
				if (!mname.startsWith("test")
				    || (m.getGenericReturnType() != boolean.class)) {
				    continue;
				}
		 		Type[] pType = m.getGenericParameterTypes();
		 		if ((pType.length != 1)
				    || Locale.class.isAssignableFrom(pType[0].getClass())) {
		 		    continue;
		 		}

				out.format("invoking %s()%n", mname);
				try {
				    m.setAccessible(true);
				    Object o = m.invoke(t, new Locale(args[1], args[2], args[3]));
				    out.format("%s() returned %b%n", mname, (Boolean) o);

				// Handle any exceptions thrown by method to be invoked.
				} catch (InvocationTargetException x) {
				    Throwable cause = x.getCause();
				    err.format("invocation of %s failed: %s%n",
					       mname, cause.getMessage());
				}
		    }

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

`Deet` invokes `getDeclaredMethods()` which will return all methods explicitly declared in the class. Also, `Class.isAssignableFrom()` is used to determine whether the parameters of the located method are compatible with the desired invocation. Technically the code could have tested whether the following statement is `true` since `Locale` is final:

```java
Locale.class == pType[0].getClass()
```

However, `Class.isAssignableFrom()` is more general.

<pre>
$ <em>java Deet Deet ja JP JP</em>
invoking testDeet()
Locale = Japanese (Japan,JP), 
ISO Language Code = jpn
testDeet() returned true
</pre>
<pre>
$ <em>java Deet Deet xx XX XX</em>
invoking testDeet()
invocation of testDeet failed: 
Couldn't find 3-letter language code for xx
</pre>

First, note that only `testDeet()` meets the declaration restrictions enforced by the code. Next, when `testDeet()` is passed an invalid argument it throws an unchecked `java.util.MissingResourceException`. In reflection, there is no distinction in the handling of checked versus unchecked exceptions. They are all wrapped in an `InvocationTargetException`.

#### Invoking Methods with a Variable Number of Arguments

`Method.invoke()` may be used to pass a variable number of arguments to a method. The key concept to understand is that methods of variable arity are implemented as if the variable arguments are packed in an array.

The `InvokeMain` example illustrates how to invoke the `main()` entry point in any class and pass a set of arguments determined at runtime.

<pre>
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InvokeMain {
    public static void main(String... args) {
		try {
		    Class<&#63;> c = Class.forName(args[0]);
		    Class[] argTypes = new Class[] { String[].class };
		    Method main = c.getDeclaredMethod("main", argTypes);
	  	    String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
		    System.out.format("invoking %s.main()%n", c.getName());
		    main.invoke(null, (Object)mainArgs);

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

First, to find the `main()` method the code searches for a class with the name "main" with a single parameter that is an array of `String`. Since `main()` is `static`, `null` is the first argument to `Method.invoke()`. The second argument is the array of arguments to be passed.

<pre>
$ <em>java InvokeMain Deet Deet ja JP JP</em>
invoking Deet.main()
invoking testDeet()
Locale = Japanese (Japan,JP), 
ISO Language Code = jpn
testDeet() returned true
</pre>

### Troubleshooting

This section contains examples of problems developers might encounter when using reflection to locate, invoke, or get information about methods.

#### NoSuchMethodException Due to Type Erasure

The `MethodTrouble` example illustrates what happens when type erasure is not taken into consideration by code which searches for a particular method in a class.

<pre>
import java.lang.reflect.Method;

public class MethodTrouble<T>  {
    public void lookup(T t) {}
    public void find(Integer i) {}

    public static void main(String... args) {
		try {
		    String mName = args[0];
		    Class cArg = Class.forName(args[1]);
		    Class<&#63;> c = (new MethodTrouble&lt;Integer>()).getClass();
		    Method m = c.getMethod(mName, cArg);
		    System.out.format("Found:%n  %s%n", m.toGenericString());

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
</pre>
<pre>
$ <em>java MethodTrouble lookup java.lang.Integer</em>
java.lang.NoSuchMethodException: MethodTrouble.lookup(java.lang.Integer)
        at java.lang.Class.getMethod(Class.java:1605)
        at MethodTrouble.main(MethodTrouble.java:12)
</pre>
<pre>
$ <em>java MethodTrouble lookup java.lang.Object</em>
Found:
  public void MethodTrouble.lookup(T)
</pre>

When a method is declared with a generic parameter type, the compiler will replace the generic type with its upper bound, in this case, the upper bound of `T` is `Object`. Thus, when the code searches for `lookup(Integer)`, no method is found, despite the fact that the instance of `MethodTrouble` was created as follows:

<pre>
Class<&#63;> c = (new MethodTrouble&lt;Integer>()).getClass();
</pre>

Searching for `lookup(Object)` succeeds as expected.

<pre>
$ <em>java MethodTrouble find java.lang.Integer</em>
Found:
  public void MethodTrouble.find(java.lang.Integer)
$ <em>java MethodTrouble find java.lang.Object</em>
java.lang.NoSuchMethodException: MethodTrouble.find(java.lang.Object)
        at java.lang.Class.getMethod(Class.java:1605)
        at MethodTrouble.main(MethodTrouble.java:12)
</pre>

In this case, `find()` has no generic parameters, so the parameter types searched for by `getMethod()` must match exactly.

---

**Tip**: Always pass the upper bound of the parameterized type when searching for a method.

---

#### IllegalAccessException when Invoking a Method

An `IllegalAccessException` is thrown if an attempt is made to invoke a private or otherwise inaccessible method.

The `MethodTroubleAgain` example shows a typical stack trace which results from trying to invoke a private method in an another class.

<pre>
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class AnotherClass {
    private void m() {}
}

public class MethodTroubleAgain {
    public static void main(String... args) {
		AnotherClass ac = new AnotherClass();
		try {
		    Class<&#63;> c = ac.getClass();
	 	    Method m = c.getDeclaredMethod("m");
	//  	    m.setAccessible(true);      // solution
	 	    Object o = m.invoke(ac);    // IllegalAccessException

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

The stack trace for the exception thrown follows.

<pre>
$ <em>java MethodTroubleAgain</em>
java.lang.IllegalAccessException: Class MethodTroubleAgain can not access a
  member of class AnotherClass with modifiers "private"
        at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:65)
        at java.lang.reflect.Method.invoke(Method.java:588)
        at MethodTroubleAgain.main(MethodTroubleAgain.java:15)
</pre>

---

**Tip**: An access restriction exists which prevents reflective invocation of methods which normally would not be accessible via direct invocation. (This includes---but is not limited to---`private` methods in a separate class and public methods in a separate private class.) However, `Method` is declared to extend `AccessibleObject` which provides the ability to suppress this check via `AccessibleObject.setAccessible()`. If it succeeds, then subsequent invocations of this method object will not fail due to this problem.

---

#### IllegalArgumentException from Method.invoke()

`Method.invoke()` has been retrofitted to be a variable-arity method. This is an enormous convenience, however it can lead to unexpected behavior. The `MethodTroubleToo` example shows various ways in which `Method.invoke()` can produce confusing results.

```java
import java.lang.reflect.Method;

public class MethodTroubleToo {
    public void ping() { System.out.format("PONG!%n"); }

    public static void main(String... args) {
		try {
		    MethodTroubleToo mtt = new MethodTroubleToo();
		    Method m = MethodTroubleToo.class.getMethod("ping");

	 	    switch(Integer.parseInt(args[0])) {
		    case 0:
		  		m.invoke(mtt);                 // works
				break;
		    case 1:
		 		m.invoke(mtt, null);           // works (expect compiler warning)
				break;
		    case 2:
				Object arg2 = null;
				m.invoke(mtt, arg2);           // IllegalArgumentException
				break;
		    case 3:
				m.invoke(mtt, new Object[0]);  // works
				break;
		    case 4:
				Object arg4 = new Object[0];
				m.invoke(mtt, arg4);           // IllegalArgumentException
				break;
			default:
				System.out.format("Test not found%n");
		    }

	        // production code should handle these exceptions more gracefully
		} catch (Exception x) {
		    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java MethodTroubleToo 0</em>
PONG!
</pre>

Since all of the parameters of `Method.invoke()` are optional except for the first, they can be omitted when the method to be invoked has no parameters.

<pre>
$ <em>java MethodTroubleToo 1</em>
PONG!
</pre>

The code in this case generates this compiler warning because `null` is ambiguous.

<pre>
$ <em>javac MethodTroubleToo.java</em>
MethodTroubleToo.java:16: warning: non-varargs call of varargs method with
  inexact argument type for last parameter;
 		m.invoke(mtt, null);           // works (expect compiler warning)
 		              ^
  cast to Object for a varargs call
  cast to Object[] for a non-varargs call and to suppress this warning
1 warning
</pre>

It is not possible to determine whether `null` represents an empty array of arguments or a first argument of `null`.

<pre>
$ <em>java MethodTroubleToo 2</em>
java.lang.IllegalArgumentException: wrong number of arguments
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke
          (NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke
          (DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at MethodTroubleToo.main(MethodTroubleToo.java:21)
</pre>

This fails despite the fact that the argument is `null`, because the type is a `Object` and `ping()` expects no arguments at all.

<pre>
$ <em>java MethodTroubleToo 3</em>
PONG!
</pre>

This works because `new Object[0]` creates an empty array, and to a varargs method, this is equivalent to not passing any of the optional arguments.

<pre>
$ <em>java MethodTroubleToo 4</em>
java.lang.IllegalArgumentException: wrong number of arguments
        at sun.reflect.NativeMethodAccessorImpl.invoke0
          (Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke
          (NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke
          (DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at MethodTroubleToo.main(MethodTroubleToo.java:28)
</pre>

Unlike the previous example, if the empty array is stored in an `Object`, then it is treated as an `Object`. This fails for the same reason that case 2 fails, `ping()` does not expect an argument.

---

**Tip:** When a method `foo(Object... o)` is declared the compiler will put all of the arguments passed to `foo()` in an array of type `Object`. The implementation of `foo()` is the same as if it were declared `foo(Object[] o)`. Understanding this may help avoid the types of problems illustrated above.

---

#### InvocationTargetException when Invoked Method Fails

An `InvocationTargetException` wraps all exceptions (checked and unchecked) produced when a method object is invoked. The `MethodTroubleReturns` example shows how to retrieve the original exception thrown by the invoked method.

<pre>
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodTroubleReturns {
    private void drinkMe(int liters) {
		if (liters < 0)
		    throw new IllegalArgumentException("I can't drink a negative amount of liquid");
    }

    public static void main(String... args) {
		try {
		    MethodTroubleReturns mtr  = new MethodTroubleReturns();
	 	    Class<&#63;> c = mtr.getClass();
	   	    Method m = c.getDeclaredMethod("drinkMe", int.class);
		    m.invoke(mtr, -1);

	        // production code should handle these exceptions more gracefully
		} catch (InvocationTargetException x) {
		    Throwable cause = x.getCause();
		    System.err.format("drinkMe() failed: %s%n", cause.getMessage());
		} catch (Exception x) {
		    x.printStackTrace();
		}
    }
}
</pre>

<pre>
$ <em>java MethodTroubleReturns</em>
drinkMe() failed: I can't drink a negative amount of liquid
</pre>

---

**Tip:** If an `InvocationTargetException` is thrown, the method was invoked. Diagnosis of the problem would be the same as if the method was called directly and threw the exception that is retrieved by `getCause()`. This exception does not indicate a problem with the reflection package or its usage.

---

## Constructors

A *constructor* is used in the creation of an object that is an instance of a class. Typically it performs operations required to initialize the class before methods are invoked or fields are accessed. Constructors are never inherited.

Similar to methods, reflection provides APIs to discover and retrieve the constructors of a class and obtain declaration information such as the modifiers, parameters, annotations, and thrown exceptions. New instances of classes may also be created using a specified constructor. The key classes used when working with constructors are `Class` and `java.lang.reflect.Constructor`. Common operations involving constructors are covered in the following sections:

- **Finding Constructors** illustrates how to retrieve constructors with specific parameters
- **Retrieving and Parsing Constructor Modifiers** shows how to obtain the modifiers of a constructor declaration and other information about the constructor
- **Creating New Class Instances** shows how to instantiate an instance of an object by invoking its constructor
- **Troubleshooting** describes common errors which may be encountered while finding or invoking constructors

### Finding Constructors

A constructor declaration includes the name, modifiers, parameters, and list of throwable exceptions. The `java.lang.reflect.Constructor` class provides a way to obtain this information.

The `ConstructorSift` example illustrates how to search a class's declared constructors for one which has a parameter of a given type.

<pre>
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import static java.lang.System.out;

public class ConstructorSift {
    public static void main(String... args) {
		try {
		    Class<&#63;> cArg = Class.forName(args[1]);

		    Class<&#63;> c = Class.forName(args[0]);
		    Constructor[] allConstructors = c.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
				Class<&#63;>[] pType  = ctor.getParameterTypes();
				for (int i = 0; i < pType.length; i++) {
				    if (pType[i].equals(cArg)) {
						out.format("%s%n", ctor.toGenericString());

						Type[] gpType = ctor.getGenericParameterTypes();
						for (int j = 0; j < gpType.length; j++) {
						    char ch = (pType[j].equals(cArg) ? '*' : ' ');
						    out.format("%7c%s[%d]: %s%n", ch,
							       "GenericParameterType", j, gpType[j]);
						}
						break;
				    }
				}
		    }

	        // production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

`Method.getGenericParameterTypes()` will consult the Signature Attribute in the class file if it's present. If the attribute isn't available, it falls back on `Method.getParameterType()` which was not changed by the introduction of generics. The other methods with name `getGenericFoo()` for some value of `Foo` in reflection are implemented similarly. The syntax for the returned values of `Method.get*Types()` is described in `Class.getName()`.

Here is the output for all constructors in `java.util.Formatter` which have a `Locale` argument.

<pre>
$ <em>java ConstructorSift java.util.Formatter java.util.Locale</em>
public
java.util.Formatter(java.io.OutputStream,java.lang.String,java.util.Locale)
throws java.io.UnsupportedEncodingException
       GenericParameterType[0]: class java.io.OutputStream
       GenericParameterType[1]: class java.lang.String
      *GenericParameterType[2]: class java.util.Locale
public java.util.Formatter(java.lang.String,java.lang.String,java.util.Locale)
throws java.io.FileNotFoundException,java.io.UnsupportedEncodingException
       GenericParameterType[0]: class java.lang.String
       GenericParameterType[1]: class java.lang.String
      *GenericParameterType[2]: class java.util.Locale
public java.util.Formatter(java.lang.Appendable,java.util.Locale)
       GenericParameterType[0]: interface java.lang.Appendable
      *GenericParameterType[1]: class java.util.Locale
public java.util.Formatter(java.util.Locale)
      *GenericParameterType[0]: class java.util.Locale
public java.util.Formatter(java.io.File,java.lang.String,java.util.Locale)
throws java.io.FileNotFoundException,java.io.UnsupportedEncodingException
       GenericParameterType[0]: class java.io.File
       GenericParameterType[1]: class java.lang.String
      *GenericParameterType[2]: class java.util.Locale
</pre>

The next example output illustrates how to search for a parameter of type `char[]` in `String`.

<pre>
$ <em>java ConstructorSift java.lang.String "[C"</em>
java.lang.String(int,int,char[])
       GenericParameterType[0]: int
       GenericParameterType[1]: int
      *GenericParameterType[2]: class [C
public java.lang.String(char[],int,int)
      *GenericParameterType[0]: class [C
       GenericParameterType[1]: int
       GenericParameterType[2]: int
public java.lang.String(char[])
      *GenericParameterType[0]: class [C
</pre>

The syntax for expressing arrays of reference and primitive types acceptable to `Class.forName()` is described in `Class.getName()`. Note that the first listed constructor is `package-private`, not `public`. It is returned because the example code uses `Class.getDeclaredConstructors()` rather than `Class.getConstructors()`, which returns only `public` constructors.

This example shows that searching for arguments of variable arity (which have a variable number of parameters) requires use of array syntax:

<pre>
$ <em>java ConstructorSift java.lang.ProcessBuilder "[Ljava.lang.String;"</em>
public java.lang.ProcessBuilder(java.lang.String[])
      *GenericParameterType[0]: class [Ljava.lang.String;
</pre>

This is the actual declaration of the `ProcessBuilder` constructor in source code:

```java
public ProcessBuilder(String... command)
```

The parameter is represented as a single-dimension array of type `java.lang.String`. This can be distinguished from a parameter that is explicitly an array of `java.lang.String` by invoking `Constructor.isVarArgs()`.

The final example reports the output for a constructor which has been declared with a generic parameter type:

<pre>
$ <em>java ConstructorSift java.util.HashMap java.util.Map</em>
public java.util.HashMap(java.util.Map<&#63; extends K, ? extends V>)
      *GenericParameterType[0]: java.util.Map<&#63; extends K, ? extends V>
</pre>

Exception types may be retrieved for constructors in a similar way as for methods. See the `MethodSpy` example described in *Obtaining Method Type Information* section for further details.

### Retrieving and Parsing Constructor Modifiers

Because of the role of constructors in the language, fewer modifiers are meaningful than for methods:

- Access modifiers: `public`, `protected`, and `private`
- Annotations

The `ConstructorAccess` example searches for constructors in a given class with the specified access modifier. It also displays whether the constructor is synthetic (compiler-generated) or of variable arity.

<pre>
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import static java.lang.System.out;

public class ConstructorAccess {
    public static void main(String... args) {
		try {
		    Class<&#63;> c = Class.forName(args[0]);
		    Constructor[] allConstructors = c.getDeclaredConstructors();
		    for (Constructor ctor : allConstructors) {
				int searchMod = modifierFromString(args[1]);
				int mods = accessModifiers(ctor.getModifiers());
				if (searchMod == mods) {
				    out.format("%s%n", ctor.toGenericString());
				    out.format("  [ synthetic=%-5b var_args=%-5b ]%n",
					       ctor.isSynthetic(), ctor.isVarArgs());
				}
		    }

	        // production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
	}

	private static int accessModifiers(int m) {
		return m & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED);
	}

	private static int modifierFromString(String s) {
		if ("public".equals(s))               return Modifier.PUBLIC;
		else if ("protected".equals(s))       return Modifier.PROTECTED;
		else if ("private".equals(s))         return Modifier.PRIVATE;
		else if ("package-private".equals(s)) return 0;
		else return -1;
    }
}
</pre>

There is not an explicit `Modifier` constant which corresponds to "package-private" access, so it is necessary to check for the absence of all three access modifiers to identify a package-private constructor.

This output shows the private constructors in `java.io.File`:

<pre>
$ <em>java ConstructorAccess java.io.File private</em>
private java.io.File(java.lang.String,int)
  [ synthetic=false var_args=false ]
private java.io.File(java.lang.String,java.io.File)
  [ synthetic=false var_args=false ]
</pre>

Synthetic constructors are rare; however the `SyntheticConstructor` example illustrates a typical situation where this may occur:

```java
public class SyntheticConstructor {
    private SyntheticConstructor() {}
    class Inner {
	// Compiler will generate a synthetic constructor since
	// SyntheticConstructor() is private.
	Inner() { new SyntheticConstructor(); }
    }
}
```

<pre>
$ <em>java ConstructorAccess SyntheticConstructor package-private</em>
SyntheticConstructor(SyntheticConstructor$1)
  [ synthetic=true  var_args=false ]
</pre>

Since the inner class's constructor references the private constructor of the enclosing class, the compiler must generate a package-private constructor. The parameter type `SyntheticConstructor$1` is arbitrary and dependent on the compiler implementation. Code which depends on the presence of any synthetic or non-public class members may not be portable.

Constructors implement `java.lang.reflect.AnnotatedElement`, which provides methods to retrieve runtime annotations with `java.lang.annotation.RetentionPolicy.RUNTIME`. For an example of obtaining annotations see the *Examining Class Modifiers and Types* section.

### Creating New Class Instances

There are two reflective methods for creating instances of classes: `java.lang.reflect.Constructor.newInstance()` and `Class.newInstance()`. The former is preferred and is thus used in these examples because:

- `Class.newInstance()` can only invoke the zero-argument constructor, while `Constructor.newInstance()` may invoke any constructor, regardless of the number of parameters.
- `Class.newInstance()` throws any exception thrown by the constructor, regardless of whether it is checked or unchecked.` Constructor.newInstance()` always wraps the thrown exception with an InvocationTargetException.
- `Class.newInstance()` requires that the constructor be visible; `Constructor.newInstance()` may invoke `private` constructors under certain circumstances.

Sometimes it may be desirable to retrieve internal state from an object which is only set after construction. Consider a scenario where it is necessary to obtain the internal character set used by `java.io.Console`. (The `Console` character set is stored in an private field and is not necessarily the same as the Java virtual machine default character set returned by `java.nio.charset.Charset.defaultCharset()`). The `ConsoleCharset` example shows how this might be achieved:

```java
import java.io.Console;
import java.nio.charset.Charset;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import static java.lang.System.out;

public class ConsoleCharset {
    public static void main(String... args) {
		Constructor[] ctors = Console.class.getDeclaredConstructors();
		Constructor ctor = null;
		for (int i = 0; i < ctors.length; i++) {
		    ctor = ctors[i];
		    if (ctor.getGenericParameterTypes().length == 0)
			break;
		}

		try {
		    ctor.setAccessible(true);
	 	    Console c = (Console)ctor.newInstance();
		    Field f = c.getClass().getDeclaredField("cs");
		    f.setAccessible(true);
		    out.format("Console charset         :  %s%n", f.get(c));
		    out.format("Charset.defaultCharset():  %s%n",
			       Charset.defaultCharset());

	        // production code should handle these exceptions more gracefully
		} catch (InstantiationException x) {
		    x.printStackTrace();
	 	} catch (InvocationTargetException x) {
	 	    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		}
    }
}
```

---

**Note:**
`Class.newInstance()` will only succeed if the constructor is has zero arguments and is already accessible. Otherwise, it is necessary to use `Constructor.newInstance()` as in the above example.

---

Example output for a UNIX system:

<pre>
$ <em>java ConsoleCharset</em>
Console charset          :  ISO-8859-1
Charset.defaultCharset() :  ISO-8859-1
</pre>

Example output for a Windows system:

<pre>
C:\> <em>java ConsoleCharset</em>
Console charset          :  IBM437
Charset.defaultCharset() :  windows-1252
</pre>

Another common application of `Constructor.newInstance()` is to invoke constructors which take arguments. The `RestoreAliases` example finds a specific single-argument constructor and invokes it:

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static java.lang.System.out;

class EmailAliases {
    private Set<String> aliases;
    private EmailAliases(HashMap<String, String> h) {
		aliases = h.keySet();
    }

    public void printKeys() {
		out.format("Mail keys:%n");
		for (String k : aliases)
		    out.format("  %s%n", k);
    }
}

public class RestoreAliases {

    private static Map<String, String> defaultAliases = new HashMap<String, String>();
    static {
		defaultAliases.put("Duke", "duke@i-love-java");
		defaultAliases.put("Fang", "fang@evil-jealous-twin");
    }

    public static void main(String... args) {
		try {
		    Constructor ctor = EmailAliases.class.getDeclaredConstructor(HashMap.class);
		    ctor.setAccessible(true);
		    EmailAliases email = (EmailAliases)ctor.newInstance(defaultAliases);
		    email.printKeys();

	        // production code should handle these exceptions more gracefully
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		}
    }
}
```

This example uses `Class.getDeclaredConstructor()` to find the constructor with a single argument of type `java.util.HashMap`. Note that it is sufficient to pass `HashMap.class` since the parameter to any `get*Constructor()` method requires a class only for type purposes. Due to type erasure, the following expression evaluates to `true`:

```java
HashMap.class == defaultAliases.getClass()
```

The example then creates a new instance of the class using this constructor with `Constructor.newInstance()`.

<pre>
$ <em>java RestoreAliases</em>
Mail keys:
  Duke
  Fang
</pre>

### Troubleshooting

The following problems are sometimes encountered by developers when trying to invoke constructors via reflection.

#### InstantiationException Due to Missing Zero-Argument Constructor

The `ConstructorTrouble` example illustrates what happens when code attempts to create a new instance of a class using `Class.newInstance()` and there is no accessible zero-argument constructor:

<pre>
public class ConstructorTrouble {
    private ConstructorTrouble(int i) {}

    public static void main(String... args){
		try {
		    Class<&#63;> c = Class.forName("ConstructorTrouble");
		    Object o = c.newInstance();  // InstantiationException

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

<pre>
$ <em>java ConstructorTrouble</em>
java.lang.InstantiationException: ConstructorTrouble
        at java.lang.Class.newInstance0(Class.java:340)
        at java.lang.Class.newInstance(Class.java:308)
        at ConstructorTrouble.main(ConstructorTrouble.java:7)
</pre>

---

*Tip:** There a number of different reasons an `InstantiationException` can occur. In this case, the problem is that the presence of the constructor with an int argument prevents the compiler from generating the default (or zero-argument) constructor and there is no explicit zero-argument constructor in the code. Remember that `Class.newInstance()` behaves very much like the new keyword and will fail whenever new would fail.

---

#### Class.newInstance() Throws Unexpected Exception

The `ConstructorTroubleToo` example shows an unresolvable problem in `Class.newInstance()`. Namely, it propagates any exception — checked or unchecked — thrown by the constructor.

<pre>
import java.lang.reflect.InvocationTargetException;
import static java.lang.System.err;

public class ConstructorTroubleToo {
    public ConstructorTroubleToo() {
 		throw new RuntimeException("exception in constructor");
    }

    public static void main(String... args) {
		try {
		    Class<&#63;> c = Class.forName("ConstructorTroubleToo");
		    // Method propagetes any exception thrown by the constructor
		    // (including checked exceptions).
		    if (args.length > 0 && args[0].equals("class")) {
				Object o = c.newInstance();
		    } else {
				Object o = c.getConstructor().newInstance();
		    }

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		    err.format("%n%nCaught exception: %s%n", x.getCause());
		}
    }
}
</pre>

<pre>
$ <em>java ConstructorTroubleToo class</em>
Exception in thread "main" java.lang.RuntimeException: exception in constructor
        at ConstructorTroubleToo.<init>(ConstructorTroubleToo.java:6)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance
          (NativeConstructorAccessorImpl.java:39)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance
          (DelegatingConstructorAccessorImpl.java:27)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
        at java.lang.Class.newInstance0(Class.java:355)
        at java.lang.Class.newInstance(Class.java:308)
        at ConstructorTroubleToo.main(ConstructorTroubleToo.java:15)
</pre>

This situation is unique to reflection. Normally, it is impossible to write code which ignores a checked exception because it would not compile. It is possible to wrap any exception thrown by a constructor by using `Constructor.newInstance()` rather than `Class.newInstance()`.

<pre>
$ <em>java ConstructorTroubleToo</em>
java.lang.reflect.InvocationTargetException
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance
          (NativeConstructorAccessorImpl.java:39)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance
          (DelegatingConstructorAccessorImpl.java:27)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
        at ConstructorTroubleToo.main(ConstructorTroubleToo.java:17)
Caused by: java.lang.RuntimeException: exception in constructor
        at ConstructorTroubleToo.<init>(ConstructorTroubleToo.java:6)
        ... 5 more


Caught exception: java.lang.RuntimeException: exception in constructor
</pre>

If an `InvocationTargetException` is thrown, the method was invoked. Diagnosis of the problem would be the same as if the constructor was called directly and threw the exception that is retrieved by `InvocationTargetException.getCause()`. This exception does not indicate a problem with the reflection package or its usage.

---

**Tip:** It is preferable to use `Constructor.newInstance()` over `Class.newInstance()` because the former API permits examination and handling of arbitrary exceptions thrown by constructors.

---

#### Problems Locating or Invoking the Correct Constructor

The `ConstructorTroubleAgain` class illustrates various ways in which incorrect code can fail to locate or invoke the expected constructor.

<pre>
import java.lang.reflect.InvocationTargetException;
import static java.lang.System.out;

public class ConstructorTroubleAgain {
    public ConstructorTroubleAgain() {}

    public ConstructorTroubleAgain(Integer i) {}

    public ConstructorTroubleAgain(Object o) {
		out.format("Constructor passed Object%n");
    }

    public ConstructorTroubleAgain(String s) {
		out.format("Constructor passed String%n");
    }

    public static void main(String... args){
		String argType = (args.length == 0 ? "" : args[0]);
		try {
		    Class<&#63;> c = Class.forName("ConstructorTroubleAgain");
		    if ("".equals(argType)) {
			// IllegalArgumentException: wrong number of arguments
			Object o = c.getConstructor().newInstance("foo");
		    } else if ("int".equals(argType)) {
			// NoSuchMethodException - looking for int, have Integer
			Object o = c.getConstructor(int.class);
		    } else if ("Object".equals(argType)) {
			// newInstance() does not perform method resolution
			Object o = c.getConstructor(Object.class).newInstance("foo");
		    } else {
			assert false;
		    }

	        // production code should handle these exceptions more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

<pre>
$ <em>java ConstructorTroubleAgain</em>
Exception in thread "main" java.lang.IllegalArgumentException: wrong number of
  arguments
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance
          (NativeConstructorAccessorImpl.java:39)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance
          (DelegatingConstructorAccessorImpl.java:27)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
        at ConstructorTroubleAgain.main(ConstructorTroubleAgain.java:23)
</pre>

An `IllegalArgumentException` is thrown because the zero-argument constructor was requested and an attempt was made to pass an argument. The same exception would be thrown if the constructor was passed an argument of the wrong type.

<pre>
$ <em>java ConstructorTroubleAgain int</em>
java.lang.NoSuchMethodException: ConstructorTroubleAgain.<init>(int)
        at java.lang.Class.getConstructor0(Class.java:2706)
        at java.lang.Class.getConstructor(Class.java:1657)
        at ConstructorTroubleAgain.main(ConstructorTroubleAgain.java:26)
</pre>

This exception may occur if the developer mistakenly believes that reflection will autobox or unbox types. Boxing (conversion of a primitive to a reference type) occurs only during compilation. There is no opportunity in reflection for this operation to occur, so the specific type must be used when locating a constructor.

<pre>
$ <em>java ConstructorTroubleAgain Object</em>
Constructor passed Object
</pre>

Here, it might be expected that the constructor taking a `String` argument would be invoked since `newInstance()` was invoked with the more specific `String` type. However it is too late! The constructor which was found is already the constructor with an `Object` argument. `newInstance()` makes no attempt to do method resolution; it simply operates on the existing constructor object.

---

**Tip:** An important difference between `new` and `Constructor.newInstance()` is that `new` performs method argument type checking, boxing, and method resolution. None of these occur in reflection, where explicit choices must be made.

---

#### IllegalAccessException When Attempting to Invoke an Inaccessible Constructor

An `IllegalAccessException` may be thrown if an attempt is made to invoke a private or otherwise inaccessible constructor. The `ConstructorTroubleAccess` example illustrates the resulting stack trace.

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class Deny {
    private Deny() {
		System.out.format("Deny constructor%n");
    }
}

public class ConstructorTroubleAccess {
    public static void main(String... args) {
		try {
		    Constructor c = Deny.class.getDeclaredConstructor();
//	  	    c.setAccessible(true);   // solution
		    c.newInstance();

	        // production code should handle these exceptions more gracefully
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		} catch (NoSuchMethodException x) {
		    x.printStackTrace();
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java ConstructorTroubleAccess</em>
java.lang.IllegalAccessException: Class ConstructorTroubleAccess can not access
  a member of class Deny with modifiers "private"
        at sun.reflect.Reflection.ensureMemberAccess(Reflection.java:65)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:505)
        at ConstructorTroubleAccess.main(ConstructorTroubleAccess.java:15)
</pre>

---

**Tip:** An access restriction exists which prevents reflective invocation of constructors which normally would not be accessible via direct invocation. (This includes, but is not limited to, private constructors in a separate class and public constructors in a separate private class.) However, `Constructor` is declared to extend `AccessibleObject` which provides the ability to suppress this check via `AccessibleObject.setAccessible()`.

---

# Arrays and Enumerated Types

From the Java virtual machine's perspective, arrays and enumerated types (or enums) are just classes. Many of the methods in `Class` may be used on them. Reflection provides a few specific APIs for arrays and enums. This lesson uses a series of code samples to describe how to distinguish each of these objects from other classes and operate on them. Various errors are also be examined.

## Arrays

An `array` is an object of reference type which contains a fixed number of components of the same type; the length of an array is immutable. Creating an instance of an array requires knowledge of the length and component type. Each component may be a primitive type (e.g. `byte`, `int`, or `double`), a reference type (e.g. `String`, `Object`, or `java.nio.CharBuffer`), or an array. Multi-dimensional arrays are really just arrays which contain components of array type.

Arrays are implemented in the Java virtual machine. The only methods on arrays are those inherited from `Object`. The length of an array is not part of its type; arrays have a `length` field which is accessible via `java.lang.reflect.Array.getLength()`.

Reflection provides methods for accessing array types and array component types, creating new arrays, and retrieving and setting array component values. The following sections include examples of common operations on arrays:

- **Identifying Array Types** describes how to determine if a class member is a field of array type
- **Creating New Arrays** illustrates how to create new instances of arrays with simple and complex component types
- **Getting and Setting Arrays and Their Components** shows how to access fields of type array and individually access array elements
- **Troubleshooting** covers common errors and programming misconceptions

All of these operations are supported via `static` methods in `java.lang.reflect.Array`.

### Identifying Array Types

Array types may be identified by invoking `Class.isArray()`. To obtain a `Class` use one of the methods described in *Retrieving Class Objects* section of this trail.

The `ArrayFind` example identifies the fields in the named class that are of array type and reports the component type for each of them.

<pre>
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import static java.lang.System.out;

public class ArrayFind {
    public static void main(String... args) {
		boolean found = false;
	 	try {
		    Class<&#63;> cls = Class.forName(args[0]);
		    Field[] flds = cls.getDeclaredFields();
		    for (Field f : flds) {
		 		Class<&#63;> c = f.getType();
				if (c.isArray()) {
				    found = true;
				    out.format("%s%n"
		                               + "           Field: %s%n"
					       + "            Type: %s%n"
					       + "  Component Type: %s%n",
					       f, f.getName(), c, c.getComponentType());
				}
		    }
		    if (!found) {
			out.format("No array fields%n");
		    }

	        // production code should handle this exception more gracefully
	 	} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

The syntax for the returned value of `Class.get*Type()` is described in `Class.getName()`. The number of '`[`' characters at the beginning of the type name indicates the number of dimensions (i.e. depth of nesting) of the array.

Samples of the output follows. User input is in italics. An array of primitive type `byte`:

<pre>
$ <em>java ArrayFind java.nio.ByteBuffer</em>
final byte[] java.nio.ByteBuffer.hb
           Field: hb
            Type: class [B
  Component Type: byte
</pre>

An array of reference type `StackTraceElement`:

<pre>
$ <em>java ArrayFind java.lang.Throwable</em>
private java.lang.StackTraceElement[] java.lang.Throwable.stackTrace
           Field: stackTrace
            Type: class [Ljava.lang.StackTraceElement;
  Component Type: class java.lang.StackTraceElement
</pre>

`predefined` is a one-dimensional array of reference type `java.awt.Cursor` and `cursorProperties` is a two-dimensional array of reference type `String`:

<pre>
$ <em>java ArrayFind java.awt.Cursor</em>
protected static java.awt.Cursor[] java.awt.Cursor.predefined
           Field: predefined
            Type: class [Ljava.awt.Cursor;
  Component Type: class java.awt.Cursor
static final java.lang.String[][] java.awt.Cursor.cursorProperties
           Field: cursorProperties
            Type: class [[Ljava.lang.String;
  Component Type: class [Ljava.lang.String;
</pre>

### Creating New Arrays

Just as in non-reflective code, reflection supports the ability to dynamically create arrays of arbitrary type and dimensions via `java.lang.reflect.Array.newInstance()`. Consider `ArrayCreator`, a basic interpreter capable of dynamically creating arrays. The syntax that will be parsed is as follows:

<pre>
fully_qualified_class_name variable_name[] = 
     { val1, val2, val3, ... }
</pre>

Assume that the `fully_qualified_class_name` represents a class that has a constructor with a single `String` argument. The dimensions of the array are determined by the number of values provided. The following example will construct an instance of an array of `fully_qualified_class_name` and populate its values with instances given by `val1`, `val2`, etc. (This example assumes familiarity with `Class.getConstructor()` and `java.lang.reflect.Constructor.newInstance()`. For a discussion of the reflection APIs for `Constructor` see the *Creating New Class Instances* section of this trail.)

<pre>
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Arrays;
import static java.lang.System.out;

public class ArrayCreator {
    private static String s = "java.math.BigInteger bi[] = { 123, 234, 345 }";
    private static Pattern p = Pattern.compile("^\\s*(\\S+)\\s*\\w+\\[\\].*\\{\\s*([^}]+)\\s*\\}");

    public static void main(String... args) {
        Matcher m = p.matcher(s);

        if (m.find()) {
            String cName = m.group(1);
            String[] cVals = m.group(2).split("[\\s,]+");
            int n = cVals.length;

            try {
                Class<&#63;> c = Class.forName(cName);
                Object o = Array.newInstance(c, n);
                for (int i = 0; i < n; i++) {
                    String v = cVals[i];
                    Constructor ctor = c.getConstructor(String.class);
                    Object val = ctor.newInstance(v);
                    Array.set(o, i, val);
                }

                Object[] oo = (Object[])o;
                out.format("%s[] = %s%n", cName, Arrays.toString(oo));

            // production code should handle these exceptions more gracefully
            } catch (ClassNotFoundException x) {
                x.printStackTrace();
            } catch (NoSuchMethodException x) {
                x.printStackTrace();
            } catch (IllegalAccessException x) {
                x.printStackTrace();
            } catch (InstantiationException x) {
                x.printStackTrace();
            } catch (InvocationTargetException x) {
                x.printStackTrace();
            }
        }
    }
}
</pre>

<pre>
$ <em>java ArrayCreator</em>
java.math.BigInteger [] = [123, 234, 345]
</pre>

The above example shows one case where it may be desirable to create an array via reflection; namely if the component type is not known until runtime. In this case, the code uses `Class.forName()` to get a class for the desired component type and then calls a specific constructor to initialize each component of the array before setting the corresponding array value.

### Getting and Setting Arrays and Their Components

Just as in non-reflective code, an array field may be set or retrieved in its entirety or component by component. To set the entire array at once, use `java.lang.reflect.Field.set(Object obj, Object value)`. To retrieve the entire array, use `Field.get(Object)`. Individual components can be set or retrieved using methods in `java.lang.reflect.Array`.

`Array` provides methods of the form `setFoo()` and `getFoo()` for setting and getting components of any primitive type. For example, the component of an `int` array may be set with `Array.setInt(Object array, int index, int value)` and may be retrieved with `Array.getInt(Object array, int index)`.

These methods support automatic *widening* of data types. Therefore, `Array.getShort()` may be used to set the values of an `int` array since a 16-bit `short` may be widened to a 32-bit `int` without loss of data; on the other hand, invoking `Array.setLong()` on an array of int will cause an `IllegalArgumentException` to be thrown because a 64-bit `long` can not be narrowed to for storage in a 32-bit `int` without loss of information. This is true regardless of whether the actual values being passed could be accurately represented in the target data type. *The Java Language Specification, Java SE 7 Edition*, sections *Widening Primitive Conversion* and *Narrowing Primitive Conversion* contains a complete discussion of widening and narrowing conversions.

The components of arrays of reference types (including arrays of arrays) are set and retrieved using `Array.set(Object array, int index, int value)` and `Array.get(Object array, int index)`.

#### Setting a Field of Type Array

The `GrowBufferedReader` example illustrates how to replace the value of a field of type array. In this case, the code replaces the backing array for a `java.io.BufferedReader` with a larger one. (This assumes that the creation of the original `BufferedReader` is in code that is not modifiable; otherwise, it would be trivial to simply use the alternate constructor `BufferedReader(java.io.Reader in, int size)` which accepts an input buffer size.)

<pre>
import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import static java.lang.System.out;

public class GrowBufferedReader {
    private static final int srcBufSize = 10 * 1024;
    private static char[] src = new char[srcBufSize];
    static {
		src[srcBufSize - 1] = 'x';
    }
    private static CharArrayReader car = new CharArrayReader(src);

    public static void main(String... args) {
		try {
		    BufferedReader br = new BufferedReader(car);

		    Class<&#63;> c = br.getClass();
		    Field f = c.getDeclaredField("cb");

		    // cb is a private field
		    f.setAccessible(true);
		    char[] cbVal = char[].class.cast(f.get(br));

		    char[] newVal = Arrays.copyOf(cbVal, cbVal.length * 2);
		    if (args.length > 0 && args[0].equals("grow"))
			f.set(br, newVal);

		    for (int i = 0; i < srcBufSize; i++)
			br.read();

		    // see if the new backing array is being used
		    if (newVal[srcBufSize - 1] == src[srcBufSize - 1])
			out.format("Using new backing array, size=%d%n", newVal.length);
		    else
			out.format("Using original backing array, size=%d%n", cbVal.length);

	        // production code should handle these exceptions more gracefully
		} catch (FileNotFoundException x) {
		    x.printStackTrace();
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (IOException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

<pre>
$ <em>java GrowBufferedReader grow</em>
Using new backing array, size=16384
$ <em>java GrowBufferedReader</em>
Using original backing array, size=8192
</pre>

Note that the above example makes use of the array utility method `java.util.Arrays.copyOf(char[] original, int newLength)`. `java.util.Arrays` contains many methods which are convenient when operating on arrays.

#### Accessing Elements of a Multidimensional Array

Multi-dimensional arrays are simply nested arrays. A two-dimensional array is an array of arrays. A three-dimensional array is an array of two-dimensional arrays, and so on. The `CreateMatrix` example illustrates how to create and initialize a multi-dimensional array using reflection.

```java
import java.lang.reflect.Array;
import static java.lang.System.out;

public class CreateMatrix {
    public static void main(String... args) {
        Object matrix = Array.newInstance(int.class, 2, 2);
        Object row0 = Array.get(matrix, 0);
        Object row1 = Array.get(matrix, 1);

        Array.setInt(row0, 0, 1);
        Array.setInt(row0, 1, 2);
        Array.setInt(row1, 0, 3);
        Array.setInt(row1, 1, 4);

        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++)
                out.format("matrix[%d][%d] = %d%n", i, j, ((int[][])matrix)[i][j]);
    }
}
```

<pre>
$ <em>java CreateMatrix</em>
matrix[0][0] = 1
matrix[0][1] = 2
matrix[1][0] = 3
matrix[1][1] = 4
</pre>

The same result could be obtained by using the following code fragment:

```java
Object matrix = Array.newInstance(int.class, 2);
Object row0 = Array.newInstance(int.class, 2);
Object row1 = Array.newInstance(int.class, 2);

Array.setInt(row0, 0, 1);
Array.setInt(row0, 1, 2);
Array.setInt(row1, 0, 3);
Array.setInt(row1, 1, 4);

Array.set(matrix, 0, row0);
Array.set(matrix, 1, row1);
```

The variable argument <code>Array.newInstance(Class<&#63;> componentType, int... dimensions)</code> provides a convenient way to create multi-dimensional arrays, but the components still need to initialized using the principle that that multi-dimensional arrays are nested arrays. (Reflection does not provide multiple indexed `get/set` methods for this purpose.)

### Troubleshooting

The following examples show typical errors which may occur when operating on arrays.

#### IllegalArgumentException due to Inconvertible Types

The `ArrayTroubleAgain` example will generate an `IllegalArgumentException`. `Array.setInt()` is invoked to set a component that is of the reference type `Integer` with a value of primitive type `int`. In the non-reflection equivalent `ary[0] = 1`, the compiler would convert (or *box*) the value `1` to a reference type as `new Integer(1)` so that its type checking will accept the statement. When using reflection, type checking only occurs at runtime so there is no opportunity to box the value.

```java
import java.lang.reflect.Array;
import static java.lang.System.err;

public class ArrayTroubleAgain {
    public static void main(String... args) {
		Integer[] ary = new Integer[2];
		try {
		    Array.setInt(ary, 0, 1);  // IllegalArgumentException

	        // production code should handle these exceptions more gracefully
		} catch (IllegalArgumentException x) {
		    err.format("Unable to box%n");
		} catch (ArrayIndexOutOfBoundsException x) {
		    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java ArrayTroubleAgain</em>
Unable to box
</pre>

To eliminate this exception, the problematic line should be replaced by the following invocation of `Array.set(Object array, int index, Object value)`:

```java
Array.set(ary, 0, new Integer(1));
```

---

**Tip:** When using reflection to set or get an array component, the compiler does not have an opportunity to perform boxing. It can only convert types that are related as described by the specification for `Class.isAssignableFrom()`. The example is expected to fail because `isAssignableFrom()` will return `false` in this test which can be used programmatically to verify whether a particular conversion is possible:

```java
Integer.class.isAssignableFrom(int.class) == false
```

Similarly, automatic conversion from primitive to reference type is also impossible in reflection.

```java
int.class.isAssignableFrom(Integer.class) == false
```

---

#### ArrayIndexOutOfBoundsException for Empty Arrays

The `ArrayTrouble` example illustrates an error which will occur if an attempt is made to access the elements of an array of zero length:

```java
import java.lang.reflect.Array;
import static java.lang.System.out;

public class ArrayTrouble {
    public static void main(String... args) {
        Object o = Array.newInstance(int.class, 0);
        int[] i = (int[])o;
        int[] j = new int[0];
        out.format("i.length = %d, j.length = %d, args.length = %d%n",
                   i.length, j.length, args.length);
        Array.getInt(o, 0);  // ArrayIndexOutOfBoundsException
    }
}
```

<pre>
$ <em>java ArrayTrouble</em>
i.length = 0, j.length = 0, args.length = 0
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException
        at java.lang.reflect.Array.getInt(Native Method)
        at ArrayTrouble.main(ArrayTrouble.java:11)
</pre>

---

**Tip:** It is possible to have arrays with no elements (empty arrays). There are only a few cases in common code where they are seen but they can occur in reflection inadvertently. Of course, it is not possible to set/get the values of an empty array because an `ArrayIndexOutOfBoundsException` will be thrown.

---

#### IllegalArgumentException if Narrowing is Attempted

The `ArrayTroubleToo` example contains code which fails because it attempts perform an operation which could potentially lose data:

```java
import java.lang.reflect.Array;
import static java.lang.System.out;

public class ArrayTroubleToo {
    public static void main(String... args) {
        Object o = new int[2];
        Array.setShort(o, 0, (short)2);  // widening, succeeds
        Array.setLong(o, 1, 2L);         // narrowing, fails
    }
}
```

<pre>
$ <em>java ArrayTroubleToo</em>
Exception in thread "main" java.lang.IllegalArgumentException: argument type
  mismatch
        at java.lang.reflect.Array.setLong(Native Method)
        at ArrayTroubleToo.main(ArrayTroubleToo.java:9)
</pre>

---

**Tip:** The `Array.set*()` and `Array.get*()` methods will perform automatic widening conversion but will throw an `IllegalArgumentException` if a narrowing conversion is attempted. For complete discussion of widening and narrowing conversions, see *The Java Language Specification, Java SE 7 Edition*, sections *Widening Primitive Conversion* and *Narrowing Primitive Conversion* respectively.

---

## Enumerated Types

An *enum* is a language construct that is used to define type-safe enumerations which can be used when a fixed set of named values is desired. All enums implicitly extend `java.lang.Enum`. Enums may contain one or more *enum constants*, which define unique instances of the enum type. An enum declaration defines an *enum type* which is very similar to a class in that it may have members such as fields, methods, and constructors (with some restrictions).

Since enums are classes, reflection has no need to define an explicit `java.lang.reflect.Enum` class. The only Reflection APIs that are specific to enums are `Class.isEnum()`, `Class.getEnumConstants()`, and `java.lang.reflect.Field.isEnumConstant()`. Most reflective operations involving enums are the same as any other class or member. For example, enum constants are implemented as `public static final` fields on the enum. The following sections show how to use `Class` and `java.lang.reflect.Field` with enums.

- **Examining Enums** illustrates how to retrieve an enum's constants and any other fields, constructors, and methods
- **Getting and Setting** Fields with Enum Types shows how to set and get fields with an enum constant value
- **Troubleshooting** describes common errors associated with enums

### Examining Enums

Reflection provides three enum-specific APIs:

<dl>
	<dt><code>Class.isEnum()</code></dt>
	<dd>Indicates whether this class represents an enum type</dd>
	<dt><code>Class.getEnumConstants()</code></dt>
	<dd>Retrieves the list of enum constants defined by the enum in the order they&#39;re declared</dd>
	<dt><code>java.lang.reflect.Field.isEnumConstant()</code></dt>
	<dd>Indicates whether this field represents an element of an enumerated type</dd>
</dl>

Sometimes it is necessary to dynamically retrieve the list of enum constants; in non-reflective code this is accomplished by invoking the implicitly declared static method `values()` on the enum. If an instance of an enum type is not available the only way to get a list of the possible values is to invoke `Class.getEnumConstants()` since it is impossible to instantiate an enum type.

Given a fully qualified name, the `EnumConstants` example shows how to retrieve an ordered list of constants in an enum using `Class.getEnumConstants()`.

<pre>
import java.util.Arrays;
import static java.lang.System.out;

enum Eon { HADEAN, ARCHAEAN, PROTEROZOIC, PHANEROZOIC }

public class EnumConstants {
    public static void main(String... args) {
		try {
		    Class<&#63;> c = (args.length == 0 ? Eon.class : Class.forName(args[0]));
		    out.format("Enum name:  %s%nEnum constants:  %s%n",
			       c.getName(), Arrays.asList(c.getEnumConstants()));
		    if (c == Eon.class)
			out.format("  Eon.values():  %s%n",
				   Arrays.asList(Eon.values()));

	        // production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

Samples of the output follows. User input is in italics.

<pre>
$ <em>java EnumConstants java.lang.annotation.RetentionPolicy</em>
Enum name:  java.lang.annotation.RetentionPolicy
Enum constants:  [SOURCE, CLASS, RUNTIME]
$ <em>java EnumConstants java.util.concurrent.TimeUnit</em>
Enum name:  java.util.concurrent.TimeUnit
Enum constants:  [NANOSECONDS, MICROSECONDS, 
                  MILLISECONDS, SECONDS, 
                  MINUTES, HOURS, DAYS]
</pre>

This example also shows that value returned by `Class.getEnumConstants()` is identical to the value returned by invoking `values()` on an enum type.

<pre>
$ <em>java EnumConstants</em>
Enum name:  Eon
Enum constants:  [HADEAN, ARCHAEAN, 
                  PROTEROZOIC, PHANEROZOIC]
Eon.values():  [HADEAN, ARCHAEAN, 
                PROTEROZOIC, PHANEROZOIC]
</pre>

Since enums are classes, other information may be obtained using the same Reflection APIs described in the *Fields*, *Methods*, and *Constructors* sections of this trail. The `EnumSpy` code illustrates how to use these APIs to get additional information about the enum's declaration. The example uses `Class.isEnum()` to restrict the set of classes examined. It also uses `Field.isEnumConstant()` to distinguish enum constants from other fields in the enum declaration (not all fields are enum constants).

<pre>
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.util.List;
import java.util.ArrayList;
import static java.lang.System.out;

public class EnumSpy {
    private static final String fmt = "  %11s:  %s %s%n";

    public static void main(String... args) {
		try {
		    Class<&#63;> c = Class.forName(args[0]);
		    if (!c.isEnum()) {
			out.format("%s is not an enum type%n", c);
			return;
		    }
		    out.format("Class:  %s%n", c);

		    Field[] flds = c.getDeclaredFields();
		    List&lt;Field> cst = new ArrayList&lt;Field>();  // enum constants
		    List&lt;Field> mbr = new ArrayList&lt;Field>();  // member fields
		    for (Field f : flds) {
			if (f.isEnumConstant())
			    cst.add(f);
			else
			    mbr.add(f);
		    }
		    if (!cst.isEmpty())
			print(cst, "Constant");
		    if (!mbr.isEmpty())
			print(mbr, "Field");

		    Constructor[] ctors = c.getDeclaredConstructors();
		    for (Constructor ctor : ctors) {
			out.format(fmt, "Constructor", ctor.toGenericString(),
				   synthetic(ctor));
		    }

		    Method[] mths = c.getDeclaredMethods();
		    for (Method m : mths) {
			out.format(fmt, "Method", m.toGenericString(),
				   synthetic(m));
		    }

	        // production code should handle this exception more gracefully
		} catch (ClassNotFoundException x) {
		    x.printStackTrace();
		}
    }

    private static void print(List&lt;Field> lst, String s) {
		for (Field f : lst) {
	 	    out.format(fmt, s, f.toGenericString(), synthetic(f));
		}
    }

    private static String synthetic(Member m) {
		return (m.isSynthetic() ? "[ synthetic ]" : "");
    }
}
</pre>

<pre>
$ <em>java EnumSpy java.lang.annotation.RetentionPolicy</em>
Class:  class java.lang.annotation.RetentionPolicy
     Constant:  public static final java.lang.annotation.RetentionPolicy
                  java.lang.annotation.RetentionPolicy.SOURCE 
     Constant:  public static final java.lang.annotation.RetentionPolicy
                  java.lang.annotation.RetentionPolicy.CLASS 
     Constant:  public static final java.lang.annotation.RetentionPolicy 
                  java.lang.annotation.RetentionPolicy.RUNTIME 
        Field:  private static final java.lang.annotation.RetentionPolicy[] 
                  java.lang.annotation.RetentionPolicy. [ synthetic ]
  Constructor:  private java.lang.annotation.RetentionPolicy() 
       Method:  public static java.lang.annotation.RetentionPolicy[]
                  java.lang.annotation.RetentionPolicy.values() 
       Method:  public static java.lang.annotation.RetentionPolicy
                  java.lang.annotation.RetentionPolicy.valueOf(java.lang.String) 
</pre>

The output shows that declaration of `java.lang.annotation.RetentionPolicy` only contains the three enum constants. The enum constants are exposed as `public static final` fields. The field, constructor, and methods are compiler generated. The `$VALUES` field is related to the implementation of the `values()` method.

---

**Note:** For various reasons, including support for evolution of the enum type, the declaration order of enum constants is important. `Class.getFields()` and `Class.getDeclaredFields()` do not make any guarantee that the order of the returned values matches the order in the declaring source code. If ordering is required by an application, use `Class.getEnumConstants()`.

---

The output for `java.util.concurrent.TimeUnit` shows that much more complicated enums are possible. This class includes several methods as well as additional fields declared `static final` which are not enum constants.

<pre>
$ <em>java EnumSpy java.util.concurrent.TimeUnit</em>
Class:  class java.util.concurrent.TimeUnit
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.NANOSECONDS
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.MICROSECONDS
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.MILLISECONDS
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.SECONDS
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.MINUTES
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.HOURS
     Constant:  public static final java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.DAYS
        Field:  static final long java.util.concurrent.TimeUnit.C0
        Field:  static final long java.util.concurrent.TimeUnit.C1
        Field:  static final long java.util.concurrent.TimeUnit.C2
        Field:  static final long java.util.concurrent.TimeUnit.C3
        Field:  static final long java.util.concurrent.TimeUnit.C4
        Field:  static final long java.util.concurrent.TimeUnit.C5
        Field:  static final long java.util.concurrent.TimeUnit.C6
        Field:  static final long java.util.concurrent.TimeUnit.MAX
        Field:  private static final java.util.concurrent.TimeUnit[] 
                  java.util.concurrent.TimeUnit. [ synthetic ]
  Constructor:  private java.util.concurrent.TimeUnit()
  Constructor:  java.util.concurrent.TimeUnit
                  (java.lang.String,int,java.util.concurrent.TimeUnit)
                  [ synthetic ]
       Method:  public static java.util.concurrent.TimeUnit
                  java.util.concurrent.TimeUnit.valueOf(java.lang.String)
       Method:  public static java.util.concurrent.TimeUnit[] 
                  java.util.concurrent.TimeUnit.values()
       Method:  public void java.util.concurrent.TimeUnit.sleep(long) 
                  throws java.lang.InterruptedException
       Method:  public long java.util.concurrent.TimeUnit.toNanos(long)
       Method:  public long java.util.concurrent.TimeUnit.convert
                  (long,java.util.concurrent.TimeUnit)
       Method:  abstract int java.util.concurrent.TimeUnit.excessNanos
                  (long,long)
       Method:  public void java.util.concurrent.TimeUnit.timedJoin
                  (java.lang.Thread,long) throws java.lang.InterruptedException
       Method:  public void java.util.concurrent.TimeUnit.timedWait
                  (java.lang.Object,long) throws java.lang.InterruptedException
       Method:  public long java.util.concurrent.TimeUnit.toDays(long)
       Method:  public long java.util.concurrent.TimeUnit.toHours(long)
       Method:  public long java.util.concurrent.TimeUnit.toMicros(long)
       Method:  public long java.util.concurrent.TimeUnit.toMillis(long)
       Method:  public long java.util.concurrent.TimeUnit.toMinutes(long)
       Method:  public long java.util.concurrent.TimeUnit.toSeconds(long)
       Method:  static long java.util.concurrent.TimeUnit.x(long,long,long)
</pre>

### Getting and Setting Fields with Enum Types

Fields which store enums are set and retrieved as any other reference type, using `Field.set()` and `Field.get()`. For more information on accessing fields, see the Fields section of this trail.

Consider application which needs to dynamically modify the trace level in a server application which normally does not allow this change during runtime. Assume the instance of the server object is available. The `SetTrace` example shows how code can translate the `String` representation of an enum into an enum type and retrieve and set the value of a field storing an enum.

<pre>
import java.lang.reflect.Field;
import static java.lang.System.out;

enum TraceLevel { OFF, LOW, MEDIUM, HIGH, DEBUG }

class MyServer {
    private TraceLevel level = TraceLevel.OFF;
}

public class SetTrace {
    public static void main(String... args) {
		TraceLevel newLevel = TraceLevel.valueOf(args[0]);

		try {
		    MyServer svr = new MyServer();
		    Class<&#63;> c = svr.getClass();
		    Field f = c.getDeclaredField("level");
		    f.setAccessible(true);
		    TraceLevel oldLevel = (TraceLevel)f.get(svr);
		    out.format("Original trace level:  %s%n", oldLevel);

		    if (oldLevel != newLevel) {
	 		f.set(svr, newLevel);
			out.format("    New  trace level:  %s%n", f.get(svr));
		    }

	        // production code should handle these exceptions more gracefully
		} catch (IllegalArgumentException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

Since the enum constants are singletons, the == and != operators may be used to compare enum constants of the same type.

<pre>
$ <em>java SetTrace OFF</em>
Original trace level:  OFF
$ <em>java SetTrace DEBUG</em>
Original trace level:  OFF
    New  trace level:  DEBUG
</pre>

### Troubleshooting

The following examples show problems which may be encountered when using enumerated types.

#### IllegalArgumentException When Attempting to Instantiate an Enum Type

As has been mentioned, instantiation of enum types is forbidden. The `EnumTrouble` example attempts this.

<pre>
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static java.lang.System.out;

enum Charge {
    POSITIVE, NEGATIVE, NEUTRAL;
    Charge() {
	out.format("under construction%n");
    }
}

public class EnumTrouble {

    public static void main(String... args) {
		try {
		    Class<&#63;> c = Charge.class;

	 	    Constructor[] ctors = c.getDeclaredConstructors();
	 	    for (Constructor ctor : ctors) {
				out.format("Constructor: %s%n",  ctor.toGenericString());
		 		ctor.setAccessible(true);
		 		ctor.newInstance();
	 	    }

	        // production code should handle these exceptions more gracefully
		} catch (InstantiationException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		} catch (InvocationTargetException x) {
		    x.printStackTrace();
		}
    }
}
</pre>

<pre>
$ <em>java EnumTrouble</em>
Constructor: private Charge()
Exception in thread "main" java.lang.IllegalArgumentException: Cannot
  reflectively create enum objects
        at java.lang.reflect.Constructor.newInstance(Constructor.java:511)
        at EnumTrouble.main(EnumTrouble.java:22)
</pre>

---

**Tip:** It is a compile-time error to attempt to explicitly instantiate an enum because that would prevent the defined enum constants from being unique. This restriction is also enforced in reflective code. Code which attempts to instantiate classes using their default constructors should invoke `Class.isEnum()` first to determine if the class is an enum.

---

#### IllegalArgumentException when Setting a Field with an Incompatible Enum Type

Fields storing enums set with the appropriate enum type. (Actually, fields of *any* type must be set with compatible types.) The `EnumTroubleToo` example produces the expected error.

```java
import java.lang.reflect.Field;

enum E0 { A, B }
enum E1 { A, B }

class ETest {
    private E0 fld = E0.A;
}

public class EnumTroubleToo {
    public static void main(String... args) {
		try {
		    ETest test = new ETest();
		    Field f = test.getClass().getDeclaredField("fld");
		    f.setAccessible(true);
	 	    f.set(test, E1.A);  // IllegalArgumentException

	        // production code should handle these exceptions more gracefully
		} catch (NoSuchFieldException x) {
		    x.printStackTrace();
		} catch (IllegalAccessException x) {
		    x.printStackTrace();
		}
    }
}
```

<pre>
$ <em>java EnumTroubleToo</em>
Exception in thread "main" java.lang.IllegalArgumentException: Can not set E0
  field ETest.fld to E1
        at sun.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException
          (UnsafeFieldAccessorImpl.java:146)
        at sun.reflect.UnsafeFieldAccessorImpl.throwSetIllegalArgumentException
          (UnsafeFieldAccessorImpl.java:150)
        at sun.reflect.UnsafeObjectFieldAccessorImpl.set
          (UnsafeObjectFieldAccessorImpl.java:63)
        at java.lang.reflect.Field.set(Field.java:657)
        at EnumTroubleToo.main(EnumTroubleToo.java:16)
</pre>

---

**Tip:** Strictly speaking, any attempt to set a field of type `X` to a value of type `Y` can only succeed if the following statement holds:

```java
X.class.isAssignableFrom(Y.class) == true
```

The code could be modified to perform the following test to verify whether the types are compatible:

```java
if (f.getType().isAssignableFrom(E0.class))
    // compatible
else
    // expect IllegalArgumentException
```

---
