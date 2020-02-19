## MyersDiffKt

Kotlin Multiplatform library implements Myers algorithm for diff calculations.

The algorithm is implemented in common source set.

The main part of the algorithm implementation is taken from the Android library - [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil).

### Use example

To calculate difference for two lists, use `DiffUtil` singleton object.

There are two public functions:

```kotlin
suspend fun diffFlow(
    oldListSize: Int,
    newListSize: Int,
    comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
): Flow<Change>
```

```kotlin
fun diffCallback(
    oldListSize: Int,
    newListSize: Int,
    comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
): DiffResult
```
