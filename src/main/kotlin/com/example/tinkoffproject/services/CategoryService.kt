package com.example.tinkoffproject.services

import com.example.tinkoffproject.models.Category


import com.example.tinkoffproject.models.Operation
import com.example.tinkoffproject.repositories.CrudRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryService(private val categoryRepository: CrudRepository) {
    fun get(): MutableList<Category> {
        val result = categoryRepository.findAll("Category")

        val categories = mutableListOf<Category>()
        while (result.next()) {
            println(result.getString("description"))
            categories.add(Category(
                result.getLong("id"),
                result.getString("title"),
                result.getInt("action"),
                result.getString("description")
            ))
        }
        return categories
    }

    fun get(id: Long): Category {
        val result =
            categoryRepository.findByEqual("Category", listOf("id"), listOf(id.toString()))
        while (result.next()) {
            return Category(
                result.getLong("id"),
                result.getString("title"),
                result.getInt("action"),
                result.getString("description")
            )
        }
        return Category()
    }

    fun getId(categoryTitle: String): Long{
        val result =
            categoryRepository.findByEqual("Category", listOf("title"), listOf(categoryTitle))
        while (result.next()) {
            return result.getLong("id")
        }
        return 0L
    }
    fun add(category: Category): Category {
        val columns = listOf("title","action", "description")
        val values =
            listOf(category.title.toString(), category.action.toString(), category.description.toString())
        categoryRepository.insert("Category", columns, values)
        return category
    }

    fun edit(id: Long, category: Category): Category {
        val columns = listOf("title", "action", "description")
        val values =
            listOf(category.title.toString(), category.action.toString(), category.description.toString())
        categoryRepository.update("Category", columns, values, id.toString())
        return category
    }

    fun delete(id: Long): Long {
        categoryRepository.delete("Category", id.toString())
        return id
    }

}
