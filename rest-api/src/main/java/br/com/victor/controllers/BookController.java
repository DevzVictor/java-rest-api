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

import br.com.victor.data.vo.v1.BookVO;
//import br.com.victor.data.vo.v2.BookVOV2;
import br.com.victor.services.BookServices;
import br.com.victor.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for managing books")
public class BookController {
	
	@Autowired
	private BookServices service;
	// private BookServices service = new BookService();
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(
			summary =  "Finds all books",
			description = "Finds all books", 
			tags = {"Book"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
					content = {
						@Content(
							mediaType =  "application/json",
							array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
						)
					}),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public ResponseEntity<PagedModel<EntityModel<BookVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "limit", defaultValue = "12") Integer limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"));
		
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@GetMapping(value = "/{id}", 
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
	@Operation(
			summary =  "Finds a book by ID",
			description = "Finds a book by ID", 
			tags = {"Book"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = BookVO.class))),
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public BookVO findById(@PathVariable(value = "id") Long id ) {
		return service.findById(id);
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
	@Operation(
			summary =  "Create a book",
			description = "Create a book by parsing in a JSON, XML, or YML representation of the book", 
			tags = {"Book"},
			responses = {
				@ApiResponse(description = "Success", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = BookVO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public BookVO create(@RequestBody BookVO book ) {
		return service.create(book);
	}
	
	
//  VERSIONAMENTO DA API
//	@PostMapping(
//			value = "/v2",
//			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML },
//			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
//	public BookVOV2 createV2(@RequestBody BookVOV2 book ) {
//		
//		return service.createV2(book);
//	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML },
			produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML } )
	@Operation(
			summary =  "Updates a book",
			description = "Updates a book by parsing in a JSON, XML, or YML representation of the book", 
			tags = {"Book"},
			responses = {
				@ApiResponse(description = "Updated", responseCode = "200", 
						content = @Content(schema = @Schema(implementation = BookVO.class))),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
			}
		)
	public BookVO update(@RequestBody BookVO book ) {
		return service.update(book);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(
			summary =  "Deletes a book",
			description = "Deletes a book by parsing in a JSON, XML, or YML representation of the book", 
			tags = {"Book"},
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
