

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
    let chatSocket = new WebSocket(wsStart + loc.host + '/ws/chat/' + roomName + '/');

    const chatContainer = document.getElementById("chat-messages-window");
    const bottomHolder = document.getElementById("contentBottom")

    chatContainer.addEventListener

    chatSocket.onmessage = function (event) {

        const data = JSON.parse(event.data);
        let messages = data['payload']['message'];
        console.log(data['payload']);

        if (messages) {
            if (typeof messages === 'string') {

                // let msgElement = document.createElement("p");

                if (data['payload']['isDoctor']) {
                    msgElement = createMessageElement(messages, data['payload']['contact_name'], true)
                    // msgElement.innerHTML = messages + " :" + data['payload']['contact_name'];
                }
                else {
                    msgElement = createMessageElement(messages, data['payload']['patient_name'], false)

                    // msgElement.innerHTML = messages + " :" + data['payload']['patient_name'];
                }
                
                // web user incoming message does not contain a timestamp.
                const timestamp = createTimeElement();
                const hr = document.createElement('hr');
                hr.setAttribute("width", "100%");

                chatContainer.appendChild(msgElement);
                chatContainer.appendChild(timestamp);
                chatContainer.appendChild(hr);

               
                
            }
            else {

                Object.keys(messages).forEach(key => {

                    if (key === 'contact') {
                        return;
                    }

                    let value = messages[key];
                    let message = value['message'];
                    let isDoctor = value['isDoctor'];
                    const timeStamp = createTimeElementFromMilli(parseInt(key));

                    const br = document.createElement('br');
                    const hr = document.createElement('hr');
                    hr.setAttribute("width", "100%");


                    if (isDoctor) {
                      
                        // timeStamp =
                        //     createTimeElementFromMilli(parseInt(key));
                        
                        msgElement =
                            createMessageElement(
                                message,
                                data['payload']['contact_name'],
                                true);
                        
                     
                        // add message and timestamp to chat window
                        chatContainer.appendChild(msgElement);
                        chatContainer.appendChild(timeStamp);
                        chatContainer.appendChild(hr);
       
                    }
                    else {
                        msgElement = createMessageElement(
                            message,
                            data['payload']['patient_name'],
                            false)


                        // add message to chat window
                        chatContainer.appendChild(msgElement);
                        chatContainer.appendChild(timeStamp);
                        chatContainer.appendChild(br);
                        chatContainer.appendChild(hr);

                    }
                })

                scrollToLastItem();
            }
        }
    };

    chatSocket.onclose = function (e) {
        console.log("closing stream");
    };
}

function scrollToLastItem() {

    const list = document.querySelectorAll('.msg-class')
    if (list.length > 0) {
        const last = list[list.length - 1];
        last.scrollIntoViewIfNeeded({ behavior: 'smooth' });
    }
}


// send message on 'Enter'
const input = document.getElementById("msg_input");
input.addEventListener("keyup", function (event) {
    if (event.keyCode == 13) { // number 13 is the "Enter" key on the keyboard
        event.preventDefault();
        document.getElementById("send_btn").click();
        this.focus();
    }
});


async function sendMsg(patientKey, path) {
    const token = $('input[name="csrfmiddlewaretoken"]').attr('value');
    const msg = document.getElementById("msg_input").value.trim();
    const chatContainer = document.getElementById("chat-messages-window");
    if (msg == "") {

        console.log("empty message");
        window.alert("cannot send an empty message");
    }
    else {

        // send message to Firebase
        $.ajax({
            headers: { 'X-CSRFToken': token },
            type: 'POST',
            url: path,
            data: {
                'message': msg,
                'patientKey': patientKey
            },
            dataType: 'json',
            success: function (payload) {
                // console.log("got message: " + payload.message);
            }
        });

        document.getElementById("msg_input").value = ""; 
        
        $(document).ready(function () {
            $("#msg_input").focus();
            
        });
    }

  


    // in the odd case of sending messages super fastr
    preventMessageOverride();
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function preventMessageOverride() {
    // $('#msg_input').attr("disabled", true);
    await sleep(1);
    // $('#msg_input').attr("disabled", false);
}

function createTimeElement() {

    let timeStamp = document.createElement("p")
    let d = new Date(Date.now());
    timeStamp.innerHTML = d.toLocaleString();
    timeStamp.classList.add('text-start');
    timeStamp.classList.add('align-text-bottom');
    timeStamp.style.color = "black";
    timeStamp.style.fontSize = "0.8rem";

    return timeStamp;
}

function createTimeElementFromMilli(millis) {

    let timeStamp = document.createElement("p")

    const d = new Date(millis);
    timeStamp.innerHTML = d.toLocaleString();
    timeStamp.classList.add('text-start');
    timeStamp.classList.add('align-text-bottom');
    timeStamp.style.color = "black";
    timeStamp.style.fontSize = "0.8rem";

    return timeStamp;
}

function createMessageElement(message, senderName, isDoctor) {

    let msgElement = document.createElement("p");

    if (isDoctor) {
        msgElement.innerHTML = senderName + " :" + message;
        msgElement.classList.add('text-end');
    }
    else {
        msgElement.innerHTML = message + " :" + senderName;
        msgElement.classList.add('text-left');
    }

    msgElement.classList.add('msg-class');
    msgElement.style.color = "black";
    msgElement.style.fontSize = "1.3rem";

    return msgElement;
}

