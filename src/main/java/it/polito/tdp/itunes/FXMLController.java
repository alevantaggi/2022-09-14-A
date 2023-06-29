/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model; 
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	Album a1= cmbA1.getValue();
    	
    	if(a1==null) {
    		txtResult.setText("Seleziona un valore dalla tendina");
    		return;
    	}
    	
    	txtResult.setText(this.model.getAnalisiComponente(a1));
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String durataS= txtDurata.getText();
    	
    	if(durataS.equals("")) {
    		txtResult.setText("Valore 'd' obbligatorio");
    		return;
    	}
    	Double duration=0.0;
    	
    	try {
			duration= Double.parseDouble(durataS);
		} catch (Exception e) {
			txtResult.setText("La durata deve essere un valore numerico");
		}
    	txtResult.setText(this.model.creaGrafo(duration));
    	
    	List<Album> album= this.model.getAlbumsGraph();
    	
    	cmbA1.getItems().setAll(album); // cmbA1.getItems().clear; cmbA1.getItems().addAll(album);
    	
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
		txtResult.setText("");

    	String dTots= txtX.getText();
    	
    	Album a1= cmbA1.getValue();
    	
    	if(a1==null) {
    		txtResult.setText("Seleziona un valore dalla tendina");
    		return;
    	}
    	
    	Double dTot=0.0;
    	
    	try {
			dTot= Double.parseDouble(dTots);
		} catch (Exception e) {
			txtResult.setText("La durata deve essere un valore numerico");
		}
    	
    	Set<Album> risultato= this.model.getSetAlbum(a1, dTot);
    	
    	if(risultato==null) {
    		txtResult.setText("La durata inserita deve essere maggiore della durata dell'album scelto nella tendina");
    		return;
    	}
    	
    	for(Album a: risultato) {
    		txtResult.appendText(""+a+"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
