## Wiki
1. `Test Class`
   - can be a top-level class, static member class, or an inner class annotated as `@Nested`
   - cannot be abstract and must have single constructor
2. `Test method`
   - an instance method that is annotated with `@Test`, `@RepeatedTest`, `@ParameterizedTest`, `@TestFactory`, or `@TestTemplate`
   - must not be abstract and return type must be void 
3. `Lifecycle method`
   - method annotated with `@BeforeAll`, `@AfterAll`, `@BeforeEach`, `@AfterEach`
4. `Tagged Tests`
   - group tests into categories
   - ```java 
     @Tag("individual")
     public class CustomerTest() {...}

     @Tag("repository")
     public class CustomerRepositoryTest
     ```
   - ```xml
     <plugin> 
       <artifactId>maven-surefire-plugin</artifactId> 
       <version>2.22.2</version> 
       <!--
       <configuration> 
         <groups>individual</groups>
         <excludedGroups>repository</excludedGroups>
       </configuration> 
       -->
     </plugin>
     ```
5. `Assertions`
   - use for performing test validations
6. `HamCrest`
   - framework that assists with the writing of tests in JUnit
7. xx

## Annotations
1. `@Disabled`
   - target: Method, Class
   - target will not be executed
```java
@Disabled("Feature still under construction")
class DisabledClassTest{ ... }

@Test // method
@Disabled("Feature still under construction")
public void testMethod(){ ... }
```

2. `@RepeatedTest`
   - placeholders:
     - `{displayName}` - Display name of the method annotated with @RepeatedTest
     - `{currentRepetition}`
     - `{totalRepetition}`


3. `@ParameterizedTest`
   - allows a test to run multiple times with different arguments
   - use `@ValueSource` to specify a single array of literal values

## JUnit Core

Sample:
```java
public class CalculatorTest {

  @Test // a unit test method
  void testAdd() {
    Calculator calculator = new Calculator();
    double result = calculator.add(10, 50);
    assertEquals(60d, result, 0d, "Test Result Is Correct.");
  }
}
```


