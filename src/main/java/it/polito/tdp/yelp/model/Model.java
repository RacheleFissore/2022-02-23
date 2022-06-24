package it.polito.tdp.yelp.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mariadb.jdbc.internal.com.send.parameters.BigDecimalParameter;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<Review, DefaultWeightedEdge> grafo;
	private YelpDao dao;
	private Map<String, Review> idMap;
	private List<Review> best;
	
	public Model() {
		dao = new YelpDao();
		idMap = new HashMap<>();
		
		for(Review review : dao.getAllReviews()) {
			idMap.put(review.getReviewId(), review);
		}
	}
	
	public List<String> getCity() {
		return dao.getCity();
	}
	
	public List<Business> getLocaliCitta(String citta) {
		return dao.getLocaliCitta(citta);
	}
	
	public void creaGrafo(String citta, Business business) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getVertici(citta, business, idMap));
		
		for(Review r1 : grafo.vertexSet()) {
			for(Review r2 : grafo.vertexSet()) {
				if(!r1.equals(r2)) {
					if(r1.getDate().isBefore(r2.getDate())) {
						int peso = (int)ChronoUnit.DAYS.between(r1.getDate(), r2.getDate());
						if(peso > 0) {
							Graphs.addEdgeWithVertices(grafo, r1, r2, peso);
						}
					}
				}
				
			}
		}
	}
	
	public String recArchiUscentiMax() {
		int max = 0;
		Review vMax = null;
		String string = "";
		
		for(Review review : grafo.vertexSet()) {
			int archiUsc = grafo.outgoingEdgesOf(review).size();
			if(archiUsc > max) {
				max = archiUsc;
				vMax = review;
			}
		}
		
		for(Review review : grafo.vertexSet()) {
			int archiUsc = grafo.outgoingEdgesOf(review).size();
			if(archiUsc == max) {
				string += vMax.getReviewId() + " # ARCHI: " + archiUsc + "\n";
			}
		}
		
		return string;
	}
	
	public Integer getNVertici() {
		return grafo.vertexSet().size();
	}
		 
	public Integer getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Review> trovaSequenza() {
		best =  new ArrayList<>();
		List<Review> parziale = new ArrayList<>();
		cerca(parziale, 0);
		
		return best;
	}

	private void cerca(List<Review> parziale, int livello) {
		if(parziale.size() > best.size()) {
			best = new ArrayList<>(parziale);
			return;
		}
		
		for(Review review : grafo.vertexSet()) {
			if(!parziale.contains(review)) {
				if(parziale.size() == 0) {
					parziale.add(review);
					cerca(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
				else {
					if(review.getStars() >= parziale.get(parziale.size()-1).getStars()) {
						parziale.add(review);
						cerca(parziale, livello+1);
						parziale.remove(parziale.size()-1);
					}
				}
				
				
				
			}
		}
		
	}
}
