package se.jesjens.shoppinglist

import com.google.firebase.firestore.FirebaseFirestore
import se.jesjens.shoppinglist.firestore.FirebaseQueryLiveData
import se.jesjens.shoppinglist.models.ListItem
import se.jesjens.shoppinglist.models.factories.ListItemFactory

class ListActivityViewModel {
    var db = FirebaseFirestore.getInstance()
    var shoppingItems = db.collection("shoppingItems")
    var itemsLiveData = FirebaseQueryLiveData(shoppingItems, ListItemFactory)

    fun saveItem(listItem: ListItem) {
        shoppingItems.document(listItem.text).set(listItem)
    }

    fun removeItem(listItem: ListItem) {
        shoppingItems.document(listItem.text).delete()
    }
}