package validation

type AddUserRegisterRecord struct {
	Username string `form:"username" validate:"required"`
	Password string `form:"password" validate:"required"`
	Phone    string `form:"phone"`
	Icon     string `form:"icon"`
	Gender   int8   `form:"gender" validate:"number,oneof=0 1 2"`
	Email    string `form:"email" validate:"required,email"`
}

type AuditUserRegisterRecord struct {
	AuditId     string `form:"auditId" validate:"required"`
	AuditResult *int8  `form:"auditResult" validate:"required,number,oneof=0 -1 1"`
}

type GetUserRegisterRecord struct {
	PageInfo
	StartTime   string `form:"startTime" validate:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime     string `form:"endTime" validate:"omitempty,datetime=2006-01-02 15:04:05"`
	AuditResult *int8  `form:"auditResult" validate:"omitempty,number,oneof=0 -1 1"`
}

type Login struct {
	Username string `form:"username" validate:"required"`
	Password string `form:"password" validate:"required"`
}

type GetUser struct {
	IsNeedPaging string `form:"isNeedPaging" validate:"required,oneof='0' '1'"`
	PageNum      int    `form:"pageNum" validate:"omitempty,number,gt=0"`
	PageSize     int    `form:"pageSize" validate:"omitempty,number,gt=0"`
	Username     string `form:"username"`
	Phone        string `form:"phone"`
	Gender       int8   `form:"gender" validate:"omitempty,oneof=0 1 2"`
	Email        string `form:"email" validate:"omitempty,email"`
}
