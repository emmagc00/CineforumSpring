package it.uniroma3.siw.cineforum.controller;

import java.time.LocalDate;
import java.time.LocalTime;

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

import it.uniroma3.siw.cineforum.controller.validator.ProiezioneValidator;
import it.uniroma3.siw.cineforum.model.Proiezione;
import it.uniroma3.siw.cineforum.service.ProiezioneService;

@Controller
public class ProiezioneController {

	@Autowired
	private ProiezioneService proiezioneService;
	
	@Autowired
	private ProiezioneValidator proiezioneValidator;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value="/addProiezione", method = RequestMethod.GET)
	public String addProiezione(Model model) {
		logger.debug("addCollezione");
		return "admin/inserimentoProiezione.html";
	}

	@RequestMapping(value = "/addProiezione", method = RequestMethod.POST)
	public String saveProiezione(@ModelAttribute("attore") Proiezione p,
			@RequestParam("sala") String sala,
			@RequestParam(value="postiTotali", required=false) Integer postiTotali,
			@RequestParam(value="data", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate data,
			@RequestParam(value="orario", required=false) @DateTimeFormat(iso = ISO.TIME) LocalTime orario,
			@RequestParam("nomeFilm") String nomeFilm,
			@RequestParam(value="annoFilm", required=false) Integer annoFilm,
			Model model, BindingResult bindingResult)
	{
		
		p.setSala(sala);
		p.setPostiTotali(postiTotali);
		
		this.proiezioneValidator.validate(p, bindingResult);
		logger.info("validato");
		if (!bindingResult.hasErrors()) { 
			this.proiezioneService.saveProiezioneToDB(sala, postiTotali, data, orario, nomeFilm, annoFilm);
			return "admin/successoOperazioneAdmin.html";
		}
		logger.info("non valido");
		return "admin/inserimentoProiezione.html";
			
		
	}
	
	@RequestMapping(value="/removeProiezione", method = RequestMethod.GET)
	public String removeProiezione(Model model) {
		logger.debug("removeProiezione");
		return "admin/cancellazioneProiezione.html";
	}
	
	@RequestMapping(value = "/removeProiezione", method = RequestMethod.POST)
	public String removeProiezione(@RequestParam("sala") String sala,
			@RequestParam(value="data", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate data,
			@RequestParam(value="ora", required=false) @DateTimeFormat(iso = ISO.TIME) LocalTime orario,
			Model model)
	{
		Proiezione p;
		try {
			p = this.proiezioneService.proiezioniPerSalaDataOra(sala, data, orario).get(0);
			this.proiezioneService.elimina(p);
			logger.debug("proiezione rimossa dal DB");
		} catch (Exception e) {
			
		}
		return "admin/successoOperazioneAdmin.html";
	}
	
//	@RequestMapping(value = "/collezione/{id}", method = RequestMethod.GET)
//    public String getCollezione(@PathVariable("id") Long id, Model model) {
//
//        model.addAttribute("collezione", this.proiezioneService.collezionePerId(id));
//        model.addAttribute("opere", this.filmService.operePerIdCollezione(id));
//        return "PaginaCollezione";
//
//    }
//	
}
