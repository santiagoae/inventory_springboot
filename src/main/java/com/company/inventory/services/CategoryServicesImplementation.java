package com.company.inventory.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.inventory.dao.ICategoryDao;
import com.company.inventory.model.Category;
import com.company.inventory.response.CategoryResponseRest;


@Service
public class CategoryServicesImplementation implements ICategoryService{
	
	@Autowired
	private ICategoryDao categoryDao;

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> search() {

		CategoryResponseRest response = new CategoryResponseRest();
		try {
			List<Category> category =  (List<Category>) categoryDao.findAll();
			
			response.getCategoryResponse().setCategory(category);
			response.setMetadata("respuesta ok", "00", "respuesta exitosa");
			
		} catch (Exception e) {
			
			response.setMetadata("respuesta no ok", "-1", "Error al consultar");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoryResponseRest> searchById(Long id) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		try {
			
			Optional<Category> category = categoryDao.findById(id);
			
			if (category.isPresent()) {
				list.add(category.get());
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("respuesta ok", "00", "Categoria encontrada");
			} else {
				response.setMetadata("respuesta no ok", "-1", "Categoria no encontrada");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);

			}
			
			
		} catch (Exception e) {
			
			response.setMetadata("respuesta no ok", "-1", "Error al consultar por id");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> save(Category category) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {	
					
			Category categorySaved = categoryDao.save(category);
			if (categorySaved != null) {
				list.add(categorySaved);
				response.getCategoryResponse().setCategory(list);
				response.setMetadata("respuesta ok", "00", "Categoria guardada correctamente");
			} else {
				response.setMetadata("respuesta no ok", "-1", "Categoria no guarda");
				return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			
			response.setMetadata("respuesta no ok", "-1", "Error al guardar categoria");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> update(Category category, Long id) {

		CategoryResponseRest response = new CategoryResponseRest();
		List<Category> list = new ArrayList<>();
		
		try {	
				Optional<Category> categorySearch = categoryDao.findById(id);
				if (categorySearch.isPresent()) {
					categorySearch.get().setName(category.getName());
					categorySearch.get().setDescription(category.getDescription());
					
					Category categoryToUpdate = categoryDao.save(categorySearch.get());
					
					if (categoryToUpdate != null) {
						list.add(categoryToUpdate);
						response.getCategoryResponse().setCategory(list);
						response.setMetadata("respuesta ok", "00", "Categoria actualizada correctamente");
					} else {
						response.setMetadata("respuesta no ok", "-1", "categoria no actualizada");
						return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.BAD_REQUEST);
					}

				} else {
					response.setMetadata("respuesta no ok", "-1", "categoria no encontrada");
					return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.NOT_FOUND);
				}
			
		} catch (Exception e) {
			
			response.setMetadata("respuesta no ok", "-1", "Error al actualizar categoria");
			e.getStackTrace();
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoryResponseRest> delete(Long id) {
		
		CategoryResponseRest response = new CategoryResponseRest();
		
		try {
			categoryDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "se ha eliminado la categoria # " + id);
		} catch (Exception e) {
			e.getStackTrace();
			response.setMetadata("respuesta no ok", "-1", "Error al eliminar el registro");
			return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoryResponseRest>(response, HttpStatus.OK);
	}


}
