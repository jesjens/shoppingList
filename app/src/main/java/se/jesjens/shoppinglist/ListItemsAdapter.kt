package se.jesjens.shoppinglist

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.list_item.view.*
import se.jesjens.shoppinglist.models.ListGUIItem
import se.jesjens.shoppinglist.models.ListItem

class ListItemsAdapter(private val items : List<ListGUIItem>,
                       private val context: Context) : RecyclerView.Adapter<ListItemsAdapter.ViewHolder>() {
    private val colorSelected = ResourcesCompat.getColor(context.resources, R.color.colorSelected, null)
    private val colorUnselected = ResourcesCompat.getColor(context.resources, R.color.colorUnselected, null)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val guiItem = items[index]
        val view = holder.view
        holder.itemText.text = guiItem.listItem.text

        updateBackgroundColor(guiItem, view)

        view.setOnClickListener{
            guiItem.isSelected = !guiItem.isSelected
            updateBackgroundColor(guiItem, view)
        }
    }

    private fun updateBackgroundColor(guiItem: ListGUIItem, view: View) {
        val backgroundColor = if (guiItem.isSelected) {
            colorSelected
        } else {
            colorUnselected
        }

        view.setBackgroundColor(backgroundColor)
    }

    fun getAllSelectedItems(): List<ListItem> {
        val selectedItems = mutableListOf<ListItem>()
        items.forEach {
            if (it.isSelected) selectedItems.add(it.listItem)
        }

        return selectedItems
    }

    fun getAllSelectedGUIItems(): List<ListGUIItem> {
        return items.filter { it.isSelected }
    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        val itemText: TextView = view.listText
    }
}

