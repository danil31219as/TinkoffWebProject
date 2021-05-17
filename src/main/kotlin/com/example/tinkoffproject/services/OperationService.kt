package com.example.tinkoffproject.services


import com.example.tinkoffproject.models.Operation
import com.example.tinkoffproject.repositories.CrudRepository
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.*

@Service
class OperationService(private val operationRepository: CrudRepository) {
    fun get(): MutableList<Operation> {
        val result = operationRepository.findAll("Operation")
        val operations = mutableListOf<Operation>()
        while (result.next()) {
            operations.add(Operation(
                result.getLong("id"),
                result.getInt("amount"),
                result.getInt("date"),
                result.getInt("action"),
                result.getInt("category_id"),
                result.getString("way"),
                result.getString("comment")
            ))
        }
        return operations
    }

    fun get(id: Long): Operation {
        val result =
            operationRepository.findByEqual("Operation", listOf("id"), listOf(id.toString()))
        while (result.next()) {
            return Operation(
                result.getLong("id"),
                result.getInt("amount"),
                result.getInt("date"),
                result.getInt("action"),
                result.getInt("category_id"),
                result.getString("way"),
                result.getString("comment")
            )
        }
        return Operation()
    }

    fun get(start: Int, end: Int, field: String): MutableList<Operation> {
        val result = operationRepository.findByRange("Operation",
            field,
            start.toString(),
            end.toString())
        val operations = mutableListOf<Operation>()
        while (result.next()) {
            operations.add(Operation(
                result.getLong("id"),
                result.getInt("amount"),
                result.getInt("date"),
                result.getInt("action"),
                result.getInt("category_id"),
                result.getString("way"),
                result.getString("comment")
            ))
        }
        return operations
    }

    fun getByCategory(categoryId: Long): MutableList<Operation>{
        val result =
            operationRepository.findByEqual("Operation", listOf("category_id"), listOf(categoryId.toString()))
        val operations = mutableListOf<Operation>()
        while (result.next()) {
            operations.add(Operation(
                result.getLong("id"),
                result.getInt("amount"),
                result.getInt("date"),
                result.getInt("action"),
                result.getInt("category_id"),
                result.getString("way"),
                result.getString("comment")
            ))
        }
        return operations
    }

    fun add(operation: Operation): Operation {
        val columns =
            listOf("amount", "date", "action", "category_id", "way", "comment")
        val values = listOf(operation.amount.toString(),
            operation.date.toString(),
            operation.action.toString(),
            operation.categoryId.toString(),
            operation.way,
            operation.comment)
        operationRepository.insert("Operation", columns, values)
        return operation
    }

    fun edit(id: Long, operation: Operation): Operation {
        val columns =
            listOf("amount", "date", "action", "category_id", "way", "comment")
        val values = listOf(operation.amount.toString(),
            operation.date.toString(),
            operation.action.toString(),
            operation.categoryId.toString(),
            operation.way,
            operation.comment)
        operationRepository.update("Operation", columns, values, id.toString())
        return operation
    }

    fun delete(id: Long): Long {
        operationRepository.delete("Operation", id.toString())
        return id
    }

//    fun edit(id: Long, operation: Operation): Operation = operationRepository.update(id, operation)
//
//    fun remove(id: Long) = operationRepository.deleteById(id)
}
