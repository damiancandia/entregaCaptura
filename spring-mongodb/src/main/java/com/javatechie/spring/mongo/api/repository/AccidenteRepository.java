package com.javatechie.spring.mongo.api.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.javatechie.spring.mongo.api.model.Accidente;


@Repository
public interface AccidenteRepository extends MongoRepository<Accidente, String>{
	
	
	@Aggregation(pipeline = "{\r\n"
			+ "    $group: {\r\n"
			+ "        _id: null,\r\n"
			+ "        Severity: {\r\n"
			+ "            $avg: \"$testDouble\"\r\n"
			+ "        }\r\n"
			+ "    }\r\n"
			+ "}")
	public List<Accidente> avgDistance();
	
	
	
	@Aggregation(pipeline = "{$group : { _id : '$Weather_Condition', Severity : {$sum : 1}}}")
	public List<Accidente> condicionComunClima();
	
	//devolver todos los accidentes ocurridos entre 2 fechas dadas
	@Query("{'Start_Time' : { $gte: ?0 , $lte: ?1 }}")
	List<Accidente> findFechas(String desde, String hasta);
	 
	
}
