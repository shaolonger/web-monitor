package utils

import "github.com/go-playground/validator/v10"

func ValidateStruct(s interface{}) (err validator.ValidationErrors) {
	validate := validator.New()
	validateErr := validate.Struct(s)
	if validateErr != nil {
		if _, ok := validateErr.(*validator.InvalidValidationError); ok {
			return nil
		}
		return validateErr.(validator.ValidationErrors)
	}
	return nil
}
