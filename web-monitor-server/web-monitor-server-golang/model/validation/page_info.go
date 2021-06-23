package validation

type PageInfo struct {
	PageNum  int `form:"pageNum" binding:"required,gt=0"`
	PageSize int `form:"pageSize" binding:"required,gt=0"`
}
