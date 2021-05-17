package com.example.tinkoffproject.controllers

import com.example.tinkoffproject.models.Category
import com.example.tinkoffproject.models.Operation
import com.example.tinkoffproject.models.OperationWithCategory
import com.example.tinkoffproject.services.CategoryService
import com.example.tinkoffproject.services.OperationService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("categories")
@Api(description = "Контроллер категорий")
class CategoryController(private val categoryService: CategoryService) {
    @ApiOperation(value = "Получение категорий")
    @GetMapping("/")
    fun getCategories(model: Model): String {
        val categories = categoryService.get()
        model["categories_incomes"] = categories.filter {it.action == 1}
        model["categories_expenses"] = categories.filter {it.action == -1}
        model["category"] = Category()
        return "category/category"
    }

    @ApiOperation(value = "Добавление категории")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@ModelAttribute category: Category, model: Model):String {
        categoryService.add(category)
        val categories = categoryService.get()
        model["categories_incomes"] = categories.filter {it.action == 1}
        model["categories_expenses"] = categories.filter {it.action == -1}
        model["category"] = Category()
        return "category/category"
    }

    @ApiOperation(value = "Получение категории по id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getCategory(@PathVariable id: Long, model: Model): String {
        val category = categoryService.get(id)
        model["category"] = category
        if (category.id == 0L) throw NotFoundCategoryException()
        else return "category/category_id"
    }

    @ApiOperation(value = "Изменение категории")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@PathVariable id: Long, @ModelAttribute category: Category, model: Model):String {
        categoryService.edit(id, category)
        val category = categoryService.get(id)
        model["category"] = category
        return "category/category_id"
    }

    @ApiOperation(value = "Изменение категории")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody category: Category,
    ): Category {
        val category = categoryService.edit(id, category)
        return category
    }

    @ApiOperation(value = "Удаление категории")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun deleteCategory(@PathVariable id: Long): Long {
        return categoryService.delete(id)
    }


    @ExceptionHandler(NotFoundCategoryException::class)
    fun notFoundException(e: NotFoundCategoryException): String {
        return e.getText()
    }
}
@RestController
@RequestMapping("api/categories")
@Api(description = "Контроллер категорий")
class CategoryRestController(private val categoryService: CategoryService) {
    @ApiOperation(value = "Получение категорий")
    @GetMapping("/")
    fun getCategories():
            MutableList<Category> {
        val categories = categoryService.get()
        return categories
    }
    @ApiOperation(value = "Получение категории по id")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getCategory(@PathVariable id: Long): Category {
        val category = categoryService.get(id)
        if (category.id == 0L) throw NotFoundCategoryException()
        else return category
    }

    @ApiOperation(value = "Добавление категории")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCategory(@RequestBody category: Category):Category {
        categoryService.add(category)
        return category
    }

    @ApiOperation(value = "Изменение категории")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun updateCategory(
        @PathVariable id: Long,
        @RequestBody category: Category,
    ): Category {
        val category = categoryService.edit(id, category)
        return category
    }

    @ApiOperation(value = "Удаление категории")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun deleteCategory(@PathVariable id: Long): Long {
        return categoryService.delete(id)
    }
}
@ResponseStatus(value = HttpStatus.NOT_FOUND,
    reason = "Not found any categories")
class NotFoundCategoryException : RuntimeException() {
    fun getText(): String {
        return "Категорий не найдено"
    }
}