package validation

type AddUserRegisterRecord struct {
	Username string `form:"username" validate:"required"`
	Password string `form:"password" validate:"required"`
	Phone    string `form:"phone"`
	Icon     string `form:"icon"`
	Gender   int8   `form:"gender"`
	Email    string `form:"email" validate:"required,email"`
}

type GetUserRegisterRecord struct {
	PageInfo
	StartTime   string `form:"startTime" validate:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime     string `form:"endTime" validate:"omitempty,datetime=2006-01-02 15:04:05"`
	AuditResult string `form:"auditResult" validate:"omitempty,oneof='0' '-1' '1'"`
}
