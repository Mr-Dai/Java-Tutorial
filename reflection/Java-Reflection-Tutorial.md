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

## Classes

Every object is either a reference or primitive type. Reference types all inherit from [`java.lang.Object`](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html). Classes, enums, arrays, and interfaces are all reference types. There is a fixed set of primitive types: `boolean`, `byte`, `short`, `int`, `long`, `char`, `float`, and `double`. Examples of reference types include [`java.lang.String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html), all of the wrapper classes for primitive types such as [`java.lang.Double`](https://docs.oracle.com/javase/8/docs/api/java/lang/Double.html), the interface [`java.io.Serializable`](https://docs.oracle.com/javase/8/docs/api/java/io/Serializable.html), and the enum [`javax.swing.SortOrder`](https://docs.oracle.com/javase/8/docs/api/javax/swing/SortOrder.html).

For every type of object, the Java virtual machine instantiates an immutable instance of [`java.lang.Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) which provides methods to examine the runtime properties of the object including its members and type information. [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) also provides the ability to create new classes and objects. Most importantly, it is the entry point for all of the Reflection APIs. This lesson covers the most commonly used reflection operations involving classes:

*   **Retrieving Class Objects** describes the ways to get a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)
*   **Examining Class Modifiers and Types** shows how to access the class declaration information
*   **Discovering Class Members** illustrates how to list the constructors, fields, methods, and nested classes in a class
*   **Troubleshooting** describes common errors encountered when using [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html)

### Retrieving Class Objects

The entry point for all reflection operations is [`java.lang.Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). With the exception of [`java.lang.reflect.ReflectPermission`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/ReflectPermission.html), none of the classes in [`java.lang.reflect`](https://docs.oracle.com/javase/8/docs/api/java/lang/reflect/package-summary.html) have public constructors. To get to these classes, it is necessary to invoke appropriate methods on [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html). There are several ways to get a [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html) depending on whether the code has access to an object, the name of class, a type, or an existing [`Class`](https://docs.oracle.com/javase/8/docs/api/java/lang/Class.html).

#### Object.getClass()

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

## The .class Syntax

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

#### Class.forName()

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

## TYPE Field for Primitive Type Wrappers

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

## Methods that Return Classes

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
	    	Class<&#63;> c = Class.forName(args[0]);
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
	    	Class<&#63;> c = Class.forName(args[0]);
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
Class<&#63;> c = warn.getClass();
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
	    	Class<&#63;> c = Class.forName("Cls");
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
