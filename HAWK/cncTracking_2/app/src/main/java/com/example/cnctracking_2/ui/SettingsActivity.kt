package com.example.cnctracking_2.ui

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cnctracking_2.R

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTitleFrag()
        saveSharedPref()
    }

    fun saveSharedPref()
    {
        val muteCheckBox = findViewById<CheckBox>(R.id.muteChkBx)

        muteCheckBox.isChecked = getSharedPreferences("settings",
                                                      MODE_PRIVATE).getBoolean("muteNotifications",
                                                                               false)
        muteCheckBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean)
            {  val sp = getSharedPreferences("settings", MODE_PRIVATE)
                val editor = sp.edit()
                editor.putBoolean("muteNotifications", isChecked)
                editor.commit()
            }
        })

    }

    fun setTitleFrag()
    {
        findViewById<View>(R.id.ivBack).setOnClickListener { onBackPressed() }

        //        try {
        ////            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val txt = findViewById<TextView>(R.id.toolbar_title)
        txt.text = "Settings"
        title = ""
        //        } catch (Exception e) {
        //        }
    }
}