package com.example.cnctracking_2.util

import com.example.cnctracking_2.data.model.MaintenanceModel

class AppUtil {
    companion object {
        fun doSortList(list: List<MaintenanceModel.MaintenanceDue>): List<MaintenanceModel.MaintenanceDue> {
            val dueList = ArrayList<MaintenanceModel.MaintenanceDue>()
            val doneList = ArrayList<MaintenanceModel.MaintenanceDue>()
            val upcomingList = ArrayList<MaintenanceModel.MaintenanceDue>()
            val overDueList = ArrayList<MaintenanceModel.MaintenanceDue>()
            for (i in 0..(list.size - 1)) {
                when (list.get(i).status) {
                    "done" -> {
                        doneList.add(list.get(i))
                    }
                    "overdue" -> {
                        overDueList.add(list.get(i))
                    }
                    "upcoming" -> {
                        upcomingList.add(list.get(i))
                    }
                    "due" -> {
                        dueList.add(list.get(i))
                    }
                }
            }
            val newList = ArrayList<MaintenanceModel.MaintenanceDue>()
            newList.addAll(overDueList)
            newList.addAll(dueList)
            newList.addAll(upcomingList)
            newList.addAll(doneList)
            return newList
        }
    }
}