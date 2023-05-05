package br.com.victor.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.victor.controllers.BookController;
import br.com.victor.data.vo.v1.BookVO;
import br.com.victor.exceptions.RequiredObjectIsNullException;
//import br.com.victor.data.vo.v2.BookVOV2;
import br.com.victor.exceptions.ResourceNotFoundException;
import br.com.victor.mapper.Mapper;
import br.com.victor.model.Book;
import br.com.victor.repositories.BookRepository;

@Service
public class BookServices {
	
	private Logger logger = Logger.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	@Autowired
	PagedResourcesAssembler<BookVO> assembler;
	
	
	public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable){
		
		logger.info("Finding all books");
		
		var bookPage = repository.findAll(pageable);
		
		var bookVosPage = bookPage.map(b -> Mapper.parseObject(b, BookVO.class));
		
		bookVosPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));
		
		
		Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		
		/*
		List<BookVO> books = Mapper.parseListObjects(repository.findAll(), BookVO.class);
		//HATEOAS
		books
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getId())).withSelfRel()));
		*/
		return assembler.toModel(bookVosPage, link);
	}

	public BookVO findById(Long id) {
		
		logger.info("Finding one book");
		
		Book entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
		
		BookVO vo = Mapper.parseObject(entity, BookVO.class);
		//HATEOAS
		vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
		
		return vo;
	}
	
	public BookVO create(BookVO book) {
		
		if (book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Create one book");
		
		Book entity = Mapper.parseObject(book, Book.class);
		
		BookVO vo = Mapper.parseObject(repository.save(entity), BookVO.class);
		//HATEOAS
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
				
		return vo;	
	}
	
//	VERSIONAMENTO DA API
//	public BookVOV2 createV2(BookVOV2 book) {
//		
//		logger.info("Create one book with version 2");
//		
//		Book entity = bookMapper.convertVoToEntity(book);
//		
//		BookVOV2 vo = bookMapper.convertEntityToVo(repository.save(entity));
//		
//		return vo;	
//	}
	
	public BookVO update(BookVO book) {
		
		if (book == null) throw new RequiredObjectIsNullException();
		
		logger.info("Update one book");
		
		Book entity = repository.findById(book.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		BookVO vo = Mapper.parseObject(repository.save(entity), BookVO.class);
		//HATEOAS
		vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
				
		return vo;
	}
	
	public void delete(Long id) {
		
		logger.info("Delete one book");
		
		Book entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));
		
		repository.delete(entity);
		
	}
	
}
