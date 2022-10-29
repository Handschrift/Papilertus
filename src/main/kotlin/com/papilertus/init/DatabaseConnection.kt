package com.papilertus.init

import org.jetbrains.exposed.sql.Database

fun connectDatabase(config: Config): Database? {

    if (config.databaseManagementSystem.isEmpty()) {
        return null
    }

    when {
        config.databaseManagementSystem.lowercase() == "mysql" || config.databaseManagementSystem.lowercase() == "mariadb" -> {
            return Database.connect("jdbc:mysql://${config.databaseUrl}:3306/", driver = "com.mysql.cj.jdbc.Driver",
                user = config.databaseUsername, password = config.databasePassword)
        }
    }

    return Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
}