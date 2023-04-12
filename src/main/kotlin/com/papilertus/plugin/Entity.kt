package com.papilertus.plugin

import com.fasterxml.jackson.annotation.JsonIgnore
import com.mongodb.client.MongoCollection
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.deleteOneById
import org.litote.kmongo.findOneById
import org.litote.kmongo.save

abstract class Entity<T : Entity<T>>(
    @JsonIgnore
    private val store: MongoCollection<T>,
) {
    /**
     * The id of the instance.
     */
    @BsonId
    val id: String = ""

    companion object {
        inline fun <reified T : Entity<T>> get(store: MongoCollection<T>, id: String, f: () -> T): T {
            return store.findOneById(id) ?: f()
        }

        inline fun <reified T : Entity<T>> exists(store: MongoCollection<T>, id: String): Boolean {
            return store.findOneById(id) != null
        }
    }

    /**
     * Searches an element from the database by the [id] provided in the instance.
     *
     * @return An instance of the entity or null if no entity with the given id exists
     */
    fun getById(): T? = store.findOneById(id)
    fun exists(): Boolean = store.findOneById(id) != null
    fun save() = store.save(this as T)
    fun delete() = store.deleteOneById(this.id)

}