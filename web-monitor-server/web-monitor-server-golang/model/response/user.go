package response

type LoginUser struct {
	Id       uint64 `json:"id"`
	Username string `json:"username"`
	IsAdmin  int8   `json:"isAdmin"`
	Token    string `json:"token"`
}
