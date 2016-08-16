window.pl_horuss_bbplay_web_d3_Diagram = function() {
	
	var diagramFrame = d3.select(this.getElement()).append("svg:svg").attr(
			"width", 536).attr("height", 500);
	var steps;
	
	function drawStep(step) {
		step.entites.forEach(function(entity, entityNo) {
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
	}
	
	this.init = function(stepsStr) {
		diagramFrame.append("svg:image")
		   .attr('width', 536)
		   .attr('height', 500)
		   .attr("xlink:href","img/court1.png")
		steps = eval(stepsStr);
		drawStep(steps[0]);
	},

	this.play = function(speed, delay) {
		steps.forEach(function(step, stepNo) {
			if (stepNo != 0) step.entites.forEach(function(entity, entityNo) {
				var selEnt = diagramFrame.select("#se" + entity.id);
				if (selEnt.empty()) {
					drawStep(step)
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