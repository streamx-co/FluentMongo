# Get in Control of Your Mongo Queries <div style="float:right">![Patent Pending](https://img.shields.io/badge/patent-pending-informational) [![GitHub](https://img.shields.io/badge/license-LGPL_3.0-success)](LICENSE) ![Java Version](https://img.shields.io/badge/java-%3E%3D%208-success) [![Build Status](https://travis-ci.org/streamx-co/FluentMongo.svg?branch=release)](https://travis-ci.org/streamx-co/FluentMongo) [![Maven Central](https://img.shields.io/maven-central/v/co.streamx.fluent/fluent-mongo?label=maven%20central)](https://search.maven.org/search?q=g:%22co.streamx.fluent%22%20AND%20a:%22fluent-mongo%22)</div>

Fluent API for writing typesafe Mongo queries in Java. See wiki for [samples & setup](https://github.com/streamx-co/FluentMongo/wiki).

## Read Operations

Official Mongo Java driver provides [helper methods](https://mongodb.github.io/mongo-java-driver/3.11/driver-async/tutorials/perform-read-operations/#filters-helper) for writing queries, so the filter condition specification looks like this:

```java
collection.find(and(gte("stars", 2), lt("stars", 5), eq("categories", "Bakery")));
```

The following example specifies the same filter condition using FluentMongo:

```java
collection.find(builder.filter(r -> r.getStars() >= 2 && r.getStars() < 5
                                                      && r.getCategories().contains("Bakery")));
```

No hard coded strings like `eq("categories", "Bakery")` or strange operators like `lt("stars", 5)`. The filter is a **normal** Java expression, with intellisense, refactoring friendly and compiler verified type safety. Moreover, we even let write `r.getCategories().contains("Bakery")` to make the expression as readable and type safe as possible.

The full [Sort with Projection](https://mongodb.github.io/mongo-java-driver/3.11/driver-async/tutorials/perform-read-operations/#sort-with-projections) example from Mongo manual reads like this:

```java
QueryBuilder<Restaurant> builder = FluentMongo.queryBuilder(Restaurant.class);

Bson filter     = builder.filter(r -> r.getStars() >= 2 && r.getStars() < 5
                                                        && r.getCategories().contains("Bakery"));
Bson sort       = builder.sort(r -> ascending(r.getName()));
Bson projection = builder.project(r -> fields(include(r.getName(), r.getStars(), r.getCategories()),
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
Bson filter = builder.filter(r -> eq("57506d62f57802807471dd41"));
Bson update = builder.update(r -> combine(set(r.getStars(), 1),
                                          set(r.getContact().getPhone(), "228-555-9999"),
                                          currentDate(r.getLastModified())));
collection.updateOne(filter, update);
```

> **Note:** FluentMongo does not replace the official driver. It parses the expression and forwards the call to the suitable helper method in the official driver, ensuring the behavior is same with direct usage of the official driver.

## License

Fluent Mongo employs a Freemium license model, where some functionality is totally free and more advanced features [require a license](https://fluentjpa.com). Data retrieval expressions (filter, projection, sort, index) are free. Data update expressions [require a license](https://fluentjpa.com).

> This work is dual-licensed under [Affero GPL 3.0](https://opensource.org/licenses/AGPL-3.0) and [Lesser GPL 3.0](https://opensource.org/licenses/LGPL-3.0).
The source code is licensed under AGPL and official binaries under LGPL.

`SPDX-License-Identifier: AGPL-3.0-only AND LGPL-3.0-only`
