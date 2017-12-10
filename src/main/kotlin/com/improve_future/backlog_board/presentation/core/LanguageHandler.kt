package com.improve_future.backlog_board.presentation.core

import org.springframework.context.i18n.LocaleContextHolder

abstract class LanguageHandler {
    fun lang(): String {
         return LocaleContextHolder.getLocale().language
    }
}