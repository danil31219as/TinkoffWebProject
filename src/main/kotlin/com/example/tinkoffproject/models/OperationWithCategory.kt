package com.example.tinkoffproject.models

import io.swagger.annotations.ApiModelProperty

data class OperationWithCategory(
    @ApiModelProperty(value = "Id")
    val id: Long = 0L,
    @ApiModelProperty(value = "Сумма")
    val amount: String = "",
    @ApiModelProperty(value = "Дата")
    val date: String = "",
    @ApiModelProperty(value = "Действие (1 - доход, -1 - расход)")
    val action: String = "Доход",
    @ApiModelProperty(value = "Название категории")
    val categoryTitle: String = "",
    @ApiModelProperty(value = "Способ совершения операции (cash - наличными, card - картой)")
    val way: String = "картой",
    @ApiModelProperty(value = "Комментарий")
    val comment: String = "",
)
