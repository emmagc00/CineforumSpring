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

import it.uniroma3.siw.cineforum.controller.validator.PrenotazioneValidator;
import it.uniroma3.siw.cineforum.model.Prenotazione;
import it.uniroma3.siw.cineforum.service.PrenotazioneService;

@Controller
public class HomePrenotazioneController {
	
	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@Autowired
	private PrenotazioneValidator prenotazioneValidator;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/prenotazione", method = RequestMethod.GET) 
	public String prenotazione (Model model) {
		return "prenotazione.html";
	}
	
	@RequestMapping(value = "/mostraPrenotazione", method = RequestMethod.GET) 
	public String mostraprenotazione (Model model) {
		model.addAttribute("prenotazioni", this.prenotazioneService.findAll());
		return "mostraPrenotazione.html";
	}
	
	@RequestMapping(value = "/cancellaPrenotazione", method = RequestMethod.GET) 
	public String cancellaprenotazione (Model model) {
		return "cancellaPrenotazione.html";
	}
	
	@RequestMapping(value = "/cancellaPrenotazione", method = RequestMethod.POST) 
	public String cancella (@RequestParam("idPrenotazione") Long idPrenotazione, Model model) {
		this.prenotazioneService.cancellaPrenotazione(idPrenotazione);
		return "successoOperazione.html";
	}
	
	@RequestMapping(value = "/prenotazione", method = RequestMethod.POST)
	public String savePrenotazione(@ModelAttribute("attore") Prenotazione p,
			@RequestParam("nomeSala") String nomeSala,
			@RequestParam("username") String username,
			@RequestParam(value="data", required=false) @DateTimeFormat(iso = ISO.DATE) LocalDate data,
			@RequestParam(value="numeroPosti", required=false) Integer numeroPosti,
			Model model, BindingResult bindingResult)
	{
		p.setNumeroPosti(numeroPosti);
		
		this.prenotazioneValidator.validate(p, bindingResult);
		logger.info("validato");
		if (!bindingResult.hasErrors()) { 
			this.prenotazioneService.savePrenotazioneToDB(nomeSala, username,data,numeroPosti);
			return "successoOperazione.html";
		}
		logger.info("non valido");
		return "prenotazione.html";
	}
}
