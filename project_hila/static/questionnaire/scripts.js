var questionsContainer = document.getElementById("questions-container");
var btn = document.getElementById("btn_add_question");
var br = document.createElement("br");
let questionIndex = 0;
let answerIndex = 0;

btn.addEventListener('click', addQuestion.bind(null, true));

var addAnswerButton = document.createElement("button")
addAnswerButton.setAttribute("type", "button");
addAnswerButton.classList.add("btn");
addAnswerButton.classList.add("btn-success");
addAnswerButton.textContent = "הוספת תשובה";
addAnswerButton.addEventListener("click", createAnswerInputField);

function createQuestionInputField(questionType) {
    var questionInputElement = document.createElement("input");
    questionInputElement.id = "question" + questionIndex + " " + questionType;

    questionInputElement.setAttribute("type", "text");
    questionInputElement.setAttribute("name", "question" + questionIndex);
    questionInputElement.setAttribute("placeholder", "רשום שאלה");

    questionInputElement.classList.add("form-control");
    questionInputElement.classList.add("mb-2");
    questionInputElement.classList.add("w-50");

    questionsContainer.appendChild(questionInputElement);
}

function createAnswerInputField() {
    var answerInputField = document.createElement("input");
    answerInputField.id = "answer" + answerIndex;

    answerInputField.setAttribute("type", "text");
    answerInputField.setAttribute("name", "answer" + answerIndex);
    answerInputField.setAttribute("placeholder", "תשובה נוספת");

    answerInputField.classList.add("form-control");
    answerInputField.classList.add("mb-2");
    answerInputField.classList.add("w-50");

    answerIndex += 1;
    questionsContainer.insertBefore(answerInputField, addAnswerButton);

}

function addQuestion(toAdd) {

    const questionType = "סוג שאלה"
    const openQuestion = "שאלה פתוחה"
    const singleChoice = "בחירה אחת"
    const MultipleChoice = "בחירה מורבה"

    var optionsArray = [questionType, openQuestion, singleChoice, MultipleChoice];
    var selectList = document.createElement("select");
    selectList.id = "questionType" + questionIndex;

    for (var i = 0; i < optionsArray.length; i++) {
        var option = document.createElement("option");
        option.value = optionsArray[i];
        option.text = optionsArray[i];
        selectList.appendChild(option);
    }

    selectList.onchange = function () {
        if (selectList.selectedIndex == 1) {
            console.log(openQuestion)
           
            createQuestionInputField("open");
            addNewLinesAfterQuestion();
        }
        else if (selectList.selectedIndex == 2) {
            console.log(singleChoice)

            createQuestionInputField("single");
        
            questionsContainer.appendChild(addAnswerButton);
            addNewLinesAfterQuestion();
        }
        else if (selectList.selectedIndex == 3) {
            console.log(MultipleChoice)

            createQuestionInputField("multiple");

            questionsContainer.appendChild(addAnswerButton);
            addNewLinesAfterQuestion();
        }
    }
    
    questionIndex += 1;
    answerIndex = 0;

    questionsContainer.appendChild(selectList);
    addNewLinesAfterQuestion();
}

function addNewLinesAfterQuestion() {
    questionsContainer.appendChild(br.cloneNode());
    questionsContainer.appendChild(br.cloneNode());
    questionsContainer.appendChild(br.cloneNode());
}

$(document).ready(function () {
    
    
});