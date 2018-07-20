package se.jesjens.shoppinglist.models.factories

import com.google.firebase.firestore.QuerySnapshot

interface Factory<T> {
    fun getObject(querySnapshot: QuerySnapshot): T
}