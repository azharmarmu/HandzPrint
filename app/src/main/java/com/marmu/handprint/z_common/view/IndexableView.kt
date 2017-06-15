package com.marmu.handprint.z_common.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.marmu.handprint.R

class IndexableView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_z_indexable_view)

        val intent = intent
        val action = intent.action
        val data = intent.data

        val actionView = findViewById(R.id.tv_action_view) as TextView
        actionView.text = "Action:  $action \nData: $data"

    }
}
