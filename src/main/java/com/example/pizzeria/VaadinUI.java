package com.example.pizzeria;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.pizzeria.dao.IngredientDAO;
import com.example.pizzeria.dao.PizzaDAO;
import com.example.pizzeria.model.Ingredient;
import com.example.pizzeria.model.Pizza;
import com.example.pizzeria.service.ingredient.IngredientService;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	IngredientDAO ingredientDao;
	@Autowired
	PizzaDAO pizzaDao;
	@Autowired
	IngredientService ingredientService;
	
	/*
	 * Grids
	 */
	Grid<Ingredient> gridIngredient = new Grid<>();
	Grid<Pizza> gridPizza = new Grid<>();
	

	/*
	 * LAYOUTS
	 */
	VerticalLayout ingredientAddition = new VerticalLayout();
	VerticalLayout ingredientElimination = new VerticalLayout();
	HorizontalLayout ingredientModifications = new HorizontalLayout();
	VerticalLayout ingredientContent = new VerticalLayout();

	VerticalLayout pizzaAddition = new VerticalLayout();
	VerticalLayout pizzaElimination = new VerticalLayout();
	HorizontalLayout pizzaModification = new HorizontalLayout();
	HorizontalLayout pizzaIngredientAddition = new HorizontalLayout();
	VerticalLayout pizzaIngredients = new VerticalLayout();
	VerticalLayout pizzaContent = new VerticalLayout();
	
	HorizontalLayout dataContent = new HorizontalLayout();
	VerticalLayout titleContent = new VerticalLayout();
	VerticalLayout refreshContent = new VerticalLayout();
	HorizontalLayout refreshHorizontalContent = new HorizontalLayout();
	HorizontalLayout headerContent = new HorizontalLayout();
	
	VerticalLayout webContent = new VerticalLayout();
	
	
	/*
	 * Buttons
	 */
	Button refresh = new Button("Actualizar las Listas.", this::refresh);
	Button addIngredientButton = new Button("Add ingredient", event -> addIngredient(event));
	Button deleteIngredientButton = new Button("Delete ingredient", event ->  deleteIngredient(event));
	Button addPizzaButton = new Button("Add pizza", event ->  savePizza(event));
	Button deletePizzaButton = new Button("Delete pizza", event ->  deletePizza(event));
	Button addIngredientToPizzaButton = new Button("Add ingredients", event ->  addIngredientToPizza(event));
	
	/*
	 * TextFields
	 */
	private TextField ingredientNameTF = new TextField("Ingredient name");
	private TextField pizzaNameTF = new TextField("Pizza name");
	private TextField pizzaIdTF = new TextField("Pizza id");
	private TextField pizzaIngredientIdTF = new TextField("Ingredient id");
	private TextField pizzaIdDeleteTF = new TextField("Pizza id");
	private TextField ingredientIdDeleteTF = new TextField("Ingredient id");

	@Override
	protected void init(VaadinRequest request) {
	
		buttonFormat();
		
		/*
		 * LAYOUTS
		 */
		setIngredientContent();
		setPizzaContent();
		setWebContent();

		/*
		 * GRIDS
		 */
		setGridIngredient();		
		setGridPizza();
		
		setContent(webContent);

	}

	private void buttonFormat()
	{
		addIngredientButton.setIcon(VaadinIcons.CHECK_CIRCLE);
		deleteIngredientButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
		addPizzaButton.setIcon(VaadinIcons.CHECK_CIRCLE);
		deletePizzaButton.setIcon(VaadinIcons.CLOSE_CIRCLE);
		addIngredientToPizzaButton.setIcon(VaadinIcons.CHECK_CIRCLE);
		refresh.setIcon(VaadinIcons.REFRESH);
		refresh.addStyleName(ValoTheme.BUTTON_PRIMARY);
	}
	
	private void setGridPizza() {
		gridPizza.addColumn(ingredient -> ingredient.getId()).setCaption("Pizza ID");
		gridPizza.addColumn(ingredient -> ingredient.getName()).setCaption("Name");
		gridPizza.addColumn(ingredient -> ingredient.ingredientsToString()).setCaption("Ingredients").setExpandRatio(1);
		listPizzas();
	}

	private void setGridIngredient() {
		gridIngredient.addColumn(ingredient -> ingredient.getId()).setCaption("Ingredient ID");
		gridIngredient.addColumn(ingredient -> ingredient.getName()).setCaption("Name");
		listIngredients();
	}

	private void setWebContent() {
		setDataContent();
		setTitleContent();
		setRefreshContent();
		setHeaderContent();
		
		webContent.addComponent(headerContent);
		webContent.addComponent(dataContent);
		//webContent.setComponentAlignment(headerContent, Alignment.TOP_CENTER);
		//webContent.setComponentAlignment(dataContent, Alignment.TOP_CENTER);
	}

	private void setHeaderContent() {
		headerContent.addComponent(titleContent);
		headerContent.addComponent(refreshContent);
	}

	private void setRefreshContent() {
		//refreshContent.setComponentAlignment(refreshHorizontalContent, Alignment.TOP_CENTER);
		refreshContent.addComponent(refreshHorizontalContent);
	}

	private void setTitleContent() {
		titleContent.addComponent(new Label("Bienvenido a Pizzería Borrego!"));
		titleContent.addComponent(refresh);
	}

	private void setDataContent() {
		dataContent.addComponent(pizzaContent);
		dataContent.addComponent(ingredientContent);
	}

	private void setPizzaContent() {
		
		setPizzaAddition();
		setPizzaElimination();
		setPizzaModification();
		setPizzaIngredientAddition();
		setPizzaIngredients();
		
		pizzaContent.addComponent(gridPizza);
		pizzaContent.addComponent(pizzaModification);
		pizzaContent.addComponent(pizzaIngredients);
	}

	private void setPizzaIngredients() {
		pizzaIngredients.addComponent(new Label("Añadir ingrediente a pizza"));
		pizzaIngredients.addComponent(pizzaIngredientAddition);
		pizzaIngredients.addComponent(addIngredientToPizzaButton);
	}

	private void setPizzaIngredientAddition() {
		pizzaIngredientAddition.addComponent(pizzaIdTF);
		pizzaIngredientAddition.addComponent(pizzaIngredientIdTF);
	}

	private void setPizzaModification() {
		pizzaModification.addComponent(pizzaAddition);
		pizzaModification.addComponent(pizzaElimination);
	}

	private void setPizzaElimination() {
		pizzaElimination.addComponent(new Label("Eliminar pizza"));
		pizzaElimination.addComponent(pizzaIdDeleteTF);
		pizzaElimination.addComponent(deletePizzaButton);
	}

	private void setPizzaAddition() {
		pizzaAddition.addComponent(new Label("Añadir pizza"));
		pizzaAddition.addComponent(pizzaNameTF);
		pizzaAddition.addComponent(addPizzaButton);
	}

	private void setIngredientContent() {
		
		setIngredientAddition();
		setIngredientElimination();
		setIngredientsModification();
		
		ingredientContent.addComponent(gridIngredient);
		ingredientContent.addComponent(ingredientModifications);
	}

	private void setIngredientsModification() {
		ingredientModifications.addComponent(ingredientAddition);
		ingredientModifications.addComponent(ingredientElimination);
	}
	
	private void setIngredientAddition() {
		ingredientAddition.addComponent(new Label("Añadir ingrediente"));
		ingredientAddition.addComponent(ingredientNameTF);
		ingredientAddition.addComponent(addIngredientButton);
	}

	private void setIngredientElimination() {
		ingredientElimination.addComponent(new Label("Eliminar ingrediente"));
		ingredientElimination.addComponent(ingredientIdDeleteTF);
		ingredientElimination.addComponent(deleteIngredientButton);
	}
	
	public void savePizza(ClickEvent clickEvent) {
		Pizza pizza = new Pizza();
		pizza.setName(pizzaNameTF.getValue());
		pizzaDao.save(pizza);
		this.refresh(clickEvent);
	}
	



	public void addIngredientToPizza(ClickEvent clickEvent) {
		Pizza pizza = pizzaDao.findOne(pizzaIdTF.getValue());
		Ingredient ingredient = ingredientDao.findOne(pizzaIngredientIdTF.getValue());
		pizza.getIngredients().add(ingredient);
		pizzaDao.save(pizza);
		this.refresh(clickEvent);
	}
	
	public void refresh(ClickEvent clickEvent) {
		listIngredients();
		listPizzas();
	}
	
	private void listIngredients() {
		gridIngredient.setItems(ingredientDao.findAll());
	}
	
	private void listPizzas() {
		gridPizza.setItems(pizzaDao.findAll());
	}
	
	private void addIngredient(ClickEvent clickEvent){
		Ingredient ingredient = new Ingredient();
		ingredient.setName(ingredientNameTF.getValue());
		ingredientDao.save(ingredient);
		this.refresh(clickEvent);
	}
	
	private void deleteIngredient(ClickEvent clickEvent)
	{

		ingredientDao.delete(ingredientIdDeleteTF.getValue());
		this.refresh(clickEvent);

	}

	private void deletePizza(ClickEvent clickEvent) 
	{

		pizzaDao.delete(pizzaIdDeleteTF.getValue());
		this.refresh(clickEvent);

	}
	
}
