## MyersDiffKt

Kotlin Multiplatform library implements iterative Myers algorithm for diff calculations without the second pass for moves detection.

The algorithm is implemented in common source set, so it can be used in any supported platform.

The main part of the algorithm implementation is rewritten to Kotlin from the Android Java library - [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil).

### Use example

To calculate difference for two lists, use `DiffUtil` singleton object. There are two public functions:

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

`diffFlow` function is suspendable and returns `Flow` instance, so difference calculations can be implemented in asynchronus style using Kotlin coroutines.

`diffCallback` function returns `DiffResult` object that holds a calculation results.

Use example for `diffFlow` function:

```kotlin
launch {
    DiffUtil.diffFlow(oldList.size, newList.size) { oldItemIndex, newItemIndex ->
        oldList[oldItemIndex] == newList[newItemIndex]
    }
    .flowOn(Dispatchers.Default)
    .collect { change ->
        applyChangeToLists(resultList, newList, change)
    }
}
```

There is simple example of Android application in **sample-android** module that uses the library to calculating and applying differences for RecyclerView content.


