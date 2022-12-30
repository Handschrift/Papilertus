package com.papilertus.init

data class Config(
    val token: String,
    val pluginDir: String,
    val feedbackRecipientId: String,
    val disableAllCoreInteractions: Boolean,
    val databaseName: String,
    val databasePort: Int,
    val databaseUrl: String,
    val databaseUsername: String,
    val databasePassword: String,
)
