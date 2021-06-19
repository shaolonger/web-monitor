package validation

type PageInfo struct {
	PageNum  int `form:"pageNum" validate:"required,gt=0"`
	PageSize int `form:"pageSize" validate:"required,gt=0"`
}
