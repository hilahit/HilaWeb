var questionsContainer = document.getElementById("questions-container");
var btn = document.getElementById("btn_add_question");
var br = document.createElement("br");
let i = 0;

btn.addEventListener('click', addQuestion.bind(null, true));

function addQuestion(toAdd) {

    deleteQuestionBtn = document.createElement("button")

    var questionInputElement = document.createElement("input");
    questionInputElement.setAttribute("type", "text");
    questionInputElement.id = "question" + i;
    questionInputElement.setAttribute("name", "question"+i);
    questionInputElement.setAttribute("placeholder", "רשום שאלה");
    questionInputElement.classList.add("form-control");
    questionInputElement.classList.add("mb-2");
    questionInputElement.classList.add("w-50");

    var optionsArray = ["שאלה פתוחה", "שאלה אמריקאית"];
    var selectList = document.createElement("select");
    selectList.id = "questionType" + i;
    for (var i = 0; i < optionsArray.length; i++) {
        var option = document.createElement("option");
        option.value = optionsArray[i];
        option.text = optionsArray[i];
        selectList.appendChild(option);
    }
    
    i += 1;
    questionsContainer.appendChild(selectList);
    questionsContainer.appendChild(br.cloneNode());
    questionsContainer.appendChild(br.cloneNode());
    questionsContainer.appendChild(questionInputElement);
    questionsContainer.appendChild(br.cloneNode());
    questionsContainer.appendChild(br.cloneNode());

    // var questionElement = document.createElement("div");

    // // a new line
    // const br = document.createElement('br');


    // // add "select question type label"
    // var optionsArray = ["שאלה פתוחה", "שאלה אמריקאית"];

    // var selectList = document.createElement("select");
    // selectList.style.direction = "rtl";

    // selectList.id = "questionType" + i;
    // for (var i = 0; i < optionsArray.length; i++) {
    //     var option = document.createElement("option");
    //     option.value = optionsArray[i];
    //     option.text = optionsArray[i];
    //     selectList.appendChild(option);
    // }


    // if (toAdd) {
    //     questionsContainer.appendChild(questionElement);
    //     i += 1;
    // }
}