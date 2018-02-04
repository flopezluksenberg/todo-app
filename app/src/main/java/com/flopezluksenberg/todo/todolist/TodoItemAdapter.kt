package com.flopezluksenberg.todo.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.flopezluksenberg.todo.R
import com.flopezluksenberg.todo.TodoItem


class TodoItemAdapter(private var items: MutableList<TodoItem> = mutableListOf()) : RecyclerView.Adapter<TodoItemAdapter.TodoItemViewHolder>() {

    private var listener: TodoItemListener? = null
    private var checkedItems: MutableList<TodoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TodoItemViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.item_todo, parent, false)
        return TodoItemViewHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TodoItemViewHolder?, position: Int) {
        holder?.txtDescription?.text = items[position].description
        holder?.btnRemove?.setOnClickListener { listener?.onTodoItemRemove(items[position]) }
        holder?.chkCheck?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) checkedItems.add(items[position]) else checkedItems.remove(items[position])
        }
    }

    fun addItem(item: TodoItem){
        items.add(item)
        notifyItemInserted(items.size)
    }

    fun setItems(items: List<TodoItem>) {
        this.items = items.toMutableList()
        this.checkedItems.clear()
        notifyDataSetChanged()
    }

    fun getItems(): List<TodoItem> {
        return items
    }

    fun deleteItem(todoItem: TodoItem) {
        this.items.remove(todoItem)

        if(checkedItems.contains(todoItem)){
            this.checkedItems.remove(todoItem)
        }

        notifyDataSetChanged()
    }

    fun setListener(listener: TodoItemListener){
        this.listener = listener
    }

    fun countChecked(): Int {
        return checkedItems.size
    }

    class TodoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var chkCheck: CheckBox? = view.findViewById(R.id.chkCheck)
        var btnRemove: ImageButton? = view.findViewById(R.id.btnRemove)
        var txtDescription: TextView? = view.findViewById(R.id.txtDescription)
    }

    interface TodoItemListener {
        fun onTodoItemRemove(todoItem: TodoItem)
    }
}
