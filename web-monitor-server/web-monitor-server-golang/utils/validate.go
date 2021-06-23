package utils

import "github.com/go-playground/validator/v10"

// 注意，因gin框架已自带go-playground/validator，因此引用gin的话不再需要引入validator
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
