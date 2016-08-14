package pl.horuss.bbplay.web.views;

import org.vaadin.hezamu.canvas.Canvas;
import org.vaadin.hezamu.canvas.Canvas.CanvasImageLoadListener;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Home", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class HomeView extends VerticalLayout implements View {

	private static final long serialVersionUID = 454182473938094294L;

	private final Canvas canvas;

	private final String[] compositeOps = { "source-over", "source-atop", "source-in",
			"source-out", "destination-atop", "destination-in", "destination-out",
			"destination-over", "lighter", "darker", "xor", "copy" };
	private int currentOpId = -1;
	private Label compositionName;

	public HomeView() {
		setSpacing(true);
		setMargin(true);

		Label header = new Label("BBPlay");
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);

		Label body = new Label("BBPlay stub with simple Canvas");
		body.setContentMode(ContentMode.HTML);
		addComponent(body);

		canvas = new Canvas();
		addComponent(canvas);

		canvas.setWidth("300px");
		canvas.setHeight("300px");

		canvas.addMouseDownListener(() -> System.out.println("Mouse down"));

		drawInitialPattern();

		final HorizontalLayout bs = new HorizontalLayout();
		addComponent(bs);
		addComponent(compositionName = new Label());
		setComponentAlignment(compositionName, Alignment.TOP_LEFT);

		bs.addComponent(new Button("Rectangles", event -> drawRects()));

		bs.addComponent(new Button("Text", event -> drawText()));

		bs.addComponent(new Button("Lines", event -> drawLines()));

		bs.addComponent(new Button("Gradients", event -> drawGradients()));
		bs.addComponent(new Button("Composition", event -> drawCompositions()));

		canvas.loadImages(new String[] {
				"http://webapp.org.ua/wp-content/uploads/2011/10/gwtlogo.jpg",
				"http://upload.wikimedia.org/wikipedia/commons/3/38/HTML5_Logo.svg",
				"http://jole.virtuallypreinstalled.com/paymate/img/vaadin-logo.png" });

		canvas.addImageLoadListener(new CanvasImageLoadListener() {
			public void imagesLoaded() {
				bs.addComponent(new Button("Images", event -> drawImages()));
			}
		});

	}

	private void drawInitialPattern() {
		canvas.saveContext();
		canvas.translate(175d, 175d);
		canvas.scale(1.6d, 1.6d);

		for (int i = 1; i < 6; ++i) {
			canvas.saveContext();
			canvas.setFillStyle("rgb(" + (51 * i) + "," + (255 - 51 * i) + ",255)");

			for (int j = 0; j < i * 6; ++j) {
				canvas.rotate((Math.PI * 2d / (i * 6)));
				canvas.beginPath();
				canvas.arc(0d, i * 12.5d, 5d, 0d, Math.PI * 2d, true);
				canvas.closePath();
				canvas.fill();
			}

			canvas.restoreContext();
		}

		canvas.closePath();

		canvas.restoreContext();
	}

	private void drawRects() {
		canvas.saveContext();
		canvas.clear();

		canvas.moveTo(0, 0);
		canvas.fillRect(10, 20, 50, 70);

		canvas.setStrokeStyle("rgb(25, 250, 150)");
		canvas.strokeRect(100, 10, 50, 50);

		canvas.beginPath();
		canvas.setStrokeStyle("rgb(255, 50, 150)");
		canvas.rect(100, 150, 50, 50);
		canvas.stroke();

		canvas.setStrokeStyle("rgb(125, 150, 255)");
		canvas.setGlobalAlpha(0.5);
		canvas.fillRect(30, 30, 100, 150);

		canvas.setGlobalAlpha(0.9);

		canvas.setFillStyle("blue");
		canvas.fillRect(280, 50, 75, 50);

		canvas.setFillStyle("yellow");
		canvas.fillRect(200, 200, 50, 50);

		canvas.transform(1, 0.5, -0.5, 1, 10, 10);
		canvas.setFillStyle("red");
		canvas.fillRect(200, 200, 50, 50);

		canvas.transform(1, 0.5, -0.5, 1, 10, 10);
		canvas.setFillStyle("blue");
		canvas.fillRect(200, 200, 50, 50);

		canvas.restoreContext();
	}

	private void drawText() {
		canvas.saveContext();
		canvas.clear();
		canvas.moveTo(0, 0);

		canvas.setTextBaseline("top");
		canvas.fillText("Text with TOP baseline", 10d, 200d, 0d);

		canvas.setTextBaseline("bottom");
		canvas.setFillStyle("rgb(0, 200, 0)");
		canvas.fillText("Text with BOTTOM baseline", 10d, 200d, 0d);

		canvas.restoreContext();
	}

	private void drawImages() {
		canvas.saveContext();
		canvas.clear();
		canvas.moveTo(0, 0);

		canvas.drawImage1("http://webapp.org.ua/wp-content/uploads/2011/10/gwtlogo.jpg", 0, 0);

		canvas.drawImage2("http://upload.wikimedia.org/wikipedia/commons/3/38/HTML5_Logo.svg", 50,
				50, 200, 200);

		canvas.drawImage3("http://jole.virtuallypreinstalled.com/paymate/img/vaadin-logo.png", 20,
				20, 100, 100, 100, 100, 50, 100);

		canvas.restoreContext();
	}

	private void drawLines() {
		canvas.saveContext();
		canvas.clear();

		canvas.beginPath();
		canvas.setLineWidth(10);
		canvas.setLineCap("round");
		canvas.setMiterLimit(1);
		canvas.moveTo(10, 50);
		canvas.lineTo(30, 150);
		canvas.lineTo(50, 50);
		canvas.stroke();
		canvas.closePath();

		canvas.beginPath();
		canvas.setLineWidth(5);
		canvas.setLineCap("butt");
		canvas.setLineJoin("round");
		canvas.setMiterLimit(1);
		canvas.moveTo(70, 50);
		canvas.lineTo(90, 150);
		canvas.lineTo(110, 50);
		canvas.stroke();
		canvas.closePath();

		canvas.beginPath();
		canvas.moveTo(20, 200);
		canvas.quadraticCurveTo(20, 275, 200, 200);
		canvas.stroke();

		canvas.restoreContext();
	}

	private void drawGradients() {
		canvas.saveContext();
		canvas.clear();

		canvas.createLinearGradient("g1", 0, 0, 170, 0);
		canvas.addColorStop("g1", 0, "black");
		canvas.addColorStop("g1", 1, "white");

		canvas.setGradientFillStyle("g1");
		canvas.fillRect(10, 10, 100, 50);

		canvas.createRadialGradient("g2", 75, 50, 5, 90, 60, 100);
		canvas.addColorStop("g2", 0, "red");
		canvas.addColorStop("g2", 1, "white");

		// Fill with gradient
		canvas.setGradientFillStyle("g2");
		canvas.fillRect(10, 100, 100, 50);

		canvas.createRadialGradient("g3", 115, 50, 5, 190, 60, 100);
		canvas.addColorStop("g3", 0, "green");
		canvas.addColorStop("g3", 1, "blue");

		canvas.beginPath();
		canvas.setGradientStrokeStyle("g3");
		canvas.setLineWidth(20);
		canvas.moveTo(170, 50);
		canvas.lineTo(190, 150);
		canvas.lineTo(210, 50);
		canvas.stroke();
		canvas.closePath();

		canvas.restoreContext();
	}

	private void drawCompositions() {
		if (++currentOpId == compositeOps.length)
			currentOpId = 0;

		canvas.saveContext();
		canvas.clear();

		compositionName.setValue(compositeOps[currentOpId]);

		// Draw destination shape
		canvas.setFillStyle("red");
		canvas.fillRect(10, 10, 100, 100);

		canvas.setGlobalCompositeOperation(compositeOps[currentOpId]);

		// Draw source shape
		canvas.setFillStyle("blue");
		canvas.fillRect(60, 60, 100, 100);

		canvas.restoreContext();
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
