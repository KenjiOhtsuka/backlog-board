$(function() {
    var updateHours = function () {
        $(".board_column").each(function () {
            var updateHour = function (tag, className) {
                var sum = 0;
                $(tag).find(".issue_tile > ." + className).each(function () {
                     var val = $(this).text()
                     if (val != "null") sum += parseFloat(val);
                });
                $(tag).find("span." + className).first().text(sum);
            };
            updateHour(this, "estimated_hour");
            updateHour(this, "actual_hour");
        });
    };

    $(".sortable").
        sortable({
            connectWith: ".board_column_body.sortable,td",
            receive: function (event, ui) {
                var newStatusId = $(this).data("status-id");
                var newData = {
                    status_id: newStatusId
                };
                if (newStatusId == 4) {
                    var actualHour = prompt("実績工数を入力してください。 Please input actual hours. (h)");
                    if (actualHour != null && actualHour.match(/^[0-9]+(\.[0-9]+)?/) != null)
                        newData["actual_hour"] = parseFloat(actualHour);
                    else {
                        $(".sortable").sortable("cancel");
                        return;
                    }
                }
                jQuery.ajax({
                    headers: {
                        "Content-Type": "application/json"
                    },
                    url: "/issue/" + $(ui.item[0]).data("id"),
                    method: "patch",
                    data: JSON.stringify({
                        _method: "PATCH",
                        issue: newData
                    })
                });
                updateHours();
            }
        });
    $(".issue_tile").
        click(function () {
            jQuery.ajax({
                url: "/issue/" + $(this).data("id"),
                method: "get",
                success: function (dataJson) {
                    var divId = "modal";
                    var issueJson = dataJson.data["issue_list"][0];
                    var targetAttributeArray = [
                        "title", "detail", "estimated_hour", "actual_hour"
                    ];
                    for (var i = 0; i < targetAttributeArray.length; ++i) {
                        var attribute = targetAttributeArray[i];
                        $("#" + divId + "_" + attribute).text(issueJson[attribute]);
                    }
                    $("#modal").modal('show');
                }
            });
        });
    $(document).contextmenu("contextmenu", function (e) {
        e.preventDefault();
        alert("");
//        $(".custom-menu").finish().toggle(100).
//
//        // In the right position (the mouse)
//        css({
//            top: event.pageY + "px",
//            left: event.pageX + "px"
//        });
    });

    updateHours();
});