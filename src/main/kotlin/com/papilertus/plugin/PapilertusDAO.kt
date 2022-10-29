package com.papilertus.plugin

interface PapilertusDAO<T> {
    fun selectAll(): List<T>
    fun selectById(id: String): T?
    fun insertOrIgnore(type: T)
    fun delete(id: String): Int
    fun deleteAll(): Int
}