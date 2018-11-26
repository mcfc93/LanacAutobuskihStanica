package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class OtkazivanjeRezervacijeController implements Initializable {

	@FXML
	private JFXTextField serijskiBrojTextField = new JFXTextField();
	@FXML
	private JFXButton pretragaKarataButton = new JFXButton();
	@FXML
	private JFXButton stornirajButton = new JFXButton();
	@FXML
	private TextField linijaTextField = new TextField();
	@FXML
	private TextField relacijaTextField = new TextField();
	@FXML
	private TextField datumTextField = new TextField();
	@FXML
	private TextField cijenaTextField  = new TextField();
	@FXML
	private ImageView checkMarkImageView = new ImageView();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serijskiBrojTextField.getValidators().addAll(Util.requredFieldValidator(serijskiBrojTextField),Util.integerValidator(serijskiBrojTextField));
		checkMarkImageView.setVisible(false);
		stornirajButton.setDisable(true);
	}
	
	@FXML
	public void pretragaKarata() {
		if(serijskiBrojTextField.validate())
		{
			Karta trazenaKarta = Karta.pronadjiKartu(Integer.parseInt(serijskiBrojTextField.getText()));
			if(trazenaKarta==null)
				showAlertPogresanSerijskiBroj();
			stornirajButton.setDisable(false);
			cijenaTextField.setText(trazenaKarta.getCijena()  + "KM");
			relacijaTextField.setText(trazenaKarta.getRelacija().getPolaziste() + " - " + trazenaKarta.getRelacija().getOdrediste());
			datumTextField.setText(trazenaKarta.getDatumIzdavanja().toString());
			linijaTextField.setText(trazenaKarta.getLinija().getNazivLinije());				
		}
		else 
			showAlertPogresanSerijskiBroj();	
	
	
	}
	
	public void showAlertPogresanSerijskiBroj() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("GRESKA");
			alert.setHeaderText("Pogresan serijski broj!");
			alert.showAndWait();
		}
		
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kupovina");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}

	@FXML
	public void storniraj() {
			if(showPotvrda()) {
				Connection c = null;
				PreparedStatement s1 = null;
				PreparedStatement s2 = null;
				String sqlKarta = "update karta set Stanje='Izbrisano' where SerijskiBroj=?";
				String sqlRezervacija = "update rezervacija set Stanje='Izbrisano' where SerijskiBroj=?";
				try {
					c = Util.getConnection();
					s1 = Util.prepareStatement(c, sqlKarta, false, Integer.parseInt(serijskiBrojTextField.getText()));
					s2 = Util.prepareStatement(c, sqlRezervacija, false, Integer.parseInt(serijskiBrojTextField.getText()));
					if(s1.executeUpdate()==1 && s2.executeUpdate()==1)
						showCheckMark();
					s2.close();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					Util.close(s1, c);
				}
				cijenaTextField.clear();
				relacijaTextField.clear();
				datumTextField.clear();
				linijaTextField.clear();
				serijskiBrojTextField.clear();
				serijskiBrojTextField.resetValidation();
			}
	
	}

	
	public void showCheckMark() {
		// TODO Auto-generated method stub
		checkMarkImageView.setVisible(true);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(3),
				new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
	            checkMarkImageView.setVisible(false);
	        }
	    }));
		timeline.play();
	}
}
