package pl.horuss.bbplay.web.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
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
	private final Gson gson = new GsonBuilder().setExclusionStrategies(
			new AnnotationExclusionStrategy()).create();

	private Diagram diagram;

	private Play selectedPlay;
	private Step selectedStep;

	@SuppressWarnings("unchecked")
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

		final FormLayout addNewControls = new FormLayout();
		addNewControls.setVisible(false);

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
			ConfirmWindow.show("Confirm", null, "Are you sure?", result -> {
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
			List<Step> steps = new ArrayList<Step>((Collection<? extends Step>) gridSteps
					.getContainerDataSource().getItemIds());
			Step newStep = new Step();
			if (!steps.isEmpty()) {
				Collections.sort(steps);
				Step lastStep = steps.get(steps.size() - 1);
				newStep.setOrder(steps.get(steps.size() - 1).getOrder() + 1);
				newStep.setPlay(selectedPlay);
				for (StepEntity entity : lastStep.getEntities()) {
					StepEntity newEntity = new StepEntity(entity);
					newEntity.setStep(newStep);
					newStep.getEntities().add(newEntity);
				}
			} else {
				newStep.setOrder(1);
				newStep.setPlay(selectedPlay);
			}
			selectedPlay.getSteps().add(newStep);
			gridSteps.getContainerDataSource().addItem(newStep);
			gridSteps.clearSortOrder();
			String jsonSelectedPlay = gson.toJson(selectedPlay);
			diagram.init(jsonSelectedPlay, true);
			gridSteps.select(null);
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
							String jsonSelectedPlay = gson.toJson(selectedPlay);
							diagram.init(jsonSelectedPlay, true);
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
				selectedStep = (Step) selectedRows.toArray()[0];
				diagram.reset();
				diagram.draw(selectedStep.getOrder() - 1);
				removeStep.setEnabled(true);
				addNewControls.setVisible(true);
			} else {
				selectedStep = null;
				removeStep.setEnabled(false);
				addNewControls.setVisible(false);
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
			List<Step> steps = new ArrayList<Step>((Collection<? extends Step>) gridSteps
					.getContainerDataSource().getItemIds());
			if (diagram.getUpdatedPlay() != null) {
				for (Step step : steps) {
					for (Step updatedStep : diagram.getUpdatedPlay().getSteps()) {
						if (updatedStep.getOrder() == step.getOrder()) {
							for (StepEntity entity : updatedStep.getEntities()) {
								entity.setStep(step);
							}
							step.setEntities(updatedStep.getEntities());
						}
					}
				}
			}
			selectedPlay.setSteps(steps);
			playbookService.save(selectedPlay);
			gridSteps.setContainerDataSource(new BeanItemContainer<Step>(Step.class, selectedPlay
					.getSteps()));
			BBPlay.info("Saved changes!");
		});
		diagramButtons.addComponent(savePlayChanges);
		right.addComponent(diagramButtons);
		diagram = new Diagram(this);
		diagram.addStyleName("diagram");
		right.addComponent(diagram);

		main.addComponent(right);

		// TODO move to another component created on demand - adding multiple is
		// not working on same instance
		StepEntity newEntity = new StepEntity();
		FieldGroup fieldGroup = new BeanFieldGroup<StepEntity>(StepEntity.class);
		fieldGroup.setItemDataSource(new BeanItem<StepEntity>(newEntity));
		addNewControls.addComponent(fieldGroup.buildAndBind("Label", "label"));
		Field<?> typeField = fieldGroup.buildAndBind("Type", "type");
		typeField.setRequired(true);
		addNewControls.addComponent(typeField);

		Button addNew = new Button("Add new");
		addNew.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addNew.addClickListener(event -> {
			try {
				fieldGroup.commit();
				newEntity.setStep(selectedStep);
				// TODO this id gen will not work with removing entities
				long max = 0;
				for (StepEntity e : selectedStep.getEntities()) {
					if (e.getEntityId() > max) {
						max = e.getEntityId();
					}
				}
				newEntity.setEntityId(max + 1);
				newEntity.setX(100);
				newEntity.setY(100);
				// TODO need to store first? moved changes not saved when adding
				// new
				selectedStep.getEntities().add(newEntity);
				fieldGroup.clear();
				String jsonSelectedPlay = gson.toJson(selectedPlay);
				diagram.init(jsonSelectedPlay, true);
				diagram.draw(selectedStep.getOrder() - 1);
			} catch (CommitException e) {
				BBPlay.error("Failed to add");
			}

		});
		addNewControls.addComponent(addNew);

		main.addComponent(addNewControls);

		addComponent(main);

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
