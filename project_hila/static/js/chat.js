let chatSocket;

/**
 * A socket is needed in order to keep the connection open.
 * @param {string} patientID patient id fetched from Firebase
 * @param {string} doctorID doctor id fetched from local database
 * @param {string} sendMessagePath path to send method in server
 */
function initializeSocket(patientID, doctorID, sendMessagePath) {

    const loc = window.location;
    let wsStart = 'ws://'

    if (loc.protocol == 'https:') {

        // production environment
        wsStart = 'wss://'
    }

    const roomName = patientID + "_" + doctorID;
    chatSocket = new WebSocket(wsStart + loc.host + '/ws/chat/' + roomName + '/');

    chatSocket.onmessage = function (e) {

        console.log(e.data)
        const data = JSON.parse(e.data);
        const recieved_msg = data.message

        const chatContainer = document.getElementById("chat-messages-window");
        let message = document.createElement("p");
        message.innerHTML = recieved_msg;
        message.classList.add('doctor-message');
        message.style.color = "black";
        message.style.fontSize = "1.3rem";

        // add message to chat window
        chatContainer.appendChild(message);

        const token = $('input[name="csrfmiddlewaretoken"]').attr('value');

        // request to post message to Firebase
        $.ajax({
            headers: { 'X-CSRFToken': token },
            type: 'POST',
            url: sendMessagePath,
            data: {
                'message': recieved_msg,
                'patientKey': patientID
            },
            dataType: 'json',
            success: function (payload) {
                console.log("got message: " + payload.message);
            }
        });
    };

    chatSocket.onclose = function (e) {
        console.error('Chat socket closed unexpectedly');
    };
}

// send message on 'Enter'
const input = document.getElementById("msg_input");
input.addEventListener("keyup", function (event) {
    if (event.keyCode == 13) { // number 13 is the "Enter" key on the keyboard
        event.preventDefault();
        document.getElementById("send_btn").click();
    }
});

async function sendMsg(patientKey, path) {
    const token = $('input[name="csrfmiddlewaretoken"]').attr('value');
    const msg = document.getElementById("msg_input").value.trim();

    if (msg == "") {

        console.log("empty message");
        window.alert("cannot send an empty message");
    }
    else {

        document.getElementById("msg_input").value = "";
        chatSocket.send(JSON.stringify({
            'message': msg
        }));
    }

    // in the odd case of sending messages super fast.
    preventMessageOverride();
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function preventMessageOverride() {
    $('#msg_input').attr("disabled", true);
    await sleep(1);
    $('#msg_input').attr("disabled", false);
}

