window.pl_horuss_bbplay_web_d3_Diagram = function() {
	
	var diagramElement = this.getElement();
	var diagramFrame = d3.select(diagramElement).append("svg:svg").attr(
			"width", 500).attr("height", 500);

	this.play = function(stepsStr, speed, delay) {

		var svg = d3.select("svg");
		svg.selectAll("*").remove();

		var steps = eval(stepsStr);
		steps.forEach(function(step, stepNo) {
			step.entites.forEach(function(entity, entityNo) {
				var selEnt = svg.select("#se" + entity.id);
				if (selEnt.empty()) {
					var g = svg.append("svg")
						.attr("class", "node")
						.attr("id", "se" + entity.id);
					g.append("circle")
						.attr("r", "10")
						.attr("cx", 12)
						.attr("cy", 12)
						.attr("stroke", "#D68400")
						.attr("stroke-width", "2")
						.style("fill", "#FFAE00");
					g.append("text")
						.attr("dy", "15")
						.attr("dx", 10)
						.attr("class", "label")
						.attr("stroke-width", "1")
						.attr("fill", "#000")
						.style("font-size", '10px')
						.text(entity.label)
					g.attr("x", entity.x).attr("y", entity.y)

				} else {
					selEnt.transition()
						.delay(delay * 1000)
						.duration(speed * 1000)
						.attr("x", entity.x).attr("y", entity.y);
				}
			});
		});

	},

	this.reset = function() {
		var svg = d3.select("svg");
		svg.selectAll("*").remove();
	}

}