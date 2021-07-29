package validation

type Subscriber struct {
	Subscriber string `form:"subscriber" json:"subscriber"`
	Category   *int8  `form:"category" json:"category" binding:"required,number,oneof=1 2"`
	IsActive   *int8  `form:"isActive" json:"isActive" binding:"required,number,oneof=0 1"`
}
