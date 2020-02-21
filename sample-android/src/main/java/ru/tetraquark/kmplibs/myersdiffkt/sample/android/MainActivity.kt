package ru.tetraquark.kmplibs.myersdiffkt.sample.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.tetraquark.kmplibs.myersdiffkt.DiffUtil
import ru.tetraquark.kmplibs.myersdiffkt.Change
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val listData = ListDataSource(
        mutableListOf("A", "B", "C", "D", "E", "F")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = listData.rvAdapter

        launch {
            delay(1000)
            listData.applyNewList(
                listOf("Z", "F", "D", "A", "B", "D", "F", "A", "C", "Z", "F", "D", "A", "B", "C")
            )

            repeat(5) {
                delay(1000)
                val newList = listData.dataList.shuffled()
                listData.applyNewList(newList)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private inner class ListDataSource(
        dataList: MutableList<String>
    ) : DataSource<String> {

        var dataList: MutableList<String> = dataList
            private set

        val rvAdapter = RvAdapter(this)

        suspend fun applyNewList(newList: List<String>) {
            val oldList = dataList
            dataList = newList.toMutableList()

            DiffUtil
                .diffFlow(oldList.size, dataList.size) { oldItemIndex, newItemIndex ->
                    oldList[oldItemIndex] == dataList[newItemIndex]
                }
                .flowOn(Dispatchers.Default) // count changes in background thread
                .collect {
                    when (it) {
                        is Change.Remove -> {
                            rvAdapter.notifyItemRangeRemoved(it.fromOldListIndex, it.count)
                        }
                        is Change.Insert -> {
                            rvAdapter.notifyItemRangeInserted(it.toOldListIndex, it.count)
                        }
                    }
                }
        }

        override fun getItemCount(): Int = dataList.size
        override fun getItem(index: Int): String = dataList.get(index)

    }

}
