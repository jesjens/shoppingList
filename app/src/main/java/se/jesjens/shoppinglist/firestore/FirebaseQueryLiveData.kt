package se.jesjens.shoppinglist.firestore

import android.arch.lifecycle.LiveData
import android.os.Handler
import android.util.Log

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import se.jesjens.shoppinglist.models.factories.Factory

class FirebaseQueryLiveData<T>(private val query: Query, val factory: Factory<T>): LiveData<T>() {
    private val listener = MyValueEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    private var listenerRemovePending = false
    private val handler = Handler()

    private val removeListener = Runnable {
        listenerRegistration!!.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        super.onActive()

        Log.d(TAG, "onActive")
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        } else {
            listenerRegistration = query.addSnapshotListener(listener)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()

        Log.d(TAG, "onInactive: ")
        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : EventListener<QuerySnapshot> {
        override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
            if (e != null) {
                Log.e(TAG, "Can't listen to query snapshots: " + querySnapshot + ":::" + e.message)
                return
            }
            value = factory.getObject(querySnapshot!!)
        }
    }

    companion object {
        val TAG = "FbaseQueryLiveData"
    }
}