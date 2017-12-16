$(function() {
    $(".issue_term").draggable({
        grid: [15, 15],
        axis: "x"
    }).resizable({
        grid: 15,
        handles: "e,w"
    });
});