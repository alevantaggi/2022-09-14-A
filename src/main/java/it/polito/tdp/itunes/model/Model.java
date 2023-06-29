package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultEdge> grafo;
	private Map<Integer, Album> albumIdMap;
	
	// Ricorsione 
	private Set<Album> migliore;
	private Set<Album> componenteConnessa;
	
	public Model() {
		this.dao= new ItunesDAO();
	}
	
	public String creaGrafo(Double duration) {		
		this.grafo= new SimpleDirectedGraph<>(DefaultEdge.class);
		List<Album> vertici= this.dao.getAlbumsWithDuration(duration);
		this.albumIdMap= new HashMap<>();
		
		for(Album a: vertici)
			this.albumIdMap.put(a.getAlbumId(), a);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		List<Arco> archi= this.dao.getCompatibleAlbums();
		
		for(Arco ar: archi) {
			if(this.albumIdMap.get(ar.getSource())!=null && this.albumIdMap.get(ar.getTarget())!=null)
				this.grafo.addEdge(albumIdMap.get(ar.getSource()), albumIdMap.get(ar.getTarget()));
		}
		return "Grafo creato\n#Vertici: "+this.grafo.vertexSet().size()+"\n#Archi: "+this.grafo.edgeSet().size();
	}
	
	public List<Album> getAlbumsGraph(){
		List<Album> risultato= new ArrayList<>(this.grafo.vertexSet());
		Collections.sort(risultato);
		return risultato;		
	}
	
	public String getAnalisiComponente(Album album) {
		Double durata=0.0;
		ConnectivityInspector<Album, DefaultEdge> inspector= new ConnectivityInspector<>(this.grafo);
		Set<Album> compConn= new HashSet<>(inspector.connectedSetOf(album));
		
		for(Album a: compConn)
			durata+=a.getDurata();
		
		return "Componente connessa: "+album+"\nDimensione complessiva: "+compConn.size()+"\nDurata componente: "+durata;
	}
	
	public Set<Album> getSetAlbum(Album album, Double maxDurata){
		if(maxDurata< album.getDurata()) {
			return null;
		}
		
		
		this.migliore= new HashSet<>();
		this.migliore.add(album);
		
		ConnectivityInspector<Album, DefaultEdge> inspector= new ConnectivityInspector<>(this.grafo);
		this.componenteConnessa= new HashSet<>(inspector.connectedSetOf(album));
		
		List<Album> parziale= new ArrayList<>();
		parziale.add(album);
		componenteConnessa.remove(album);
		
		ricorsione(parziale, maxDurata,album.getDurata());
		
		return this.migliore;
	}
	
	private void ricorsione(List<Album> parziale, Double maxDurata, Double durAttuale) {
		if(parziale.size()> this.migliore.size()) {
			this.migliore= new HashSet<>(parziale); 
		}
		
		for(Album a: this.componenteConnessa) {
			if(! parziale.contains(a) && (durAttuale+a.getDurata())<= maxDurata) {
				parziale.add(a);
				ricorsione(parziale, maxDurata, durAttuale+a.getDurata());
				parziale.remove(parziale.size()-1);
			}
		}
		return;
	}
	
	
}
