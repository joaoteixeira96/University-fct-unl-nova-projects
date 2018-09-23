"use strict";

var canvas;
var menu;
var gl;

var cindex = 0.0;
var maxPoint = 50000;
var index = 0;
var red = 0.5;
var blue = 0.5;
var green = 0.5;
var opacity = 1.0;
var currentColor;
var preview;
var modeButton;
var manualMode = true;
var timeLoc;
var vtime = 0.0;
var sizes = 50.0 ;
var velocity = 0.0;
var shapeSizeVeloc;
var shapeSizeVelocBuffer;
var shape = 0.0;

var vBuffer;
var cBuffer;
var sizeBuffer;
var shapeBuffer;


window.onload = function init() {
    canvas = document.getElementById( "gl-canvas" );
    menu = document.getElementById("mymenu" );
    preview = document.getElementById("preview");
    modeButton = document.getElementById("mode");
  
    
    modeButton.onclick = function () {
        manualMode = !manualMode;
        
        if(manualMode)
            modeButton.innerHTML = "Automatic Mode";
        else
        {
            modeButton.innerHTML = "Manual Mode";
            
           
            
            
        }
    };
    gl = WebGLUtils.setupWebGL( canvas );
    if ( !gl ) { alert( "WebGL isn't available" ); }
    
    
    
    menu.onchange = function( event ) {
        cindex =  event.target.value || event.srcElement.value; 
        
    };
    
    canvas.addEventListener("mousedown", function(event){
         
        if(manualMode){
        gl.bindBuffer( gl.ARRAY_BUFFER, vBuffer );
        var t = vec2(2*event.clientX/canvas.width-1,
           2*(canvas.height-event.clientY)/canvas.height-1);
        gl.bufferSubData(gl.ARRAY_BUFFER,8*index, flatten(t));

        gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
        t = vec4(red,green,blue,opacity);
        gl.bufferSubData(gl.ARRAY_BUFFER, 16*index, flatten(t));

        gl.bindBuffer( gl.ARRAY_BUFFER, shapeSizeVelocBuffer );

        velocity = Math.random() * (3 -(-3)) -3 ;   
        sizes = (Math.random()*(50-10+1)+10);
        shape = cindex;
    
        shapeSizeVeloc = vec3(shape,sizes,velocity);
        gl.bufferSubData(gl.ARRAY_BUFFER,12*index, flatten(shapeSizeVeloc));


    

        index++;
        }
    } );


    gl.viewport( 0, 0, canvas.width, canvas.height );
    gl.clearColor( 0.5, 0.5, 0.5, 1.0 );
    gl.enable(gl.BLEND);
    gl.blendFuncSeparate(gl.SRC_ALPHA, gl.ONE_MINUS_SRC_ALPHA, gl.ONE, gl.ONE_MINUS_SRC_ALPHA);

    //
    //  Load shaders and initialize attribute buffers
    //
    var program = initShaders( gl, "vertex-shader", "fragment-shader" );
    gl.useProgram( program );


    vBuffer = gl.createBuffer();
    gl.bindBuffer( gl.ARRAY_BUFFER, vBuffer);
    gl.bufferData( gl.ARRAY_BUFFER, 8*maxPoint, gl.STATIC_DRAW );

    var vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);

    cBuffer = gl.createBuffer();
    gl.bindBuffer( gl.ARRAY_BUFFER, cBuffer );
    gl.bufferData( gl.ARRAY_BUFFER, 16*maxPoint, gl.STATIC_DRAW );

    var vColor = gl.getAttribLocation( program, "vColor" );
    gl.vertexAttribPointer( vColor, 4, gl.FLOAT, false, 0, 0 );
    gl.enableVertexAttribArray( vColor );

   shapeSizeVelocBuffer= gl.createBuffer();
    gl.bindBuffer( gl.ARRAY_BUFFER, shapeSizeVelocBuffer);
    gl.bufferData( gl.ARRAY_BUFFER, 12*maxPoint, gl.DYNAMIC_DRAW );
    
    var vshapeSizeVeloc = gl.getAttribLocation(program, "vshapeSizeVeloc");
    gl.vertexAttribPointer(vshapeSizeVeloc, 3, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vshapeSizeVeloc); 
    
    document.getElementById("red").onchange =
    function(event) {
     red = event.target.value||event.srcElement.value;
     changeBackground();
    };
    document.getElementById("green").onchange =
    function(event) {
     green = event.target.value||event.srcElement.value;
     changeBackground();
    };
    document.getElementById("blue").onchange =
    function(event) {
     blue = event.target.value||event.srcElement.value;
     changeBackground();
    };
    document.getElementById("Opa").onchange =
    function(event) {
      opacity = event.target.value||event.srcElement.value;
      changeBackground();
    };
    
    timeLoc = gl.getUniformLocation(program, "vtime");
    render();

}

function randInt(max) {
  return Math.random() * max | 0;
}

function changeBackground()
{

    preview.style.background ="rgba("+Math.floor(red*255)+","+Math.floor(green*255)+","+Math.floor(blue*255)+","+opacity+")";
}


function render() {
    
    vtime += 0.01;
    gl.uniform1f(timeLoc,vtime);
    
    
    var randomnum = Math.random()*(50-0+1)+0;
    
    if(!manualMode && randomnum > 48)
    {
        
        gl.bindBuffer(gl.ARRAY_BUFFER,vBuffer);
        var x = randInt(gl.canvas.width);
        var y = randInt(gl.canvas.height);
        var randomPos = vec2((x*2)/canvas.width-1,2*(canvas.height - y)/canvas.height-1);
        gl.bufferSubData(gl.ARRAY_BUFFER,8*index,flatten(randomPos));
        
        gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
        var newcolor = vec4(red,green,blue,opacity);
        gl.bufferSubData(gl.ARRAY_BUFFER, 16*index, flatten(newcolor));
        
       gl.bindBuffer( gl.ARRAY_BUFFER, shapeSizeVelocBuffer );
        velocity = Math.random() * (3 -(-3)) -3 ;   
        sizes = (Math.random()*(50-10+1)+10);
        shape = cindex;
        shapeSizeVeloc = vec3(shape,sizes,velocity);
        gl.bufferSubData(gl.ARRAY_BUFFER,12*index, flatten(shapeSizeVeloc));
            
       
        
        index++;
    }
    
    gl.drawArrays( gl.POINTS, 0,index ); 
    window.requestAnimFrame(render); 
}
