package com.improve_future.backlog_board.presentation.lang

import com.improve_future.backlog_board.base.Lang

enum class CommonLang(
        override val enText: String,
        override val jaText: String = enText
): Lang {
    Index("Index", "インデックス"),
    New("New", "新規作成"),
    Edit("Edit", "編集"),
    CreateNewRecord("Create New %s", "%s 新規作成"),
    EditRecord("Edit %s", "%s 編集"),
    IsItOkToDeleteTheRecord(
            "Is it OK to delete the record?",
            "削除してもよろしいですか？"),
    Confirmation("Confirmation", "確認"),
    ButtonTextSubmit("Submit", "サブミット"),
    ButtonTextDelete("Delete", "削除"),
    ButtonTextCancel("Cancel", "キャンセル"),
    SuccessfullyDeleted(
            "Successfully deleted.",
            "正常に削除しました。"),
    SuccessfullyCreated(
            "Successfully Created.",
            "正常に作成しました。"),
    SuccessfullyUpdated(
            "Successfylly Updated.",
            "正常に更新しました。")
}