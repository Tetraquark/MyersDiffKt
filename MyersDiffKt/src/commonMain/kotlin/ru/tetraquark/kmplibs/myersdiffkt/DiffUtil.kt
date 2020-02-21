package ru.tetraquark.kmplibs.myersdiffkt

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DiffUtil {

    private val myersDiff = MyersDiff()

    suspend fun diffFlow(
        oldListSize: Int,
        newListSize: Int,
        comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
    ): Flow<Change> {
        return flow {
            convertSnakesToChanges(
                myersDiff.findSnakes(oldListSize, newListSize, comparator),
                oldListSize,
                newListSize
            ) {
                emit(it)
            }
        }
    }

    fun diffCallback(
        oldListSize: Int,
        newListSize: Int,
        comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
    ): DiffResult {
        return DiffResult(
            oldListSize,
            newListSize,
            myersDiff.findSnakes(oldListSize, newListSize, comparator)
        )
    }

}

class DiffResult internal constructor(
    private val oldListSize: Int,
    private val newListSize: Int,
    private val snakes: List<Snake>
) {

    fun applyChanges(onChange: (change: Change) -> Unit) {
        convertSnakesToChanges(snakes, oldListSize, newListSize) {
            onChange(it)
        }

    }

}

private inline fun convertSnakesToChanges(
    snakes: List<Snake>,
    oldListSize: Int,
    newListSize: Int,
    emitter: (Change) -> Unit
) {
    var posOld = oldListSize
    var posNew = newListSize

    snakes.asReversed().forEach {
        val snakeOffset = it.offset
        val endX = it.start + snakeOffset
        val endY = it.end + snakeOffset

        if (endX < posOld) {
            emitter(Change.Remove(endX, posOld - endX))
        }
        if (endY < posNew) {
            emitter(Change.Insert(endX, endY, posNew - endY))
        }
        posOld = it.start
        posNew = it.end
    }

}
