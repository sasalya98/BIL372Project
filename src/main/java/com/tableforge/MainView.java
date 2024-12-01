package com.tableforge;

import com.tableforge.models.*;
import com.tableforge.queries.BaseQueries;
import com.tableforge.queries.DivisionQueries;
import com.tableforge.queries.MilitaryEventQueries;
import com.tableforge.queries.MissionQueries;
import com.tableforge.queries.OperationQueries;
import com.tableforge.queries.Queries;
import com.tableforge.queries.SoldierFamilyQueries;
import com.tableforge.queries.SoldierQueries;
import com.tableforge.queries.VehicleQueries;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainView extends VBox {

    private ComboBox<String> tableSelector;
    private TextField searchField;
    private TableView<ObservableList<String>> tableView;
    private ObservableList<ObservableList<String>> tableData;
    private HBox buttonLayout;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Label statusLabel;

		// Soldier filter controls
		private CheckBox isSoldierAliveCheckBox;
		private CheckBox veteranStatusCheckBox;
		private TextField soldierIdFilterField;
		private TextField soldierDivisionIdField;
		private DatePicker soldierBirthDateAfterPicker;
		private DatePicker soldierBirthDateBeforePicker;

		// Base filter controls
		private TextField baseIdField;
		private TextField baseCommanderIdField;
		private ComboBox<String> baseCapacityInequalityComboBox;//yeni
		private TextField baseCapacityField;
		private ComboBox<String> baseExpensesInequalityComboBox;//yeni
		private TextField baseExpensesField;

		// Division filter controls
		private TextField divisionIdField;
		private TextField divisionCommanderIdField;
		private ComboBox<String> divisionQuotaInequalityComboBox;//yeni
		private TextField divisionQuotaField;
		private TextField divisionBaseIdField;
		private TextField divisionSuperDivisionIdField;

		// Vehicle filter controls
		private TextField vehicleIdField;
		private CheckBox isOperationalCheckBox;
		private ComboBox<String> damageExpensesInequalityComboBox;//yeni
		private TextField damageExpensesField;
		private TextField vehicleBaseIdField;
		private TextField vehicleMissionIdField;

		// Soldier Family filter controls
		private TextField soldierFamilyIdField;
		private TextField soldierFamilySoldierIdField;

		// Operation filter controls
		private TextField operationIdField;
		private TextField operationDivisionIdField;
		private DatePicker operationStartDateAfterPicker;
		private DatePicker operationStartDateBeforePicker;
		private DatePicker operationEndDateAfterPicker;
		private DatePicker operationEndDateBeforePicker;

		// Mission filter controls
		private TextField missionIdField;
		private TextField missionDivisionIdField;
		private TextField missionOperationIdField;
		private DatePicker missionStartDateAfterPicker;
		private DatePicker missionStartDateBeforePicker;
		private DatePicker missionEndDateAfterPicker;
		private DatePicker missionEndDateBeforePicker;

		// Military Event filter controls
		private TextField militaryEventIdField;
		private TextField militaryEventOperationIdField;
		private TextField militaryEventMissionIdField;
		private DatePicker militaryEventStartDateAfterPicker;
		private DatePicker militaryEventStartDateBeforePicker;
		private DatePicker militaryEventEndDateAfterPicker;
		private DatePicker militaryEventEndDateBeforePicker;

		// Filter layouts
		private HBox soldierFilterLayout;
		private HBox soldierDateFilterLayout;
		private HBox baseFilterLayout;
		private HBox divisionFilterLayout;
		private HBox vehicleFilterLayout;
		private HBox soldierFamilyFilterLayout;
		private HBox operationFilterLayout;
		private HBox operationDateFilterLayout;
		private HBox missionFilterLayout;
		private HBox missionDateFilterLayout;
		private HBox militaryEventFilterLayout;
		private HBox militaryEventDateFilterLayout;

		// Filter layouts with date pickers
		private VBox soldierFilterLayoutWithDate;
		private VBox operationFilterLayoutWithDate;
		private VBox missionFilterLayoutWithDate;
		private VBox militaryEventFilterLayoutWithDate;


    public MainView() {
        initialize();
    }

    private void initialize() {
        tableSelector = new ComboBox<>();
        tableSelector.setPromptText("Select a table...");

        searchField = new TextField();
        searchField.setPromptText("Search...");

        buttonLayout = new HBox(10);
        addButton = new Button("Add");
        editButton = new Button("Edit");
        deleteButton = new Button("Delete");

        buttonLayout.getChildren().addAll(addButton, editButton, deleteButton);

        tableView = new TableView<>();

				// Initialize filter controls
				
				// Soldier filter controls
        isSoldierAliveCheckBox = new CheckBox("Is Alive");
        veteranStatusCheckBox = new CheckBox("Veteran Status");
        soldierIdFilterField = new TextField();
        soldierIdFilterField.setPromptText("ID");
        FormatText.restrictToNumeric(soldierIdFilterField);

        isSoldierAliveCheckBox.setAllowIndeterminate(true);
        veteranStatusCheckBox.setAllowIndeterminate(true);

        isSoldierAliveCheckBox.setIndeterminate(true);
        veteranStatusCheckBox.setIndeterminate(true);

        isSoldierAliveCheckBox.setOnAction(event -> reloadData());
        veteranStatusCheckBox.setOnAction(event -> reloadData());
        soldierIdFilterField.setOnAction(event -> reloadData());
				soldierIdFilterField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());
				
				soldierDivisionIdField = new TextField();
    		soldierDivisionIdField.setPromptText("Division ID");
    		FormatText.restrictToNumeric(soldierDivisionIdField); 

				soldierDivisionIdField.setOnAction(event -> reloadData());
				soldierDivisionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				soldierBirthDateAfterPicker = new DatePicker();
				soldierBirthDateAfterPicker.setPromptText("To Birth Date");
				soldierBirthDateAfterPicker.setOnAction(event -> reloadData());

				soldierBirthDateBeforePicker = new DatePicker();
				soldierBirthDateBeforePicker.setPromptText("From Birth Date");
				soldierBirthDateBeforePicker.setOnAction(event -> reloadData());

				soldierDateFilterLayout = new HBox(10);
				soldierDateFilterLayout.getChildren().addAll(
						new Label("From Birth Date:"), soldierBirthDateBeforePicker,
						new Label("To Birth Date:"), soldierBirthDateAfterPicker
				);

        soldierFilterLayout = new HBox(10);
				soldierFilterLayout.getChildren().addAll(
					isSoldierAliveCheckBox,
						veteranStatusCheckBox, new Label("ID:"),
						soldierIdFilterField,
						new Label("Division ID:"),
						soldierDivisionIdField
				);

				soldierFilterLayoutWithDate = new VBox();
				soldierFilterLayoutWithDate.getChildren().addAll(soldierFilterLayout, soldierDateFilterLayout);

				// Base filter controls
				baseIdField = new TextField();
				baseIdField.setPromptText("ID");
				FormatText.restrictToNumeric(baseIdField);

				baseIdField.setOnAction(event -> reloadData());
				baseIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				baseCommanderIdField = new TextField();
				baseCommanderIdField.setPromptText("Commander ID");
				FormatText.restrictToNumeric(baseCommanderIdField);

				baseCommanderIdField.setOnAction(event -> reloadData());
				baseCommanderIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				// Inequality ComboBox//yeni
				baseCapacityInequalityComboBox = new ComboBox<>();
				baseCapacityInequalityComboBox.getItems().addAll("<", "<=", "=",">", ">=");
				baseCapacityInequalityComboBox.setValue("<"); // Varsayılan değer
				baseCapacityInequalityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> reloadData());

				baseCapacityField = new TextField();
				baseCapacityField.setPromptText("Capacity");
				FormatText.restrictToNumeric(baseCapacityField);

				baseCapacityField.setOnAction(event -> reloadData());
				baseCapacityField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				// Inequality ComboBox//yeni
				baseExpensesInequalityComboBox = new ComboBox<>();
				baseExpensesInequalityComboBox.getItems().addAll("<", "<=", "=",">", ">=");
				baseExpensesInequalityComboBox.setValue("<"); // Varsayılan değer
				baseExpensesInequalityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> reloadData());

				baseExpensesField = new TextField();
				baseExpensesField.setPromptText("Expenses");
				FormatText.restrictToDecimal(baseExpensesField);

				baseExpensesField.setOnAction(event -> reloadData());
				baseExpensesField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				baseFilterLayout = new HBox(10);
				baseFilterLayout.getChildren().addAll(new Label("ID:"), baseIdField, new Label("Commander ID:"),
						baseCommanderIdField, new Label("Capacity:"), baseCapacityInequalityComboBox,baseCapacityField, new Label("Expenses:"), baseExpensesInequalityComboBox,baseExpensesField);

				// Division filter controls
				divisionIdField = new TextField();
				divisionIdField.setPromptText("ID");
				FormatText.restrictToNumeric(divisionIdField);

				divisionIdField.setOnAction(event -> reloadData());
				divisionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionCommanderIdField = new TextField();
				divisionCommanderIdField.setPromptText("Commander ID");
				FormatText.restrictToNumeric(divisionCommanderIdField);

				divisionCommanderIdField.setOnAction(event -> reloadData());
				divisionCommanderIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionQuotaInequalityComboBox = new ComboBox<>();
				divisionQuotaInequalityComboBox.getItems().addAll("<", "<=", "=",">", ">=");
				divisionQuotaInequalityComboBox.setValue("<");
				divisionQuotaInequalityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionQuotaField = new TextField();
				divisionQuotaField.setPromptText("Quota");
				FormatText.restrictToNumeric(divisionQuotaField);

				divisionQuotaField.setOnAction(event -> reloadData());
				divisionQuotaField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionBaseIdField = new TextField();
				divisionBaseIdField.setPromptText("Base ID");
				FormatText.restrictToNumeric(divisionBaseIdField);

				divisionBaseIdField.setOnAction(event -> reloadData());
				divisionBaseIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionSuperDivisionIdField = new TextField();
				divisionSuperDivisionIdField.setPromptText("Super Division ID");
				FormatText.restrictToNumeric(divisionSuperDivisionIdField);

				divisionSuperDivisionIdField.setOnAction(event -> reloadData());
				divisionSuperDivisionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				divisionFilterLayout = new HBox(10);
				divisionFilterLayout.getChildren().addAll(new Label("ID:"), divisionIdField, new Label("Commander ID:"), divisionCommanderIdField,
						new Label("Quota:"), divisionQuotaInequalityComboBox, divisionQuotaField, new Label("Base ID:"), divisionBaseIdField,
						new Label("Super Division ID:"), divisionSuperDivisionIdField);

				// Vehicle filter controls
				vehicleIdField = new TextField();
				vehicleIdField.setPromptText("ID");
				FormatText.restrictToNumeric(vehicleIdField);

				vehicleIdField.setOnAction(event -> reloadData());
				vehicleIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				isOperationalCheckBox = new CheckBox("Is Operational");
				isOperationalCheckBox.setAllowIndeterminate(true);
				isOperationalCheckBox.setIndeterminate(true);
				isOperationalCheckBox.setOnAction(event -> reloadData());
        
				// Inequality ComboBox//yeni
				damageExpensesInequalityComboBox = new ComboBox<>();
				damageExpensesInequalityComboBox.getItems().addAll("<", "<=", "=",">", ">=");
				damageExpensesInequalityComboBox.setValue("<"); // Varsayılan değer
				damageExpensesInequalityComboBox.valueProperty().addListener((observable, oldValue, newValue) -> reloadData());

				damageExpensesField = new TextField();
				damageExpensesField.setPromptText("Damage Expenses");

				damageExpensesField.setOnAction(event -> reloadData());
				damageExpensesField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				vehicleBaseIdField = new TextField();
				vehicleBaseIdField.setPromptText("Base ID");
				FormatText.restrictToNumeric(vehicleBaseIdField);

				vehicleBaseIdField.setOnAction(event -> reloadData());
				vehicleBaseIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				vehicleMissionIdField = new TextField();
				vehicleMissionIdField.setPromptText("Mission ID");
				FormatText.restrictToNumeric(vehicleMissionIdField);

				vehicleMissionIdField.setOnAction(event -> reloadData());
				vehicleMissionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				vehicleFilterLayout = new HBox(10);
				vehicleFilterLayout.getChildren().addAll(new Label("ID:"), vehicleIdField, isOperationalCheckBox,
						new Label("Damage Expenses:"), damageExpensesInequalityComboBox, damageExpensesField, new Label("Base ID:"), vehicleBaseIdField,
						new Label("Mission ID:"), vehicleMissionIdField);

				// Soldier Family filter controls
				soldierFamilyIdField = new TextField();
				soldierFamilyIdField.setPromptText("ID");
				FormatText.restrictToNumeric(soldierFamilyIdField);

				soldierFamilyIdField.setOnAction(event -> reloadData());
				soldierFamilyIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				soldierFamilySoldierIdField = new TextField();
				soldierFamilySoldierIdField.setPromptText("Soldier ID");
				FormatText.restrictToNumeric(soldierFamilySoldierIdField);

				soldierFamilySoldierIdField.setOnAction(event -> reloadData());
				soldierFamilySoldierIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				soldierFamilyFilterLayout = new HBox(10);
				soldierFamilyFilterLayout.getChildren().addAll(new Label("ID:"), soldierFamilyIdField, new Label("Soldier ID:"),
						soldierFamilySoldierIdField);

				// Operation filter controls
				operationIdField = new TextField();
				operationIdField.setPromptText("ID");
				FormatText.restrictToNumeric(operationIdField);

				operationIdField.setOnAction(event -> reloadData());
				operationIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				operationDivisionIdField = new TextField();
				operationDivisionIdField.setPromptText("Division ID");
				FormatText.restrictToNumeric(operationDivisionIdField);

				operationDivisionIdField.setOnAction(event -> reloadData());
				operationDivisionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				operationStartDateAfterPicker = new DatePicker();
				operationStartDateAfterPicker.setPromptText("To Start Date");
				operationStartDateAfterPicker.setOnAction(event -> reloadData());

				operationStartDateBeforePicker = new DatePicker();
				operationStartDateBeforePicker.setPromptText("From Start Date");
				operationStartDateBeforePicker.setOnAction(event -> reloadData());

				operationEndDateAfterPicker = new DatePicker();
				operationEndDateAfterPicker.setPromptText("To End Date");
				operationEndDateAfterPicker.setOnAction(event -> reloadData());

				operationEndDateBeforePicker = new DatePicker();
				operationEndDateBeforePicker.setPromptText("From End Date");
				operationEndDateBeforePicker.setOnAction(event -> reloadData());

				operationDateFilterLayout = new HBox(10);
				operationDateFilterLayout.getChildren().addAll(
						new Label("From Start Date:"), operationStartDateBeforePicker,
						new Label("To Start Date:"), operationStartDateAfterPicker,
						new Label("From End Date:"), operationEndDateBeforePicker,
						new Label("To End Date:"), operationEndDateAfterPicker
				);

				operationFilterLayout = new HBox(10);
				operationFilterLayout.getChildren().addAll(
						new Label("ID:"),
						operationIdField,
						new Label("Division ID:"),
						operationDivisionIdField
				);

				operationFilterLayoutWithDate = new VBox();
				operationFilterLayoutWithDate.getChildren().addAll(operationFilterLayout, operationDateFilterLayout);

				// Mission filter controls
				missionIdField = new TextField();
				missionIdField.setPromptText("ID");
				FormatText.restrictToNumeric(missionIdField);

				missionIdField.setOnAction(event -> reloadData());
				missionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				missionDivisionIdField = new TextField();
				missionDivisionIdField.setPromptText("Division ID");
				FormatText.restrictToNumeric(missionDivisionIdField);

				missionDivisionIdField.setOnAction(event -> reloadData());
				missionDivisionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				missionOperationIdField = new TextField();
				missionOperationIdField.setPromptText("Operation ID");
				FormatText.restrictToNumeric(missionOperationIdField);

				missionOperationIdField.setOnAction(event -> reloadData());
				missionOperationIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				missionStartDateAfterPicker = new DatePicker();
				missionStartDateAfterPicker.setPromptText("To Start Date");
				missionStartDateAfterPicker.setOnAction(event -> reloadData());

				missionStartDateBeforePicker = new DatePicker();
				missionStartDateBeforePicker.setPromptText("From Start Date");
				missionStartDateBeforePicker.setOnAction(event -> reloadData());

				missionEndDateAfterPicker = new DatePicker();
				missionEndDateAfterPicker.setPromptText("End Date After");
				missionEndDateAfterPicker.setOnAction(event -> reloadData());

				missionEndDateBeforePicker = new DatePicker();
				missionEndDateBeforePicker.setPromptText("End Date Before");
				missionEndDateBeforePicker.setOnAction(event -> reloadData());

				missionDateFilterLayout = new HBox(10);
				missionDateFilterLayout.getChildren().addAll(
						new Label("From Start Date:"), missionStartDateBeforePicker,
						new Label("To Start Date:"), missionStartDateAfterPicker,
						new Label("From End Date:"), missionEndDateBeforePicker,
						new Label("To End Date:"), missionEndDateAfterPicker
				);

				missionFilterLayout = new HBox(10);
				missionFilterLayout.getChildren().addAll(
						new Label("ID:"),
						missionIdField,
						new Label("Division ID:"),
						missionDivisionIdField,
						new Label("Operation ID:"), missionOperationIdField
				);

				missionFilterLayoutWithDate = new VBox();
				missionFilterLayoutWithDate.getChildren().addAll(missionFilterLayout, missionDateFilterLayout);

				// Military Event filter controls
				militaryEventIdField = new TextField();
				militaryEventIdField.setPromptText("ID");
				FormatText.restrictToNumeric(militaryEventIdField);

				militaryEventIdField.setOnAction(event -> reloadData());
				militaryEventIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				militaryEventOperationIdField = new TextField();
				militaryEventOperationIdField.setPromptText("Operation ID");
				FormatText.restrictToNumeric(militaryEventOperationIdField);

				militaryEventOperationIdField.setOnAction(event -> reloadData());
				militaryEventOperationIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				militaryEventMissionIdField = new TextField();
				militaryEventMissionIdField.setPromptText("Mission ID");
				FormatText.restrictToNumeric(militaryEventMissionIdField);

				militaryEventMissionIdField.setOnAction(event -> reloadData());
				militaryEventMissionIdField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

				militaryEventStartDateAfterPicker = new DatePicker();
				militaryEventStartDateAfterPicker.setPromptText("To Start Date");
				militaryEventStartDateAfterPicker.setOnAction(event -> reloadData());

				militaryEventStartDateBeforePicker = new DatePicker();
				militaryEventStartDateBeforePicker.setPromptText("From Start Date");
				militaryEventStartDateBeforePicker.setOnAction(event -> reloadData());

				militaryEventEndDateAfterPicker = new DatePicker();
				militaryEventEndDateAfterPicker.setPromptText("To End Date");
				militaryEventEndDateAfterPicker.setOnAction(event -> reloadData());

				militaryEventEndDateBeforePicker = new DatePicker();
				militaryEventEndDateBeforePicker.setPromptText("From End Date");
				militaryEventEndDateBeforePicker.setOnAction(event -> reloadData());

				militaryEventDateFilterLayout = new HBox(10);
				militaryEventDateFilterLayout.getChildren().addAll(
						new Label("From Start Date:"), militaryEventStartDateBeforePicker,
						new Label("To Start Date:"), militaryEventStartDateAfterPicker,
						new Label("From End Date:"), militaryEventEndDateBeforePicker,
						new Label("To End Date:"), militaryEventEndDateAfterPicker
				);

				militaryEventFilterLayout = new HBox(10);
				militaryEventFilterLayout.getChildren().addAll(
						new Label("ID:"),
						militaryEventIdField,
						new Label("Operation ID:"),
						militaryEventOperationIdField,
						new Label("Mission ID:"),
						militaryEventMissionIdField
				);

				militaryEventFilterLayoutWithDate = new VBox();
				militaryEventFilterLayoutWithDate.getChildren().addAll(militaryEventFilterLayout, militaryEventDateFilterLayout);

        hideAllFilterLayouts();

        loadTableNames();

        tableSelector.setOnAction(event -> {
            String selectedTable = tableSelector.getSelectionModel().getSelectedItem();
            if (selectedTable != null) {
								hideAllFilterLayouts();

                // Show filters only for the selected table
                switch (selectedTable.toLowerCase()) {
                    case "soldier":
												hideAllFilterLayouts();
												soldierFilterLayout.setVisible(true);
												soldierFilterLayout.setManaged(true);
												soldierDateFilterLayout.setVisible(true);
												soldierDateFilterLayout.setManaged(true);
                        soldierFilterLayoutWithDate.setVisible(true);
												soldierFilterLayoutWithDate.setManaged(true);
                        break;
                    case "base":
                        hideAllFilterLayouts();
                        baseFilterLayout.setVisible(true);
												baseFilterLayout.setManaged(true);
                        break;
                    case "division":
                        hideAllFilterLayouts();
                        divisionFilterLayout.setVisible(true);
												divisionFilterLayout.setManaged(true);
                        break;
                    case "vehicle":
                        hideAllFilterLayouts();
                        vehicleFilterLayout.setVisible(true);
												vehicleFilterLayout.setManaged(true);
                        break;
                    case "soldier_family":
                        hideAllFilterLayouts();
                        soldierFamilyFilterLayout.setVisible(true);
												soldierFamilyFilterLayout.setManaged(true);
                        break;
                    case "operation":
                        hideAllFilterLayouts();
												operationFilterLayout.setVisible(true);
												operationFilterLayout.setManaged(true);
												operationDateFilterLayout.setVisible(true);
												operationDateFilterLayout.setManaged(true);
                        operationFilterLayoutWithDate.setVisible(true);
												operationFilterLayoutWithDate.setManaged(true);
                        break;
                    case "mission":
                        hideAllFilterLayouts();
												missionFilterLayout.setVisible(true);
												missionFilterLayout.setManaged(true);
												missionDateFilterLayout.setVisible(true);
												missionDateFilterLayout.setManaged(true);
                        missionFilterLayoutWithDate.setVisible(true);
												missionFilterLayoutWithDate.setManaged(true);
                        break;
                    case "military_event":
                        hideAllFilterLayouts();
												militaryEventFilterLayout.setVisible(true);
												militaryEventFilterLayout.setManaged(true);
												militaryEventDateFilterLayout.setVisible(true);
												militaryEventDateFilterLayout.setManaged(true);
                        militaryEventFilterLayoutWithDate.setVisible(true);
												militaryEventFilterLayoutWithDate.setManaged(true);
                        break;
                    default:
                        break;
                }
                reloadData();
            }
				});

        searchField.setOnAction(event -> reloadData());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> reloadData());

        editButton.setOnAction(event -> editSelectedRow());
        deleteButton.setOnAction(event -> deleteSelectedRow());
        addButton.setOnAction(event -> addNewRow());

        statusLabel = new Label();

        this.getChildren().addAll(
            tableSelector,
            searchField,
            soldierFilterLayoutWithDate,
            baseFilterLayout,
            divisionFilterLayout,
            vehicleFilterLayout,
            soldierFamilyFilterLayout,
            operationFilterLayoutWithDate,
            missionFilterLayoutWithDate,
            militaryEventFilterLayoutWithDate,
            buttonLayout,
            tableView,
            statusLabel
        );
        this.setSpacing(10);
				this.setPadding(new Insets(15));
				
				this.getStyleClass().add("main-view");
				this.setStyleClasses();		
    }

    private void loadTableNames() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:army.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE '%_FTS%'")) {

            ObservableList<String> tableNames = FXCollections.observableArrayList();
            while (rs.next()) {
                tableNames.add(rs.getString("name"));
            }
            tableSelector.setItems(tableNames);

        } catch (SQLException ex) {
            ex.printStackTrace();
            showErrorAlert("Database Error", "Failed to load table names.", ex.getMessage());
        }
    }

    private void loadTableData(String tableName, String query) {
        tableView.getColumns().clear();
        tableData = FXCollections.observableArrayList();

        try {
            switch (tableName.toLowerCase()) {
								case "soldier":
										Boolean isAliveFilter = isSoldierAliveCheckBox.isIndeterminate() ? null
												: isSoldierAliveCheckBox.isSelected();
										Boolean veteranStatusFilter = veteranStatusCheckBox.isIndeterminate() ? null
												: veteranStatusCheckBox.isSelected();
										Integer soldierIdFilter = soldierIdFilterField.getText().isEmpty() ? null
												: Integer.parseInt(soldierIdFilterField.getText());
										Integer soldierDivisionIdFilter = soldierDivisionIdField.getText().isEmpty() ? null
												: Integer.parseInt(soldierDivisionIdField.getText());
										LocalDate soldierBirthDateAfter = soldierBirthDateAfterPicker.getValue();
										LocalDate soldierBirthDateBefore = soldierBirthDateBeforePicker.getValue();

                    List<Soldier> soldiers = SoldierQueries.getSoldiers(query, isAliveFilter, veteranStatusFilter, soldierIdFilter, 
												soldierDivisionIdFilter, soldierBirthDateAfter, soldierBirthDateBefore);
                    setupColumnsAndData(soldiers);
                    break;
								case "base":
										Integer baseIdFilter = baseIdField.getText().isEmpty() ? null
												: Integer.parseInt(baseIdField.getText());
										Integer baseCommanderIdFilter = baseCommanderIdField.getText().isEmpty() ? null
												: Integer.parseInt(baseCommanderIdField.getText());
										String inequalityBaseCapacity = baseCapacityInequalityComboBox.getValue();
										Integer baseCapacityFilter = baseCapacityField.getText().isEmpty() ? null
												: Integer.parseInt(baseCapacityField.getText());
										String inequalityBaseExpenses = baseExpensesInequalityComboBox.getValue();
										Double baseExpensesFilter = baseExpensesField.getText().isEmpty() ? null
												: Double.parseDouble(baseExpensesField.getText());

										List<Base> bases = BaseQueries.getBases(query, baseIdFilter, baseCommanderIdFilter,
										inequalityBaseCapacity, baseCapacityFilter,inequalityBaseExpenses, baseExpensesFilter);
										setupColumnsAndData(bases);
										break;
								case "division":
										Integer divisionIdFilter = divisionIdField.getText().isEmpty() ? null
												: Integer.parseInt(divisionIdField.getText());
										Integer divisionCommanderIdFilter = divisionCommanderIdField.getText().isEmpty() ? null
												: Integer.parseInt(divisionCommanderIdField.getText());
										String inequalityDivisionQuota = divisionQuotaInequalityComboBox.getValue();
												Integer divisionQuotaFilter = divisionQuotaField.getText().isEmpty() ? null
												: Integer.parseInt(divisionQuotaField.getText());
										Integer divisionBaseIdFilter = divisionBaseIdField.getText().isEmpty() ? null
												: Integer.parseInt(divisionBaseIdField.getText());
										Integer divisionSuperDivisionIdFilter = divisionSuperDivisionIdField.getText().isEmpty() ? null
												: Integer.parseInt(divisionSuperDivisionIdField.getText());

										List<Division> divisions = DivisionQueries.getDivisions(query, divisionIdFilter,
												divisionCommanderIdFilter,inequalityDivisionQuota, divisionQuotaFilter, divisionBaseIdFilter,
												divisionSuperDivisionIdFilter);
										setupColumnsAndData(divisions);
										break;
								case "vehicle":
										Integer vehicleIdFilter = vehicleIdField.getText().isEmpty() ? null
												: Integer.parseInt(vehicleIdField.getText());
										Boolean isOperationalFilter = isOperationalCheckBox.isIndeterminate() ? null
												: isOperationalCheckBox.isSelected();
										String inequalityDamageExpenses = damageExpensesInequalityComboBox.getValue();
										Double damageExpensesFilter = damageExpensesField.getText().isEmpty() ? null
												: Double.parseDouble(damageExpensesField.getText());
										Integer vehicleBaseIdFilter = vehicleBaseIdField.getText().isEmpty() ? null
												: Integer.parseInt(vehicleBaseIdField.getText());
										Integer vehicleMissionIdFilter = vehicleMissionIdField.getText().isEmpty() ? null
												: Integer.parseInt(vehicleMissionIdField.getText());

										List<Vehicle> vehicles = VehicleQueries.getVehicles(query, vehicleIdFilter, isOperationalFilter,
												inequalityDamageExpenses, damageExpensesFilter, vehicleBaseIdFilter, vehicleMissionIdFilter);
										setupColumnsAndData(vehicles);
										break;
								case "soldier_family":
										Integer soldierFamilyIdFilter = soldierFamilyIdField.getText().isEmpty() ? null
												: Integer.parseInt(soldierFamilyIdField.getText());
										Integer soldierFamilySoldierIdFilter = soldierFamilySoldierIdField.getText().isEmpty() ? null
												: Integer.parseInt(soldierFamilySoldierIdField.getText());

										List<SoldierFamily> soldierFamilies = SoldierFamilyQueries.getSoldierFamilies(query,
												 soldierFamilySoldierIdFilter, soldierFamilyIdFilter);
										setupColumnsAndData(soldierFamilies);
										break;
								case "operation":
										Integer operationIdFilter = operationIdField.getText().isEmpty() ? null
												: Integer.parseInt(operationIdField.getText());
										Integer operationDivisionIdFilter = operationDivisionIdField.getText().isEmpty() ? null
												: Integer.parseInt(operationDivisionIdField.getText());
										LocalDate operationStartDateAfter = operationStartDateAfterPicker.getValue();
										LocalDate operationStartDateBefore = operationStartDateBeforePicker.getValue();
										LocalDate operationEndDateAfter = operationEndDateAfterPicker.getValue();
										LocalDate operationEndDateBefore = operationEndDateBeforePicker.getValue();

										List<Operation> operations = OperationQueries.getOperations(query, operationIdFilter,
												operationDivisionIdFilter, operationStartDateAfter, operationStartDateBefore,	operationEndDateAfter, operationEndDateBefore);
										setupColumnsAndData(operations);
										break;
								case "mission":
										Integer missionIdFilter = missionIdField.getText().isEmpty() ? null
												: Integer.parseInt(missionIdField.getText());
										Integer missionDivisionIdFilter = missionDivisionIdField.getText().isEmpty() ? null
												: Integer.parseInt(missionDivisionIdField.getText());
										Integer missionOperationIdFilter = missionOperationIdField.getText().isEmpty() ? null
												: Integer.parseInt(missionOperationIdField.getText());
										LocalDate missionStartDateAfter = missionStartDateAfterPicker.getValue();
										LocalDate missionStartDateBefore = missionStartDateBeforePicker.getValue();
										LocalDate missionEndDateAfter = missionEndDateAfterPicker.getValue();
										LocalDate missionEndDateBefore = missionEndDateBeforePicker.getValue();

										List<Mission> missions = MissionQueries.getMissions(query, missionIdFilter, missionDivisionIdFilter,
												missionOperationIdFilter, missionStartDateAfter, missionStartDateBefore, missionEndDateAfter, missionEndDateBefore);
										setupColumnsAndData(missions);
										break;
								case "military_event":
										Integer militaryEventIdFilter = militaryEventIdField.getText().isEmpty() ? null
												: Integer.parseInt(militaryEventIdField.getText());
										Integer militaryEventOperationIdFilter = militaryEventOperationIdField.getText().isEmpty() ? null
												: Integer.parseInt(militaryEventOperationIdField.getText());
										Integer militaryEventMissionIdFilter = militaryEventMissionIdField.getText().isEmpty() ? null
												: Integer.parseInt(militaryEventMissionIdField.getText());
										LocalDate militaryEventStartDateAfter = militaryEventStartDateAfterPicker.getValue();
										LocalDate militaryEventStartDateBefore = militaryEventStartDateBeforePicker.getValue();
										LocalDate militaryEventEndDateAfter = militaryEventEndDateAfterPicker.getValue();
										LocalDate militaryEventEndDateBefore = militaryEventEndDateBeforePicker.getValue();

										List<MilitaryEvent> militaryEvents = MilitaryEventQueries.getMilitaryEvents(query,
												militaryEventIdFilter, militaryEventOperationIdFilter, militaryEventMissionIdFilter,
														militaryEventStartDateAfter, militaryEventStartDateBefore, militaryEventEndDateAfter, militaryEventEndDateBefore);
										setupColumnsAndData(militaryEvents);
										break;
              default:
                    showErrorAlert("Unknown Table", "The selected table is not recognized.", "Table: " + tableName);
                    return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorAlert("Data Loading Error", "Failed to load data for table: " + tableName, ex.getMessage());
        }
    }

    private void reloadData() {
        String selectedTable = tableSelector.getSelectionModel().getSelectedItem();
        String query = searchField.getText();
        if (selectedTable != null) {
            loadTableData(selectedTable, query);
        }
    }

    private <T> void setupColumnsAndData(List<T> dataList) {
        if (dataList.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList());
						statusLabel.setText("No results found.");
            return;
        }

        // Dynamically create columns based on the object's fields
        T firstItem = dataList.get(0);
        Class<?> clazz = firstItem.getClass();
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            String fieldName = field.getName();
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(fieldName);
            final int colIndex = getFieldIndex(clazz, fieldName);
            col.setCellValueFactory(cellData -> {
                ObservableList<String> rowValues = cellData.getValue();
                if (rowValues.size() > colIndex && colIndex >= 0) {
                    return new SimpleStringProperty(rowValues.get(colIndex));
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tableView.getColumns().add(col);
        }

        // Populate table data
        for (T item : dataList) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    row.add(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    row.add("");
                }
            }
            tableData.add(row);
        }

				tableView.setItems(tableData);
				statusLabel.setText("Returned " + dataList.size() + " rows.");
    }

		private int getFieldIndex(Class<?> clazz, String fieldName) {
			java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (fields[i].getName().equals(fieldName)) {
					return i;
				}
			}
			return -1;
		}

		private void editSelectedRow() {
			String selectedTable = tableSelector.getSelectionModel().getSelectedItem();
			ObservableList<String> selectedRow = tableView.getSelectionModel().getSelectedItem();

			if (selectedTable == null) {
				showErrorAlert("No Table Selected", "Please select a table to edit.", null);
				return;
			}

			if (selectedRow == null) {
				showErrorAlert("No Row Selected", "Please select a row to edit.", null);
				return;
			}

			try {
				switch (selectedTable.toLowerCase()) {
					case "soldier":
						editSoldier(selectedRow);
						break;
					case "operation":
						editOperation(selectedRow);
						break;
					case "mission":
						editMission(selectedRow);
						break;
					case "base":
						editBase(selectedRow);
						break;
					case "military_event":
						editMilitaryEvent(selectedRow);
						break;
					case "division":
						editDivision(selectedRow);
						break;
					case "soldier_family":
						editSoldierFamily(selectedRow);
						break;
					case "vehicle":
						editVehicle(selectedRow);
						break;
					default:
						showErrorAlert("Edit Not Implemented", "Editing not implemented for table: " + selectedTable, null);
				}
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				showErrorAlert("Edit Error", "An error occurred while editing the row.", ex.getMessage());
			} catch (Exception ex) {
				ex.printStackTrace();
				showErrorAlert("Edit Error", "An error occurred while editing the row.", ex.getMessage());
			}
		}

		private void editSoldier(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			boolean isAlive = Boolean.parseBoolean(row.get(1));
			String name = row.get(2);
			String surname = row.get(3);
			String birthDate = row.get(4);
			String rank = row.get(5);
			boolean veteranStatus = Boolean.parseBoolean(row.get(6));
			int divisionId = Integer.parseInt(row.get(7));

			Soldier soldier = new Soldier(id, isAlive, name, surname, birthDate, rank, veteranStatus, divisionId);

			Dialog<Soldier> dialog = new Dialog<>();
			dialog.setTitle("Edit Soldier");
			dialog.setHeaderText("Edit Soldier Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField nameField = new TextField(soldier.getName());
			TextField surnameField = new TextField(soldier.getSurname());
			TextField birthDateField = new TextField(soldier.getBirth_date());
			TextField rankField = new TextField(soldier.getRank());
			CheckBox isAliveBox = new CheckBox();
			isAliveBox.setSelected(soldier.isIs_alive());
			CheckBox veteranStatusBox = new CheckBox();
			veteranStatusBox.setSelected(soldier.isVeteran_status());
			TextField divisionIdField = new TextField(String.valueOf(soldier.getDivision_id()));

			FormatText.restrictToNumeric(divisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Name:"), nameField,
					new Label("Surname:"), surnameField,
					new Label("Birth Date:"), birthDateField,
					new Label("Rank:"), rankField,
					new Label("Is Alive:"), isAliveBox,
					new Label("Veteran Status:"), veteranStatusBox,
					new Label("Division ID:"), divisionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Soldier updatedSoldier = new Soldier(
							soldier.getSoldier_id(),
							isAliveBox.isSelected(),
							nameField.getText(),
							surnameField.getText(),
							birthDateField.getText(),
							rankField.getText(),
							veteranStatusBox.isSelected(),
							updatedDivisionId);
					return updatedSoldier;
				}
				return null;
			});

			try {
				dialog.showAndWait().ifPresent(updatedSoldier -> {
					validateDate(updatedSoldier.getBirth_date());
					SoldierQueries.updateSoldier(updatedSoldier);
					loadTableData("Soldier", searchField.getText());
				});

				statusLabel.setText("Soldier table updated successfully.");
			} catch (IllegalArgumentException e) {
				showErrorAlert("Edit Error", "An error occurred while editing the row.", e.getMessage());
			}
		}

		private void editOperation(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			String operationName = row.get(1);
			String operationType = row.get(2);
			int divisionId = Integer.parseInt(row.get(3));
			String startDate = row.get(4);
			String endDate = row.get(5);

			Operation operation = new Operation(id, operationName, operationType, divisionId, startDate, endDate);

			Dialog<Operation> dialog = new Dialog<>();
			dialog.setTitle("Edit Operation");
			dialog.setHeaderText("Edit Operation Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField operationNameField = new TextField(operation.getOperationName());
			TextField operationTypeField = new TextField(operation.getOperationType());
			TextField divisionIdField = new TextField(String.valueOf(operation.getDivisionId()));
			TextField startDateField = new TextField(operation.getStartDate());
			TextField endDateField = new TextField(operation.getEndDate());

			FormatText.restrictToNumeric(divisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Operation Name:"), operationNameField,
					new Label("Operation Type:"), operationTypeField,
					new Label("Division ID:"), divisionIdField,
					new Label("Start Date:"), startDateField,
					new Label("End Date:"), endDateField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Operation updatedOperation = new Operation(
							operation.getOperationId(),
							operationNameField.getText(),
							operationTypeField.getText(),
							updatedDivisionId,
							startDateField.getText(),
							endDateField.getText());
					return updatedOperation;
				}
				return null;
			});

			try {
				dialog.showAndWait().ifPresent(updatedOperation -> {
					validateDate(updatedOperation.getStartDate());
					validateDate(updatedOperation.getEndDate());
					OperationQueries.updateOperation(updatedOperation);
					loadTableData("Operation", searchField.getText());
				});
				statusLabel.setText("Operation table updated successfully.");
			} catch (IllegalArgumentException e) {
				showErrorAlert("Edit Error", "An error occurred while editing the row.", e.getMessage());
			}
		}

		private void editMission(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			String missionName = row.get(1);
			int divisionId = Integer.parseInt(row.get(2));
			String startDate = row.get(3);
			String endDate = row.get(4);
			String missionType = row.get(5);
			int operationId = Integer.parseInt(row.get(6));

			Mission mission = new Mission(id, missionName, divisionId, startDate, endDate, missionType, operationId);

			Dialog<Mission> dialog = new Dialog<>();
			dialog.setTitle("Edit Mission");
			dialog.setHeaderText("Edit Mission Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField missionNameField = new TextField(mission.getMissionName());
			TextField divisionIdField = new TextField(String.valueOf(mission.getDivisionId()));
			TextField startDateField = new TextField(mission.getStartDate());
			TextField endDateField = new TextField(mission.getEndDate());
			TextField missionTypeField = new TextField(mission.getMissionType());
			TextField operationIdField = new TextField(String.valueOf(mission.getOperationId()));

			FormatText.restrictToNumeric(divisionIdField);
			FormatText.restrictToNumeric(operationIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Mission Name:"), missionNameField,
					new Label("Division ID:"), divisionIdField,
					new Label("Start Date:"), startDateField,
					new Label("End Date:"), endDateField,
					new Label("Mission Type:"), missionTypeField,
					new Label("Operation ID:"), operationIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Integer updatedOperationId = operationIdField.getText().isEmpty() ? null
							: Integer.parseInt(operationIdField.getText());
					Mission updatedMission = new Mission(
							mission.getMissionId(),
							missionNameField.getText(),
							updatedDivisionId,
							startDateField.getText(),
							endDateField.getText(),
							missionTypeField.getText(),
							updatedOperationId);
					return updatedMission;
				}
				return null;
			});

			try {
				dialog.showAndWait().ifPresent(updatedMission -> {
					validateDate(updatedMission.getStartDate());
					validateDate(updatedMission.getEndDate());
					MissionQueries.updateMission(updatedMission);
					loadTableData("Mission", searchField.getText());
				});

				statusLabel.setText("Mission table updated successfully.");
			} catch (IllegalArgumentException e) {

			}
		}

		private void editBase(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			Integer commanderId = row.get(1).isEmpty() ? null : Integer.parseInt(row.get(1));
			String baseName = row.get(2);
			double baseExpenses = Double.parseDouble(row.get(3));
			int baseCapacity = Integer.parseInt(row.get(4));

			Base base = new Base(id, commanderId, baseName, baseExpenses, baseCapacity);

			Dialog<Base> dialog = new Dialog<>();
			dialog.setTitle("Edit Base");
			dialog.setHeaderText("Edit Base Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField commanderIdField = new TextField(commanderId != null ? commanderId.toString() : "");
			TextField baseNameField = new TextField(base.getBaseName());
			TextField baseExpensesField = new TextField(String.valueOf(base.getBaseExpenses()));
			TextField baseCapacityField = new TextField(String.valueOf(base.getBaseCapacity()));

			FormatText.restrictToNumeric(commanderIdField);
			FormatText.restrictToDecimal(baseExpensesField);
			FormatText.restrictToNumeric(baseCapacityField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Commander ID:"), commanderIdField,
					new Label("Base Name:"), baseNameField,
					new Label("Base Expenses:"), baseExpensesField,
					new Label("Base Capacity:"), baseCapacityField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedCommanderId = commanderIdField.getText().isEmpty() ? null
							: Integer.parseInt(commanderIdField.getText());
					Double updatedBaseExpenses = baseExpensesField.getText().isEmpty() ? 0.0
							: Double.parseDouble(baseExpensesField.getText());
					Integer updatedBaseCapacity = baseCapacityField.getText().isEmpty() ? 0
							: Integer.parseInt(baseCapacityField.getText());
					Base updatedBase = new Base(
							base.getBaseId(),
							updatedCommanderId,
							baseNameField.getText(),
							updatedBaseExpenses,
							updatedBaseCapacity);
					return updatedBase;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(updatedBase -> {
				BaseQueries.updateBase(updatedBase);
				loadTableData("Base", searchField.getText());
			});

			statusLabel.setText("Base table updated successfully.");
		}

		private void editMilitaryEvent(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			String eventTitle = row.get(1);
			String eventDescription = row.get(2);
			String startDate = row.get(3);
			String endDate = row.get(4);
			Integer operationId = row.get(5).isEmpty() ? null : Integer.parseInt(row.get(5));
			Integer missionId = row.get(6).isEmpty() ? null : Integer.parseInt(row.get(6));

			MilitaryEvent event = new MilitaryEvent(id, eventTitle, eventDescription, startDate, endDate, operationId,
					missionId);

			Dialog<MilitaryEvent> dialog = new Dialog<>();
			dialog.setTitle("Edit Military Event");
			dialog.setHeaderText("Edit Military Event Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField eventTitleField = new TextField(event.getEventTitle());
			TextArea eventDescriptionArea = new TextArea(event.getEventDescription());
			TextField startDateField = new TextField(event.getStartDate());
			TextField endDateField = new TextField(event.getEndDate());
			TextField operationIdField = new TextField(operationId != null ? operationId.toString() : "");
			TextField missionIdField = new TextField(missionId != null ? missionId.toString() : "");

			FormatText.restrictToNumeric(operationIdField);
			FormatText.restrictToNumeric(missionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Event Title:"), eventTitleField,
					new Label("Event Description:"), eventDescriptionArea,
					new Label("Start Date:"), startDateField,
					new Label("End Date:"), endDateField,
					new Label("Operation ID:"), operationIdField,
					new Label("Mission ID:"), missionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedOperationId = operationIdField.getText().isEmpty() ? null
							: Integer.parseInt(operationIdField.getText());
					Integer updatedMissionId = missionIdField.getText().isEmpty() ? null
							: Integer.parseInt(missionIdField.getText());
					MilitaryEvent updatedEvent = new MilitaryEvent(
							event.getEventId(),
							eventTitleField.getText(),
							eventDescriptionArea.getText(),
							startDateField.getText(),
							endDateField.getText(),
							updatedOperationId,
							updatedMissionId);
					return updatedEvent;
				}
				return null;
			});
			try {
				dialog.showAndWait().ifPresent(updatedEvent -> {
					validateDate(updatedEvent.getStartDate());
					validateDate(updatedEvent.getEndDate());
					MilitaryEventQueries.updateMilitaryEvent(updatedEvent);
					loadTableData("Military_Event", searchField.getText());
				});
				statusLabel.setText("Military Event table updated successfully.");
			} catch (IllegalArgumentException e) {
				showErrorAlert("Edit Error", "An error occurred while editing the row.", e.getMessage());
			}
		}

		private void editDivision(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			int commanderId = Integer.parseInt(row.get(1));
			int quota = Integer.parseInt(row.get(2));
			String divisionName = row.get(3);
			int baseId = Integer.parseInt(row.get(4));
			int superDivisionId = Integer.parseInt(row.get(5));

			Division division = new Division(id, commanderId, quota, divisionName, baseId, superDivisionId);

			Dialog<Division> dialog = new Dialog<>();
			dialog.setTitle("Edit Division");
			dialog.setHeaderText("Edit Division Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField commanderIdField = new TextField(String.valueOf(division.getCommanderId()));
			TextField quotaField = new TextField(String.valueOf(division.getQuota()));
			TextField divisionNameField = new TextField(division.getDivisionName());
			TextField baseIdField = new TextField(String.valueOf(division.getBaseId()));
			TextField superDivisionIdField = new TextField(String.valueOf(division.getSuperDivisionId()));

			FormatText.restrictToNumeric(commanderIdField);
			FormatText.restrictToDecimal(quotaField);
			FormatText.restrictToNumeric(baseIdField);
			FormatText.restrictToNumeric(superDivisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Commander ID:"), commanderIdField,
					new Label("Quota:"), quotaField,
					new Label("Division Name:"), divisionNameField,
					new Label("Base ID:"), baseIdField,
					new Label("Super Division ID:"), superDivisionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedCommanderId = commanderIdField.getText().isEmpty() ? null
							: Integer.parseInt(commanderIdField.getText());
					Integer updatedBaseId = baseIdField.getText().isEmpty() ? null
							: Integer.parseInt(baseIdField.getText());
					Integer updatedSuperDivisionId = superDivisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(superDivisionIdField.getText());
					Integer updatedQuota = quotaField.getText().isEmpty() ? null
							: Integer.parseInt(quotaField.getText());
					Division updatedDivision = new Division(
							division.getDivisionId(),
							updatedCommanderId,
							updatedQuota,
							divisionNameField.getText(),
							updatedBaseId,
							updatedSuperDivisionId);
					return updatedDivision;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(updatedDivision -> {
				DivisionQueries.updateDivision(updatedDivision);
				loadTableData("Division", searchField.getText());
			});

			statusLabel.setText("Division table updated successfully.");
		}

		private void editSoldierFamily(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			int relatedSoldierId = Integer.parseInt(row.get(1));
			String name = row.get(2);
			String surname = row.get(3);
			String relationType = row.get(4);
			String phoneNumber = row.get(5);

			SoldierFamily family = new SoldierFamily(id, relatedSoldierId, name, surname, relationType, phoneNumber);

			Dialog<SoldierFamily> dialog = new Dialog<>();
			dialog.setTitle("Edit Soldier Family");
			dialog.setHeaderText("Edit Soldier Family Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField relatedSoldierIdField = new TextField(String.valueOf(family.getRelatedSoldierId()));
			TextField nameField = new TextField(family.getName());
			TextField surnameField = new TextField(family.getSurname());
			TextField relationTypeField = new TextField(family.getRelationType());
			TextField phoneNumberField = new TextField(family.getPhoneNumber());

			FormatText.restrictToNumeric(relatedSoldierIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Related Soldier ID:"), relatedSoldierIdField,
					new Label("Name:"), nameField,
					new Label("Surname:"), surnameField,
					new Label("Relation Type:"), relationTypeField,
					new Label("Phone Number:"), phoneNumberField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					SoldierFamily updatedFamily = new SoldierFamily(
							family.getRelatedFamilyMemberId(),
							Integer.parseInt(relatedSoldierIdField.getText()),
							nameField.getText(),
							surnameField.getText(),
							relationTypeField.getText(),
							phoneNumberField.getText());
					return updatedFamily;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(updatedFamily -> {
				SoldierFamilyQueries.updateSoldierFamily(updatedFamily);
				loadTableData("Soldier_Family", searchField.getText());
			});

			statusLabel.setText("Soldier Family table updated successfully.");
		}

		private void editVehicle(ObservableList<String> row) {
			int id = Integer.parseInt(row.get(0));
			String vehicleType = row.get(1);
			boolean isOperational = Boolean.parseBoolean(row.get(2));
			double damageExpenses = Double.parseDouble(row.get(3));
			int baseId = Integer.parseInt(row.get(4));
			String model = row.get(5);
			String licensePlate = row.get(6);
			int missionId = Integer.parseInt(row.get(7));

			Vehicle vehicle = new Vehicle(id, vehicleType, isOperational, damageExpenses, baseId, model, licensePlate,
					missionId);

			Dialog<Vehicle> dialog = new Dialog<>();
			dialog.setTitle("Edit Vehicle");
			dialog.setHeaderText("Edit Vehicle Details");

			ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField vehicleTypeField = new TextField(vehicle.getVehicleType());
			CheckBox isOperationalBox = new CheckBox();
			isOperationalBox.setSelected(vehicle.isOperational());
			TextField damageExpensesField = new TextField(String.valueOf(vehicle.getDamageExpenses()));
			TextField baseIdField = new TextField(String.valueOf(vehicle.getBaseId()));
			TextField modelField = new TextField(vehicle.getModel());
			TextField licensePlateField = new TextField(vehicle.getLicensePlate());
			TextField missionIdField = new TextField(String.valueOf(vehicle.getMissionId()));

			FormatText.restrictToDecimal(damageExpensesField);
			FormatText.restrictToNumeric(baseIdField);
			FormatText.restrictToNumeric(missionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Vehicle Type:"), vehicleTypeField,
					new Label("Is Operational:"), isOperationalBox,
					new Label("Damage Expenses:"), damageExpensesField,
					new Label("Base ID:"), baseIdField,
					new Label("Model:"), modelField,
					new Label("License Plate:"), licensePlateField,
					new Label("Mission ID:"), missionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedBaseId = baseIdField.getText().isEmpty() ? null
							: Integer.parseInt(baseIdField.getText());
					Integer updatedMissionId = missionIdField.getText().isEmpty() ? null
							: Integer.parseInt(missionIdField.getText());
					Double updatedDamageExpenses = damageExpensesField.getText().isEmpty() ? 0.0
							: Double.parseDouble(damageExpensesField.getText());
					Vehicle updatedVehicle = new Vehicle(
							vehicle.getVehicleId(),
							vehicleTypeField.getText(),
							isOperationalBox.isSelected(),
							updatedDamageExpenses,
							updatedBaseId,
							modelField.getText(),
							licensePlateField.getText(),
							updatedMissionId);
					return updatedVehicle;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(updatedVehicle -> {
				VehicleQueries.updateVehicle(updatedVehicle);
				loadTableData("Vehicle", searchField.getText());
			});

			statusLabel.setText("Vehicle table updated successfully.");
		}

		private void addNewRow() {
        String selectedTable = tableSelector.getSelectionModel().getSelectedItem();

        if (selectedTable == null) {
            showErrorAlert("No Table Selected", "Please select a table to add to.", null);
            return;
        }

        try {
            switch (selectedTable.toLowerCase()) {
                case "soldier":
                    addSoldier();
                    break;
                case "operation":
                    addOperation();
                    break;
                case "mission":
                    addMission();
                    break;
                case "base":
                    addBase();
                    break;
                case "military_event":
                    addMilitaryEvent();
                    break;
                case "division":
                    addDivision();
                    break;
                case "soldier_family":
                    addSoldierFamily();
                    break;
                case "vehicle":
                    addVehicle();
                    break;
                default:
                    showErrorAlert("Add Not Implemented", "Adding not implemented for table: " + selectedTable, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorAlert("Add Error", "An error occurred while adding a new row.", ex.getMessage());
        }
    }

		private void addSoldier() {
			Dialog<Soldier> dialog = new Dialog<>();
			dialog.setTitle("Add Soldier");
			dialog.setHeaderText("Enter Soldier Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField nameField = new TextField();
			TextField surnameField = new TextField();
			TextField birthDateField = new TextField();
			TextField rankField = new TextField();
			CheckBox isAliveBox = new CheckBox();
			CheckBox veteranStatusBox = new CheckBox();
			TextField divisionIdField = new TextField();

			FormatText.restrictToNumeric(divisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Name:"), nameField,
					new Label("Surname:"), surnameField,
					new Label("Birth Date (YYYY-MM-DD):"), birthDateField,
					new Label("Rank:"), rankField,
					new Label("Is Alive:"), isAliveBox,
					new Label("Veteran Status:"), veteranStatusBox,
					new Label("Division ID:"), divisionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Soldier newSoldier = new Soldier(
							0, // ID will be auto-generated
							isAliveBox.isSelected(),
							nameField.getText(),
							surnameField.getText(),
							birthDateField.getText(),
							rankField.getText(),
							veteranStatusBox.isSelected(),
							updatedDivisionId);
					return newSoldier;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newSoldier -> {
				try {
					validateDate(newSoldier.getBirth_date());
					SoldierQueries.insertSoldier(newSoldier);
					loadTableData("Soldier", searchField.getText());
					statusLabel.setText("New soldier added successfully.");
				} catch (IllegalArgumentException e) {
					showErrorAlert("Invalid Input", e.getMessage(), null);
				}
			});
		}

		private void addOperation() {
			Dialog<Operation> dialog = new Dialog<>();
			dialog.setTitle("Add Operation");
			dialog.setHeaderText("Enter Operation Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField operationNameField = new TextField();
			TextField operationTypeField = new TextField();
			TextField divisionIdField = new TextField();
			TextField startDateField = new TextField();
			TextField endDateField = new TextField();

			FormatText.restrictToNumeric(divisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Operation Name:"), operationNameField,
					new Label("Operation Type:"), operationTypeField,
					new Label("Division ID:"), divisionIdField,
					new Label("Start Date (YYYY-MM-DD):"), startDateField,
					new Label("End Date (YYYY-MM-DD):"), endDateField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Operation newOperation = new Operation(
							0, // ID will be auto-generated
							operationNameField.getText(),
							operationTypeField.getText(),
							updatedDivisionId,
							startDateField.getText(),
							endDateField.getText());
					return newOperation;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newOperation -> {
				try {
					validateDate(newOperation.getStartDate());
					validateDate(newOperation.getEndDate());
					OperationQueries.insertOperation(newOperation);
					loadTableData("Operation", searchField.getText());
					statusLabel.setText("New operation added successfully.");
				} catch (IllegalArgumentException e) {
					showErrorAlert("Invalid Input", e.getMessage(), null);
				}
			});
		}
		
		private void addVehicle() {
			Dialog<Vehicle> dialog = new Dialog<>();
			dialog.setTitle("Add Vehicle");
			dialog.setHeaderText("Enter Vehicle Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField vehicleTypeField = new TextField();
			CheckBox isOperationalBox = new CheckBox();
			TextField damageExpensesField = new TextField();
			TextField baseIdField = new TextField();
			TextField modelField = new TextField();
			TextField licensePlateField = new TextField();
			TextField missionIdField = new TextField();

			FormatText.restrictToDecimal(damageExpensesField);
			FormatText.restrictToNumeric(baseIdField);
			FormatText.restrictToNumeric(missionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Vehicle Type:"), vehicleTypeField,
					new Label("Is Operational:"), isOperationalBox,
					new Label("Damage Expenses:"), damageExpensesField,
					new Label("Base ID:"), baseIdField,
					new Label("Model:"), modelField,
					new Label("License Plate:"), licensePlateField,
					new Label("Mission ID:"), missionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedBaseId = baseIdField.getText().isEmpty() ? null
							: Integer.parseInt(baseIdField.getText());
					Integer updatedMissionId = missionIdField.getText().isEmpty() ? null
							: Integer.parseInt(missionIdField.getText());
					Double updatedDamageExpenses = damageExpensesField.getText().isEmpty() ? 0.0
							: Double.parseDouble(damageExpensesField.getText());
					Vehicle newVehicle = new Vehicle(
							0, // ID will be auto-generated
							vehicleTypeField.getText(),
							isOperationalBox.isSelected(),
							updatedDamageExpenses,
							updatedBaseId,
							modelField.getText(),
							licensePlateField.getText(),
							updatedMissionId);
					return newVehicle;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newVehicle -> {
				VehicleQueries.insertVehicle(newVehicle);
				loadTableData("Vehicle", searchField.getText());
				statusLabel.setText("New vehicle added successfully.");
			});
		}
		
		private void addDivision() {
			Dialog<Division> dialog = new Dialog<>();
			dialog.setTitle("Add Division");
			dialog.setHeaderText("Enter Division Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField commanderIdField = new TextField();
			TextField quotaField = new TextField();
			TextField divisionNameField = new TextField();
			TextField baseIdField = new TextField();
			TextField superDivisionIdField = new TextField();

			FormatText.restrictToNumeric(commanderIdField);
			FormatText.restrictToDecimal(quotaField);
			FormatText.restrictToNumeric(baseIdField);
			FormatText.restrictToNumeric(superDivisionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Commander ID:"), commanderIdField,
					new Label("Quota:"), quotaField,
					new Label("Division Name:"), divisionNameField,
					new Label("Base ID:"), baseIdField,
					new Label("Super Division ID:"), superDivisionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedCommanderId = commanderIdField.getText().isEmpty() ? null
							: Integer.parseInt(commanderIdField.getText());
					Integer updatedBaseId = baseIdField.getText().isEmpty() ? null
							: Integer.parseInt(baseIdField.getText());
					Integer updatedSuperDivisionId = superDivisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(superDivisionIdField.getText());
					Integer updatedQuota = quotaField.getText().isEmpty() ? null
							: Integer.parseInt(quotaField.getText());
					Division newDivision = new Division(
							0, // ID will be auto-generated
							updatedCommanderId,
							updatedQuota,
							divisionNameField.getText(),
							updatedBaseId,
							updatedSuperDivisionId);
					return newDivision;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newDivision -> {
				DivisionQueries.insertDivision(newDivision);
				loadTableData("Division", searchField.getText());
				statusLabel.setText("New division added successfully.");
			});
		}

		private void addMilitaryEvent() {
			Dialog<MilitaryEvent> dialog = new Dialog<>();
			dialog.setTitle("Add Military Event");
			dialog.setHeaderText("Enter Military Event Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField eventTitleField = new TextField();
			TextArea eventDescriptionArea = new TextArea();
			TextField startDateField = new TextField();
			TextField endDateField = new TextField();
			TextField operationIdField = new TextField();
			TextField missionIdField = new TextField();

			FormatText.restrictToNumeric(operationIdField);
			FormatText.restrictToNumeric(missionIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Event Title:"), eventTitleField,
					new Label("Event Description:"), eventDescriptionArea,
					new Label("Start Date (YYYY-MM-DD):"), startDateField,
					new Label("End Date (YYYY-MM-DD):"), endDateField,
					new Label("Operation ID:"), operationIdField,
					new Label("Mission ID:"), missionIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedOperationId = operationIdField.getText().isEmpty() ? null
							: Integer.parseInt(operationIdField.getText());
					Integer updatedMissionId = missionIdField.getText().isEmpty() ? null
							: Integer.parseInt(missionIdField.getText());
					MilitaryEvent newEvent = new MilitaryEvent(
							0, // ID will be auto-generated
							eventTitleField.getText(),
							eventDescriptionArea.getText(),
							startDateField.getText(),
							endDateField.getText(),
							updatedOperationId,
							updatedMissionId);
					return newEvent;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newEvent -> {
				try {
					validateDate(newEvent.getStartDate());
					validateDate(newEvent.getEndDate());
					MilitaryEventQueries.insertMilitaryEvent(newEvent);
					loadTableData("Military_Event", searchField.getText());
					statusLabel.setText("New military event added successfully.");
				} catch (IllegalArgumentException e) {
					showErrorAlert("Invalid Input", e.getMessage(), null);
				}
			});
		}

		private void addBase() {
			Dialog<Base> dialog = new Dialog<>();
			dialog.setTitle("Add Base");
			dialog.setHeaderText("Enter Base Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField commanderIdField = new TextField();
			TextField baseNameField = new TextField();
			TextField baseExpensesField = new TextField();
			TextField baseCapacityField = new TextField();

			FormatText.restrictToNumeric(commanderIdField);
			FormatText.restrictToDecimal(baseExpensesField);
			FormatText.restrictToNumeric(baseCapacityField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Commander ID:"), commanderIdField,
					new Label("Base Name:"), baseNameField,
					new Label("Base Expenses:"), baseExpensesField,
					new Label("Base Capacity:"), baseCapacityField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedCommanderId = commanderIdField.getText().isEmpty() ? null
							: Integer.parseInt(commanderIdField.getText());
					Double updatedBaseExpenses = baseExpensesField.getText().isEmpty() ? 0.0
							: Double.parseDouble(baseExpensesField.getText());
					Integer updatedBaseCapacity = baseCapacityField.getText().isEmpty() ? 0
							: Integer.parseInt(baseCapacityField.getText());
					Base newBase = new Base(
							0, // ID will be auto-generated
							updatedCommanderId,
							baseNameField.getText(),
							updatedBaseExpenses,
							updatedBaseCapacity);
					return newBase;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newBase -> {
				BaseQueries.insertBase(newBase);
				loadTableData("Base", searchField.getText());
				statusLabel.setText("New base added successfully.");
			});
		}

		private void addMission() {
			Dialog<Mission> dialog = new Dialog<>();
			dialog.setTitle("Add Mission");
			dialog.setHeaderText("Enter Mission Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField missionNameField = new TextField();
			TextField divisionIdField = new TextField();
			TextField startDateField = new TextField();
			TextField endDateField = new TextField();
			TextField missionTypeField = new TextField();
			TextField operationIdField = new TextField();

			FormatText.restrictToNumeric(divisionIdField);
			FormatText.restrictToNumeric(operationIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Mission Name:"), missionNameField,
					new Label("Division ID:"), divisionIdField,
					new Label("Start Date (YYYY-MM-DD):"), startDateField,
					new Label("End Date (YYYY-MM-DD):"), endDateField,
					new Label("Mission Type:"), missionTypeField,
					new Label("Operation ID:"), operationIdField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					Integer updatedDivisionId = divisionIdField.getText().isEmpty() ? null
							: Integer.parseInt(divisionIdField.getText());
					Integer updatedOperationId = operationIdField.getText().isEmpty() ? null
							: Integer.parseInt(operationIdField.getText());
					Mission newMission = new Mission(
							0, // ID will be auto-generated
							missionNameField.getText(),
							updatedDivisionId,
							startDateField.getText(),
							endDateField.getText(),
							missionTypeField.getText(),
							updatedOperationId);
					return newMission;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newMission -> {
				try {
					validateDate(newMission.getStartDate());
					validateDate(newMission.getEndDate());
					MissionQueries.insertMission(newMission);
					loadTableData("Mission", searchField.getText());
					statusLabel.setText("New mission added successfully.");
				} catch (IllegalArgumentException e) {
					showErrorAlert("Invalid Input", e.getMessage(), null);
				}
			});
		}

		private void addSoldierFamily() {
			Dialog<SoldierFamily> dialog = new Dialog<>();
			dialog.setTitle("Add Soldier Family");
			dialog.setHeaderText("Enter Soldier Family Details");

			ButtonType saveButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

			TextField relatedSoldierIdField = new TextField();
			TextField nameField = new TextField();
			TextField surnameField = new TextField();
			TextField relationTypeField = new TextField();
			TextField phoneNumberField = new TextField();

			FormatText.restrictToNumeric(relatedSoldierIdField);

			VBox content = new VBox(10);
			content.getChildren().addAll(
					new Label("Related Soldier ID:"), relatedSoldierIdField,
					new Label("Name:"), nameField,
					new Label("Surname:"), surnameField,
					new Label("Relation Type:"), relationTypeField,
					new Label("Phone Number:"), phoneNumberField);

			dialog.getDialogPane().setContent(content);

			dialog.setResultConverter(dialogButton -> {
				if (dialogButton == saveButtonType) {
					SoldierFamily newFamily = new SoldierFamily(
							0, // ID will be auto-generated
							Integer.parseInt(relatedSoldierIdField.getText()),
							nameField.getText(),
							surnameField.getText(),
							relationTypeField.getText(),
							phoneNumberField.getText());
					return newFamily;
				}
				return null;
			});

			dialog.showAndWait().ifPresent(newFamily -> {
				SoldierFamilyQueries.insertSoldierFamily(newFamily);
				loadTableData("Soldier_Family", searchField.getText());
				statusLabel.setText("New soldier family member added successfully.");
			});
		}

		private void deleteSelectedRow() {
			String selectedTable = tableSelector.getSelectionModel().getSelectedItem();
			ObservableList<String> selectedRow = tableView.getSelectionModel().getSelectedItem();

			if (selectedTable == null) {
				showErrorAlert("No Table Selected", "Please select a table to delete from.", null);
				return;
			}

			if (selectedRow == null) {
				showErrorAlert("No Row Selected", "Please select a row to delete.", null);
				return;
			}

			// Confirm deletion
			Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
			confirmationAlert.setTitle("Delete Confirmation");
			confirmationAlert.setHeaderText("Are you sure you want to delete this row?");
			confirmationAlert.setContentText("This action cannot be undone.");

			confirmationAlert.showAndWait().ifPresent(response -> {
				if (response == ButtonType.OK) {
					try {
						int id = Integer.parseInt(selectedRow.get(0)); // Assuming 'id' is the first column
						Queries.deleteRow(selectedTable, id);
						// Reload the table data after deletion
						loadTableData(selectedTable, searchField.getText());
					} catch (NumberFormatException ex) {
						showErrorAlert("Invalid ID", "The selected row has an invalid ID.", ex.getMessage());
					}
				}
			});

			statusLabel.setText("Selected row deleted successfully.");
		}

		private void showErrorAlert(String title, String headerText, String contentText) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle(title);
			alert.setHeaderText(headerText);
			if (contentText != null && !contentText.isEmpty()) {
				alert.setContentText(contentText);
			}
			alert.showAndWait();
		}

		public static void validateDate(String date) throws IllegalArgumentException {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			try {
				LocalDate inputDate = LocalDate.parse(date, formatter);
				LocalDate today = LocalDate.now();

				if (inputDate.isAfter(today)) {
					throw new IllegalArgumentException("Date can not be later than today: " + date);
				}
			} catch (DateTimeParseException e) {
				throw new IllegalArgumentException("Invalid date format, date should be in this format: YYYY-MM-DD: " + date);
			}
		}

		private void hideAllFilterLayouts() {
        soldierFilterLayout.setVisible(false);
				soldierFilterLayout.setManaged(false);
				
				soldierDateFilterLayout.setVisible(false);
				soldierDateFilterLayout.setManaged(false);

        baseFilterLayout.setVisible(false);
        baseFilterLayout.setManaged(false);

        divisionFilterLayout.setVisible(false);
        divisionFilterLayout.setManaged(false);

        vehicleFilterLayout.setVisible(false);
        vehicleFilterLayout.setManaged(false);

        soldierFamilyFilterLayout.setVisible(false);
        soldierFamilyFilterLayout.setManaged(false);

        operationFilterLayout.setVisible(false);
				operationFilterLayout.setManaged(false);
				
				operationDateFilterLayout.setVisible(false);
				operationDateFilterLayout.setManaged(false);

        missionFilterLayout.setVisible(false);
				missionFilterLayout.setManaged(false);
				
				missionDateFilterLayout.setVisible(false);
				missionDateFilterLayout.setManaged(false);

        militaryEventFilterLayout.setVisible(false);
				militaryEventFilterLayout.setManaged(false);
				
				militaryEventDateFilterLayout.setVisible(false);
				militaryEventDateFilterLayout.setManaged(false);

				soldierFilterLayoutWithDate.setVisible(false);
				soldierFilterLayoutWithDate.setManaged(false);

				operationFilterLayoutWithDate.setVisible(false);
				operationFilterLayoutWithDate.setManaged(false);

				missionFilterLayoutWithDate.setVisible(false);
				missionFilterLayoutWithDate.setManaged(false);

				militaryEventFilterLayoutWithDate.setVisible(false);
				militaryEventFilterLayoutWithDate.setManaged(false);
    }
		
		private void setStyleClasses() {
				tableSelector.getStyleClass().add("combo-box");
        searchField.getStyleClass().add("text-field");

        addButton.getStyleClass().add("button");
        editButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button");

				statusLabel.getStyleClass().add("status-label");
				buttonLayout.getStyleClass().add("button-layout");
				
				soldierFilterLayout.getStyleClass().add("hbox");
				soldierDateFilterLayout.getStyleClass().add("hbox");
				baseFilterLayout.getStyleClass().add("hbox");
				divisionFilterLayout.getStyleClass().add("hbox");
				vehicleFilterLayout.getStyleClass().add("hbox");
				soldierFamilyFilterLayout.getStyleClass().add("hbox");
				operationFilterLayout.getStyleClass().add("hbox");
				operationDateFilterLayout.getStyleClass().add("hbox");
				missionFilterLayout.getStyleClass().add("hbox");
				missionDateFilterLayout.getStyleClass().add("hbox");
				militaryEventFilterLayout.getStyleClass().add("hbox");
				militaryEventDateFilterLayout.getStyleClass().add("hbox");

				soldierFilterLayoutWithDate.getStyleClass().add("vbox");
				operationFilterLayoutWithDate.getStyleClass().add("vbox");
				missionFilterLayoutWithDate.getStyleClass().add("vbox");
				militaryEventFilterLayoutWithDate.getStyleClass().add("vbox");
		}
}