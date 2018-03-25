function createRevision(script) {
    var scriptItem = $('<p></p>').text(script.text);
    var revDiv = $('<span></span>').text(script.revision);
    revDiv.addClass('rev-btn');

    revDiv.click(() => {
        $('#textbox').val(script.text);
    });

    $('#revisions').append(revDiv);

}

function updateRevisions() {
    $.ajax("/api/script").done(function (result) {

            $('#revisions').empty();

            for (var idx in result) {
                var script = result[idx];
                createRevision(script);
            }
        }
    );
}

function onSave() {
    var scriptText = $('#textbox').val();

    var data = {text: scriptText};

    $.ajax({
        type: 'PUT',
        url: '/api/script',
        content: 'json',
        data: JSON.stringify(data),
        success: () => { console.log("success"); },
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    });
    updateRevisions();
}


function initialization() {
    $.ajax("/api/script").done(function (result) {
            result = result[result.length - 1];
            $("textarea#textbox").val(result.text);
        }
    );
    updateRevisions();

    $('#script-form').submit(() => onSave());


}

$(function() {
    initialization();
    console.log("load");
});