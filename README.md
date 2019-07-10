# Mapper

---
**📦📦📦❗️❗️ This page is under construction ❗️❗️📦📦📦**

---

[![Travis (.org) branch][build_badge]][build_link] [![coverage][coverage_badge]][coverage_link] [![GitHub][license_badge]][license_link]  ![GitHub top language][top_language_badge] [![GitHub file size in bytes][code_size_badge]][download_jar]

---
## Prerequisites
The library is written in [Java 8][Java_8_link] with no external dependencies!

---
## Let's code!
Just before looking for the first example, there is a little prerequisite that we must follow: all destination class of a mapping must have an empty contructor, otherwise the `Mapper` will not be able to create a new instance to return. Maybe in the future this won't be absolutely required (see "In the future" section, point 2)!

Now let's do some mappings!

**Step 1**
the first step is to create a `Mapper` instance that will contains all our wanted mappings:
```java
Mapper mapper = new Mapper();
```
**Step 2**
once we have a `mapper`, we can define the mapping we want. Todo that specify a `Source` class and a `Destination` class:
```java
mapper.add(Source.class, Destination.class);    // create a mapping between Source and Destination types
```

**Step 3**
Once we have created a `mapper` and defined some mappings inside it (here from `Source` to `Destination`), we can map our objects:
```java
Source src = /*creation of a Source istance*/;
Destination dest = mapper.map(src);   // mapping src object into Destination type
```

**Full code**
```java
Mapper mapper = new Mapper();
mapper.add(Source.class, Destination.class);
Source src = /*creation of a Source istance*/;
Destination dest = mapper.map(src);
```
And that's all!

---
## Mappings of multiple classes and complex objects
The example above show the basic case of a `Mapper` with only one effective mapping inside. IT's not rare to want to convert different object or complex object that have not only simple field such that `String`, `int` or similar, but also complex variables like `ComplexSourceObject`.

The solution is simply: add more mappings to the `mapper`.

For example the `Source` class contains a `ComplexSourceObject` field and `Destination` a `ComplexDestinationObject` one.

```java
Mapper mapper = new Mapper();
mapper.add(Source.class, Destination.class);
mapper.add(ComplexSourceObject.class, ComplexDestinationObject.class);
Source src = /*creation of a Source istance*/;
Destination dest = mapper.map(src);
```

---
## How the mapper works?
When the `Mapper` is build, it search automatically the field in the source type and in the destination type that are called with the same name and of the same type (or il the source field can be assigned to the destination field).

But what happens if the needed mapping is between two variables with differents names, for example `String name` in the `Source` class and `String user_name` in the `Destination` class? There are different ways to do it, but the simplest is the folowing:
use the `@AliasNames` annotation to the `name` field passing the destination field name. For example:<br>
```java
public class Source {
    // ...
    @AliasNames("user_name")
    private String name
    // ...
}
```
We also can put the annotation to the destination field `@AliasNames("name")`, the result will be the same, or to both field (the same name inside the annotation): `@AliasNames("friend_name")`

For more customize mappings, see the dedicated section below ("Customize the mapping").

---
## Multiple mappings from/to the same type
In the `Mapper`, the identifier of a given mapping is the pair Source-Destination classes, so we can easily add all the mapping we want.
### From the same type
First we add two different mapping from the `Source` class.
```java
Mapper mapper = new Mapper();
mapper.add(Source.class, Destination1.class);
mapper.add(Source.class, Destination2.class);
```

To map a `source` instance, we can do as following:
```java
Source src = /*creation of a Source istance*/;
Destination1 dest = mapper.map(src,Destination1.class);
```
In this case we have to specify the destination class otherwise the `mapper` is not able to determine the destination type of the required mapping! If the `Source` class has only one mapping, the destination class in not mandatory (`mapper.map(src);`)
### To the same type
First we add two different mapping to the `Destination` class.
```java
Mapper mapper = new Mapper();
mapper.add(Source1.class, Destination.class);
mapper.add(Source2.class, Destination.class);
```
Now we have a known situation (one source class for one destination class) and we already know how manage it!
```java
Source1 src1 = /*creation of a Source1 istance*/;
Source2 src2 = /*creation of a Source2 istance*/;
Destination dest1 = mapper.map(src1);
Destination dest2 = mapper.map(src2);
```

---
## Customize the mapping
So far we only saw how to create default mapping, but rarely is the mapping we want at the end!

// TODO

---
## In the future
1) add to possibility to specify a `Supplier` how to create a destination class.
2) add options to the `Mapper` for adding annotation to use for the variable name (default @AliasNames). This can allow the user to use, for example, the JPA annotation @Column or any other annotation!
3) add the possibility to deep copy the values during the mapping.

---
## Download ![GitHub file size in bytes][code_size_badge_light]
You can download the jar to include into you project [here][download_jar].

---
## License
The code in this project is licensed under [MIT License][license_link].

---

[Java_8_link]: https://www.java.com/it/download/
[build_link]: https://travis-ci.org/eschoysman/Mapper
[coverage_link]: https://codecov.io/gh/eschoysman/Mapper
[license_link]: https://github.com/eschoysman/Mapper/blob/master/LICENSE
[download_jar]: https://github.com/eschoysman/Mapper/tree/master/jar
[build_badge]: https://img.shields.io/travis/eschoysman/Mapper/master.svg?style=popout-square&logo=travis
[coverage_badge]: https://img.shields.io/codecov/c/github/eschoysman/Mapper/master.svg?token=4fc9648063d6409691336c38bab54628&style=popout-square&logo=codecov
[license_badge]: https://img.shields.io/github/license/eschoysman/Mapper.svg?style=popout-square&logo=data:image/png;base64,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
[top_language_badge]: https://img.shields.io/github/languages/top/eschoysman/Mapper.svg?label=Java%208&style=popout-square&logo=java&logoColor=ff0000
[code_size_badge]: https://img.shields.io/github/size/eschoysman/Mapper/jar/mapper.jar.svg?style=popout-square&logo=data:image/png;base64,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
[code_size_badge_light]: https://img.shields.io/github/size/eschoysman/Mapper/jar/mapper.jar.svg?style=popout-square
