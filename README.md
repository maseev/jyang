JYANG
=====
[![Build Status](https://travis-ci.org/maseev/jyang.svg?branch=master)](https://travis-ci.org/maseev/jyang)
[![Coverage Status](https://coveralls.io/repos/github/maseev/jyang/badge.svg?branch=master)](https://coveralls.io/github/maseev/jyang?branch=master)

Java To YANG Translator
---------------------
JYANG is a Java to [YANG](https://tools.ietf.org/html/rfc6020) Translator. 
The main goal of this project is to translate your NETCONF RPCs written in Java to YANG.


How to build
-----------
* Clone this repository
* Run ``` ./gradlew clean install ``` in the project folder to build the project and install it to the local
Maven repository

How to use
---------

Add `jyang` as a dependency:

##### Maven
```xml
<dependency>
    <groupId>io.github.maseev</groupId>
    <artifactId>jyang</artifactId>
    <version>1.0</version>
</dependency>
```

##### Gradle
```groovy
compile group: 'io.github.maseev', name: 'jyang', version: '1.0'
```

Add a `@NetconfEndpoint` annotation on your Java class and a `@NetconfProcedure` annotation on 
your Java methods. Notice that you can alter your endpoint and procedure names by specifying the 
annotations value fields. Otherwise, class and method names will be used as endpoint and 
procedure names accordingly:

```java

public class UserDTO {
  private int id;
  private String name;
  
  // constructors, getters, setters ...
}

@MapsTo(UserDTO.class)
public class User {
  private int id;
  private String name;
  
  // constructors, getters, setters ...
}

// ServiceProcedure.java
@NetconfEndpoint("users")
public class UserEndpoint {

  @NetconfProcedure("getbyId")
  public User getUser(final String id) {
    // your implementation goes here ...
  }
}
```

Also, notice that you can alter your incoming parameter types as well as return type by using 
`@MapsTo` annotation.

Use the following code snippet in order to translate a NETCONF endpoint we defined earlier into 
a YANG model:

```java

Pair pair = new Translator().translateEndpoint(UserEndpoint.class);
Module module =
  new Module("user", "user endpoint YANG model", "namespace", "user",
    new Revision(new Date(), "Initial model revision"));
module.getGroupings().addAll(pair.getGroupings());
module.getRpcs().addAll(pair.getRpcs());

try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.yang"))) {
  writer.write(TemplateUtil.transform(module, "templates/module.mustache"));
}

```

Your YANG model will look like very similar to this:

```yaml
module user {
    namespace "namespace";
    prefix "user";
    revision "2017-03-03" {
        description "Initial model revision";
    }
    typedef char {
        type string {
            length "1";
        }
    }
    typedef timestamp {
        type uint32;
    }
    typedef double {
        type decimal64 {
            fraction-digits 8;
        }
    }
    typedef float {
        type binary {
            length "32";
        }
    }
    grouping UserDTO {
        leaf id {
            type int32;
        }
        leaf name {
            type string;
        }
    }
    rpc users.getbyId {
        input {
            leaf String {
                type string;
            }
        }
        output {
            container UserDTO {
                uses UserDTO;
            }
        }
    }
}
```

How to contribute
---------------
Found a bug or have an idea how to improve this project? Don't hesitate to open a new issue in the issue tracker or send a PR. I :heart: PRs.