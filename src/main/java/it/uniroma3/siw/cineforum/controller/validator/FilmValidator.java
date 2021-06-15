package it.uniroma3.siw.cineforum.controller.validator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.cineforum.model.Film;


@Component
public class FilmValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(FilmValidator.class);

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titolo", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "trama", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "annoUscita", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "foto", "required");

		if (!errors.hasErrors()) {
			logger.debug("confermato: valori non nulli");
		}
		else {
			logger.info("non ha errori");
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Film.class.equals(aClass);
	}
}
