package com.papilertus.init

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

fun connectDatabase(config: Config): Database? {

    if (config.databaseManagementSystem.isEmpty()) {
        return null
    }

    TransactionManager.manager.defaultIsolationLevel =
        Connection.TRANSACTION_SERIALIZABLE

    return Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
}