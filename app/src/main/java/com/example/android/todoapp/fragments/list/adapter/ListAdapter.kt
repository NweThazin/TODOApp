package com.example.android.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.todoapp.data.model.ToDoData
import com.example.android.todoapp.databinding.ListItemTodoBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.TODOViewHolder>() {

    private var dataList = emptyList<ToDoData>()

    class TODOViewHolder(private val binding: ListItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            //data binding
            fun from(parent: ViewGroup): TODOViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTodoBinding.inflate(layoutInflater, parent, false)
                return TODOViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TODOViewHolder {
        return TODOViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TODOViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    //note: using diff util
    fun setData(toDoData: List<ToDoData>) {
        //refresh and update recyclerview and adapter by using diff util
        val toDoDiffUtil = ToDoDiffUtil(this.dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }

    fun getDataList(): List<ToDoData> {
        return dataList
    }

}