package com.webster.commerces.extensions

import com.google.firebase.database.*

inline fun <reified T> DatabaseReference.addListDataListener(crossinline listener: (List<T>, Boolean) -> Unit) {
    addValueEventListener(object : ValueEventListener {
        override fun onCancelled(firebaseError: DatabaseError) {
            firebaseError.toException().printStackTrace()
            listener(emptyList(), false)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val list: ArrayList<T> = ArrayList()
            for (child in dataSnapshot.children) {
                val data = child.getValue(T::class.java)!!
                list.add(data)
            }
            listener(list, true)
        }
    })
}

inline fun <reified T> Query.addListDataListener(crossinline listener: (List<T>, Boolean) -> Unit) {
    addValueEventListener(object : ValueEventListener {
        override fun onCancelled(firebaseError: DatabaseError) {
            firebaseError.toException().printStackTrace()
            listener(emptyList(), false)
        }

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val list: ArrayList<T> = ArrayList()
            for (child in dataSnapshot.children) {
                val data = child.getValue(T::class.java)!!
                list.add(data)
            }
            listener(list, true)
        }
    })
}

