package org.unibl.etf.salterski_radnik;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.util.Util;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class OtkazivanjeRezervacijeController implements Initializable {

	private static Karta jednokratnaKarta;
	private static MjesecnaKarta mjesecnaKarta;
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
	@FXML
	private JFXRadioButton jednokratnaKartaRadioButton;
	@FXML
	private JFXRadioButton mjesecnaKartaRadioButton;
	@FXML
	private ToggleGroup toggleGroup = new ToggleGroup();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serijskiBrojTextField.getValidators().addAll(Util.requiredFieldValidator(serijskiBrojTextField),Util.integerValidator(serijskiBrojTextField));
		checkMarkImageView.setVisible(false);
		stornirajButton.setDisable(true);
	}
	
	@FXML
	public void pretragaKarata() {
		if(serijskiBrojTextField.validate())
		{
			//otkazivanje jednokratne
			if(jednokratnaKartaRadioButton.isSelected()) {
				jednokratnaKarta = Karta.pronadjiKartu(Integer.parseInt(serijskiBrojTextField.getText()));
				if(jednokratnaKarta==null) {
			    	Util.getNotifications("Greška", "Pogrešan serijski broj ili karta već stornirana.", "Error").show();
					return;
				}
				stornirajButton.setDisable(false);
				cijenaTextField.setText(jednokratnaKarta.getCijena()  + "KM");
				relacijaTextField.setText(jednokratnaKarta.getRelacija().getPolaziste().getNazivStajalista() + " - " + jednokratnaKarta.getRelacija().getOdrediste().getNazivStajalista());
				datumTextField.setText(jednokratnaKarta.getDatumIzdavanja().toString());
				linijaTextField.setText(jednokratnaKarta.getRelacija().getLinija().getNazivLinije());		
				// otkazivanje jednokratne
			}
			else {
				System.out.println("Storniranje mjesecne");
				mjesecnaKarta = MjesecnaKarta.pronadjiKartu(Integer.parseInt(serijskiBrojTextField.getText()));
				if(mjesecnaKarta==null) {
			    	Util.getNotifications("Greška", "Pogrešan serijski broj ili karta već stornirana.", "Error").show();
			    	return;
				}
				System.out.println("Mjesecna cijena: " + mjesecnaKarta.getCijena());
				System.out.println(mjesecnaKarta.getRelacija().getCijenaJednokratna());
				System.out.println(mjesecnaKarta.getRelacija().getCijenaMjesecna());
				
				stornirajButton.setDisable(false);
				cijenaTextField.setText(mjesecnaKarta.getRelacija().getCijenaMjesecna() + "KM");
				relacijaTextField.setText(mjesecnaKarta.getRelacija().getPolaziste() + " - " + mjesecnaKarta.getRelacija().getOdrediste());
				datumTextField.setText(mjesecnaKarta.getDatumIzdavanja().toString());
				linijaTextField.setText(mjesecnaKarta.getRelacija().getLinija().getNazivLinije());		
			}
			
		}
		else 
	    	Util.getNotifications("Greška", "Pogrešan serijski broj.", "Error").show();
	
	
	}
	
	/*public void showAlertPogresanSerijskiBroj() {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("GRESKA");
			alert.setHeaderText("Pogresan serijski broj ili karta vec stornirana!");
			alert.showAndWait();
		}*/
		
	public boolean showPotvrda() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kupovina");
		alert.setHeaderText("Da li ste sigurni?");
		Optional<ButtonType> action = alert.showAndWait();
		return action.get().equals(ButtonType.OK);
	}

	@FXML
	public void storniraj() {
		// storniraj jednokratnu
		if(jednokratnaKartaRadioButton.isSelected()) {
			if(showPotvrda()) {
				
				if(Karta.stornirajKartu(jednokratnaKarta.getSerijskiBroj()))
					Util.getNotifications("Uspjeh", "Uspješno stornirana karta.", "Confirmation").show();
					cijenaTextField.clear();
					relacijaTextField.clear();
					datumTextField.clear();
					linijaTextField.clear();
					serijskiBrojTextField.clear();
					serijskiBrojTextField.resetValidation();
			}
		}
			//end storniraj jednokratnu
			else {
				System.out.println("Storniraj mjesecnu...");
				if(showPotvrda()) {
						MjesecnaKarta.storniraj(mjesecnaKarta);
				    	Util.getNotifications("Uspjeh", "Uspješno stornirana mjesečna karta.", "Confirmation").show();
				    	cijenaTextField.clear();
						relacijaTextField.clear();
						datumTextField.clear();
						linijaTextField.clear();
						serijskiBrojTextField.clear();
						serijskiBrojTextField.resetValidation();
				}
			}
	
	}

	/*@FXML
	public void pregledMjesecneKarte() {
		try {
	        Parent root = FXMLLoader.load(getClass().getResource("/org/unibl/etf/administrativni_radnik/MjesecnaKartaView.fxml"));
			Scene scene = new Scene(root);
			Stage stage=new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
        } catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	/*public void showCheckMark() {
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
	}*/
}
