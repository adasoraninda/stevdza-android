package com.codetron.todoapp.data.model

enum class Priority(val value:String) {
    HIGH("High Priority"),
    MEDIUM("Medium Priority"),
    LOW("Low Priority");

    companion object{
        fun parsePriority(priority:String?):Priority{
            return when (priority) {
                HIGH.value -> HIGH
                MEDIUM.value ->MEDIUM
                LOW.value->LOW
                else -> throw IllegalArgumentException("Value not found")
            }
        }
    }

}