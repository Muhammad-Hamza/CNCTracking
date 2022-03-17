package com.example.cnctracking_2.data.model.notifications

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("maintenanceDue")
	val maintenanceDue: List<MaintenanceDueItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class MaintenanceDueItem(

	@field:SerializedName("regNo")
	val regNo: String? = null,

	@field:SerializedName("alertTypeId")
	val alertTypeId: String? = null,

	@field:SerializedName("datetime")
	val datetime: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("info")
	val info: String? = null
)
