package com.flopezluksenberg.todo.todolist

import com.flopezluksenberg.todo.TodoItem


interface TodoItemsInteractor {
    fun saveItems(items: List<TodoItem>)
    fun getItems(listener: Listener)
    fun deleteAllItems()

    interface Listener {
        fun onGetItemsSuccess(items: List<TodoItem>)
        fun onGetItemsNotSuccess()
        fun onGetItemsFailure()
    }
}