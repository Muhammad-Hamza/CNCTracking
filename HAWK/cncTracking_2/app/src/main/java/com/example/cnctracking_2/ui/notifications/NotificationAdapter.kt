package com.example.cnctracking_2.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cnctracking_2.R
import com.example.cnctracking_2.data.model.notifications.MaintenanceDueItem
import com.example.cnctracking_2.databinding.ListItemNotificationBinding
import com.example.cnctracking_2.util.DateTimeUtil
import java.util.*

class NotificationAdapter(
        val context: Context?,
        var users: List<MaintenanceDueItem?>?
                 ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterType: Int = -1
    lateinit var binding: ListItemNotificationBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.list_item_notification, parent, false)
        return NotificationHolder(binding)
    }

    fun setData(users: MutableList<MaintenanceDueItem?>?) {
        this.users = users
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return if (users != null) return users!!.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NotificationHolder) {
            users?.get(position).let {
                holder.bindData(users!!.get(position), (position + 1))
            }
        }
    }

    inner class NotificationHolder(binding: ListItemNotificationBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: MaintenanceDueItem?, position: Int) {
            binding.tvDate.text = DateTimeUtil.getCurrentDateTime(DateTimeUtil.FORMAT_DATE_TIME_LOGGING, Locale.getDefault(), user?.datetime!!.toLong())
            binding.tvTime.text = DateTimeUtil.getCurrentDateTime(DateTimeUtil.FORMAT_TIME_BOOKING, Locale.getDefault(), user.datetime.toLong())
            binding.tvDesc.text = user.description
            binding.tvType.text = user.type


            when(user.alertTypeId){
                "1"->{
                    binding.imgNotificationType.setImageResource(R.drawable.car_battery)
                }
                "2"->{
                    binding.imgNotificationType.setImageResource(R.drawable.ignition)
                }
                "3"->{
                    binding.imgNotificationType.setImageResource(R.drawable.speedometer)
                }
                "4"->{
                    binding.imgNotificationType.setImageResource(R.drawable.geofence)
                }
                "5"->{
                    binding.imgNotificationType.setImageResource(R.drawable.ignitionoff)
                }
                "6"->{
                    binding.imgNotificationType.setImageResource(R.drawable.alert)
                }
            }
        }
    }
}
