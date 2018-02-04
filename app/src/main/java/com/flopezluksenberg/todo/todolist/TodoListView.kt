package com.flopezluksenberg.todo.todolist

import com.flopezluksenberg.todo.TodoItem


interface TodoListView {
    fun showItemBlankDialog()
    fun addTodoItem(item: TodoItem)
    fun setTodoItems(items: List<TodoItem>)
    fun showLoading()
    fun hideLoading()
    fun getItems(): List<TodoItem>
    fun deleteAllItems()
    fun deleteItem(todoItem: TodoItem)
    fun showInfo()
    fun showEmptyPlaceholder()
    fun hideEmptyPlaceholder()
}