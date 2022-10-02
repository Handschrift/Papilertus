package com.papilertus.plugin

import com.google.gson.Gson

class PluginData private constructor(val author: String, val mainClass: String, val name: String) {

    companion object {
        @JvmStatic
        fun getFromJson(json: String?): PluginData {
            return Gson().fromJson(json, PluginData::class.java)
        }
    }
}