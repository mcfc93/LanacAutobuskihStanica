package org.unibl.etf.salterski_radnik;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import org.unibl.etf.karta.Karta;
import org.unibl.etf.karta.MjesecnaKarta;
import org.unibl.etf.karta.TipKarte;
import org.unibl.etf.prijava.PrijavaController;
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
import java.awt.image.BufferedImage;
import com.itextpdf.text.pdf.BarcodeEAN;

public class MjesecnaKartaController implements Initializable {
	
	public static MjesecnaKarta karta;
	//public static LocalDate datum;
	public static final int JANUAR = 1;
	public static final int DECEMBAR = 12;
	
	@FXML
	private ImageView barcodeImageView;
	
	@FXML
    private Label barcodeLabel;

	@FXML
	private ImageView exitImageView = new ImageView();
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
	private Label imePrezimeLabel;
	
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
    public static int mjesecVazenja;
    public static String mjesecVazenjaString;
	public static LocalDate localDate;

	@FXML
	public void nazad(ActionEvent e) {
		ProdajaKarataController.potvrda = false;
    	((Stage)((Node)e.getSource()).getScene().getWindow()).close();

	}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	localDate = LocalDate.now();
    	mjesecVazenjaString =
        		(localDate.getDayOfMonth() >= 25?
        			(((localDate.getMonthValue()+1)%12) + "-" + (localDate.getMonthValue()==DECEMBAR?localDate.getYear()+1:localDate.getYear())):
        				(localDate.getMonthValue() + "-" + localDate.getYear()));
       	
    	prevoznikLabel.setText(karta.getRelacija().getLinija().getPrevoznik().getNaziv());
    		
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
    	
		stampajButton.setDefaultButton(true);
    	nazadButton.setCancelButton(true);   
    	
    	
    	 	
    	if(TipKarte.ĐAČKA.equals(karta.getTip())) {
        	//mjesecnaAnchorPane.getStylesheets().add(getClass().getResource("/org/unibl/etf/administrator/administrator.css").toExternalForm());
        	mjesecnaAnchorPane.getStyleClass().add("mjesecnaDjacka");
        } else if(TipKarte.PENZIONERSKA.equals(karta.getTip())) {
        	mjesecnaAnchorPane.getStyleClass().add("mjesecnaPenzionerska");
        } else {
        	mjesecnaAnchorPane.getStyleClass().add("mjesecnaRadnicka");
        }
    	
    	barcodeLabel.setMinWidth(Region.USE_PREF_SIZE);
    	barcodeLabel.setVisible(true);
    	imeKorisnikaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	prevoznikLabel.setMinWidth(Region.USE_PREF_SIZE);
    	linijaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	relacijaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	mjesecVazenjaLabel.setMinWidth(Region.USE_PREF_SIZE);
    	tipLabel.setMinWidth(Region.USE_PREF_SIZE);
   
		prevoznikLabel.setText(karta.getRelacija().getLinija().getPrevoznik().getNaziv());

		
		
		
    	if(!ProdajaKarataController.produzavanjeKarte) {
    		try {
    			slika.setImage(new Image(ProdajaKarataController.odabranaSlika.toURI().toString()));
    		} catch (Exception e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		}
    	}
    	else {
    		try {
    			slika.setImage(new Image(karta.getSlika().toURI().toString()));
    		} catch (Exception e) {
    			Util.LOGGER.log(Level.SEVERE, e.toString(), e);
    		}
    		linijaLabel.setText(karta.getRelacija().getLinija().getNazivLinije());
    		barcodeLabel.setVisible(true);
    		barcodeLabel.setText(String.format("%013d", karta.getSerijskiBroj()));
    		BarcodeEAN codeEAN = new BarcodeEAN();
    		codeEAN.setCode(String.format("%013d", karta.getSerijskiBroj()));
            codeEAN.setCodeType(BarcodeEAN.EAN13); 
            codeEAN.setBarHeight(40);
            java.awt.Image img = codeEAN.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
            BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, null);
    	
        	barcodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    	}

    	linijaLabel.setText(karta.getRelacija().getLinija().getNazivLinije());
    	relacijaLabel.setText(karta.getRelacija().getPolaziste().getNazivStajalista() + " - " + karta.getRelacija().getOdrediste().getNazivStajalista());
    	mjesecVazenjaLabel.setText(mjesecVazenjaString);
    	tipLabel.setText(karta.getTip().toString().toUpperCase());
    	imePrezimeLabel.setText(karta.getIme() + " " + karta.getPrezime());
    	
		
	}
    
    
    @FXML
    void close(MouseEvent event) {
    	Stage stage=((Stage)((Node)event.getSource()).getScene().getWindow());
    	ProdajaKarataController.potvrda = false;
		stage.close();
    }

    
    @FXML
    void stampaj(ActionEvent event) {

    	if(ProdajaKarataController.produzavanjeKarte) {
        	barcodeLabel.setVisible(true);
        	barcodeLabel.setText(String.valueOf(karta.getSerijskiBroj()));
    		ProdajaKarataController.idKarte = Karta.kreirajKartu(karta, localDate);
    		System.out.println("Serijski broj kreirane karte: " + ProdajaKarataController.idKarte);
    		karta.setSerijskiBroj(ProdajaKarataController.idKarte);


    		MjesecnaKarta.stampajKartu(karta, 0, localDate, karta.getIme() + " " + karta.getPrezime(), karta.getTip());
    		
    		BarcodeEAN codeEAN = new BarcodeEAN();
    		System.out.println(String.format("%013d", karta.getSerijskiBroj()));
	    	codeEAN.setCode(String.format("%013d", karta.getSerijskiBroj()));
            codeEAN.setCodeType(BarcodeEAN.EAN13); 
            codeEAN.setBarHeight(40);
            //kreiranje slike
            java.awt.Image img = codeEAN.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
            BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            bufferedImage.getGraphics().drawImage(img, 0, 0, null);
    		barcodeLabel.setVisible(true);
    		barcodeLabel.setText(String.format("%013d", karta.getSerijskiBroj()));
        	barcodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        	SnapshotParameters parameters = new SnapshotParameters();
 
			WritableImage image = mjesecnaAnchorPane.snapshot(parameters, null);
        	
        	File file = new File("karte\\slike\\mjesecna_" + String.format("%013d",karta.getIdMjesecneKarte()) + "-" + mjesecVazenjaString + ".png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}catch (IOException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
    		
    		
    	}
    	else {

	    	barcodeLabel.setVisible(true);
	    	barcodeLabel.setText(String.valueOf(karta.getSerijskiBroj()));
			karta.setJIBStanice(PrijavaController.autobuskaStanica.getJib());

	    	Karta.kreirajKartu(karta, localDate);
	    	karta.setSerijskiBroj(ProdajaKarataController.idKarte);
			ProdajaKarataController.idMjesecneKarte = MjesecnaKarta.kreirajKartu(karta);
			serijskiBroj = ProdajaKarataController.idKarte;
			System.out.println("SERIJSKI BROJ: " + ProdajaKarataController.idKarte);
			System.out.println("Serijski mjesecne: " + ProdajaKarataController.idMjesecneKarte);
			MjesecnaKarta.stampajKartu(karta, 0,localDate, karta.getIme() + " " + karta.getPrezime(), karta.getTip());
			barcodeLabel.setText(String.format("%013d",ProdajaKarataController.idMjesecneKarte));
			ProdajaKarataController.potvrda = true;
			BarcodeEAN codeEAN = new BarcodeEAN();
	    	codeEAN.setCode(String.format("%013d", ProdajaKarataController.idMjesecneKarte));
	        codeEAN.setCodeType(BarcodeEAN.EAN13); 
	        codeEAN.setBarHeight(40);
	        java.awt.Image img = codeEAN.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
	        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
	        bufferedImage.getGraphics().drawImage(img, 0, 0, null);
	        barcodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
	        
	        SnapshotParameters parameters = new SnapshotParameters();
	      
			WritableImage image = mjesecnaAnchorPane.snapshot(parameters, null);
			File file = new File("karte\\slike\\mjesecna_" + String.format("%013d", ProdajaKarataController.idMjesecneKarte) + "-" + mjesecVazenjaString + ".png");
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			}catch (IOException e) {
				Util.LOGGER.log(Level.SEVERE, e.toString(), e);
			}
			
			
	    }

    	Platform.runLater(() -> {
    		Util.getNotifications("Obavještenje", "Mjesečna karta kreirana.", "Information").show();
    	});
    	
    	((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
