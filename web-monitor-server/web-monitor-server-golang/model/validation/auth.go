package validation

type AddUserRegisterRecord struct {
	Username string `form:"username" validate:"required"`
	Password string `form:"password" validate:"required"`
	Phone    string `form:"phone"`
	Icon     string `form:"icon"`
	Gender   int8   `form:"gender"`
	Email    string `form:"email" validate:"required,email"`
}
