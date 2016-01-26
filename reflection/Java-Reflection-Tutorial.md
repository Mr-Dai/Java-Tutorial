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
