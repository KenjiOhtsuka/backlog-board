package com.improve_future.backlog_board.domain.backlog.factory

import com.nulabinc.backlog4j.PullRequest
import com.nulabinc.backlog4j.api.option.PullRequestQueryParams

class PrGetQueryParamBuilder {
    var issueIdSet = mutableSetOf<Long>()
    var statusTypeSet = mutableSetOf<PullRequest.StatusType>()

    fun setFinished(): PrGetQueryParamBuilder {
        statusTypeSet = mutableSetOf(
                PullRequest.StatusType.Closed, PullRequest.StatusType.Merged)
        return this
    }

    fun toParam(): PullRequestQueryParams {
        val param = PullRequestQueryParams()
        if (statusTypeSet.size > 0)
            param.statusType(statusTypeSet.toList())
        if (issueIdSet.size > 0)
            param.issueIds(issueIdSet.toList())
        return param
    }
}