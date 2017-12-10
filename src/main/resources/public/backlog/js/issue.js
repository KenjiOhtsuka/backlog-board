$(function() {
    $(".sortable").sortable({
        connectWith: "td",
        receive: function (event, ui) {
            jQuery.ajax({
                url: "/issue/" + $(ui.item[0]).data("id"),
                method: "post",
                data: {
                    _method: "PATCH",
                    status_id: $(this).data("status-id")
                }
            });
        }
    });
});