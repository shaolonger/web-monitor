package global

import (
	"context"
	"github.com/go-redis/redis/v8"
)

type Redis struct {
	Client  *redis.Client
	Context *context.Context
}
