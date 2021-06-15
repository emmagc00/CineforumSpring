package it.uniroma3.siw.cineforum.controller.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.cineforum.model.Regista;
import it.uniroma3.siw.cineforum.service.RegistaService;


@Component
public class RegistaValidator implements Validator {
	
	@Autowired
	private RegistaService registaService;

	private static final Logger logger = LoggerFactory.getLogger(RegistaValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cognome", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
			if (this.registaService.alreadyExists((Regista)o)) {
				logger.debug("e' un duplicato");
				errors.reject("duplicato");
			}
		}
		else {
			logger.info("non ha errori");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Regista.class.equals(aClass);
	}
}
