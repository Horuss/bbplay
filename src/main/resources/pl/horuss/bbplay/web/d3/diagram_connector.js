window.pl_horuss_bbplay_web_d3_Diagram = function() {
	
	var diagramFrame = d3.select(this.getElement()).append("svg:svg").attr(
			"width", 500).attr("height", 500);
	var steps;
	
	this.init = function(stepsStr) {
		steps = eval(stepsStr);
		steps[0].entites.forEach(function(entity, entityNo) {
			var g = diagramFrame.append("svg")
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
		});
	},

	this.play = function(speed, delay) {
		steps.shift();
		steps.forEach(function(step, stepNo) {
			step.entites.forEach(function(entity, entityNo) {
				var selEnt = diagramFrame.select("#se" + entity.id);
				if (selEnt.empty()) {
					var g = diagramFrame.append("svg")
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
		diagramFrame.selectAll("*").remove();
	}

}