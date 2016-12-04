package krugdev.me;

import java.util.logging.Level;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import krugdev.me.region.RegionRepository;
import krugdev.me.region.RegionService;
import krugdev.me.region.domain.Region;
import krugdev.me.siteService.SiteRepository;

public class RegionApp extends Application {
	
	private static final DBService DB_SERVICE = new DBService("sax_her");
	private RegionRepository regionRepo;
	private RegionService regionService;
	SiteRepository siteRepo;
	
	public RegionApp() {
		regionRepo = new RegionRepository(DB_SERVICE);
		regionService = new RegionService();
		siteRepo = new SiteRepository(DB_SERVICE, null);
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		RegionApp regionApp = new RegionApp();
		Application.launch();
		DB_SERVICE.close();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Region Manager");
		
		Region region = regionRepo.newRegion("London");
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 300, 250);
		MenuBar menuBar = new MenuBar();
		Menu campaignMenu = new Menu("Campaigns");
		MenuItem createCampaign = new MenuItem("Create New Campaign");
		campaignMenu.getItems().add(createCampaign);
		menuBar.getMenus().add(campaignMenu);
		root.setTop(menuBar);
		
		createCampaign.setOnAction((ActionEvent event) -> {
			CampaignCreator campaignCreator = new CampaignCreator(primaryStage, scene);
			campaignCreator.setRegionRepository(regionRepo);
			campaignCreator.setRegionService(regionService);
			campaignCreator.createNewCampaign(region);
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
