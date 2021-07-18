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
        })
    });
}
