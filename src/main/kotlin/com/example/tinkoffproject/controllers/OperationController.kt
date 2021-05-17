package com.example.tinkoffproject.controllers


import com.example.tinkoffproject.models.Category
import com.example.tinkoffproject.models.Operation
import com.example.tinkoffproject.models.OperationWithCategory
import com.example.tinkoffproject.services.CategoryService
import com.example.tinkoffproject.services.OperationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
@RequestMapping("operations")
@Api(description = "Контроллер операций")
class OperationController(
    private val operationService: OperationService,
    private val categoryService: CategoryService,
) {
    @ApiOperation(value = "Получаение всех операций")
    @GetMapping("/")
    fun getOperations(model: Model): String {
        val operations = operationService.get()
        val categories = categoryService.get()
        val operationsWithCategories = operations.map {
            OperationWithCategory(
                it.id,
                it.amount.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date(it.date.toLong() * 1000)),
                if (it.action == 1) "Доход" else "Расход",
                categoryService.get(it.categoryId.toLong()).title,
                if (it.way == "card") "картой" else "начиличными",
                it.comment,
            )
        }
        model["incomes"] =
            operationsWithCategories.filter { it.action == "Доход" }
        model["expenses"] =
            operationsWithCategories.filter { it.action == "Расход" }
        model["categories"] = categoryService.get()
        model["operation"] = OperationWithCategory()
        model["incomesSum"] =
            operationsWithCategories.filter { it.action == "Доход" }
                .map { it.amount.toInt() }.sum()
        model["expensesSum"] =
            operationsWithCategories.filter { it.action == "Расход" }
                .map { it.amount.toInt() }.sum()
        model["title"] = "Все доходы и расходы"
        return "operation/money"
    }

    @ApiOperation(value = "Добавление операции")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperation(
        @ModelAttribute operationWithCategory: OperationWithCategory,
        model: Model,
    ): String {
        operationService.add(Operation(
            amount = operationWithCategory.amount.toInt(),
            date = LocalDate.parse(operationWithCategory.date,
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(
                ZoneId.systemDefault()).toInstant().epochSecond.toInt(),
            action = operationWithCategory.action.toInt(),
            way = operationWithCategory.way,
            comment = operationWithCategory.comment,
            categoryId = categoryService.getId(operationWithCategory.categoryTitle)
                .toInt(),
        ))
        val operations = operationService.get()
        val categories = categoryService.get()
        val operationsWithCategories = operations.map {
            OperationWithCategory(
                it.id,
                it.amount.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date(it.date.toLong() * 1000)),
                if (it.action == 1) "Доход" else "Расход",
                categoryService.get(it.categoryId.toLong()).title,
                if (it.way == "card") "картой" else "начиличными",
                it.comment,
            )
        }
        model["incomes"] =
            operationsWithCategories.filter { it.action == "Доход" }
        model["expenses"] =
            operationsWithCategories.filter { it.action == "Расход" }
        model["categories"] = categoryService.get()
        model["operation"] = OperationWithCategory()
        model["incomesSum"] =
            operationsWithCategories.filter { it.action == "Доход" }
                .map { it.amount.toInt() }.sum()
        model["expensesSum"] =
            operationsWithCategories.filter { it.action == "Расход" }
                .map { it.amount.toInt() }.sum()
        model["title"] = "Все доходы и расходы"
        return "operation/money"
    }

    @ApiOperation(value = "Получение операции по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getOperation(@PathVariable id: Long, model: Model): String? {
        val operation = operationService.get(id)
        model["operation"] = OperationWithCategory(
            operation.id,
            operation.amount.toString(),
            SimpleDateFormat("dd-MM-yyyy").format(Date(operation.date.toLong() * 1000)),
            if (operation.action == 1) "Доход" else "Расход",
            categoryService.get(operation.categoryId.toLong()).title,
            if (operation.way == "card") "картой" else "начиличными",
            operation.comment,
        )
        model["categories"] = categoryService.get()
        if (operation.id == 0L) throw NotFoundOperationException()
        else return "operation/money_id"
    }

    @ApiOperation(value = "Получение операций за период",
        notes = "Первое число - меньшая граница периода, второе - большая")
    @GetMapping("{start}/{end}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getOperationByDate(
        @PathVariable start: Int,
        @PathVariable end: Int,
        model: Model,
    ): String {
        val operations = operationService.get(start, end, "date")
        val categories = categoryService.get()
        val operationsWithCategories = operations.map {
            OperationWithCategory(
                it.id,
                it.amount.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date(it.date.toLong() * 1000)),
                if (it.action == 1) "Доход" else "Расход",
                categoryService.get(it.categoryId.toLong()).title,
                if (it.way == "card") "картой" else "начиличными",
                it.comment,
            )
        }
        model["incomes"] =
            operationsWithCategories.filter { it.action == "Доход" }
        model["expenses"] =
            operationsWithCategories.filter { it.action == "Расход" }
        model["categories"] = categoryService.get()
        model["operation"] = OperationWithCategory()
        model["incomesSum"] =
            operationsWithCategories.filter { it.action == "Доход" }
                .map { it.amount.toInt() }.sum()
        model["expensesSum"] =
            operationsWithCategories.filter { it.action == "Расход" }
                .map { it.amount.toInt() }.sum()
        model["title"] = "Все доходы и расходы за период с ${
            SimpleDateFormat("dd-MM-yyyy").format(Date(start.toLong() * 1000))
        } по ${SimpleDateFormat("dd-MM-yyyy").format(Date(end.toLong() * 1000))}"
        return "operation/money"

    }

    @ApiOperation(value = "Получение операций по категории")
    @GetMapping("category/{categoryTitle}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getByCategory(
        @PathVariable categoryTitle: String,
        model: Model,
    ): String {
        val categoryId =
            categoryService.getId(categoryTitle[0].toUpperCase() + categoryTitle.substring(
                1,
                categoryTitle.length))
        val operations = operationService.getByCategory(categoryId)
        val categories = categoryService.get()
        val operationsWithCategories = operations.map {
            OperationWithCategory(
                it.id,
                it.amount.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date(it.date.toLong() * 1000)),
                if (it.action == 1) "Доход" else "Расход",
                categoryService.get(it.categoryId.toLong()).title,
                if (it.way == "card") "картой" else "начиличными",
                it.comment,
            )
        }
        model["incomes"] =
            operationsWithCategories.filter { it.action == "Доход" }
        model["expenses"] =
            operationsWithCategories.filter { it.action == "Расход" }
        model["categories"] = categoryService.get()
        model["operation"] = OperationWithCategory()
        model["incomesSum"] =
            operationsWithCategories.filter { it.action == "Доход" }
                .map { it.amount.toInt() }.sum()
        model["expensesSum"] =
            operationsWithCategories.filter { it.action == "Расход" }
                .map { it.amount.toInt() }.sum()
        model["title"] = "Все доходы и расходы с категорией $categoryTitle"
        return "operation/money"
    }

    @ApiOperation(value = "Изменение операции")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperation(
        @PathVariable id: Long,
        @ModelAttribute operationWithCategory: OperationWithCategory,
        model: Model,
    ): String {
        operationService.edit(id, Operation(
            amount = operationWithCategory.amount.toInt(),
            date = LocalDate.parse(operationWithCategory.date,
                DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(
                ZoneId.systemDefault()).toInstant().epochSecond.toInt(),
            action = operationWithCategory.action.toInt(),
            way = operationWithCategory.way,
            comment = operationWithCategory.comment,
            categoryId = categoryService.getId(operationWithCategory.categoryTitle)
                .toInt(),
        ))
        model["operation"] = operationWithCategory
        model["categories"] = categoryService.get()
        return "operation/money_id"
    }


    @ExceptionHandler(NotFoundOperationException::class)
    fun notFoundException(e: NotFoundOperationException): String {
        return e.getText()
    }
}

@RestController
@RequestMapping("api/operations")
@Api(description = "Контроллер операций")
class OperationRestController(
    private val operationService: OperationService,
    private val categoryService: CategoryService,
) {
    @ApiOperation(value = "Получаение всех операций")
    @GetMapping("/")
    fun getOperations(): List<OperationWithCategory> {
        val operations = operationService.get()
        val categories = categoryService.get()
        val operationsWithCategories = operations.map {
            OperationWithCategory(
                it.id,
                it.amount.toString(),
                SimpleDateFormat("dd-MM-yyyy").format(Date(it.date.toLong() * 1000)),
                if (it.action == 1) "Доход" else "Расход",
                categoryService.get(it.categoryId.toLong()).title,
                if (it.way == "card") "картой" else "начиличными",
                it.comment,
            )
        }
        return operationsWithCategories
    }

    @ApiOperation(value = "Получение операции по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getOperation(@PathVariable id: Long): OperationWithCategory? {
        val operation = operationService.get(id)
        val operationWithCategory = OperationWithCategory(
            operation.id,
            operation.amount.toString(),
            SimpleDateFormat("dd-MM-yyyy").format(Date(operation.date.toLong() * 1000)),
            if (operation.action == 1) "Доход" else "Расход",
            categoryService.get(operation.categoryId.toLong()).title,
            if (operation.way == "card") "картой" else "начиличными",
            operation.comment,
        )
        if (operationWithCategory.id == 0L) throw NotFoundOperationException()
        else return operationWithCategory
    }

    @ApiOperation(value = "Добавление операции")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createOperation(
        @RequestBody operation: Operation,
    ): OperationWithCategory {
        operationService.add(operation)
        return OperationWithCategory(
            operation.id,
            operation.amount.toString(),
            SimpleDateFormat("dd-MM-yyyy").format(Date(operation.date.toLong() * 1000)),
            if (operation.action == 1) "Доход" else "Расход",
            categoryService.get(operation.categoryId.toLong()).title,
            if (operation.way == "card") "картой" else "начиличными",
            operation.comment,
        )
    }

    @ApiOperation(value = "Изменение операции")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun updateOperation(
        @PathVariable id: Long,
        @RequestBody operation: Operation,
    ): Long {
        val operation = operationService.edit(id, operation)
        return operation.id
    }

    @ApiOperation(value = "Удаление операции")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun deleteOperation(@PathVariable id: Long): Long {
        return operationService.delete(id)
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND,
    reason = "Not found any operations")
class NotFoundOperationException : RuntimeException() {
    fun getText(): String {
        return "Операций не найдено"
    }
}

