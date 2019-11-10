# FluentMongo <div style="float:right">![Patent Pending](https://img.shields.io/badge/patent-pending-informational) [![GitHub](https://img.shields.io/github/license/streamx-co/FluentMongo)](https://fluentjpa.com) ![Java Version](https://img.shields.io/badge/java-%3E%3D%208-success) [![Build Status](https://travis-ci.org/streamx-co/FluentJPA.svg?branch=master)](https://travis-ci.org/streamx-co/FluentJPA) [![Maven Central](https://img.shields.io/maven-central/v/co.streamx.fluent/fluent-mongo?label=maven%20central)](https://search.maven.org/search?q=g:%22co.streamx.fluent%22%20AND%20a:%22fluent-mongo%22)</div>

Fluent API for writing typesafe Mongo queries in Java.

## Read Operations

To facilitate the creation of filter document, the Mongo Java driver provides [helper methods](https://mongodb.github.io/mongo-java-driver/3.11/driver-async/tutorials/perform-read-operations/#filters-helper), so the filter condition specification looks like this:

```java
collection.find(and(gte("stars", 2), lt("stars", 5), eq("categories", "Bakery")));
```

The following example specifies the same filter condition using FluentMongo:

```java
collection.find(FLUENT.filter(r -> r.getStars() >= 2 && r.getStars() < 5 && r.getCategories().contains("Bakery")));
```

No hard coded strings like `eq("categories", "Bakery")` or strange operators like `lt("stars", 5)`. The filter is a **normal** Java expression, with intellisense, refactorings and compiler verified type safety. Moreover, we even let write `r.getCategories().contains("Bakery")` to make the expression as readable and type safe as possible.

The full [Sort with Projection](https://mongodb.github.io/mongo-java-driver/3.11/driver-async/tutorials/perform-read-operations/#sort-with-projections) example from Mongo manual reads like this:

```java
TypedCollection<Restaurant> FLUENT = FluentMongo.collection(Restaurant.class);

Bson filter     = FLUENT.filter(r -> r.getStars() >= 2 && r.getStars() < 5
                                                       && r.getCategories().contains("Bakery"));
Bson sort       = FLUENT.sort(r -> ascending(r.getName()));
Bson projection = FLUENT.project(r -> fields(include(r.getName(), r.getStars(), r.getCategories()),
                                                                                      excludeId()));

collection.find(filter).sort(order).projection(projection);
```

## Write Operations

[Mongo official driver](https://mongodb.github.io/mongo-java-driver/3.11/driver-async/tutorials/perform-write-operations/#update-a-single-document):

```java
collection.updateOne(
                eq("_id", new ObjectId("57506d62f57802807471dd41")),
                combine(set("stars", 1),
                        set("contact.phone", "228-555-9999"),
                        currentDate("lastModified")));
```

FluentMongo:

```java
// Full intellisense and compiler verified type safety:
Bson filter = FLUENT.filter(r -> eq("57506d62f57802807471dd41"));
Bson update = FLUENT.update(r -> combine(set(r.getStars(), 1),
                                         set(r.getContact().getPhone(), "228-555-9999"),
                                         currentDate(r.getLastModified())));
collection.updateOne(filter, update);
```

> **Note:** FluentMongo does not replace the official driver. It parses the expression and forwards the call to the suitable helper method in the official driver, ensuring the behavior is same with direct usage of the official driver.
