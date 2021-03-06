package com.example.android.sample.worldclock

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import java.sql.Time
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // MARK: fields
        // Activity
        val timezone_select = Intent(this, TimeZoneSelectActivity::class.java)
        // UI
        val timeZoneView = this.findViewById<TextView>(R.id.timeZone)
        val addButton = findViewById<Button>(R.id.add)
        // timeZone
        val timeZone = TimeZone.getDefault()

        // MARK: get value
        timeZoneView.text = timeZone.displayName

        // MARK: methods
        addButton.setOnClickListener {
            startActivityForResult(timezone_select, 1)
        }

        showWorldClocks()
    }

    private fun showWorldClocks() {
        val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val timeZones = pref.getStringSet("time_zone", setOf())

        val list = findViewById<ListView>(R.id.clockList)
        list.adapter = TimeZoneAdapter(this, timeZones.toTypedArray())
    }

    // Callback from startActivityForResult()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1
            && resultCode == Activity.RESULT_OK
            && data != null) {
            val timeZone = data.getStringExtra("timeZone")
            val pref = getSharedPreferences("prefs", Context.MODE_PRIVATE)
            val timeZones = pref.getStringSet("time_zone", mutableSetOf())

            timeZones.add(timeZone)
            pref.edit().putStringSet("time_zone", timeZones).apply()

            showWorldClocks()
        }
    }
}
