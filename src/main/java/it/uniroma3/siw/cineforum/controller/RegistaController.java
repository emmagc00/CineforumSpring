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

import it.uniroma3.siw.cineforum.controller.validator.RegistaValidator;
import it.uniroma3.siw.cineforum.model.Regista;
import it.uniroma3.siw.cineforum.service.RegistaService;

@Controller
public class RegistaController {


	@Autowired
	private RegistaService registaService;
	
	@Autowired
	private RegistaValidator registaValidator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/addRegista", method = RequestMethod.GET)
	public String addRegista(Model model) {
		logger.debug("addRegista");
		return "admin/inserimentoRegista.html";
	}

	@RequestMapping(value = "/addRegista", method = RequestMethod.POST)
	public String saveRegista(@ModelAttribute("attore") Regista r,
			@RequestParam("file") MultipartFile file,
			@RequestParam("nome") String nome,
			@RequestParam("cognome") String cognome,
			@RequestParam(value="dataDiNascita", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataDiNascita,
			@RequestParam(value="dataDiMorte", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate dataDiMorte,
			@RequestParam("luogoDiNascita") String luogoDiNascita,
			@RequestParam("luogoDiMorte") String luogoDiMorte,
			Model model, BindingResult bindingResult)
	{
		
		r.setNome(nome);
		r.setCognome(cognome);
		
		this.registaValidator.validate(r, bindingResult);
		logger.info("validato");
		if (!bindingResult.hasErrors()) { 
			this.registaService.saveRegistaToDB(file, nome, cognome, dataDiNascita, dataDiMorte,
	                 luogoDiNascita, luogoDiMorte);
			return "admin/successoOperazioneAdmin.html";
		}
		logger.info("non valido");
		return "admin/inserimentoRegista.html";
		
		
		
	}
	
	@RequestMapping(value="/removeRegista", method = RequestMethod.GET)
	public String removeRegista(Model model) {
		logger.debug("removeRegista");
		return "admin/cancellazioneRegista.html";
	}
	
	@RequestMapping(value = "/removeRegista", method = RequestMethod.POST)
	public String removeRegista(@RequestParam("nome") String nome, 
			@RequestParam("cognome") String cognome, Model model)
	{
		logger.info("removeRegista");
		Regista r;
		try {
			r = this.registaService.registaPerNomeECognome(nome, cognome).get(0);
			this.registaService.elimina(r);
			logger.info("regista rimosso dal DB");
			return "admin/successoOperazioneAdmin.html";
		} catch (Exception e) {
			logger.info("regista NON rimosso dal DB");
		}
		return "admin/cancellazioneRegista.html";
	}
	
}
