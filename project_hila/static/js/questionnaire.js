function updateQuestionnaireCounter() {
    let counter = document.getElementById("questionnaireCount");
    var checkboxes = document.querySelectorAll("input[type=checkbox]");

    let enabledCheckboxes = []

    // Use Array.forEach to add an event listener to each checkbox.
    checkboxes.forEach(function (checkbox) {
        checkbox.addEventListener('change', function () {
            enabledCheckboxes =
                Array.from(checkboxes) // Convert checkboxes to an array to use filter and map.
                    .filter(i => i.checked) // Use Array.filter to remove unchecked checkboxes.
                    .map(i => i.value) // Use Array.map to extract only the checkbox values from the array of objects.

            counter.textContent = enabledCheckboxes.length;

            if (enabledCheckboxes.length > 0) {
                document.getElementById("sendQuestionnairesBtn").disabled = false;
            }
            else if (enabledCheckboxes.length == 0) {
                document.getElementById("sendQuestionnairesBtn").disabled = true;
            }
        })
    });

}

function createNewQuestionnaire() {

    var alertEmpty = document.getElementById("alert-empty");
    var titleInput = document.getElementById("questionnaire-title");
    const qTitle = titleInput.value;
 
    if (qTitle.length == 0) {
        alertEmpty.style.display = "block";
    }
    else {
        alertEmpty.style.display = "none";        
    }
}