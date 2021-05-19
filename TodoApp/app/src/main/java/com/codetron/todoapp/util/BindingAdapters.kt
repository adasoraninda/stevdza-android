package com.codetron.todoapp.util

import android.content.res.ColorStateList
import android.view.View
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.codetron.todoapp.R
import com.codetron.todoapp.data.model.Priority

@BindingAdapter("app:priorityColor")
fun View.setPriorityColor(priority: Priority?) {
    backgroundTintList =
        when (priority) {
            Priority.HIGH -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
            Priority.MEDIUM -> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.yellow))
            Priority.LOW ->ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            else-> ColorStateList.valueOf(ContextCompat.getColor(context, R.color.purple_500))
        }
}

@BindingAdapter("app:selectedItem")
fun Spinner.setSelectionItem(priority: Priority?){
    setSelection(priority?.ordinal?:0)
}