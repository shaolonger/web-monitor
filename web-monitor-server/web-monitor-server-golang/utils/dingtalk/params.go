package dingtalk

const (
	MsgtypeText       = "text"
	MsgtypeLink       = "link"
	MsgtypeMarkdown   = "markdown"
	MsgtypeActionCard = "actionCard"
	MsgtypeFeedCard   = "feedCard"
)

type AtParams struct {
	AtMobiles []string `json:"atMobiles"`
	AtUserIds []string `json:"atUserIds"`
	IsAtAll   bool     `json:"isAtAll"`
}

// text类型的参数
type TextContent struct {
	Content string `json:"content"`
}
type textParams struct {
	At      AtParams    `json:"at"`
	Text    TextContent `json:"text"`
	Msgtype string      `json:"msgtype"`
}

// GetTextParams 获取text类型的参数
func GetTextParams() *textParams {
	return &textParams{
		At:      AtParams{},
		Text:    TextContent{},
		Msgtype: MsgtypeText,
	}
}

// link类型的参数
type linkContent struct {
	Title      string `json:"title"`
	Text       string `json:"text"`
	PicUrl     string `json:"picUrl"`
	MessageUrl string `json:"messageUrl"`
}
type linkParams struct {
	Link    linkContent `json:"link"`
	Msgtype string      `json:"msgtype"`
}

// GetLinkParams 获取link类型的参数
func GetLinkParams() *linkParams {
	return &linkParams{
		Link:    linkContent{},
		Msgtype: MsgtypeLink,
	}
}

// markdown类型的参数
type markdownContent struct {
	Title string `json:"title"`
	Text  string `json:"text"`
}
type markdownParams struct {
	At       AtParams        `json:"at"`
	Markdown markdownContent `json:"markdown"`
	Msgtype  string          `json:"msgtype"`
}

// GetMarkdownParams 获取markdown类型的参数
func GetMarkdownParams() *markdownParams {
	return &markdownParams{
		At:       AtParams{},
		Markdown: markdownContent{},
		Msgtype:  MsgtypeMarkdown,
	}
}

// 整体跳转ActionCard类型的参数
type actionCardContent struct {
	Title          string `json:"title"`
	Text           string `json:"text"`
	BtnOrientation string `json:"btnOrientation"`
	SingleTitle    string `json:"singleTitle"`
	SingleURL      string `json:"singleURL"`
}
type actionCardParams struct {
	ActionCard actionCardContent `json:"actionCard"`
	Msgtype    string            `json:"msgtype"`
}

// GetActionCardParams 获取整体跳转ActionCard类型的参数
func GetActionCardParams() *actionCardParams {
	return &actionCardParams{
		ActionCard: actionCardContent{},
		Msgtype:    MsgtypeActionCard,
	}
}

// 独立跳转ActionCard类型的参数
type cusActionCardBtn struct {
	Title     string `json:"title"`
	ActionURL string `json:"actionURL"`
}
type cusActionCardContent struct {
	Title          string             `json:"title"`
	Text           string             `json:"text"`
	BtnOrientation string             `json:"btnOrientation"`
	Btns           []cusActionCardBtn `json:"btns"`
}
type cusActionCardParams struct {
	ActionCard cusActionCardContent `json:"actionCard"`
	Msgtype    string               `json:"msgtype"`
}

// GetCusActionCardParams 获取独立跳转ActionCard类型的参数
func GetCusActionCardParams() *cusActionCardParams {
	return &cusActionCardParams{
		ActionCard: cusActionCardContent{},
		Msgtype:    MsgtypeActionCard,
	}
}

// feedCard类型的参数
type feedCardContentItem struct {
	Title      string `json:"title"`
	MessageURL string `json:"messageURL"`
	PicURL     string `json:"picURL"`
}
type feedCardContent struct {
	Links []feedCardContentItem `json:"links"`
}
type feedCardParams struct {
	FeedCard feedCardContent `json:"feedCard"`
	Msgtype  string          `json:"msgtype"`
}

// GetFeedCardParams 获取feedCard类型的参数
func GetFeedCardParams() *feedCardParams {
	return &feedCardParams{
		FeedCard: feedCardContent{},
		Msgtype:  MsgtypeFeedCard,
	}
}
