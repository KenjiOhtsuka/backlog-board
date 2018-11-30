package com.improve_future.backlog_board.domain.backlog.repository

import com.nulabinc.backlog4j.api.option.GetIssuesParams


class GetIssueParam(val projectIdCollection: Collection<Long>):
        GetIssuesParams(projectIdCollection.toList()) {
    fun clone(): GetIssueParam {
        val newParam = GetIssueParam(projectIdCollection)
        newParam.parameters = this.parameters.toMutableList()
        return newParam
    }
}