package se.jesjens.shoppinglist

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import kotlinx.android.synthetic.main.activity_list.*
import se.jesjens.shoppinglist.models.ListGUIItem
import se.jesjens.shoppinglist.models.ListItem

class ListActivity : AppCompatActivity() {
    private val viewModel = ListActivityViewModel()
    private lateinit var listItemsAdapter: ListItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setupObserverForListItems()
        setupItemsAdapter()
        addNewItemButton.setOnClickListener {addNewItem()}
        deleteSelectedItemsButton.setOnClickListener {deleteSelectedItems()}
        setupOnKeyListener()
    }

    private fun setupItemsAdapter() {
        itemsList.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        listItemsAdapter = ListItemsAdapter(listOf(), this)
        itemsList.adapter = listItemsAdapter
    }

    private fun setupOnKeyListener() {
        newItemText.setOnKeyListener {_, keyCode, event ->
            if ((event.action == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                addNewItem()
            }

            false
        }
    }

    private fun setupObserverForListItems() {
        viewModel.itemsLiveData.observe(this, Observer<List<ListItem>> { listItems ->
            val topics = mutableListOf<ListGUIItem>()
            val selectedItems = listItemsAdapter.getAllSelectedGUIItems()

            listItems?.forEach { listItem ->
                var isItemSelected
                        = selectedItems.firstOrNull { it.listItem.text == listItem.text}?.isSelected
                if (isItemSelected == null) {
                    isItemSelected = false
                }

                topics.add(ListGUIItem(listItem, isItemSelected))
            }

            listItemsAdapter = ListItemsAdapter(topics, this)
            itemsList.adapter = listItemsAdapter
        })
    }

    private fun addNewItem() {
        val newItemTextString = newItemText.text.toString()
                .replace("\n", "")
                .replace("\r", "")

        if (newItemTextString.isNotEmpty()) {
            viewModel.saveItem(ListItem(newItemTextString))

            newItemText.setText("")
        }
    }

    private fun deleteSelectedItems() {
        listItemsAdapter.getAllSelectedItems().forEach { listItem ->
            viewModel.removeItem(listItem)
        }
    }
}
