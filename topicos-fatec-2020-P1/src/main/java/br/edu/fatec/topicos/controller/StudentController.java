package br.edu.fatec.topicos.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fatec.topicos.domain.Student;
import br.edu.fatec.topicos.repository.StudentRepository;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController
{	@Autowired private StudentRepository repository;
	
	@GetMapping 			//isso serve para quando queremos editar um produto
	public ModelAndView listar(@ModelAttribute("editableStudent") Student student)
	{	ModelAndView model = new ModelAndView("students");
	
		model.addObject("students", this.repository.findAll());
		return model;
	}
	
	@PostMapping("/insert")
	public ModelAndView salvar(Student student)
	{	this.repository.save(student);
		return new ModelAndView("success");
	}
	
	@GetMapping("/delete")
	public ModelAndView excluir(@RequestParam("id") Long id)
	{	this.repository.deleteById(id);
		return new ModelAndView("success");
	}
	
	@GetMapping("beforeEdit")		//isso serve para quando queremos editar um produto
	public ModelAndView preAlteracao(@RequestParam("id") Long id)
	{	Optional<Student> optional = this.repository.findById(id);//tratamento para caso possa retornar um valor nulo
		
		if(optional.isPresent())	
		{	ModelAndView model = new ModelAndView("students");
			
			model.addObject("students", this.repository.findAll());
			//o método get() só funciona caso optional for encotrado
			model.addObject("editableStudent", optional.get());
			return model;
		}
		
		else return new ModelAndView("fail");
	}
	
	//finalmente o metodo alterar
	@PostMapping("/update")
	public ModelAndView alterar(Student student)
	{	this.repository.save(student); //no JPA geralmente o metodo "save" faz as dias coisas
		
		return new ModelAndView("success");
	}
}