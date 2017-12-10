package com.improve_future.backlog_board.base

interface HumanSelectInterface {
    val humanSelectId: Long?

    fun humanSelectName(lang: String? = null): String
}