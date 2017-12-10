# Application Explanation

This application shows backlog issues as board.

## Component

This works on JDK 1.8.

## Development

* launch server
    * for Linux: `./gradlew bootrun -PactiveProfile=default`
    * for Windows: `gradlew bootrun -PactiveProfile=default`

If you want to create another environment, copy `application-default.properties.sample` to `application-your_env.properties`, and execute `./gradlew bootrun -PactiveProfile=your_env`

### Example

* `./gradlew deployWar -PactiveProfile=development -PserverRole=dev`
    * deploy after building war and test
* `./gradlew deployWar -PactiveProfile=development -PserverRole=dev -x war -x test`
    * deploy without building war or test

## Test

* all test
    * `./gradlew test`
* one test
    * `./gradlew test --tests "*TestClass"`
    * `./gradlew test --tests "com.package.name.dir.TestClass"`
    
## Coding Convention

### Common

* Lang class must inherit `Lang`.
* Use `Long`, not `Int`, as much as you can.
* Please prevent use `else` block when you use `if` statement.
* follow idea standard

```$kotlin
1 + 2 * 3 / 4
if (...) {
}
while (...) {
}
var v: V
val w: W
```

#### Naming

##### General

* Use `start` and `end` to represent interval
    * Good: `startDate`, `endTime`
    * Bad: `beginDate`
* Use `from` and `to` to represent search condition
* make variable and method camel case naming
* abbreviation must not be composed only of capital letters.
    * `Url`, `Ufo`
* Every method and variables have to be in Camel Case format.
    
##### Variables

* Add `List`, `Array`, `Set`, `Map` to its variables. Don't use plural form for representing collections
    * Good: `festivalList: List<Festival>`
    * Bad: `festivals: List<Festival>`
    
##### Method

* Begin method signature with verb

##### Presentation

* Don't write text directly, except for English. Use Lang class.

###### Controller

* Finish its name with `Controller`
* Add this line to every controller
    * `companion object State: RedirectAttributesHandler()`

##### View

* Finish its name with `View`
* Every view must be object
* The last parameter of every method showing html must be RedirectAttributes

###### Paging

* Use `Pager` class

##### Static File

* library => `public/lib`
    * ex) if you want to use Foundation library, create foundation directory under `public/lib` and extract it there.
* css file => `public/css`
* javascript file => `public/js`

* static file must be served from web server application with cache system (under construction)

##### URL

* for crud
    * GET / => index
    * POST / => create
    * GET /{id} => show (and edit)
    * GET /{id}/edit => edit
    * PATCH /{id} => update
    * DELETE /{id} => delete
