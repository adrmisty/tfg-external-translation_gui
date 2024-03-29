/*
    For text settings (append, prepend...) in the HTML file.

    @author Adriana R.F. (UO282798@uniovi.es)
    @version March 2024
*/
class TextUtil {
    constructor(){ }

    apP(sel,text){
        // Appends a paragraph
        $(selector).append('<p>' + text + '</p>');
    }

    preP(sel,text){
        // Prepends a paragraph
        $(sel).prepend('<p>' + text + '</p>');
    }

    apSub(sel,text){
        // Appends a subtitle (h5)
        $(sel).append('<h5>' + text + '</h5>');
    }

    preSub(sel,text){
        // Prepends a subtitle (h5)
        $(sel).prepend('<h5>' + text + '</h5>');
    }

    new(sel,tag){
        // Creates an element
        $(sel).append("<" + tag + "></" + tag + ">");
    }

    del(sel){
        $(sel).empty();
    }

}


/*
    Manager for TranslationApp on the web side (interaction with Spring Boot, file upload, showing results...).
    @author Adriana R.F. - UO282798
*/
class TranslationWeb {
    
    constructor(jq){
        this.jq = jq; 
    }

    // Computes the size of the uploaded file
    size(){
        jq.del("section");
        jq.del("article");
        jq.apSub("article","Selected file:");

        // Info about selected file
        let file = document.querySelector("input[type=multipart/file]").files[0];

        jq.appendParrafo("article","File size: " + file.size + " bytes");
    }

    // Fetches all supported languages
    languages(){
        fetch('/languages.txt')
        .then(response => response.text())
        .then(data => {
            const comboBox = document.getElementById('language');
            data.split('\n').forEach(lang => {
                const option = document.createElement('option');
                option.text = lang.trim();
                comboBox.add(option);
            });
        })
        .catch(error => {
            console.error('Error: could not fetch supported languages', error);
        })
    }

    // Toggle submit button
    enable(){
        var file = document.getElementById('file');
        var language = document.getElementById('language');
        var submit = document.querySelector('button[type="submit"]');

        if (file.files.length > 0 && language.value != 'Select language...'){
            submit.disabled = false;
        } else {
            submit.disabled = true;
        }
    }

    // Translate uploaded file
    translate(){

        let file = document.getElementById('file').files[0];

        // No selected file
        if (file == null){
            jq.del("article");
            this.jq.del("section");

        // Translate file
        } else {

            var formData = new FormData();

            // Params
            formData.append('file', file);
            formData.append('targetLanguage', document.getElementById('language').value);
            fetch('http://localhost:8080/translateFile', {
                method:'POST',
                body: formData
            })
            .then(response => response.text())
            .then(results => {
                this.jq.del('section');
                this.jq.apSub('section', 'Your file has been translated correctly');
                this.jq.new('section','textarea');
                document.getElementsByTagName('textarea').innerHTML = results;
            })
            .catch(error => {
                this.jq.preSub("results", "There has been an issue while carrying out the file's translation.");
                console.error('Error: ', error);
            })
        }
    }
}

var text = new TextUtil();
var translator = new TranslationWeb(text);
// Set supported languages to select
translator.languages();
