package ru.tetraquark.kmplibs.myersdiffkt

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class DiffUtilTest {

    private var oldList = mutableListOf<String>()

    @Before
    fun prepareData() {
        oldList = mutableListOf("A", "B", "C", "D", "E", "F", "G")
        Dispatchers
    }

    @Test
    fun diffCallbackTest() {
        val newList = mutableListOf("D", "C", "A", "B", "X", "G", "F", "Z", "O", "D", "E", "Y")
        val resultList = oldList.toMutableList()

        DiffUtil.diffCallback(oldList.size, newList.size) { oldItemIndex, newItemIndex ->
            oldList[oldItemIndex] == newList[newItemIndex]
        }.applyChanges {
            applyChangeToLists(resultList, newList, it)
        }

        assertTrue { resultList == newList }
    }

    @Test
    fun diffFlowTest() = runBlocking {
        val newList = mutableListOf("Z", "D", "A", "A", "O", "B", "C", "Y", "P", "Y")
        val resultList = oldList.toMutableList()

        val job = launch {
            DiffUtil.diffFlow(oldList.size, newList.size) { oldItemIndex, newItemIndex ->
                oldList[oldItemIndex] == newList[newItemIndex]
            }.collect {
                applyChangeToLists(resultList, newList, it)
            }
        }

        job.join()

        assertTrue { resultList == newList }
    }

    private fun applyChangeToLists(resultList: MutableList<String>, newList: List<String>, change: Change) {
        when (change) {
            is Change.Insert -> {
                var i = change.toOldListIndex
                var j = change.fromNewListIndex
                while (i < change.toOldListIndex + change.count) {
                    resultList.add(i, newList[j])
                    i++
                    j++
                }
            }
            is Change.Remove -> {
                var i = change.count
                var j = change.fromOldListIndex + change.count - 1
                while (i > 0) {
                    resultList.removeAt(j)
                    j--
                    i--
                }
            }
        }
    }
}
