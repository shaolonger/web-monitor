package response

import (
	"github.com/gin-gonic/gin"
	"net/http"
)

type Response struct {
	Success bool        `json:"success"`
	Data    interface{} `json:"data"`
	Msg     string      `json:"msg"`
}

func ResponseFactory(success bool, data interface{}, msg string, c *gin.Context) {
	c.JSON(http.StatusOK, Response{
		Success: success,
		Data:    data,
		Msg:     msg,
	})
}

func SuccessWithData(data interface{}, c *gin.Context) {
	ResponseFactory(true, data, "", c)
}

func FailWithError(err error, c *gin.Context) {
	ResponseFactory(false, err.Error(), "", c)
}
