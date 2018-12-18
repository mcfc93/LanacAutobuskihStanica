package org.unibl.etf.administrativni_radnik;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.unibl.etf.util.Util;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MjesecnaKartaController implements Initializable {
	
	@FXML
    private AnchorPane anchorPane;
	
	@FXML
    private AnchorPane mjesecnaAnchorPane;

	@FXML
    private JFXButton stampajButton;

    @FXML
    private JFXButton nazadButton;
    
    private double xOffset=0;
    private double yOffset=0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	anchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
				xOffset = stage.getX() - event.getScreenX();
				yOffset = stage.getY() - event.getScreenY();
			}
		});
						
		anchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
			   	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			    if(!stage.isMaximized()) {
			    	stage.setX(event.getScreenX() + xOffset);
			    	stage.setY(event.getScreenY() + yOffset);
			    	stage.setOpacity(0.8);
			    }
			}
		});
						
		anchorPane.setOnMouseReleased((event) -> {
			Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
			stage.setOpacity(1.0);
		});
	}
    
    @FXML
    void close(MouseEvent event) {
    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
		stage.close();
    }

    @FXML
    void stampaj(ActionEvent event) {
    	Platform.runLater(() -> {
			WritableImage image = mjesecnaAnchorPane.snapshot(new SnapshotParameters(), null);
			File file = new File("src\\screenshoot.png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}catch (IOException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		});
    }
}
