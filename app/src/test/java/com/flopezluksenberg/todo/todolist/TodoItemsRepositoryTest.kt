package com.flopezluksenberg.todo.todolist

import android.content.SharedPreferences
import com.fasterxml.jackson.databind.ObjectMapper
import com.flopezluksenberg.todo.ObjectMapperHelper
import com.flopezluksenberg.todo.TodoItem
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TodoItemsRepositoryTest {

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedEditor: SharedPreferences.Editor

    @Mock
    private lateinit var listener: TodoItemsInteractor.Listener

    private lateinit var objectMapper: ObjectMapper

    private lateinit var interactor: TodoItemsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(sharedPreferences.edit()).thenReturn(sharedEditor)
        Mockito.`when`(sharedEditor.putString(eq("items"), any())).thenReturn(sharedEditor)

        objectMapper = ObjectMapperHelper.getInstance()
        interactor = TodoItemsRepository(sharedPreferences, objectMapper)
    }

    @Test
    fun saveItems() {
        val items = arrayListOf(TodoItem("A"), TodoItem("B"))
        interactor.saveItems(items)
        verify(sharedEditor).apply()
    }

    @Test
    fun test_get_items_and_list_is_null_should_call_onGetItemsNotSuccess_listener_method() {
        Mockito.`when`(sharedEditor.putString("items", null)).thenReturn(null)
        interactor.getItems(listener)
        verify(listener).onGetItemsNotSuccess()
    }

    @Test
    fun test_get_items_and_list_has_objects_should_call_onGetItemsSuccess_listener_method() {
        val items = arrayListOf(TodoItem("ads"))
        Mockito.`when`(sharedPreferences.getString("items", null)).thenReturn(objectMapper.writeValueAsString(items))
        interactor.getItems(listener)
        verify(listener).onGetItemsSuccess(items)
    }

    @Test
    fun test_get_items_and_list_has_wrong_mapped_objects_should_call_onGetItemsFailure_listener_method() {
        Mockito.`when`(sharedPreferences.getString("items", null)).thenReturn("This is a wrong object mapped data")
        interactor.getItems(listener)
        verify(listener).onGetItemsFailure()
    }

    @Test
    fun deleteAllItems() {
        Mockito.`when`(sharedEditor.remove(any())).thenReturn(sharedEditor)
        interactor.deleteAllItems()
        verify(sharedEditor).remove("items")
        verify(sharedEditor).apply()
    }
}