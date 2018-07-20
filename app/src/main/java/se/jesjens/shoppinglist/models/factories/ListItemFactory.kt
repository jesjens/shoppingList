package se.jesjens.shoppinglist.models.factories

import com.google.firebase.firestore.QuerySnapshot
import se.jesjens.shoppinglist.models.ListItem

object ListItemFactory : Factory<List<ListItem>> {
    override fun getObject(querySnapshot: QuerySnapshot): List<ListItem> {
        val listItems = mutableListOf<ListItem>()

        querySnapshot.forEach { documentSnapshot ->
            listItems.add(
                    ListItem(
                            documentSnapshot.getString("text")!!))
        }

        return listItems
    }

}