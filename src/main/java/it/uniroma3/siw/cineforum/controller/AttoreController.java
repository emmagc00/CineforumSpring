package it.uniroma3.siw.cineforum.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.cineforum.controller.validator.AttoreValidator;
import it.uniroma3.siw.cineforum.model.Attore;
import it.uniroma3.siw.cineforum.service.AttoreService;

@Controller
public class AttoreController {


	@Autowired
	private AttoreService attoreService;
	
	@Autowired 
	private AttoreValidator attoreValidator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/addAttore", method = RequestMethod.GET)
	public String addAttore(Model model) {
		logger.debug("addAttore");
		return "admin/inserimentoAttore.html";
	}

	@RequestMapping(value = "/addAttore", method = RequestMethod.POST)
	public String saveAttore(@ModelAttribute("attore") Attore a,
			@RequestParam("file") MultipartFile file,
			@RequestParam("nome") String nome,
			@RequestParam("cognome") String cognome,
			@RequestParam(value="dataDiNascita", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataDiNascita,
			@RequestParam(value="dataDiMorte", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataDiMorte,
			@RequestParam("luogoDiNascita") String luogoDiNascita,
			@RequestParam("luogoDiMorte") String luogoDiMorte,
			Model model, BindingResult bindingResult)
	{
		
		a.setNome(nome);
		a.setCognome(cognome);
		
		this.attoreValidator.validate(a, bindingResult);
		logger.info("validato");
		if (!bindingResult.hasErrors()) { 
			this.attoreService.saveAttoreToDB(file, nome, cognome, dataDiNascita, dataDiMorte,
	                 luogoDiNascita, luogoDiMorte);
			logger.info("valido");
			return "admin/successoOperazioneAdmin.html";
		}
		logger.info("non valido");
		return "admin/inserimentoAttore.html";
		
		
	}
	
	@RequestMapping(value="/removeAttore", method = RequestMethod.GET)
	public String removeAttore(Model model) {
		logger.debug("removeAttore");
		return "admin/cancellazioneAttore.html";
	}
	
	@RequestMapping(value = "/removeAttore", method = RequestMethod.POST)
	public String removeAttore(@RequestParam("nome") String nome, 
			@RequestParam("cognome") String cognome, Model model)
	{
		logger.info("removeAttore");
		Attore a;
		try {
			a = this.attoreService.attorePerNomeECognome(nome, cognome).get(0);
			this.attoreService.elimina(a);
			logger.info("attore rimosso dal DB");
			return "admin/successoOperazioneAdmin.html";
		} catch (Exception e) {
			logger.info("attore NON rimosso dal DB");
		}
		return "admin/cancellazioneAttore.html";
	}
	
}
