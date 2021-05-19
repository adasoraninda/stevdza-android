package com.codetron.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codetron.todoapp.data.model.ToDoData
import com.codetron.todoapp.databinding.ItemTodoBinding
import javax.inject.Inject

class ToDoListAdapter @Inject constructor() :
    ListAdapter<ToDoData, ToDoListAdapter.ToDoViewHolder>(DIFF_CALLBACK) {

    private var clickListener: ((toDo: ToDoData?) -> Unit)? = null

    fun setOnClickListener(clickListener: ((toDo: ToDoData?) -> Unit)?) {
        this.clickListener = clickListener
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ToDoData>() {
            override fun areItemsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
                return oldItem == newItem
            }
        }
    }

    class ToDoViewHolder private constructor(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ToDoViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemTodoBinding.inflate(inflater, parent, false)
                return ToDoViewHolder(binding)
            }
        }

        fun bind(toDoData: ToDoData?, clickListener: ((toDo: ToDoData?) -> Unit)?) {
            with(binding) {
                toDo = toDoData
                root.setOnClickListener { clickListener?.invoke(toDo) }
                executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder =
        ToDoViewHolder.from(parent)

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}