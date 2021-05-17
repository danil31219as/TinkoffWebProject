package com.example.tinkoffproject.models

import com.fasterxml.jackson.annotation.*
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate
import java.util.*

@ApiModel(value = "Операции")
data class Operation(
    @ApiModelProperty(value = "Id")
    val id: Long = 0L,
    @ApiModelProperty(value = "Сумма")
    val amount: Int = 0,
    @ApiModelProperty(value = "Время в формате Unix")
    val date: Int = 1609444800,
    @ApiModelProperty(value = "Действие (1 - доход, -1 - расход)")
    val action: Int = 1,
    @ApiModelProperty(value = "Id категории")
    val categoryId: Int = 0,
    @ApiModelProperty(value = "Способ совершения операции (cash - наличными, card - картой)")
    val way: String = "cash",
    @ApiModelProperty(value = "Комментарий")
    val comment: String = "",
)
