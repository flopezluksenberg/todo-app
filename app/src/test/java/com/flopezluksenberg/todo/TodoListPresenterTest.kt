package com.flopezluksenberg.todo

import com.flopezluksenberg.todo.todolist.TodoItemsInteractor
import com.flopezluksenberg.todo.todolist.TodoListPresenter
import com.flopezluksenberg.todo.todolist.TodoListView
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class TodoListPresenterTest {

    @Mock
    private lateinit var view: TodoListView

    @Mock
    private lateinit var interactor: TodoItemsInteractor

    private val viewItems = listOf(TodoItem("SavedItem"))

    private lateinit var presenter: TodoListPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        presenter = TodoListPresenter(view, interactor)
    }


    @Test
    fun when_presenter_is_created_then_view_is_attached() {
        assertTrue(presenter.isViewAttached())
    }

    @Test
    fun when_presenter_calls_detach_view_then_view_is_null() {
        presenter.detachView()
        assertFalse(presenter.isViewAttached())
    }

    @Test
    fun when_presenter_add_item_then_view_show_new_item(){
        Mockito.`when`(view.getItems()).thenReturn(viewItems)
        presenter.addItem("Hola")
        verify(view).addTodoItem(any())
        verify(interactor).saveItems(viewItems)
    }
}
