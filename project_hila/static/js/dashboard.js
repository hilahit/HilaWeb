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

function showNoEntries() {

    let container = document.getElementById('new-entry-container');
    let liObject = document.createElement('li');
    liObject.classList.add('list-group-item');

    let span = document.createElement('span');
    span.classList.add('material-icons');
    span.classList.add('icon-no-msg');
    span.innerHTML = "do_not_disturb";

    h6 = document.createElement('h6');
    h6.innerHTML = "אין רשומות";

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

function appendNewEntry(entry) {
    let container = document.getElementById('new-entry-container');
    let liObject = document.createElement('li');
    liObject.classList.add('list-group-item');
    
    let userAction = document.createElement('h6');
    userAction.classList.add('user-action');
    userAction.innerHTML = entry.action;

    let userActionDate = document.createElement('h6');
    userActionDate.classList.add('user-action-date');
    userActionDate.innerHTML = entry.action_date;

    liObject.appendChild(userAction);
    liObject.appendChild(userActionDate);

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
            showNoNewMessages()
        }
    });
}

function fetchLatestEntries(entryUrl, userToken) {
    
    const spinner = document.getElementById('entry-spinner-container');
    const entryContainer = document.getElementById('new-entry-container');
    entryContainer.style.display = "none";
    
    const token = userToken;
    $.ajax({
        headers: { 'X-CSRFToken': token },
        type: 'POST',
        url: entryUrl,
        data: {
            'event': 'fetch_latest_entries'
        },
        dataType: 'json',
        success: function (data) {

            if (data.entries != null && data.entries.length != 0) {

                for (var i = 0; i < data.entries.length; i++) {
                    entry = data.entries[i];
                    appendNewEntry(entry);
                }

                spinner.style.display = "none";
                entryContainer.style.display = "block";
            }

            
            else {
                showNoNewMessages();
            }

        
        },
        error: function (event) {
            showNoEntries()
        }
    });
}


