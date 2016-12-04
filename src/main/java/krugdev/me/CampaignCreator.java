package krugdev.me;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import krugdev.me.region.RegionRepository;
import krugdev.me.region.RegionService;
import krugdev.me.region.domain.MarketingCampaign;
import krugdev.me.region.domain.Region;
import krugdev.me.region.domain.RegionSite;

public class CampaignCreator {

	private Stage primaryStage;
	private Scene previousScene;
	private RegionRepository regionRepo;
	private RegionService regionService;
	
	
	public CampaignCreator(Stage primaryStage, Scene previousScene) {
		this.primaryStage = primaryStage;
		this.previousScene = previousScene;
		
	}
	
	public void createNewCampaign(Region region) {
		
		// set layout of the scene
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 800, 400);
		
		GridPane gridpane = new GridPane();
		gridpane.setPadding(new Insets(5));
		gridpane.setHgap(10);
		gridpane.setVgap(10);
		root.setCenter(gridpane);
		
		
		// creates table with region sites which meet the requirements for next marketing campaign
		Label regionSiteLbl = new Label("Region Sites");
		GridPane.setHalignment(regionSiteLbl, HPos.CENTER);
		
		Set<RegionSite> campaignCandidates =  
				regionService.getRegionSitesForCampaign(region);
		ObservableList<RegionSite> campaignCandidatesList = 
				FXCollections.<RegionSite>observableArrayList(campaignCandidates);
		final TableView<RegionSite> campaignCandidatesTableView = getRegionSiteTable(campaignCandidatesList);
		
		// creates the table for region site which will be included in campaign
		Label campaignSiteLbl = new Label("Campaign Sites");
		GridPane.setHalignment(campaignSiteLbl, HPos.CENTER);
		
		ObservableList<RegionSite> campaignRegionSites = 
				FXCollections.<RegionSite>observableArrayList();
		final TableView<RegionSite> campaignSitesTableView = getRegionSiteTable(campaignRegionSites);
		
		// creates buttons to move region sites between tables
		Button selectButton = getMoveSiteButton(campaignCandidatesTableView, campaignCandidatesList, campaignRegionSites, ">");
		Button deselectButton = getMoveSiteButton(campaignSitesTableView, campaignRegionSites, campaignCandidatesList, "<");
		
		VBox buttons = new VBox(5);
		buttons.getChildren().addAll(selectButton, deselectButton);
		
		// creates elements to set campaign parameters
		GridPane campaignName = getCampaignParameterNode("Campaign_Name", "Text");
		GridPane targetMultiplier = getCampaignParameterNode("Target_Multiplier", "Text");
		validateIfMultiplierInRange(targetMultiplier);
		GridPane startDate = getCampaignParameterNode("Start_Date" , "Date");
		GridPane endDate = getCampaignParameterNode("End_Date", "Date");
		
		VBox campaignParameters = new VBox(5);
		campaignParameters.getChildren().addAll(campaignName, targetMultiplier, startDate, endDate);
		
		// creates button to add campaign to region 
		Button createCampaignBtn = new Button("Create Campaign");
		createCampaignBtn.setOnAction(event -> {
			Set<RegionSite> campaignSites = new HashSet<>(campaignRegionSites);
			TextField textField = (TextField)scene.lookup("#Target_Multiplier");
			double targetMultiplierValue = Double.valueOf(textField.getText());
			textField = (TextField)scene.lookup("#Campaign_Name");
			String nameValue = textField.getText();
			DatePicker datePicker = (DatePicker)scene.lookup("#Start_Date");
			LocalDate startDateValue = datePicker.getValue();
			datePicker = (DatePicker)scene.lookup("#End_Date");
			LocalDate endDateValue = datePicker.getValue();
			MarketingCampaign campaign = new MarketingCampaign.Builder(startDateValue, endDateValue)
					.name(nameValue).regionSites(campaignSites).targetMultiplier(targetMultiplierValue).build();
			regionRepo.addMarketingCampaign(region, campaign);
			primaryStage.setScene(previousScene);
		});
		
		// binds graphic elements to the layout
		gridpane.add(regionSiteLbl, 0, 0);
		gridpane.add(campaignSiteLbl, 2, 0);
		gridpane.add(campaignCandidatesTableView, 0, 1);
		gridpane.add(buttons, 1, 1);
		gridpane.add(campaignSitesTableView, 2, 1);
		gridpane.add(campaignParameters, 0, 2);
		gridpane.add(createCampaignBtn, 2, 2);
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void validateIfMultiplierInRange(GridPane pane) {
		TextField textField = (TextField)pane.lookup("#Target_Multiplier");
		textField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
	        if (!newValue) { 
	            if(!textField.getText().matches("[1]\\.[0-9]")){             
	                textField.setText("1.0");
	                pane.setStyle("-fx-border-color: red");
	            } else {
	            	pane.setStyle(null);
	            }
	        }
	    });	
	}

	@SuppressWarnings("unchecked")
	private TableView<RegionSite> getRegionSiteTable(ObservableList<RegionSite> inputObservableList) {
		
		final TableView<RegionSite> tableView = new TableView<>();
		tableView.setPrefWidth(360);
		tableView.setItems(inputObservableList);
		
		TableColumn<RegionSite, String> siteNameCol = new TableColumn<>("Site Name");
		siteNameCol.setCellValueFactory(new PropertyValueFactory<RegionSite, String>("siteName"));
		siteNameCol.setPrefWidth(tableView.getPrefWidth() * 3/4);

		TableColumn<RegionSite, String> sitePriorityCol = new TableColumn<>("Priority");
		sitePriorityCol.setCellValueFactory(new PropertyValueFactory<RegionSite, String>("priorityString"));
		sitePriorityCol.setPrefWidth(tableView.getPrefWidth() * 1/4);
		sitePriorityCol.setStyle("-fx-alignment: CENTER;");
	
		tableView.getColumns().setAll(siteNameCol, sitePriorityCol);
		return tableView;
	}
	
	private Button getMoveSiteButton(TableView<RegionSite> inputTable,ObservableList<RegionSite> inputList, 
				ObservableList<RegionSite> outputList, String sign) {
		
		Button button = new Button(sign);
		button.setOnAction(event -> {
			RegionSite potential = inputTable.getSelectionModel().getSelectedItem();
			if (potential != null) {
				inputTable.getSelectionModel().clearSelection();
				inputList.remove(potential);
				outputList.add(potential);
			}
		});		
		return button;
	}
	
	private <T> GridPane getCampaignParameterNode(String nodeId, String nodeType) {
		GridPane nodePane = new GridPane();
		nodePane.setPadding(new Insets(5));
		nodePane.setHgap(10);
		nodePane.setVgap(10);
		nodePane.setAlignment(Pos.CENTER_LEFT);
		Label nodeLabel = new Label(nodeId + ":");
		nodeLabel.setPrefWidth(120);
		nodePane.add(nodeLabel, 0, 0);
		if (nodeType.equals("Text")) {
			TextField nodeText = new TextField();
			nodeText.setId(nodeId);
			nodePane.add(nodeText, 1, 0);
		} else if (nodeType.equals("Date")){
			DatePicker datePicker = new DatePicker(LocalDate.now());
			datePicker.setId(nodeId);
			nodePane.add(datePicker, 1, 0);
		}
		return nodePane;
	}
	
	public void setRegionRepository(RegionRepository regionRepo) {
		this.regionRepo = regionRepo;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}
}
