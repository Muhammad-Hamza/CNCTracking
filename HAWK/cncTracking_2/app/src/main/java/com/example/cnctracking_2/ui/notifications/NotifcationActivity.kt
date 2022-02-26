package com.example.cnctracking_2.ui.notifications

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cnctracking_2.R
import com.example.cnctracking_2.data.model.notifications.NotificationResponse
import com.example.cnctracking_2.util.ConstantUtil

class NotifcationActivity : AppCompatActivity()
{
    private var mViewModel: NotificationViewModel? = null
    private var recyclerView: RecyclerView? = null
    public override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        setContentView(R.layout.activity_notifcation)
        findViewById<View>(R.id.ivBack).setOnClickListener { onBackPressed() }
        setTitleFrag()
        recyclerView = findViewById(R.id.recyclerView)
        var extraIndex = -1
        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null)
        {
            if (bundle.containsKey(ConstantUtil.PREF_EXTRA_BUNDLE_1))
            {
                extraIndex = bundle.getInt(ConstantUtil.PREF_EXTRA_BUNDLE_1)
            }
        }
        mViewModel!!.getNotifications(this, extraIndex) { response ->
            if (!TextUtils.isEmpty(response.message))
            {
                Toast.makeText(this, "" + response.message, Toast.LENGTH_SHORT).show()
            }
            else
            {
                initAdapter(response)
            }
            findViewById<View>(R.id.progressBar).visibility = View.GONE
        }
    }

    private fun initAdapter(response: NotificationResponse)
    {
        //        ReportAdapter reportAdapter = new ReportAdapter(response.getWeeklyReports());
        val reportAdapter = NotificationAdapter(applicationContext,response.maintenanceDue)
        recyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setItemViewCacheSize(100)
        recyclerView!!.adapter = reportAdapter
    }

    fun setTitleFrag()
    {
//        try {
////            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val txt = findViewById<TextView>(R.id.toolbar_title)
        txt.text = "Notifications"
        title = ""
        //        } catch (Exception e) {
//        }
    }
}