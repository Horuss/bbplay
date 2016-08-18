window.pl_horuss_bbplay_web_d3_Diagram = function() {
	
	var connector = this;
	var diagramFrame;
	var play;
	var scheduledEvents = [];
	
	var courtSizePx = {"OFFENSE" : [536, 500]}
	
	var colorFill = {"BALL" : "#FF8133", "PLAYER_1" : "#7DFF6B", "PLAYER_2" : "#8C80FF"};
	var colorStroke = {"BALL" : "#FF6200", "PLAYER_1" : "#1EFF00", "PLAYER_2" : "#1900FF"};
	var size = {"BALL" : 10, "PLAYER_1" : 14, "PLAYER_2" : 14};
	
	function px(coordCm) {
		return courtSizePx[play.type][0] * coordCm / 1500
	}
	
	function py(coordCm) {
		return courtSizePx[play.type][1] * coordCm / 1400
	}
	
	function drawStep(step) {
		step.entites.forEach(function(entity, entityNo) {
			var g = diagramFrame.append("svg")
				.attr("class", "node")
				.attr("id", "se" + entity.id);
			g.append("circle")
				.attr("r", size[entity.type])
				.attr("cx", size[entity.type] + 2)
				.attr("cy", size[entity.type] + 2)
				.attr("stroke", colorStroke[entity.type])
				.attr("stroke-width", "2")
				.style("fill", colorFill[entity.type]);
			g.append("text")
				.attr("text-anchor", "middle")
				.attr("dy", size[entity.type] + 6)
				.attr("dx", size[entity.type] + 2)
				.attr("class", "label")
				.attr("stroke-width", "1")
				.attr("fill", "#000")
				.style("font-size", '10px')
				.text(entity.label)
			g.attr("x", px(entity.x)).attr("y", py(entity.y))
		});
	}
	
	this.init = function(data) {
		play = eval('('+data+')' );
		if(diagramFrame !== undefined) diagramFrame.remove()
		diagramFrame = d3.select(this.getElement()).append("svg:svg")
			.attr("width", courtSizePx[play.type][0])
			.attr("height", courtSizePx[play.type][1])
		diagramFrame.selectAll("*").remove();
		diagramFrame.append("svg:image")
		   .attr('width', courtSizePx[play.type][0])
		   .attr('height', courtSizePx[play.type][1])
		   .attr("xlink:href","img/court1.png")
		drawStep(play.steps[0]);
		connector.updateState("init", play.steps[0].order, play.steps[0].desc)
	},

	this.play = function(speed, delay) {
		diagramFrame.selectAll(".node").remove();
		drawStep(play.steps[0]);
		play.steps.forEach(function(step, stepNo) {
			if (stepNo != 0) {
				step.entites.forEach(function(entity, entityNo) {
					var selEnt = diagramFrame.select("#se" + entity.id);
					if (selEnt.empty()) {
						drawStep(step)
					} else {
						selEnt.transition()
							.delay(delay * 1000 * stepNo + speed * 1000 * (stepNo-1))
							.duration(speed * 1000)
							.attr("x", px(entity.x)).attr("y", py(entity.y));
					}
				});
			}
			var oper;
			if (play.steps.length == step.order) {
				oper= "end";
			} else { 
				oper= "step"
			}
			scheduledEvents.push(setTimeout(function() { connector.updateState(oper, step.order, step.desc); }, (delay + speed) * 1000 * stepNo));
		});

	},
	
	this.reset = function() {
		diagramFrame.selectAll(".node").remove();
		scheduledEvents.forEach(function(scheduledEvent, it) {
			clearTimeout(scheduledEvent);
		});
		scheduledEvents = [];
		drawStep(play.steps[0]);
		connector.updateState("init", play.steps[0].order, play.steps[0].desc);
	}

}