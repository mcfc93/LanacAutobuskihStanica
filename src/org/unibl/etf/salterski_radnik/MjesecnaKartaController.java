package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.TipKarte;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class MjesecnaKartaController implements Initializable {
	
	public static MjesecnaKarta karta = new MjesecnaKarta();
	
	@FXML
	private ImageView slika = new ImageView();
	@FXML
	private Label prevoznikLabel = new Label();
	@FXML
	private Label linijaLabel = new Label();
	@FXML
	private Label relacijaLabel = new Label();
	@FXML
	private Label mjesecVazenjaLabel = new Label();
	@FXML
	private Label tipLabel = new Label();
	@FXML
	private Label imeKorisnikaLabel = new Label();
	@FXML
	private static Label serijskiBrojLabel = new Label();
	
	@FXML
    private AnchorPane anchorPane;
	
	@FXML
    private AnchorPane mjesecnaAnchorPane;

	@FXML
    private JFXButton stampajButton;

    @FXML
    private JFXButton nazadButton;
    public static int serijskiBroj;
    private double xOffset=0;
    private double yOffset=0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	serijskiBrojLabel.setMinWidth(Region.USE_PREF_SIZE);
    	serijskiBrojLabel.setVisible(false);
    	imeKorisnikaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	prevoznikLabel.setMinWidth(Region.USE_PREF_SIZE);
    	linijaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	relacijaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	mjesecVazenjaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	tipLabel.setMinWidth(Region.USE_PREF_SIZE);
    	
    	
    	LocalDate localDate = LocalDate.now();
		int mjesecVazenja = (localDate.getDayOfMonth()>25) ? localDate.getMonthValue()+1: localDate.getMonthValue();
    	prevoznikLabel.setText(karta.getNazivPrevoznika());
    	try {
			slika.setImage(new Image(ProdajaKarataController.odabranaSlika.toURI().toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	linijaLabel.setText(karta.getNazivLinije());
    	relacijaLabel.setText(karta.getRelacija().getPolaziste() + " - " + karta.getRelacija().getOdrediste());
    	mjesecVazenjaLabel.setText(String.valueOf(mjesecVazenja) + "\\" + localDate.getYear());
    	tipLabel.setText(karta.getTip().toString().toUpperCase());
    	
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
    void close(ActionEvent event) {
    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
    	ProdajaKarataController.potvrda = false;
		stage.close();
    }

    @FXML
    void stampaj(ActionEvent event) {
    	System.out.println("Stampanje izz mjes cont");
    	System.out.println(karta);
    	serijskiBrojLabel.setVisible(true);
    	serijskiBrojLabel.setText(karta.getIdKarte());
    	
    	
    	
    	int brojKarata = Karta.provjeriBrojKarata(karta, karta.getDatumPolaska());
			Karta.kreirajKartu(karta, brojKarata+1, datum);
			MjesecnaKartaController.kreirajKartu(karta,brojKarata+1,datum, karta.getIme(),karta.getPrezime(),karta.getTip(),karta.getSlika());
			MjesecnaKarta.stampajKartu(karta, brojKarata+1, datum, karta.getIme() + " " + karta.getPrezime(), karta.getTip());
			serijskiBrojLabel.setText(String.valueOf(ProdajaKarataController.idKarte));
			System.out.println("ID: " + serijskiBrojLabel.getText());
			WritableImage image = mjesecnaAnchorPane.snapshot(new SnapshotParameters(), null);
			File file = new File("src\\screenshoot2.png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}catch (IOException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
		
			
			
    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
    	ProdajaKarataController.potvrda = true;

		stage.close();
    }
public static int brojKarata;
public static LocalDate datum;
public static String ime;
public static String preziime;
public static TipKarte tipKarte;
	public static void kreirajKartu(Karta karta, int brojKarata, LocalDate datum, String ime, String prezime, TipKarte tipKarte,
			File odabranaSlika) {
		//MjesecnaKarta.kreirajKartu(karta, brojKarata+1, datum, ime, prezime,tipKarte,odabranaSlika.getPath());	
		//MjesecnaKarta.stampajKartu(karta, brojKarata+1, datum, ime + " " + prezime, tipKarte);
		
		serijskiBroj = ProdajaKarataController.idKarte;
		System.out.println("Serijski broj iz mjesCoontroller:" + serijskiBroj);
		serijskiBrojLabel.setText("fas");

	}

	public static void setSerijskiBroj() {
		// TODO Auto-generated method stub
		
	}
}
