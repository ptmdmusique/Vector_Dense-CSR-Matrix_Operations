//Plotly https://plot.ly/nodejs/getting-started/
console.log('Hello world');
var fs = require('fs');             //Read from file
var path = require('path');         //Setup path
var jsonPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOut1.json');                    //Input json
var jsonOutPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOutBeautified1.json');       //Output json in a beatiful way!

var plotly = require('plotly')("ptmdmusique", "m5TxWYcJyVin3iDoA779") //Set up plotly
var layout = { title: "Error Graph", xaxis: { title: "Max step", autorange: true}, yaxis: { title: "Error", autorange: true}};
var layout2 = { title: "Running Time Graph", xaxis: { title: "Max step", autorange: true }, yaxis: { title: "Running Time (milliseconds)", autorange: true } };
var layout3 = { title: "Residual Length Graph", xaxis: { title: "Steps", autorange: true }, yaxis: { title: "Residual Length", autorange: true } };
var graphOptions = { layout: layout, filename: "Error Graph", fileopt: "overwrite" };
var graphOptions2 = { layout: layout2, filename: "Run Time Graph", fileopt: "overwrite" };
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
var stepError = { x: [], y: [], type: 'scatter', name: 'Error 1' };
partContent.forEach(item => {
    stepError.x.push(item["maxStep"]);
    stepError.y.push(item["error"]);
});
var stepRunTime = { x: [], y: [], type: 'scatter', name: 'Run Time 1' };
partContent.forEach(item => {
    stepRunTime.x.push(item["maxStep"]);
    stepRunTime.y.push(item["runTime"]);
});
var stepResidual = { x: [], y: [], type: 'scatter', name: 'Residual 1' };
var counter = 0;
jsonContent[jsonContent.length - 1]['residualLength'].forEach(item => {
    stepResidual.y.push(item);
    stepResidual.x.push(counter++);
});


var jsonPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOut2.json');                    //Input json
var jsonOutPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOutBeautified2.json');       //Output json in a beatiful way!
//Read from json file
var fileContent = fs.readFileSync(jsonPath);
var jsonContent = JSON.parse(fileContent);  //Parse from json into object
//Write the same json with a very nice format!
fs.writeFile(jsonOutPath, JSON.stringify(jsonContent, null, 4), function (err) {
    if (err) throw err;
});
var partContent = jsonContent.map(function (item) {
    return { maxStep: item["maxStep"], error: item["error"], runTime: item['runTime'] };
});
var stepError2 = { x: [], y: [], type: 'scatter', name: 'Error 2' };
partContent.forEach(item => {
    stepError2.x.push(item["maxStep"]);
    stepError2.y.push(item["error"]);
});
var stepRunTime2 = { x: [], y: [], type: 'scatter', name: 'Run Time 2' };
partContent.forEach(item => {
    stepRunTime2.x.push(item["maxStep"]);
    stepRunTime2.y.push(item["runTime"]);
});
var stepResidual2 = { x: [], y: [], type: 'scatter', name: 'Residual 2' };
counter = 0;
jsonContent[jsonContent.length - 1]['residualLength'].forEach(item => {
    stepResidual2.y.push(item);
    stepResidual2.x.push(counter++);
});

var jsonPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOut3.json');                    //Input json
var jsonOutPath = path.join(__dirname, '..', 'solutionOutput', 'solutionOutBeautified3.json');       //Output json in a beatiful way!
//Read from json file
var fileContent = fs.readFileSync(jsonPath);
var jsonContent = JSON.parse(fileContent);  //Parse from json into object
//Write the same json with a very nice format!
fs.writeFile(jsonOutPath, JSON.stringify(jsonContent, null, 4), function (err) {
    if (err) throw err;
});
var partContent = jsonContent.map(function (item) {
    return { maxStep: item["maxStep"], error: item["error"], runTime: item['runTime'] };
});
var stepError3 = { x: [], y: [], type: 'scatter', name: 'Error 3' };
partContent.forEach(item => {
    stepError3.x.push(item["maxStep"]);
    stepError3.y.push(item["error"]);
});
var stepRunTime3 = { x: [], y: [], type: 'scatter', name: 'Run Time 3' };
partContent.forEach(item => {
    stepRunTime3.x.push(item["maxStep"]);
    stepRunTime3.y.push(item["runTime"]);
});
var stepResidual3 = { x: [], y: [], type: 'scatter', name: 'Residual 3' };
counter = 0;
jsonContent[jsonContent.length - 1]['residualLength'].forEach(item => {
    stepResidual3.y.push(item);
    stepResidual3.x.push(counter++);
});

var errorData = [stepError, stepError2, stepError3]
var runTimeData = [stepRunTime, stepRunTime2, stepRunTime3];
var residualData = [stepResidual, stepResidual2, stepResidual3];

plotly.plot(errorData, graphOptions, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});
plotly.plot(runTimeData, graphOptions2, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});
plotly.plot(residualData, graphOptions3, function (err, msg) {
    if (err) return console.log(err);
    console.log(msg);
});