$(function() {
    var updateHours = function () {
        $(".board_column").each(function () {
            var updateHour = function (tag, className) {
                var sum = 0;
                $(tag).find(".issue_tile > ." + className).each(function () {
                     var val = $(this).text()
                     if (val != "null") sum += parseFloat(val);
                });
                $(tag).find("." + className).first().text(sum);
            };
            updateHour(this, "estimated_hour");
            updateHour(this, "actual_hour");
        });
    };

    var updateIssue = function (id, newData) {
        jQuery.ajax({
            headers: {
                "Content-Type": "application/json"
            },
            url: "/issue/" + id.toString(),
            method: "patch",
            data: JSON.stringify({
                _method: "PATCH",
                issue: newData
            })
        });
    }

    var updateIssueTile = function (id, data) {
        var tileArray = $(".issue_tile[data-id=" + id.toString() + "]");
        tileArray.each(function () {
            for (var key in data) {
                $(this).find("." + key).text(data[key]);
            }
        });
    }

    var updateModal = function (data) {
        for (var key in data) {
            $("#modal_" + key).text(data[key]);
        }
    }

    $(".sortable").
        sortable({
            connectWith: ".board_column_body.sortable,td",
            receive: function (event, ui) {
                var newStatusId = $(this).data("status-id");
                var newData = {
                    status_id: newStatusId
                };
                if (newStatusId == 4) {
                    var actualHour = promptFloat("Please input actual hours. (h)");
                    if (actualHour != null && actualHour != undefined)
                        newData["actual_hour"] = parseFloat(actualHour);
                    else {
                        $(".sortable").sortable("cancel");
                        return;
                    }
                }
                updateIssue($(ui.item[0]).data("id"), newData);
                updateHours();
            }
        });

    $(".issue_tile").
        click(function (e) {
            if (e.target.tagName == 'A') return;
            jQuery.ajax({
                url: "/issue/" + $(this).data("id"),
                method: "get",
                success: function (dataJson) {
                    var divId = "modal";
                    var issueJson = dataJson.data["issue_list"][0];
                    var targetAttributeArray = [
                        "id", "title", "detail", "estimated_hour", "actual_hour"
                    ];
                    for (var i = 0; i < targetAttributeArray.length; ++i) {
                        var attribute = targetAttributeArray[i];
                        var value = issueJson[attribute];
                        if (value === null) value = null
                        switch (attribute) {
                            case "detail":
                                value = value.replace("\n", "<br>");
                                break;
                        }
                        $("#" + divId + "_" + attribute).html(value);
                    }
                    $("#modal").modal('show');
                }
            });
        });

    function parsePromptFloat(value) {
        if (value == null) return; // cancel
        if (value.match(/^[0-9]+(\.[0-9]+)?/) != null)
            return parseFloat(value);
        if (value == "") return null; // null value
        alert("Please input Float or Int value!");
        return;
    }

    function promptFloat(message) {
        var value = prompt(message);
        return parsePromptFloat(value);
    }

    var hourClassArray = ["estimated_hour", 'actual_hour'];
    for (var key in hourClassArray) {
        var hourClass = hourClassArray[key]
        $("." + hourClass).click(function (e) {
            var domId = $(this).attr("id");
            var onModal = (domId && domId.match(/^modal_/) != null);

            var title = hourClass.split("_").map(function (element, index, array) {
                return StringUtility.capitalizeFirstLetter(element);
            }).join(" ");
            var newValue = promptFloat("Update " + title + " to:");
            if (newValue === undefined) return;
            var newData = {};
            newData[hourClass] = newValue;

            var id;
            if (onModal) id = parseInt($("#modal_id").text())
            else id = parseInt($(event.target).closest(".issue_tile").data("id"));

            updateIssue(id, newData);
            updateIssueTile(id, newData);
            if (onModal) updateModal(newData);
            updateHours();
        });
    }

    $(document).contextmenu("contextmenu", function (e) {
        e.preventDefault();
        alert("Under construction");
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