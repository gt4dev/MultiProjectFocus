# HOTest guide

This document describes how to write tests with the HOTest framework.

The core role of the HOTest framework is to enable writing human-readable tests - tests that are easy to read and explain the 'business idea' of the production code to the reader.
HOTest mixes the approaches of `gherkin language` and Kotlin language.
The final result is Kotlin code, but as human-friendly as possible.
Any low-level Kotlin code may be used in a test if it is practical and increases the brevity of the test code.


## core definitions
Each test is equivalent to a test scenario.
A test is a regular Kotlin function annotated with @Test.

A test scenario contains a series of steps.

The core role of a step is to present in a 'human-readable way' a single action which the app can execute.
Usually it is some call to production code which has business meaning, like:
`basket.addItem(item)`
`order.pay(txParams)`



## step definition
A 'step definition' is a notion from the gherkin / cucumber framework.
A 'step definition' is an implementation of a function called in a test.

Each step of a test should be placed in a class named like `TargetClassOfStep`.
For example:
- if there is a step: `given 'product repository mock' returns products`(..)
- and internally this step is aimed to call a function from an object with `interface ProductRepository` (from package `com.mylogic.domain`)
- then the step definition should be:
1/ placed in the same package as `interface ProductRepository`, here it is the package `com.mylogic.domain`
2/ placed in kotlin object: `ProductRepositorySteps`


## test body location
each test should:
1/ take its name from the component it tests
2/ be placed in the package where the tested component resides

for example
1/ if the test tests the component `ProjectReadModel` then the test should be named `ProjectReadModelTest`
2/ if the tested component `ProjectReadModel` is placed in package `com.mylogic.domain`
    then the test should also be placed in `com.mylogic.domain`


## test body content
The primary and most important aspect is to create test scenarios that are human-friendly and easy to read.
Thus, steps in the scenario must be human-friendly and easy to understand.
The style of language in tests should be similar to gherkin scenarios.

Each step should clearly describe 'what is expected' from the code, not the API details.

For example:
- if we have a scenario for testing the buying flow of a Shopping component
- then the test could have steps / body like:
```
`give 'shopping component' have user basket`(
  userId = "sampleUserId"
  products = listOf(
    SampleProduct(id = 101, name = "Sample1", price = 3)
    SampleProduct(id = 102, name = "Sample2", price = 5)
    SampleProduct(id = 103, name = "Sample3", price = 8)
  )
)
`when in 'shopping component' is called 'buy'`()
`then in 'inventory component' is called 'get from stock' for`(
  products = listOf(
    InventoryReservation(productId = 101, amount = 1)
    InventoryReservation(productId = 102, amount = 1)
    InventoryReservation(productId = 103, amount = 1)
)
`then 'payment component' is called to get payment`(123.45, USD)
```

## internal models used by steps
In the above steps, the classes SampleProduct and InventoryReservation are used.
These classes come from test code, not production code,
because their presence increases the readability of the scenario / step.
The API provided by production code is often less readable, burdened with technical details that hide the business sense of the production code.

Auxiliary model classes should be kept in `object Models` in the package of the related step.
For example:
1/ SampleProduct
- it belongs to the step `give 'shopping component' have user basket`
  so class SampleProduct must be nested in `object Models` in the test in package `com.mylogic.domain`
2/ InventoryReservation
- it belongs to the step `then in 'inventory component' is called 'get from stock' for`
  this step belongs to package `com.mylogic.domain.repositories`
  so `class InventoryReservation` should be nested in `object Models` in package `com.mylogic.domain.repositories`

