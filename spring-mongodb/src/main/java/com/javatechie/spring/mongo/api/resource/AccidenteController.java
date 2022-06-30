package com.javatechie.spring.mongo.api.resource;

import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation.GroupOperationBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.javatechie.spring.mongo.api.model.Accidente;
import com.javatechie.spring.mongo.api.repository.AccidenteRepository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

import com.mongodb.client.model.geojson.Position;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


import org.springframework.data.mongodb.core.geo.*;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RestController
public class AccidenteController {

	@Autowired
	private AccidenteRepository repository;
	 @Autowired
	    private MongoTemplate mongoTemplate;
	 
	//Devolver todos los accidentes ocurridos entre 2 fechas dadas
	@GetMapping("/entreFechas/{desde}/{hasta}")
	public List<Accidente> getEntreFechas(@PathVariable String desde, @PathVariable String hasta) throws ParseException {
		return repository.findFechas(desde, hasta);
	}
	
	//Convierte los resultado en el formato del arreglo que va a retornar
	private List<Accidente> getContentFromGeoResults(GeoResults<Accidente> geoResults) {
        List<Accidente> list = new ArrayList<Accidente>();
        for (GeoResult<Accidente> result : geoResults.getContent()) {
            list.add(result.getContent());
        }
        return list;
    }
	
	
	@GetMapping("/buscarPunto2/{latitud}/{longitud}/{radio}")
	public List<Accidente> buscarPunto2(@PathVariable Double latitud, @PathVariable Double longitud, @PathVariable Double radio) throws ParseException {
		Point center = new Point(latitud,longitud);
		NearQuery query = NearQuery.near(center).maxDistance(new Distance(radio / 1000, Metrics.KILOMETERS));
		GeoResults<Accidente> targets = mongoTemplate.geoNear(query, Accidente.class);
		return getContentFromGeoResults(targets);
	}
	
	@GetMapping("/puntosMasPeligrosos/{radio}")
	public List<Accidente> puntosMasPeligrosos(@PathVariable Double radio) throws ParseException {
		 //Me quedo solamente con los primeros 20 para poder probar, ya que la memoria de la computadora no me respondia 
		 List<Accidente> accidents = mongoTemplate.findAll(Accidente.class).subList(0, 20);
		 List<Accidente> list = new ArrayList<Accidente>();		 
		 Map<String, Integer> map = new HashMap<String, Integer>();
		 for (int i=0;i<accidents.size();i++) {
			//Para cada punto retorno la cantidad de puntos cercanos que estan a cierto radio recibido por parametro, 
			Point center = new Point(Double.parseDouble(accidents.get(i).getStartLat()),Double.parseDouble(accidents.get(i).getStartLng()));
			NearQuery query = NearQuery.near(center).maxDistance(new Distance(radio / 1000, Metrics.KILOMETERS));
			GeoResults<Accidente> targets = mongoTemplate.geoNear(query, Accidente.class);
			list =  getContentFromGeoResults(targets);
			for (int j=0;j<list.size();j++) {
				map.put(list.get(j).getId(),list.size());
			 }
		 }
		//Devuelvo los 4 puntos 
		return accidents.subList(0, 4);
	}
	
		//Dado un punto geográfico y un radio (expresado en kilómetros) devolver todos los accidentes ocurridos dentro del radio.
		@GetMapping("/distanciaPromedio")
		public String distanciaPromedio() throws ParseException {
			//Retonar un objeto accidente que contiene la distancia promedio en su atributo Severity
			List<Accidente> d = repository.avgDistance();
			//Utilizo el atributo Severity para retornar el atributo de la consulta que retorna el promedio
			return "El promedio es:" + d.get(0).getSeverity();
		}
		
		//Determinar las condiciones más comunes en los accidentes.
		@GetMapping("/condicionesComunes")
		public List<String> condicionesComunes() throws ParseException {
					List<Accidente> d = repository.condicionComunClima();
					List<String> resultado = new ArrayList<String>();
					//Convierto el resultado que devuelve el repositorio a un formato de string para que retorne la API
					for (int i=0;i<d.size();i++) {
						String s =  d.get(i).getId() + ":" + d.get(i).getSeverity(); 
						resultado.add(s);	
					    }
					return resultado;
		}

}
