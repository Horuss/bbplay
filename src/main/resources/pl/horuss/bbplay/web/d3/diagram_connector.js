window.pl_horuss_bbplay_web_d3_Diagram = function() {
	
	var connector = this;
	var diagramFrame;
	var play;
	var currentStep;
	var scheduledEvents = [];
	var editable = false;
	
	var courtSizePx = {"OFFENSE" : [536, 500], "DEFENSE" : [536, 500],
			"FULL_COURT" : [268, 500] }
	
	var colorFill = {"BALL" : "#FF8133", "PLAYER_1" : "#7DFF6B", "PLAYER_2" : "#8C80FF"};
	var colorStroke = {"BALL" : "#FF6200", "PLAYER_1" : "#1EFF00", "PLAYER_2" : "#1900FF"};
	var size = {"BALL" : 10, "PLAYER_1" : 14, "PLAYER_2" : 14};
	
	var drag = d3.drag()
    	.on("start", dragstarted)
    	.on("drag", dragged)
    	.on("end", dragended)
    
    function dragstarted(d) {
		d3.select(this).raise().classed("active", true);
	}

	function dragged(d) {
		d3.select(this).attr("x", d3.event.x).attr("y", d3.event.y);
	}

	function dragended(d) {
		var obj = d3.select(this);
		obj.classed("active", false);
		var id = obj.attr("id").substr(2);
		currentStep.entities.forEach(function(entity, entityNo) {
			if (entity.entityId == id) {
				obj.attr("x", obj.attr("x") - size[entity.type] - 2)
				obj.attr("y", obj.attr("y") - size[entity.type] - 2)
				entity.x = cx(parseFloat(obj.attr("x")) + size[entity.type] + 2)
				entity.y = cy(parseFloat(obj.attr("y")) + size[entity.type] + 2)
			}
		})
		connector.updatePlay(play); 
	}
	
	function px(coordCm) {
		var sizeOfCourt = 1500;
		if (play.type == "FULL_COURT") {
			sizeOfCourt *= 2
		}
		return courtSizePx[play.type][0] * coordCm / sizeOfCourt
	}
	
	function py(coordCm) {
		var sizeOfCourt = 1400;
		if (play.type == "FULL_COURT") {
			sizeOfCourt *= 2
		}
		return courtSizePx[play.type][1] * coordCm / sizeOfCourt
	}
	
	function cx(coordPx) {
		var sizeOfCourt = 1500;
		if (play.type == "FULL_COURT") {
			sizeOfCourt *= 2
		}
		return Math.round(sizeOfCourt * coordPx / courtSizePx[play.type][0])
	}
	
	function cy(coordPx) {
		var sizeOfCourt = 1400;
		if (play.type == "FULL_COURT") {
			sizeOfCourt *= 2
		}
		return Math.round(sizeOfCourt * coordPx / courtSizePx[play.type][1])
	}
	
	function drawStep(step) {
		diagramFrame.selectAll(".node").remove();
		currentStep = step;
		step.entities.forEach(function(entity, entityNo) {
			var g = diagramFrame.append("svg")
				.attr("class", "node")
				.attr("id", "se" + entity.entityId);
			if (editable) {
				g.call(drag);
			}
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
				.style("font-size", '15px')
				.style("font-weight", 'bold')
				.style("fill", '#555')
				.text(entity.label)
			g.attr("x", px(entity.x) - size[entity.type] - 2).attr("y", py(entity.y) - size[entity.type] - 2)
		});
		
		var oper;
		if (play.steps.length == step.order) {
			oper= "end";
		} else if (1 == step.order) {
			oper= "init"
		} else { 
			oper= "step"
		}
		connector.updateState(oper, step.order, step.desc);
	}
	
	this.init = function(data, edit) {
		editable = edit;
		play = eval('('+data+')' );
		if(diagramFrame !== undefined) diagramFrame.remove()
		diagramFrame = d3.select(this.getElement()).append("svg:svg")
			.attr("width", courtSizePx[play.type][0])
			.attr("height", courtSizePx[play.type][1])
		diagramFrame.selectAll("*").remove();
		if (play.type == "OFFENSE") {
			diagramFrame.append("svg:image")
			   .attr('width', courtSizePx[play.type][0])
			   .attr('height', courtSizePx[play.type][1])
			   .attr("xlink:href","VAADIN/img/court1.png")
		} else if (play.type == "DEFENSE") {
			diagramFrame.append("svg:image")
			   .attr('width', courtSizePx[play.type][0])
			   .attr('height', courtSizePx[play.type][1])
			   .attr("xlink:href","VAADIN/img/court1.png")
			   .attr('y', -courtSizePx[play.type][1])
			   .attr('transform', 'scale(1,-1)')
		} else if (play.type == "FULL_COURT") {
			diagramFrame.append("svg:image")
			   .attr('width', courtSizePx[play.type][0])
			   .attr('height', courtSizePx[play.type][1] / 2)
			   .attr("xlink:href","VAADIN/img/court1.png")
			diagramFrame.append("svg:image")
			   .attr('width', courtSizePx[play.type][0])
			   .attr('height', courtSizePx[play.type][1] / 2)
			   .attr('y', -courtSizePx[play.type][1])
			   .attr('transform', 'scale(1,-1)')
			   .attr("xlink:href","VAADIN/img/court1.png");
			diagramFrame.append("line")
				.attr("x1", 0)
				.attr("y1", courtSizePx[play.type][1] / 2)
				.attr("x2", courtSizePx[play.type][0])
				.attr("y2", courtSizePx[play.type][1] / 2)
				.attr("stroke-width", 2)
				.attr("stroke", "black");
		}
	},
	
	this.draw = function(step) {
		drawStep(play.steps[step]);
	},

	this.play = function(speed, delay) {
		diagramFrame.selectAll(".node").remove();
		drawStep(play.steps[0]);
		play.steps.forEach(function(step, stepNo) {
			if (stepNo != 0) {
				step.entities.forEach(function(entity, entityNo) {
					var selEnt = diagramFrame.select("#se" + entity.entityId);
					if (selEnt.empty()) {
						// draw new entity
					} else {
						selEnt.transition()
							.delay(delay * 1000 * stepNo + speed * 1000 * (stepNo-1))
							.duration(speed * 1000)
							.attr("x", px(entity.x) - size[entity.type] - 2).attr("y", py(entity.y) - size[entity.type] - 2);
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
		connector.updateState("init", play.steps[0].order, play.steps[0].desc);
	}

}