package com.flopezluksenberg.todo.todolist

import com.flopezluksenberg.todo.TodoItem

class TodoListPresenter(private var view: TodoListView?, private val interactor: TodoItemsInteractor, savedItems: List<TodoItem>? = null) : TodoItemsInteractor.Listener {

    init {
        view?.showLoading()
        savedItems?.apply {
            if(isEmpty()){
                view?.showEmptyPlaceholder()
            }else{
                view?.hideEmptyPlaceholder()
                view?.setTodoItems(this)
            }

            view?.hideLoading()
        } ?: interactor.getItems(this)
    }

    fun detachView(){
        this.view = null
    }

    fun addItem(item: String) {
        if(item.isBlank())
            view?.showItemBlankDialog()
        else{
            view?.hideEmptyPlaceholder()
            view?.addTodoItem(TodoItem(item))
            interactor.saveItems(view?.getItems() ?: listOf())
        }
    }

    fun deleteAllItems() {
        view?.deleteAllItems()
        view?.showEmptyPlaceholder()
        interactor.deleteAllItems()
    }

    fun deleteItem(todoItem: TodoItem) {
        view?.deleteItem(todoItem)
        if(view?.getItems()?.size == 0){
            view?.showEmptyPlaceholder()
        }
        interactor.saveItems(view?.getItems() ?: listOf())
    }

    fun showInfo() {
        view?.showInfo()
    }

    //Interactor Callbacks
    override fun onGetItemsSuccess(items: List<TodoItem>) {
        if(items.isEmpty()){
            view?.showEmptyPlaceholder()
        }else{
            view?.hideEmptyPlaceholder()
        }
        view?.setTodoItems(items)
        view?.hideLoading()
    }

    override fun onGetItemsNotSuccess() {
        view?.hideLoading()
        view?.showEmptyPlaceholder()
    }

    override fun onGetItemsFailure() {
        view?.hideLoading()
        view?.showEmptyPlaceholder()
    }

    //Este metodo solo se utiliza para testing
    fun isViewAttached(): Boolean{
        return view != null
    }

}