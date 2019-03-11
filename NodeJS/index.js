console.log('Hello world');
var fs = require('fs');             //Read from file
var path = require('path');         //Setup path

//Read from json file
var content = fs.readFileSync(path.join(__dirname, '..', 'solutionOutput', 'solutionOut.json'));

//console.log("Output content: \n" + content);