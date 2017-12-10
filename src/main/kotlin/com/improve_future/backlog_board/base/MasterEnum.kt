package com.improve_future.backlog_board.base

import com.improve_future.backlog_board.base.HumanSelectInterface

interface MasterEnum: HumanSelectInterface {
    val id: Long
    val name: String
    val enName: String
    val jaName: String
    val enDescription: String
    val jaDescription: String

    fun name(locale: String = "en"): String {
        if (locale == "ja") return this.jaName
        return this.enName
    }

    fun description(locale: String = "en"): String {
        if (locale == "ja") return this.jaDescription
        return this.enName
    }

    override val humanSelectId: Long?
    get() { return id }

    override fun humanSelectName(lang: String?): String {
        if (lang == null) return enName
        return name(lang)
    }
}