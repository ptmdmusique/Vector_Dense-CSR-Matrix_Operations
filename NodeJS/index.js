//Plotly https://plot.ly/nodejs/getting-started/
console.log('Hello world');
var fs = require('fs');             //Read from file
var path = require('path');         //Setup path
var jsonPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOut.json');                    //Input json
var jsonOutPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOutBeautified.json');       //Output json in a beatiful way!

var plotly = require('plotly')("ptmdmusique", "m5TxWYcJyVin3iDoA779") //Set up plotly
var layout = { title: "Error Graph", xaxis: { title: "Max step", autorange: true}, yaxis: { title: "Error", autorange: true}};
var layout2 = { title: "Running Time Graph", xaxis: { title: "Max step", autorange: true }, yaxis: { title: "Running Time (milliseconds)", autorange: true } };
var layout3 = { title: "Residual Length Graph", xaxis: { title: "Steps", autorange: true }, yaxis: { title: "Residual Length", autorange: true } };
var graphOptions = { layout: layout, filename: "Run Time Graph", fileopt: "overwrite" };
var graphOptions2 = { layout: layout2, filename: "Error Graph", fileopt: "overwrite" };
var graphOptions3 = { layout: layout3, filename: "Residual Length Graph", fileopt: "overwrite" };

//Read from json file
var fileContent = fs.readFileSync(jsonPath);
var jsonContent = JSON.parse(fileContent);  //Parse from json into object

//Write the same json with a very nice format!
fs.writeFile(jsonOutPath, JSON.stringify(jsonContent, null, 4), function (err) {
    if (err) throw err;
});

var partContent = jsonContent.map(function (item) {
    return { maxStep: item["maxStep"], error: item["error"], runTime: item['runTime']};
});


var stepError = { x: [], y: [], type: 'scatter' };
partContent.forEach(item => {
    stepError.x.push(item["maxStep"]);
    stepError.y.push(item["error"]);
});
var stepRunTime = { x: [], y: [], type: 'scatter' };
partContent.forEach(item => {
    stepRunTime.x.push(item["maxStep"]);
    stepRunTime.y.push(item["runTime"]);
});
var stepResidual = { x: [], y: [], type: 'scatter' };
var counter = 0;
jsonContent[jsonContent.length - 1]['residualLength'].forEach(item => {
    stepResidual.y.push(item);
    stepResidual.x.push(counter++);
});

plotly.plot(stepError, graphOptions, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});
plotly.plot(stepRunTime, graphOptions2, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});
plotly.plot(stepResidual, graphOptions3, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});