package com.webster.commerces.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.webster.commerces.R
import com.webster.commerces.entity.Commerce
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.activity_update_commerce.*

class UpdateCommerceActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_commerce)
        setSupportActionBar(toolbar2)

        //initUpdate()

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("TAG", "onChildAdded:" + dataSnapshot.key!!)

                val commerce = dataSnapshot.getValue(Commerce::class.java)

            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }
        commercesReference.addChildEventListener(childEventListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        return true
    }

    private fun initUpdate() {
        val commerce = Commerce()
        commercesReference.child(commerce.commerceId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                textName.setText(data.child(commerce.name).value.toString())
            }

            override fun onCancelled(dataError: DatabaseError) {
                Log.e("Error", "Error!", dataError.toException())

            }
        })
    }
}