package com.easyget.terminal.base;

import com.easyget.terminal.base.provider.embed.EmbedProvider;
import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@SuppressWarnings("restriction")
public class TakeTest extends Application {
	
	public static void main(String[] args) {
		LauncherImpl.launchApplication(TakeTest.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try {
			final EmbedProvider provider = new TakeProvider().getProvider();
			final VBox root = (VBox) provider.getFxmlLoader().load();
			final Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}