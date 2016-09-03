package pl.horuss.bbplay.web.views;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.d3.Diagram;
import pl.horuss.bbplay.web.json.AnnotationExclusionStrategy;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.model.Step;
import pl.horuss.bbplay.web.model.StepEntity;
import pl.horuss.bbplay.web.parts.ConfirmWindow;
import pl.horuss.bbplay.web.parts.EditPlayWindow;
import pl.horuss.bbplay.web.services.PlaybookService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Secured("ROLE_ADMIN")
@SpringView(name = "playbook-edit")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Playbook Edit", order = 3)
@FontAwesomeIcon(FontAwesome.PLAY_CIRCLE_O)
public class PlaybookEditView extends VerticalLayout implements View {

	private static final long serialVersionUID = 5633848238589020925L;

	private final PlaybookService playbookService;
	private final Gson gson = new GsonBuilder().setExclusionStrategies(new AnnotationExclusionStrategy()).create();

	private Diagram diagram;
	
	private Play selectedPlay;

	@Autowired
	public PlaybookEditView(PlaybookService playbookService) {
		this.playbookService = playbookService;

		setSpacing(true);
		setMargin(true);

		HorizontalLayout main = new HorizontalLayout();

		VerticalLayout left = new VerticalLayout();
		left.setWidth("250px");
		left.setMargin(true);
		left.setSpacing(true);

		VerticalLayout right = new VerticalLayout();
		right.setMargin(true);
		right.setSpacing(true);
		right.setVisible(false);

		VerticalLayout stepsContainer = new VerticalLayout();
		stepsContainer.setSpacing(true);
		stepsContainer.setVisible(false);

		BeanItemContainer<Play> container = new BeanItemContainer<Play>(Play.class,
				this.playbookService.getPlays());

		Grid gridPlays = new Grid(container);
		gridPlays.setWidth("100%");
		gridPlays.setHeight("200px");
		gridPlays.setColumns("name", "call");
		gridPlays.setSelectionMode(SelectionMode.SINGLE);

		gridPlays.addItemClickListener(event -> {
			if (event.isDoubleClick()) {
				Play bean = (Play) event.getItemId();
				EditPlayWindow editPlayWindow = new EditPlayWindow(playbookService, bean);
				editPlayWindow.addCloseListener(e -> {
					if (editPlayWindow.getSavedModel() != null) {
						gridPlays.clearSortOrder();
					}
				});
				UI.getCurrent().addWindow(editPlayWindow);
			}
		});

		HorizontalLayout playsButtons = new HorizontalLayout();
		playsButtons.setSpacing(true);
		Button addPlay = new Button("Add");
		addPlay.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addPlay.addClickListener(event -> {
			EditPlayWindow editPlayWindow = new EditPlayWindow(playbookService, new Play());
			editPlayWindow.addCloseListener(e -> {
				if (editPlayWindow.getSavedModel() != null) {
					container.addItem(editPlayWindow.getSavedModel());
					gridPlays.clearSortOrder();
				}
			});
			UI.getCurrent().addWindow(editPlayWindow);
		});
		Button removePlay = new Button("Remove");
		removePlay.setEnabled(false);
		removePlay.addClickListener(event -> {
			ConfirmWindow.show("Confirm", null, "Are you sure?",
					result -> {
						if (result) {
							if (selectedPlay.isPersist()) {
								playbookService.delete(selectedPlay);
							}
							gridPlays.select(null);
							gridPlays.getContainerDataSource().removeItem(selectedPlay);
							gridPlays.clearSortOrder();
						}
					});
		});
		playsButtons.addComponent(addPlay);
		playsButtons.addComponent(removePlay);

		Grid gridSteps = new Grid();
		gridSteps.setEditorEnabled(true);
		gridSteps.setWidth("100%");
		gridSteps.setHeight("300px");
		gridSteps.setSelectionMode(SelectionMode.SINGLE);

		HorizontalLayout stepsButtons = new HorizontalLayout();
		stepsButtons.setSpacing(true);
		Button addStep = new Button("Add");
		addStep.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addStep.addClickListener(event -> {
			//TODO fill step with entites from previous one
			gridSteps.getContainerDataSource().addItem(new Step());
			gridSteps.clearSortOrder();
		});
		Button removeStep = new Button("Remove");
		removeStep.setEnabled(false);
		removeStep.addClickListener(event -> {
			ConfirmWindow.show("Confirm", null, "Are you sure?",
					result -> {
						if (result) {
							Step bean = (Step) gridSteps.getSelectionModel().getSelectedRows()
									.toArray()[0];
							gridSteps.select(null);
							gridSteps.getContainerDataSource().removeItem(bean);
							gridSteps.clearSortOrder();
						}
					});
		});
		stepsButtons.addComponent(addStep);
		stepsButtons.addComponent(removeStep);

		stepsContainer.addComponent(stepsButtons);
		stepsContainer.addComponent(gridSteps);

		gridPlays.addSelectionListener(event -> {
			Collection<Object> selectedRows = gridPlays.getSelectionModel().getSelectedRows();
			if (selectedRows != null && !selectedRows.isEmpty()) {
				selectedPlay = (Play) selectedRows.toArray()[0];
				gridSteps.setContainerDataSource(new BeanItemContainer<Step>(Step.class,
						selectedPlay.getSteps()));
				gridSteps.setColumns("order", "desc");
				String jsonSelectedPlay = gson.toJson(selectedPlay);
				diagram.init(jsonSelectedPlay, true);
				stepsContainer.setVisible(true);
				removePlay.setEnabled(true);
				right.setVisible(true);
			} else {
				selectedPlay = null;
				stepsContainer.setVisible(false);
				removePlay.setEnabled(false);
				right.setVisible(false);
			}
		});

		gridSteps.addSelectionListener(event -> {
			Collection<Object> selectedRows = gridSteps.getSelectionModel().getSelectedRows();
			if (selectedRows != null && !selectedRows.isEmpty()) {
				Step selectedStep = (Step) selectedRows.toArray()[0];
				diagram.reset();
				diagram.draw(selectedStep.getOrder() - 1);
				removeStep.setEnabled(true);
			} else {
				removeStep.setEnabled(false);
			}
		});

		left.addComponent(playsButtons);
		left.addComponent(gridPlays);
		left.addComponent(stepsContainer);

		main.addComponent(left);

		HorizontalLayout diagramButtons = new HorizontalLayout();
		diagramButtons.setSpacing(true);
		Button savePlayChanges = new Button("Save changes");
		savePlayChanges.addStyleName(ValoTheme.BUTTON_PRIMARY);
		savePlayChanges.addClickListener(event -> {
			if (diagram.getUpdatedPlay() != null) {
				// filling steps and play reference due to JSON serialization
				for (Step step : diagram.getUpdatedPlay().getSteps()) {
					step.setPlay(selectedPlay);
					for (StepEntity entity : step.getEntities()) {
						entity.setStep(step);
					}
				}
				selectedPlay.setSteps(diagram.getUpdatedPlay().getSteps());
			}
			playbookService.save(selectedPlay);
			BBPlay.info("Saved changes!");
		});
		diagramButtons.addComponent(savePlayChanges);
		right.addComponent(diagramButtons);
		diagram = new Diagram(this);
		diagram.addStyleName("diagram");
		right.addComponent(diagram);

		main.addComponent(right);

		addComponent(main);
		
		//TODO add new entites

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
