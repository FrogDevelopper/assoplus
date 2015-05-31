package fr.frogdevelopment.assoplus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;

public class Main extends Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

	@Override
	public void start(Stage primaryStage) throws Exception {
		LOGGER.info("Chargement de la configuration");

		Parent root = load("/fxml/main.fxml");

		primaryStage.setTitle("AssoManager");
		primaryStage.setScene(new Scene(root, 600, 600));

		LOGGER.info("Ouverture de l'application");
		primaryStage.show();
	}

	private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("/spring.xml");

	public static Parent load(String url) {
		try (InputStream fxmlStream = Class.class.getResourceAsStream(url)) {
			FXMLLoader loader = new FXMLLoader();
			loader.setControllerFactory(CONTEXT::getBean);
			loader.setLocation(Class.class.getResource("/fxml/"));
			return loader.load(fxmlStream);
		} catch (IOException ioException) {
			throw new RuntimeException(ioException);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
