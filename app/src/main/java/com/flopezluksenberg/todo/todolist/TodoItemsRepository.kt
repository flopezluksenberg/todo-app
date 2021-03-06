package com.flopezluksenberg.todo.todolist

import android.content.SharedPreferences
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.flopezluksenberg.todo.TodoItem

class TodoItemsRepository(private val sharedPreferences: SharedPreferences, private val objectMapper: ObjectMapper) : TodoItemsInteractor {
    companion object {
        const val TODO_APP = "com.flopezluksenberg.todo.TODO_APP"
        private const val ITEMS = "items"
    }

    override fun saveItems(items: List<TodoItem>) {
        sharedPreferences.edit().putString(ITEMS, objectMapper.writeValueAsString(items)).apply()
    }

    override fun getItems(listener: TodoItemsInteractor.Listener) {
        try {
            val itemsString = sharedPreferences.getString(ITEMS, null)
            if (itemsString != null) {
                listener.onGetItemsSuccess(objectMapper.readValue(itemsString, object : TypeReference<List<TodoItem>>() {}))
            } else {
                listener.onGetItemsNotSuccess()
            }
        } catch (e: Exception) {
            listener.onGetItemsFailure()
        }
    }

    override fun deleteAllItems() {
        sharedPreferences.edit().remove(ITEMS).apply()
    }

}