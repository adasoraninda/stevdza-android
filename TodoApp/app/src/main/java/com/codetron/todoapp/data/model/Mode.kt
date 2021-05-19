package com.codetron.todoapp.data.model

enum class Mode(private val value: String) {
    ADD("Add"),
    UPDATE("Update");

    override fun toString(): String {
        return value;
    }
}