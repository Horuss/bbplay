window.pl_horuss_bbplay_web_d3_Diagram = function() {
    var diagramElement = this.getElement();
    var diagramFrame = d3.select(diagramElement).append("svg:svg").attr("width", 500).attr("height", 500);
    diagramFrame.append("svg:circle").attr("cx", 250).attr("cy", 250).attr("r", 20).attr("fill", "red");
 
    this.onStateChange = function() {
        var coords = this.getState().coords;
        d3.selectAll("circle").transition().attr("cx", parseInt(coords[0]));
        d3.selectAll("circle").transition().delay(500).attr("cy", parseInt(coords[1]));
    }
}