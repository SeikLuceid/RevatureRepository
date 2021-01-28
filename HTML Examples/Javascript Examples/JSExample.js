// function dynamicValueExampleFunction(message)
// {
//     var dynamicValue = 0;
//     var msg = "D.quote ${dynamicValue}";
//     alert(msg);
//     msg = `Backtick ${dynamicValue}`;
//     alert(msg);
//     msg = 'S.quote ${dynamicValue}';
//     alert(msg);
//     document.getElementById("demo").innerHTML = "Paragraph changed." + message;
// }
//
// var add = function(x, y) { myFunction(x + y); }
//
// function callbackExampleFunction(subject, callback)
// {
//     alert(`Starting my ${subject} homework.`);
//     callback();
// }
//
// callbackExampleFunction('math', alertFinished);
// dynamicValueExampleFunction('Message');
//
// function alertFinished(){ alert('Finished my homework'); }
// setTimeout(alertFinished, 30000);

setTimeoutForMyFunctionThatHasParameters(5000, 99, 'Bugs');

function myFunctionThatHasParameters(param1, param2)
{
    alert("do things with params " + param1 + param2);
}

function setTimeoutForMyFunctionThatHasParameters(timeDelay, param1, param2)
{
    setTimeout(function() {
        myFunctionThatHasParameters(param1, param2)
    }, timeDelay)
}