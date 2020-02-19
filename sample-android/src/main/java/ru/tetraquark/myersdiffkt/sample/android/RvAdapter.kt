package ru.tetraquark.myersdiffkt.sample.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(private val dataSource: DataSource<String>) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item, parent, false)
            .let { ViewHolder(it) }
    }

    override fun getItemCount(): Int = dataSource.getItemCount()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(dataSource.getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textView = itemView.findViewById<TextView>(R.id.item_text)

        fun bindData(text: String) {
            textView.text = text
        }

    }

}

interface DataSource <T> {

    fun getItemCount(): Int

    fun getItem(index: Int): T

}

