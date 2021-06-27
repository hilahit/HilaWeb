// function sendMsg() {

//     var token = $('input[name="csrfmiddlewaretoken"]').attr('value')

//     msg = document.getElementById("msg_input").value
//     console.log("sending message: " + msg)
//     $.ajax({
//         headers: { 'X-CSRFToken': token },
//         type: 'POST',
//         url: '/chat/send_message',
//         data: { 'message': msg },
//         dataType: 'json',
//         success: function (data) {
//             console.log(data.message)
//         },
//         error: function (xhr, ajaxOptions, thrownError) {
//             console.log(thrownError)
//         }
//     });
// }