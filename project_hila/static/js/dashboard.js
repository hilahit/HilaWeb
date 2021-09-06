function showNoNewMessages() {

    let container = document.getElementById('new-msg-container');
    let liObject = document.createElement('li');
    liObject.classList.add('list-group-item');

    let span = document.createElement('span');
    span.classList.add('material-icons');
    span.classList.add('icon-no-msg');
    span.innerHTML = "do_not_disturb";

    h6 = document.createElement('h6');
    h6.innerHTML = "אין הודעות חדשות";

    liObject.appendChild(span);
    liObject.appendChild(h6);
    container.appendChild(liObject);
}

function appendNewMsg(name, key) {

    let container = document.getElementById('new-msg-container');
    let liObject = document.createElement('li');
    liObject.classList.add('list-group-item');
    liObject.innerHTML = ` <h6>${name}</h6>
                                <button onclick="navigateToPatient('${key}')" class="mdc-icon-button material-icons icon-btn">
                                <div class="mdc-icon-button__ripple"></div>
                                comment
                                </button>`;


    container.append(liObject);
}

function fetchChatData(chatDataUrl, userToken) {
    const spinner = document.getElementById('spinner-container');

    const msgContainer = document.getElementById('new-msg-container');
    msgContainer.style.display = "none";


    const token = userToken;
    $.ajax({
        headers: { 'X-CSRFToken': token },
        type: 'POST',
        url: chatDataUrl,
        data: {
            'event': 'fetch_chats_data'
        },
        dataType: 'json',
        success: function (data) {

            if (data.messages != null && data.messages.length != 0) {

                for (var i = 0; i < data.messages.length; i++) {
                    msg = data.messages[i];
                    appendNewMsg(msg.patient_name, msg.patient_key);
                }
            }
            else {
                showNoNewMessages();
            }

            spinner.style.display = "none";
            msgContainer.style.display = "block";
        },
        error: function (event) {
        }
    });
}
