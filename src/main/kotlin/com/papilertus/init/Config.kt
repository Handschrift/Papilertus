package com.papilertus.init

data class Config(
    val token: String,
    val pluginDir: String,
    val feedbackRecipientId: String,
    val disableAllCoreInteractions: Boolean,
    val databaseManagementSystem: String
    )
