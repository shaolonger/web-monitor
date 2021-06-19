package middleware

import (
	"errors"
	"github.com/gin-gonic/gin"
	"web.monitor.com/model/response"
	"web.monitor.com/service"
)

func TokenAuth() gin.HandlerFunc {
	return func(c *gin.Context) {
		// 从请求头中获取token
		token := c.Request.Header.Get("token")
		if token == "" {
			response.FailWithError(errors.New("token不能为空"), c)
			c.Abort()
			return
		} else if !service.HasToken(token) {
			response.FailWithError(errors.New("token已失效"), c)
			c.Abort()
			return
		}
		c.Next()
	}
}
