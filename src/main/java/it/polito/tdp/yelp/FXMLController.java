/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.event.IIOReadWarningListener;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;
	private boolean entrato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnMiglioramento"
    private Button btnMiglioramento; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiLocali(ActionEvent event) {
    	this.cmbLocale.getItems().clear();
    	String citta = this.cmbCitta.getValue();
    	if(citta != null) {
    		List<Business> businesses = model.getLocaliCitta(citta);
    		cmbLocale.getItems().addAll(businesses);    		
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	if(cmbCitta.getValue() != null && cmbLocale.getValue() != null) {
    		entrato = true;
    		txtResult.clear();
        	model.creaGrafo(cmbCitta.getValue(), cmbLocale.getValue());
        	txtResult.appendText("Grafo creato con " + model.getNVertici() + " vertici e " + model.getNArchi() + " archi\n\n");
        	txtResult.appendText(model.recArchiUscentiMax());
    	}
    }

    @FXML
    void doTrovaMiglioramento(ActionEvent event) {
    	txtResult.clear();
    	if(entrato) {
    		List<Review> result = model.trovaSequenza();
    		for(Review review : result) {
    			txtResult.appendText(review + "\n");
    		}
    		
    		Review r0 = result.get(0);
    		Review rL = result.get(result.size()-1);
    		int giorni = (int)ChronoUnit.DAYS.between(r0.getDate(), rL.getDate());
    		txtResult.appendText("Giorni tra prima e ultima recensione: " + giorni);
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMiglioramento != null : "fx:id=\"btnMiglioramento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbCitta.getItems().addAll(model.getCity());
    }
}
