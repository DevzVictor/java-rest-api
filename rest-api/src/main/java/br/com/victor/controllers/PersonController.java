package br.com.victor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.victor.data.vo.v1.PersonVO;
//import br.com.victor.data.vo.v2.PersonVOV2;
import br.com.victor.services.PersonServices;
import br.com.victor.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing peoples")
public class PersonController {
	
	@Autowired
	private PersonServices service;
	// private PersonServices service = new PersonService();
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(
			summary =  "Finds all peoples",
			description = "Finds all peoples", 
			tags = {"People"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = {
						@Content(
							mediaType =  "application/json",
							array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
						)
					}),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction ) {
		// se for igual a desc recebendo direction como parametro entao ele vai retortar Direction.DESC, se não Direction.ASC;
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@GetMapping(
			value = "/findPersonByName/{firstName}",
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(
			summary =  "Finds peoples by name",
			description = "Finds peoples by name", 
			tags = {"People"},
			responses = {
					@ApiResponse(description = "Success", responseCode = "200", 
							content = {
									@Content(
											mediaType =  "application/json",
											array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
											)
					}),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
			)
	public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findPersonByName(
			@PathVariable(value = "firstName") String firstName,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction ) {
		// se for igual a desc recebendo direction como parametro entao ele vai retortar Direction.DESC, se não Direction.ASC;
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findPersonByName(firstName, pageable));
	}
	
	
	@GetMapping(value = "/{id}", 
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(
			summary =  "Finds a person by ID",
			description = "Finds a person by ID", 
			tags = {"People"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = PersonVO.class))),
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public PersonVO findById(@PathVariable(value = "id") Long id ) {
		return service.findById(id);
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
	@Operation(
			summary =  "Create a person",
			description = "Create a person by parsing in a JSON, XML, or YML representation of the person", 
			tags = {"People"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = PersonVO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public PersonVO create(@RequestBody PersonVO person ) {
		return service.create(person);
	}
	
	
//  VERSIONAMENTO DA API
//	@PostMapping(
//			value = "/v2",
//			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML },
//			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//	public PersonVOV2 createV2(@RequestBody PersonVOV2 person ) {
//		
//		return service.createV2(person);
//	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
	@Operation(
			summary =  "Updates a person",
			description = "Updates a person by parsing in a JSON, XML, or YML representation of the person", 
			tags = {"People"},
			responses = {
				@ApiResponse(description = "Updated", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = PersonVO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public PersonVO update(@RequestBody PersonVO person ) {
		return service.update(person);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(
			summary =  "Deletes a person",
			description = "Deletes a person by parsing in a JSON, XML, or YML representation of the person", 
			tags = {"People"},
			responses = {
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public ResponseEntity<?> delete(@PathVariable(value = "id") Long id ) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
