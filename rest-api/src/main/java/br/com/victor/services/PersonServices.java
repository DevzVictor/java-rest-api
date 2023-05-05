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

import br.com.victor.controllers.PersonController;
import br.com.victor.data.vo.v1.PersonVO;
import br.com.victor.exceptions.RequiredObjectIsNullException;
//import br.com.victor.data.vo.v2.PersonVOV2;
import br.com.victor.exceptions.ResourceNotFoundException;
import br.com.victor.mapper.Mapper;
import br.com.victor.mapper.custom.PersonMapper;
import br.com.victor.model.Person;
import br.com.victor.repositories.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	@Autowired
	PersonMapper personMapper;

	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {

		logger.info("Finding all persons");

		// usa propriedade do repository com paginação
		var personPage = repository.findAll(pageable);
		// converte para VO
		var personVosPage = personPage.map(p -> Mapper.parseObject(p, PersonVO.class));
		// HATEOAS
		personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));

		/*
		 * MANEIRA SEM PAGINAÇÃO List<PersonVO> persons =
		 * Mapper.parseListObjects(repository.findAll(), PersonVO.class); //HATEOAS
		 * persons .stream() .forEach(p ->
		 * p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).
		 * withSelfRel()));
		 */

		Link link = linkTo(
				methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();

		return assembler.toModel(personVosPage, link);
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstname, Pageable pageable) {
		
		logger.info("Finding persons by name");
		
		// usa propriedade do repository com paginação
		var personPage = repository.findPersonsByName(firstname, pageable);
		// converte para VO
		var personVosPage = personPage.map(p -> Mapper.parseObject(p, PersonVO.class));
		// HATEOAS
		personVosPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
				.withSelfRel();
		
		return assembler.toModel(personVosPage, link);
	}

	public PersonVO findById(Long id) {

		logger.info("Finding one person");

		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));

		PersonVO vo = Mapper.parseObject(entity, PersonVO.class);
		// HATEOAS
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());

		return vo;
	}

	public PersonVO create(PersonVO person) {

		if (person == null)
			throw new RequiredObjectIsNullException();

		logger.info("Create one person");

		Person entity = Mapper.parseObject(person, Person.class);

		PersonVO vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
		// HATEOAS
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());

		return vo;
	}

//	VERSIONAMENTO DA API
//	public PersonVOV2 createV2(PersonVOV2 person) {
//		
//		logger.info("Create one person with version 2");
//		
//		Person entity = personMapper.convertVoToEntity(person);
//		
//		PersonVOV2 vo = personMapper.convertEntityToVo(repository.save(entity));
//		
//		return vo;	
//	}

	public PersonVO update(PersonVO person) {

		if (person == null)
			throw new RequiredObjectIsNullException();

		logger.info("Update one person");

		Person entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		PersonVO vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
		// HATEOAS
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());

		return vo;
	}

	public void delete(Long id) {

		logger.info("Delete one person");

		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found this ID"));

		repository.delete(entity);

	}

}
