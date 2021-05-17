package com.example.tinkoffproject.models
import com.fasterxml.jackson.annotation.*
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate
import java.util.*


@ApiModel(value = "Категории")
data class Category(
    @ApiModelProperty(value = "Id")
    val id: Long = 0L,
    @ApiModelProperty(value = "Название")
    val title: String = "",
    @ApiModelProperty(value = "Действие (1 - доход, -1 - расход)")
    val action: Int = 1,
    @ApiModelProperty(value = "Описание")
    val description: String = ""
)
