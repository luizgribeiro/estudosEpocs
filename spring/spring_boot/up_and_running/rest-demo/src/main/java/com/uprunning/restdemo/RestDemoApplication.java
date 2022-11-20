package com.uprunning.restdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

}

interface CoffeeRepository extends CrudRepository<Coffee, String>{}

@Entity
class Coffee {
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	@Id
	private String id;
	private String name;
public Coffee ( String id, String name  ) {
    	this.id = id;
		this.name = name;
	}

	public Coffee(){}

	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(String id) {
		this.id = id;
	}
}

@Component
class DataLoader {
	private final CoffeeRepository coffeeRepository;

	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		coffeeRepository.saveAll(List.of(new Coffee("Café Cereza"), new Coffee("Café Ganador"), new Coffee("Café Lareño"), new Coffee ("Café Três Pontas")));
	}
}

@RestController
@RequestMapping("/coffees")
class RestApiController {
	private final CoffeeRepository coffeeRepository;

	public RestApiController(CoffeeRepository coffeeRepository){
		this.coffeeRepository = coffeeRepository;

	}

	@GetMapping
	Iterable<Coffee> getCoffees(){
		return coffeeRepository.findAll();
	}

	@GetMapping("/coffees/{id}")
	Optional<Coffee> getCoffeById(@PathVariable String id) {
		return coffeeRepository.findById(id);
	}
	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		Coffee coffeeWithtid;
		if (coffee.getId() == null) {
			coffeeWithtid = new Coffee(coffee.getName());
		} else {
			coffeeWithtid = coffee;
		}
		return coffeeRepository.save(coffeeWithtid);
	}

	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		return coffeeRepository.existsById(id)?  new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK): new ResponseEntity<>(postCoffee(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffeeRepository.deleteById(id);
	}
}
