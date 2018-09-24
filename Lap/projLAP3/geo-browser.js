/*	 GeoBrowser

Aluno 1: 48047 Joao Teixeira
Aluno 2: 48142 Li Zixiang

ComentÃ¡rio:

?????????????????????????
?????????????????????????
?????????????????????????
?????????????????????????
?????????????????????????
?????????????????????????

01234567890123456789012345678901234567890123456789012345678901234567890123456789


HTML DOM documentation: https://www.w3schools.com/js/js_htmldom.asp
Leaflet documentation: http://leafletjs.com/reference-1.0.3.html
Mapbox documentation: https://www.mapbox.com/api-documentation/

INSIDE THIS FILE, YOU CAN CHANGE EVERYTHING YOU WANT!
*/


// JAVA CLASSES EMULATION

var JSRoot = {
	SUPER: function(method) {
		return method.apply(this,
			Array.prototype.slice.apply(arguments).slice(1));
	},
	INIT: function() {
		throw "*** MISSING INITIALIZER ***";
	}
};

function NEW(clazz) { // Create an object and applies INIT(...) to it
	function F() {}
	F.prototype = clazz;
	var obj = new F();
	obj.INIT.apply(obj, Array.prototype.slice.apply(arguments).slice(1));
	return obj;
};

function EXTENDS(clazz, added) { // Creates a subclass of a given class
	function F() {}
	F.prototype = clazz;
	var subclazz = new F();
	for(var prop in added)
		subclazz[prop] = added[prop];
	return subclazz;
};


/* GLOBAL CONSTANTS */

const RESOURCES_DIR = "resources/";
const CACHES_FILE = "caches.xml";
const LOAD_PERCENTAGE = 100;  // Should be 100%
const WORLD_CENTRE = [38.661,-9.2044]


/* GLOBAL VARIABLES */

// We use global variables here because this is a small script focused
// on the handling of a single entity, the map. In a larger program with
// multiple main entities, we would have created a class "MapControl" and
// put inside everything related to the map.
// But beware, cache specific operations should not be defined at the
// global level but instead inside the class "Cache".
var map = null;
var markers = []; 
var caches = [];
var filters = {
    Traditional: true,
    Multi: true,
    Mystery: true,
    Mega: true,
    CITO: true,
    Earthcache: true,
    Event: true,
    Letterbox: true,
    Other: true,
    Virtual: true,
    Webcam: true,
    Wherigo: true,
    Archived: false,
    Micro: true,
    Small: true,
    Regular: true,
    Large: true,
    Other: true,
    Unknown: true,
    t10: true,
    t15: true,
    t20: true,
    t25: true,
    t30: true,
    t35: true,
    t40: true,
    t45: true,
    t50: true,
    d10: true,
    d15: true,
    d20: true,
    d25: true,
    d30: true,
    d35: true,
    d40: true,
    d45: true,
    d50: true
};

/* POI CLASS */

// Digital maps for GPS receivers usually include a selection of POI installed
// on the maps. POI means "Point of interest". A POI specifies a name, a latitude
// and a longitude. Each POI can represent, for example, a touristic location,
// a supermarket, a hotel, a police radar, a shopping mall, and so on. So,
// the concept of POI is a very general one.
var POI = EXTENDS(JSRoot, {
	name: "",
	latitude: 0.0,
	longitude: 0.0,
	INIT: function(xml) {
		this.name = getFirstValueByTagName(xml, "name");
		this.latitude = getFirstValueByTagName(xml, "latitude");
		this.longitude = getFirstValueByTagName(xml, "longitude");
	}
});


/* CACHE CLASS */

// A Cache is a specific type of POI. Adds a lot of attributes,
// all those we have available in our database of caches.
var Cache = EXTENDS(POI, {
	code: 0,
	kind: "",
	status: "",
    owner: "",
    altitude: "",
    size: "",
    difficulty: "",
    terrain: "",
    favorites: "",
    founds: "",
    not_founds: "",
    state: "",
    county: "",
    publish: "",
    last_log: "",
	INIT: function(xml) {
		this.SUPER(POI.INIT, xml);
		this.code = getFirstValueByTagName(xml, "code");
		this.kind = getFirstValueByTagName(xml, "kind");
		this.status = getFirstValueByTagName(xml, "status");
		this.owner = getFirstValueByTagName(xml, "owner");
		this.altitude = getFirstValueByTagName(xml, "altitude");
		this.size = getFirstValueByTagName(xml, "size");
		this.difficulty = getFirstValueByTagName(xml, "difficulty");
		this.terrain = getFirstValueByTagName(xml, "terrain");
		this.favorites = getFirstValueByTagName(xml, "favorites");
		this.founds = getFirstValueByTagName(xml, "founds");
		this.not_founds = getFirstValueByTagName(xml, "not_founds");
		this.state= getFirstValueByTagName(xml, "state");
		this.county = getFirstValueByTagName(xml, "county");
		this.publish = getFirstValueByTagName(xml, "publish");
		this.last_log = getFirstValueByTagName(xml, "last_log");
	}
   	// ... more methods? ...
});

// Loads the database and converts it to an array of cache objects.
// Uses our XML operations, loadXMLDoc and getAllValuesByTagName.
// The array of caches is global.
function loadCaches(filename)
{
	var xmlDoc = loadXMLDoc(filename);
	var xs = getAllValuesByTagName(xmlDoc, "cache"); 
	caches = [];
	if(xs.length == 0)
		alert("Empty cache file");
	else {
		caches.length = xs.length * LOAD_PERCENTAGE / 100;
		for(var i = 0 ; i < caches.length ; i++)
			caches[i] = NEW(Cache, xs[i]);
	}
	alert(caches.length + " caches loaded!");
}


/* XML */

// AJAX - cf. end of lecture 21Z
function loadXMLDoc(filename)
{
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", filename, false);
	try {
		xhttp.send();
	}
	catch(err) {
		alert("Could not access the local geocaching database via AJAX.\n"
			+ "Therefore, no geocaches will be visible.\n\n"
			+ "Please, use the Firefox browser to develop the project!");
	}
	return xhttp.responseXML;	
}

// Two simple function are enough to navigate in the XML document that
// represents the database of caches.
function getAllValuesByTagName(xml, name)  {
	return xml.getElementsByTagName(name);
}

function getFirstValueByTagName(xml, name)  {
	return getAllValuesByTagName(xml, name)[0].childNodes[0].nodeValue;
}


/* MISC */

// Capitalize the first letter of a string.
function capitalize(str)
{
	return str.length > 0
			? str[0].toUpperCase() + str.slice(1)
			: str;
}

// Distance in km between to pairs of coordinates over the earth's surface.
// https://en.wikipedia.org/wiki/Haversine_formula
function haversine(lat1, lon1, lat2, lon2)
{
	function toRad(deg) { return deg * 3.1415926535898 / 180.0; }
	var dLat = toRad(lat2 - lat1), dLon = toRad (lon2 - lon1);
	var sa = Math.sin(dLat / 2.0), so = Math.sin(dLon / 2.0);
	var a = sa * sa + so * so * Math.cos(toRad(lat1)) * Math.cos(toRad(lat2));
	return 6372.8 * 2.0 * Math.asin (Math.sqrt(a))
}


/* MAP */

// Creates a map. "L" is a global variable that stores the object that
// implements the Leaflet services. "L" is an abbreviation of "Leaflet".
// Our map is stored in a global variable.
function makeMap(center, zoom)
{
	map = L.map("map", { center: center, zoom: zoom});
}

// Creates a layer that will be installed in a map latter.
// The layer will consist in a map of Mapbox, the open source
// mapping platform. "spec" is a string that designates a kind of map,
// for example: "mapbox.streets", "mapbox.satellite", or
// "mapbox.run-bike-hikes".
function makeLayerMapBox(name, spec)
{
	var urlTemplate =
		  "https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png"
		+ "?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXV"
		+ "ycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
	var attr =
		  "Map data &copy; <a href='http://openstreetmap.org'>"
		+ "OpenStreetMap</a> contributors, "
		+ "<a href='http://creativecommons.org/licenses/by-sa/2.0/'>"
		+ "CC-BY-SA</a>, "
		+ "Imagery Â© <a href='http://mapbox.com'>Mapbox</a>";
	var errorTileUrl =
		"https://upload.wikimedia.org/wikipedia/commons/e/e0/SNice.svg";
	var layer =
		L.tileLayer(urlTemplate, {
				minZoom: 10,
				maxZoom: 18,
				errorTileUrl: errorTileUrl,
				id: spec,
				attribution: attr
		});
	return layer;
}

// "specs" is a list of kind of map designators. All
// the designators are used to create layers, but only the first
// layer (with index zero) is directly installed in the map.
// All the layers are used to create a "layer control" that
// is installed at the top left of the map and thatthe user
// will use to select among several map styles that are available.
// This function also creates a "scale control" that shows
// in the screen the current scale of the map.
function addBaseLayers(specs)
{
	var controls = {};
	for(var i in specs)
		controls[capitalize(specs[i])] =
			makeLayerMapBox(specs[i], "mapbox." + specs[i]);
	controls[capitalize(specs[0])].addTo(map);
	L.control.scale({maxWidth: 150, metric: true, imperial: false})
								.setPosition("topleft").addTo(map);
	L.control.layers(controls, {}).setPosition("topleft").addTo(map);
}

// This exemplifies how to install a click handler on the map.
// When you need to implement something less standard involving the
// interaction with the user, you will need to handle the events yourself.
// One detail: In this code, we avoid creating a new popup for each click.
// To save memory, we create a single popup and reuse it reused again
// and again.  We could have stored the popup in a global variable, but
// we decided to store it directly in a extra field in the map, only the
// show that this alternative exists.
function addClickHandler()
{
	if( map.popup === undefined )
		map.popup = L.popup();		// ad-hoc addition of new field to map
	map.on("click", 
		function (e) {
			this.popup
				.setLatLng(e.latlng)
				.setContent("You clicked the map at " + e.latlng.toString())
				.openOn(this);
	});
}

// Removes all the markers (geocaches) from the map.
// Technically, each marker is a single layer. It would be
// better to create a "layer group" for all the markers and then
// remove all the markers from the map using a single operation,
// instead of a cycle.
function clearMap() 
{
	for(var i = 0 ; i < markers.length ; i++)
		map.removeLayer(markers[i]);
	markers = [];
}

// Populates the map with all the caches contained in the global variable
// 'caches'.
function populateMap() 
{
	clearMap();
    var highAltitude = "-32768";
    var owners ={};
    var ownersArray = [];
	for(var i = 0 ; i < caches.length ; i++){
        var c = caches[i];
       
        if(filters[c.kind] && document.getElementById("datepicker").value 
           >= c.publish && filters[c.size] && filters["t"+c.terrain*10]
            && filters["d"+c.difficulty*10]){
		          addMarker(c);
                  if(parseInt(c.altitude) > parseInt(highAltitude))
                        highAltitude = c.altitude;
                  if(c.owner in owners)
                    owners[c.owner]++;  
                  else
                    owners[c.owner] = 1;
        }
    }
    
    document.getElementById("currentCaches").innerHTML = markers.length;
    if(highAltitude != "-32768")
        document.getElementById("highAltitude").innerHTML = highAltitude;
    else
        document.getElementById("highAltitude").innerHTML = "altitude unknown";
    
    ownersArray = Object.keys(owners)
                        .sort(function(a,b){return owners[b]-owners[a]});
    
    document.getElementById("top1").innerHTML = ownersArray[0];
    document.getElementById("top2").innerHTML = ownersArray[1];
    document.getElementById("top3").innerHTML = ownersArray[2];
    
    markerPopup();
}

// An auxiliary object used in the function 'addMarker'.
// It is reused again and again to save memory.
var iconOptions = {
		iconUrl: "??",
		shadowUrl: "??",
		iconSize: [16, 16], // size of the icon
		shadowSize: [16, 16], // size of the shadow
		iconAnchor: [8, 8], // marker's location
		shadowAnchor: [8, 8],  // the same for the shadow
		popupAnchor: [0, -6] // offset the determines where the popup should open
};

// Creates a marker corresponding to a given cache and installs the marker on the map.
// Also saves the marker in the global array 'markers', so that we will be able to
// access them later to erase them (in the function 'clearMap').
// Our markers are a bit inefficient because a new icon is created for each marker
// (using the operation L.icon). It would be better to create a single icon for each
// kind of cache, a reuse those icons. Try to do that...
function addMarker(cache)
{
	iconOptions.iconUrl = RESOURCES_DIR + cache.kind + ".png";
	iconOptions.shadowUrl = RESOURCES_DIR + (cache.status == "A" 
                                             && filters["Archived"]
                                             && document.getElementById("datepicker").value >= cache.last_log
                                             ? "Archived" : "Alive") + ".png";
	var marker = L.marker([cache.latitude, cache.longitude], {icon: L.icon(iconOptions)});
    markers.push(marker);
	marker
		.bindPopup("<b>Code</b>: " + cache.code + "<br />" +
                   "<b>Owner</b>: " + cache.owner + "<br />" +
                   "<b>Latitude</b>: " + cache.latitude + "<br />" +
                   "<b>Longitude</b>: " + cache.longitude + "<br />" +
                   "<b>Altitute</b>: " + cache.altitude + "<br />" +
                   "<b>Kind</b>: " + cache.kind + "<br />" +
                   "<b>Size</b>: " + cache.size + "<br />" +
                   "<b>Difficulty</b>: " + cache.difficulty + "<br />" +
                   "<b>Terrain</b>: " + cache.terrain + "<br />" +
                   "<b>Favorites</b>: " + cache.favorites + "<br />" +
                   "<b>Founds</b>: " + cache.founds + "<br />" +
                   "<b>Not_Founds</b>: " + cache.not_founds + "<br />" +
                   "<b>State</b>: " + cache.state + "<br />" +
                   "<b>County</b>: " + cache.county + "<br />" +
                   "<b>Publish</b>: " + cache.publish + "<br />" +
                   "<b>Status</b>: " + cache.status + "<br />" +
                   "<b>Last_Log</b>: " + cache.last_log + "<br />")
			.bindTooltip(cache.name)
				.addTo(map);
}

function markerPopup(){
    for(var i = 0; i<markers.length; i++)
        markers[i]._popup.setContent(markers[i]._popup.getContent() + "<b>nNeighbor</b>: " + getnNeighbor(i));
}

function getnNeighbor(markerIndice){
    var nNeighbor = 0;
	for(var i = 0; i < markers.length; i++)
        if(markerIndice != i && markers[markerIndice].getLatLng().distanceTo(markers[i].getLatLng()) <= 500)
            nNeighbor++;
    return nNeighbor;
}

var timer = undefined;
function navigationOP(checkbox){
    if(checkbox.checked){
        var currentDate = new Date();
        var refDate = new Date(document.getElementById("datepicker").value);
        timer = setInterval(function(){incrementDate(refDate, currentDate)}, 1);}
    else
        clearInterval(timer);
}

function incrementDate(refDate, currentDate){
    if(refDate < currentDate){
        refDate.setDate(refDate.getDate() + 1);
        document.getElementById("datepicker").value = dateString(refDate);
        populateMap();
    }
    else
        clearInterval(timer);
}

function dateString(refDate){
    var year = refDate.getFullYear();
    var month = refDate.getMonth() + 1;
    var day = refDate.getDate();
    if(month < 10)
        month = "0" + month;
    if(day < 10)
        day = "0" + day;
    return year + "/" + month + "/" + day;
}


/* INTERACTIVE CONTROL PANEL */

// This listener is installed in the checkboxes that contrl the filter
// currently in force. To store the current filter specification you may
// use a global variable named 'filters'.
function checkboxUpdate(checkbox)
{
    filters[checkbox.id] = checkbox.checked;
    populateMap();
	alert("clicked");
}

/* ONLOAD */

// What to do when the Web page opens. This is called just after the Web page
// has been loaded and all the DOM elements are available for manipulation.
function onLoad()
{
	makeMap(WORLD_CENTRE, 14);
	addBaseLayers(["streets", "light", "dark", "satellite", "streets-satellite",
				"wheatpaste", "streets-basic", "comic", "outdoors", "comic",
				"run-bike-hike", "pencil", "pirates", "emerald", "high-contrast"]
	);
	addClickHandler();
	loadCaches(RESOURCES_DIR + CACHES_FILE);
	populateMap();
}
