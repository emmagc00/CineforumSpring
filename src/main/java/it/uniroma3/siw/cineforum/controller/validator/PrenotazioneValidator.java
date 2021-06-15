package it.uniroma3.siw.cineforum.controller.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.cineforum.model.Attore;


@Component
public class PrenotazioneValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(PrenotazioneValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "numeroPosti", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
		}
		else {
			logger.info("non ha errori");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Attore.class.equals(aClass);
	}
}
