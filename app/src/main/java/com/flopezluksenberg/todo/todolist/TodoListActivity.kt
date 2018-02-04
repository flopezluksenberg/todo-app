package com.flopezluksenberg.todo.todolist

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.flopezluksenberg.todo.AddItemDialogFragment
import com.flopezluksenberg.todo.R
import com.flopezluksenberg.todo.TodoItem
import com.flopezluksenberg.todo.objectMapper
import kotlinx.android.synthetic.main.activity_todolist.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import java.io.Serializable

class TodoListActivity : AppCompatActivity(), TodoListView, AddItemDialogFragment.Listener, TodoItemAdapter.TodoItemListener {
    private lateinit var presenter: TodoListPresenter
    private var alertDialog: AlertDialog? = null

    companion object {
        private const val ITEMS = "items"
    }

    //Lifecycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist)

        initViews()

        val repository = TodoItemsRepository(applicationContext.getSharedPreferences(TodoItemsRepository.TODO_APP, Context.MODE_PRIVATE), applicationContext.objectMapper)
        val savedItems = savedInstanceState?.getSerializable(ITEMS) as? List<TodoItem>

        presenter = TodoListPresenter(this, repository, savedItems)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_todolist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            R.id.mnuInfo -> {
                presenter.showInfo()
                true
            }
            R.id.mnuRemoveAll -> {
                presenter.deleteAllItems()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        alertDialog?.show()
    }

    override fun onStop() {
        super.onStop()
        alertDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(ITEMS, (rclTodoItems.adapter as TodoItemAdapter).getItems() as Serializable)
    }


    //TodoListView
    override fun showItemBlankDialog() {
        alertDialog = alert("No puede añadir un item sin texto", "Item vacio") {
            okButton {
                alertDialog = null
                it.dismiss()
            }
        }.show()
    }

    override fun addTodoItem(item: TodoItem) {
        (rclTodoItems?.adapter as? TodoItemAdapter)?.addItem(item)
    }

    override fun setTodoItems(items: List<TodoItem>) {
        (rclTodoItems?.adapter as? TodoItemAdapter)?.setItems(items)
    }

    override fun showLoading() {
        relLoading?.visibility = View.VISIBLE
        fabAddItem.visibility = View.GONE
    }

    override fun hideLoading() {
        relLoading?.visibility = View.GONE
        fabAddItem.visibility = View.VISIBLE
    }

    override fun getItems(): List<TodoItem> = (rclTodoItems.adapter as TodoItemAdapter).getItems()

    override fun deleteAllItems() {
        (rclTodoItems.adapter as TodoItemAdapter).setItems(listOf())
    }

    override fun deleteItem(todoItem: TodoItem) {
        (rclTodoItems.adapter as TodoItemAdapter).deleteItem(todoItem)
    }

    override fun showInfo() {
        val adapter = (rclTodoItems.adapter as TodoItemAdapter)
        val itemsCheckedCount = adapter.countChecked()
        val percent = if (adapter.getItems().isEmpty()) 100 else (itemsCheckedCount * 100) / adapter.getItems().size

        alertDialog = alert(
                "${percent}% finalizado. ",
                "Progreso") {
            okButton {
                alertDialog = null
                it.dismiss()
            }
        }.show()
    }

    override fun showEmptyPlaceholder() {
        relEmpty.visibility = View.VISIBLE
    }

    override fun hideEmptyPlaceholder() {
        relEmpty.visibility = View.GONE
    }



    //AddItemDialogFragment
    override fun onAddItem(itemText: String) {
        presenter.addItem(itemText)
    }

    //TodoItemsAdapter.OnTodoItemClicked
    override fun onTodoItemRemove(todoItem: TodoItem) {
        presenter.deleteItem(todoItem)
    }


    //Private methods
    private fun initViews() {
        setSupportActionBar(toolbar)
        rclTodoItems?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rclTodoItems?.adapter = TodoItemAdapter()
        (rclTodoItems?.adapter as? TodoItemAdapter)?.setListener(this)
        fabAddItem.setOnClickListener { showAddItemDialog() }
    }

    private fun showAddItemDialog(){
        val addItemDialogFragment: Fragment? = supportFragmentManager.findFragmentByTag(AddItemDialogFragment.TAG)

        if (addItemDialogFragment != null) {
            supportFragmentManager.beginTransaction().remove(addItemDialogFragment).commit()
        }

        AddItemDialogFragment.newInstance().show(supportFragmentManager, AddItemDialogFragment.TAG)
    }

}
