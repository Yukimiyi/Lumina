# Lumina 前端

Vue 3 + Vue Router + Axios + Vite，包含应用主页、AI 恋爱大师和 AI 超级智能体两个聊天页面，适配手机与桌面。

## 运行

需要 Node.js 20.19+ 或 22.12+。

```powershell
cd C:\work_place\lumina\lumina-frontend
npm install
npm run dev
```

打开终端显示的地址（默认 http://localhost:5173），后端应运行在 http://localhost:8123。

```sh
npm test
npm run build
npm run preview
```

## 接口

开发时浏览器请求 `/api`，由 Vite 代理到 `http://localhost:8123/api`。

| 应用 | GET 接口 | 查询参数 |
| --- | --- | --- |
| AI 恋爱大师 | `/api/ai/love_app/chat/sse` | `message`, `chatId` |
| AI 超级智能体 | `/api/ai/manus/chat` | `message` |

Axios 使用 fetch adapter 和 `responseType: 'stream'` 读取 SSE。解析器处理网络分片、UTF-8、多行 data、CR/LF/CRLF 换行、注释和命名事件。恋爱应用逐片追加文本并保留空白；智能体每个事件为一个完整步骤，步骤间增加空行。`[DONE]`、`event: done` 或连接正常关闭表示结束，`event: error` 显示错误。不会自动重连，避免重复触发智能体任务。AI 输出作为纯文本安全显示，不执行 HTML。

进入聊天页和点击「新建会话」都会生成 UUID。恋爱应用在本次会话的每个请求中复用 chatId；智能体接口按后端签名仅传 message，后端每次创建新智能体，页面聊天记录不代表后端支持多轮记忆。聊天记录保存在当前页面内存中，刷新、切换应用或新建会话后清空。

支持 Enter 发送、Shift + Enter 换行、中文输入法、停止生成、发送期间防重复提交、失败保留已生成内容。主动向上滚动阅读时暂停自动滚动；滚动到底部恢复。切换页面或新建会话会取消当前请求。浏览器取消连接不保证后端停止工具执行，取决于后端取消机制。

## 部署

`npm run build` 生成 `dist/`。使用 hash 路由，无需前端路由重写。生产服务器需将 `/api` 反向代理至后端，并关闭 SSE 响应缓冲、设置适合长连接的读取超时；Vite 的开发代理不包含在构建产物中。

也可以复制 `.env.example` 为 `.env.local`，设置 `VITE_API_BASE_URL=http://localhost:8123/api` 后重启开发服务或重新构建。直接跨域访问时后端需要允许前端来源；HTTPS 页面必须连接 HTTPS 后端。

SSE 解析说明参考 [Axios 官方 fetch adapter 文档](https://github.com/axios/axios#fetch-adapter)。
