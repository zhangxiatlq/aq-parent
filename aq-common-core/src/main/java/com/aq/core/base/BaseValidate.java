/**
 * 
 */
package com.aq.core.base;


import com.aq.core.base.author.ValidatorCenter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;


public class BaseValidate implements Serializable {

	private static final long serialVersionUID = -5300113985007593228L;

	// 验证FormBean
	public String validateForm() {

		return this.validateForm((Class<?>[]) null);
	}

	// 验证FormBean
	public String validateForm(Class<?>... clazz) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<BaseValidate>> constraintViolation;
		if (clazz == null) {
			constraintViolation = validator.validate(this);
		} else {
			constraintViolation = validator.validate(this, clazz);
		}
		Optional<ConstraintViolation<BaseValidate>> first = constraintViolation.stream().findFirst();
		return first.map(ConstraintViolation::getMessage).orElse(null);
	}

	public String validateFormHaveCustomize(BaseMapper<?> mapper) {
		String message = this.validateForm();
		if (message != null) {
			return message;
		}
		ValidatorCenter validator = new ValidatorCenter(mapper);
		if (!validator.checkAnnotationValidate(this)) {
			return validator.getError();
		}
		return null;
	}

}
