package com.papilertus.init

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import org.litote.kmongo.KMongo


object Database {
    fun connect(config: Config) {

        if (connection != null) {
            return
        }

        if (config.databaseName.isEmpty() || config.databaseUrl.isEmpty()) {
            return
        }

        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString("mongodb://${config.databaseUrl}:${config.databasePort}/${config.databaseName}"))

        if (config.databasePassword.isNotEmpty() && config.databaseUsername.isNotEmpty()) {
            val credential = MongoCredential.createCredential(
                config.databaseUsername,
                config.databaseName,
                config.databasePassword.toCharArray()
            )
            settings.credential(credential)
        }

        connection = KMongo.createClient(settings.build())

    }

    var connection: MongoClient? = null
}