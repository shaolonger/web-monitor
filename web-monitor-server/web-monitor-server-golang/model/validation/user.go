package validation

type AddUserRegisterRecord struct {
	Username string `form:"username" binding:"required"`
	Password string `form:"password" binding:"required"`
	Phone    string `form:"phone"`
	Icon     string `form:"icon"`
	Gender   *int8  `form:"gender" binding:"number,oneof=0 1 2"`
	Email    string `form:"email" binding:"required,email"`
}

type AuditUserRegisterRecord struct {
	AuditId     *uint64 `form:"auditId" binding:"required,number"`
	AuditResult *int8   `form:"auditResult" binding:"required,number,oneof=0 -1 1"`
}

type GetUserRegisterRecord struct {
	PageInfo
	StartTime   string `form:"startTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	EndTime     string `form:"endTime" binding:"omitempty,datetime=2006-01-02 15:04:05"`
	AuditResult *int8  `form:"auditResult" binding:"omitempty,number,oneof=0 -1 1"`
}

type Login struct {
	Username string `form:"username" binding:"required"`
	Password string `form:"password" binding:"required"`
}

type GetUser struct {
	IsNeedPaging *int8  `form:"isNeedPaging" binding:"omitempty,number,oneof=0 1"`
	PageNum      int    `form:"pageNum" binding:"required_if=IsNeedPaging 1,number,gt=0"`
	PageSize     int    `form:"pageSize" binding:"required_if=IsNeedPaging 1,number,gt=0"`
	Username     string `form:"username"`
	Phone        string `form:"phone"`
	Gender       *int8  `form:"gender" binding:"omitempty,number,oneof=0 1 2"`
	Email        string `form:"email"`
}
