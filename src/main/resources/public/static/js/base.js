var StringUtility = function () {
    return {
        capitalizeFirstLetter: function (text) {
            if (text.length > 0) {
                return text[0].toUpperCase() + text.substring(1);
            }
        }
    }
}();