package com.improve_future.backlog_board.presentation.core

import org.springframework.web.servlet.mvc.support.RedirectAttributes
import com.improve_future.backlog_board.presentation.lang.CommonLang

abstract class RedirectAttributesHandler: LanguageHandler() {
    fun addMessageMapToRedirectAttributes(
            messageMap: com.improve_future.backlog_board.presentation.core.MessageMap,
            attributes: org.springframework.web.servlet.mvc.support.RedirectAttributes) {
        attributes.addFlashAttribute("messages", messageMap)
    }

    fun addSuccessfullyDeletedMessage(
            attributes: org.springframework.web.servlet.mvc.support.RedirectAttributes) {
        getMessageMap(attributes).addSuccessMessage(
                CommonLang.SuccessfullyDeleted.text(lang()))
    }

    fun addSuccessfullyCreatedMessage(
            attributes: org.springframework.web.servlet.mvc.support.RedirectAttributes) {
        getMessageMap(attributes).addSuccessMessage(
                CommonLang.SuccessfullyCreated.text(lang()))
    }

    fun addSuccessfullyUpdatedMessage(
            attributes: org.springframework.web.servlet.mvc.support.RedirectAttributes) {
        getMessageMap(attributes).addSuccessMessage(
                CommonLang.SuccessfullyUpdated.text(lang()))
    }

    fun getMessageMap(attributes: org.springframework.web.servlet.mvc.support.RedirectAttributes): com.improve_future.backlog_board.presentation.core.MessageMap {
        if (!attributes.flashAttributes.containsKey("messages")) {
            val messageMap = com.improve_future.backlog_board.presentation.core.MessageMap()
            attributes.addFlashAttribute("messages", messageMap)
            return messageMap
        }
        return attributes.flashAttributes.getValue("messages") as MessageMap
    }

    fun getSuccessMessages(
            attributes: RedirectAttributes): List<Message> {
        return getMessageMap(attributes).getSuccessMessages()
    }

    fun getErrorMessages(
            attributes: RedirectAttributes): List<Message> {
        return getMessageMap(attributes).getErrorMessages()
    }

    fun hasSuccessMessage(attributes: RedirectAttributes): Boolean {
        return getMessageMap(attributes).hasSuccessMessage()
    }

    fun hasErrorMessage(attributes:RedirectAttributes): Boolean {
        return getMessageMap(attributes).hasErrorMessage()
    }
}