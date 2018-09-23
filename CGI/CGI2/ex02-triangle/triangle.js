var gl;

window.onload = function init() {
    var canvas = document.getElementById("gl-canvas");
    gl = WebGLUtils.setupWebGL(canvas);
    if(!gl) { alert("WebGL isn't available"); }
    
    // Three vertices
    var vertices = [
        vec2(-0.5,-0.5),
        vec2(0.5,-0.5),
        vec2(0,0.5)
    ];
    var cores =[
        vec4(1.0,0.0,0.0,1.0),
        vec4(0.0,1.0,0.0,1.0),
        vec4(0.0,0.0,1.0,1.0)
    ];
    
    // Configure WebGL
    gl.viewport(0,0,canvas.width, canvas.height);
    gl.clearColor(1.0, 1.0, 1.0, 1.0);
    
    // Load shaders and initialize attribute buffers
    var program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);

    // Load the data into the GPU
    var bufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, bufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(vertices), gl.STATIC_DRAW);
    
   
   //Usando 2 buffers
//gl.bufferSubData(gl.ARRAY_BUFFER,0,flatten(vertices));
   //gl.bufferSubData(gl.ARRAY_BUFFER,?,flatten(cores));
    // Associate our shader variables with our data buffer
   
    var vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);
    
    //2.2
    var bufferId2 = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, bufferId2);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(cores), gl.STATIC_DRAW);
    var vColor = gl.getAttribLocation(program, "vColor");
    gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vColor);
    
    
   //2.1//var colorLoc = gl.getUniformLocation(program, "color"); 
    
    
    var fColor =gl.getAttribLocation(program, "fColor");
  //2.1 // gl.vertexAttribPointer(fColor, 2, gl.FLOAT, false, 0, 0);
  //2.1 // gl.enableVertexAttribArray(fColor);
    
    
    render();
}

function render() {
    var color = vec4(1.0, 0.0, 0.0, 1.0);
    gl.clear(gl.COLOR_BUFFER_BIT);
   //2.1// gl.uniform4fv(colorLoc, color);
    gl.drawArrays(gl.TRIANGLES, 0, 3);
  //2.1 // color = vec4(1.0, 0.5, 0.6, 1.0);
  //2.1 // gl.drawArrays(gl.LINE_LOOP, 0, 3);
   
}
