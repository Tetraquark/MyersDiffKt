package ru.tetraquark.myersdiffkt

import kotlin.math.abs

class MyersDiff {

    fun findSnakes(
        oldListSize: Int,
        newListSize: Int,
        comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
    ): List<Snake> {
        val snakes = mutableListOf<Snake>()
        val rangeStack = mutableListOf(Range(0, oldListSize, 0, newListSize))

        val max = oldListSize + newListSize + abs(oldListSize - newListSize)

        val forward = IntArray(max * 2)
        val backward = IntArray(max * 2)

        val rangePool = mutableListOf<Range>()

        while (rangeStack.isNotEmpty()) {
            val range = rangeStack.removeAt(rangeStack.size - 1)

            val snake = findMiddleSnake(
                startOld = range.firstStart,
                endOld = range.firstEnd,
                startNew = range.secondStart,
                endNew = range.secondEnd,
                forward = forward,
                backward = backward,
                kOffset = max,
                comparator = comparator
            )

            if (snake != null) {
                if (snake.offset > 0) {
                    snakes.add(snake)
                }

                snake.start += range.firstStart
                snake.end += range.secondStart

                val left = if (rangePool.isNotEmpty()) {
                    rangePool.removeAt(rangePool.size - 1)
                } else {
                    Range()
                }.apply {
                    firstStart = range.firstStart
                    secondStart = range.secondStart

                    if (snake.reverse) {
                        firstEnd = snake.start
                        secondEnd = snake.end
                    } else {
                        if (snake.removal) {
                            firstEnd = snake.start - 1
                            secondEnd = snake.end
                        } else {
                            firstEnd = snake.start
                            secondEnd = snake.end - 1
                        }
                    }
                }
                rangeStack.add(left)

                val right = range
                if (snake.reverse) {
                    if (snake.removal) {
                        right.firstStart = snake.start + snake.offset + 1
                        right.secondStart = snake.end + snake.offset
                    } else {
                        right.firstStart = snake.start + snake.offset
                        right.secondStart = snake.end + snake.offset + 1
                    }
                } else {
                    right.firstStart = snake.start + snake.offset
                    right.secondStart = snake.end + snake.offset
                }
                rangeStack.add(right)
            } else {
                rangePool.add(range)
            }
        }

        snakes.sortWith(object : Comparator<Snake> {
            override fun compare(a: Snake, b: Snake): Int {
                val cmpX = a.start - b.start
                return if (cmpX == 0) a.end - b.end else cmpX
            }
        })

        // Add the root snake if needed
        val firstSnake = snakes.firstOrNull()
        if (firstSnake == null || firstSnake.start != 0 || firstSnake.end != 0) {
            snakes.add(0, Snake(start = 0, end = 0, offset = 0, removal = false, reverse = false))
        }

        return snakes
    }

    fun findMiddleSnake(
        startOld: Int,
        endOld: Int,
        startNew: Int,
        endNew: Int,
        forward: IntArray,
        backward: IntArray,
        kOffset: Int,
        comparator: (oldItemIndex: Int, newItemIndex: Int) -> Boolean
    ): Snake? {
        val oldSize = endOld - startOld
        val newSize = endNew - startNew

        if (endOld - startOld < 1 || endNew - startNew < 1)
            return null

        val delta = oldSize - newSize
        val dLimit = (oldSize + newSize + 1) / 2

        for (i in kOffset - dLimit - 1 until kOffset + dLimit + 1)
            forward[i] = 0
        for (i in kOffset - dLimit - 1 + delta until kOffset + dLimit + 1 + delta)
            backward[i] = oldSize

        val checkInFwd = delta % 2 != 0

        var d = 0
        while (d <= dLimit) {
            var k = -d
            while (k <= d) {

                var x: Int
                val removal: Boolean
                if (k == -d || (k != d && forward[kOffset + k - 1] < forward[kOffset + k + 1])) {
                    x = forward[kOffset + k + 1]
                    removal = false
                } else {
                    x = forward[kOffset + k - 1] + 1
                    removal = true
                }

                var y = x - k
                while (x < oldSize && y < newSize && comparator(startOld + x, startNew + y)) {
                    x++
                    y++
                }
                forward[kOffset + k] = x

                if (checkInFwd && k >= delta - d + 1 && k <= delta + d - 1) {
                    if (forward[kOffset + k] >= backward[kOffset + k]) {
                        return Snake(
                            start = backward[kOffset + k],
                            end = backward[kOffset + k] - k,
                            offset = forward[kOffset + k] - backward[kOffset + k],
                            removal = removal,
                            reverse = false
                        )
                    }
                }

                k += 2
            }

            k = -d
            while (k <= d) {
                val backwardK = k + delta

                var x: Int
                val removal: Boolean
                if (backwardK == d + delta || (backwardK != -d + delta && backward[kOffset + backwardK - 1] < backward[kOffset + backwardK + 1])) {
                    x = backward[kOffset + backwardK - 1]
                    removal = false
                } else {
                    x = backward[kOffset + backwardK + 1] - 1
                    removal = true
                }

                var y = x - backwardK
                while (x > 0 && y > 0 && comparator(startOld + x - 1, startNew + y - 1)) {
                    x--
                    y--
                }
                backward[kOffset + backwardK] = x
                if (!checkInFwd && k + delta >= -d && k + delta <= d) {
                    if (forward[kOffset + backwardK] >= backward[kOffset + backwardK]) {
                        return Snake(
                            start = backward[kOffset + backwardK],
                            end = backward[kOffset + backwardK] - backwardK,
                            offset = forward[kOffset + backwardK] - backward[kOffset + backwardK],
                            removal = removal,
                            reverse = true
                        )
                    }
                }
                k += 2
            }

            d++
        }

        throw IllegalStateException(
            "DiffUtil hit an unexpected case while trying to calculate"
                    + " the optimal path. Please make sure your data is not changing during the"
                    + " diff calculation."
        )
    }

}

class Snake(
    var start: Int,
    var end: Int,
    val offset: Int,
    val removal: Boolean,
    val reverse: Boolean
)

private class Range(
    var firstStart: Int = 0,
    var firstEnd: Int = 0,
    var secondStart: Int = 0,
    var secondEnd: Int = 0
)
