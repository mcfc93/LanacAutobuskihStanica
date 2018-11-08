package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.TipKarte;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class KreiranjeMjesecneKarteController implements Initializable {	
	
	
	public static Set<String> relacijeSet = new HashSet<>();
	public static List<Integer> linijeIDList = new ArrayList<>();
	public static ObservableList<Karta> karteObs = FXCollections.observableArrayList();
	public static File odabranaSlika = null;
	
	@FXML
	private TableView<Karta> linijeTable = new TableView<>();
	@FXML
	private TableColumn<Karta,String> nazivLinijeColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,Double> cijenaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,LocalTime> vrijemePolaskaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,LocalTime> vrijemeDolaskaColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,String> prevoznikColumn = new TableColumn<>();
	@FXML
	private TableColumn<Karta,Integer> peronColumn = new TableColumn<>();
	@FXML
	private JFXButton odaberiSlikuButton = new JFXButton();
	@FXML
	private Label slikaKorisnikaLabel;
	@FXML
	private JFXTextField imeField = new JFXTextField();
	@FXML
	private JFXTextField prezimeField = new JFXTextField();
	@FXML
	private JFXTextField nazivSlikeTextField;
	@FXML
	private JFXButton kreirajButton = new JFXButton();
	@FXML
	private JFXButton pretragaButton = new JFXButton();
	@FXML
	private JFXComboBox<TipKarte> tipKarteComboBox;
	@FXML
	private JFXTextField polazisteTextField;
	@FXML
	private JFXTextField odredisteTextField;
	
	@FXML
	public ImageView slikaKorisnika;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		kreirajButton.disableProperty().bind(Bindings.isEmpty(linijeTable.getSelectionModel().getSelectedItems()));
		linijeTable.setItems(karteObs);
		ProdajaKarataController.ucitajRelacije();
		linijeTable.setPlaceholder(new Label("Odaberite relaciju"));
		

		nazivSlikeTextField.setEditable(false);
		tipKarteComboBox.setItems(FXCollections.observableArrayList(TipKarte.values()));
		kreirajButton.disableProperty().bind(Bindings.createBooleanBinding(
			    () -> imeField.getText().isEmpty() || prezimeField.getText().isEmpty() || 
			    	  nazivSlikeTextField.getText().isEmpty() || 
			    	  tipKarteComboBox.getSelectionModel().getSelectedItem() == null, 
			    imeField.textProperty(), prezimeField.textProperty(), nazivSlikeTextField.textProperty(),
			    tipKarteComboBox.getSelectionModel().selectedItemProperty()
			    ));
	}
	
	@FXML
	public void odaberiSliku() {
		System.out.println("fasf");
		FileChooser fc  = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG Files", "*.png"));
	    odabranaSlika = fc.showOpenDialog(null);
		if(odabranaSlika!=null) {
			try {
				slikaKorisnika.setImage(new Image(new FileInputStream(odabranaSlika)));
				nazivSlikeTextField.setText(odabranaSlika.getName());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	public void kreirajKartu() {
    

	}
	

}
