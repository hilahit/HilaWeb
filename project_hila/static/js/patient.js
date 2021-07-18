
document.getElementById("copy_img_email").
    addEventListener("click", function () {

    const span = document.getElementById("patient_email");
    const copyText = span.textContent;

        copyTextToClipBoard(copyText, "email");
    })


document.getElementById("copy_img_phone").
    addEventListener("click", function () {

        const span = document.getElementById("patient_phone");
        const copyText = span.textContent;

        copyTextToClipBoard(copyText, "phone number");
    })

function copyTextToClipBoard(text, element) {

    const textArea = document.createElement("textarea");
    textArea.value = text;

    document.body.appendChild(textArea);
    textArea.select();
    document.execCommand("copy");
    textArea.remove();

    alert("copied " + element);
}

