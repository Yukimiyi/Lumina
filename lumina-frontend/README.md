# Lumina 前端

Vue 3 + Vue Router + Axios + Vite，包含应用主页、恋爱顾问和 恋爱策划师两个聊天页面，适配手机与桌面。

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
| 恋爱顾问 | `/api/ai/love_app/chat/sse` | `message`, `chatId` |
| 恋爱策划师 | `/api/ai/manus/chat` | `message` |

Axios 使用 fetch adapter 和 `responseType: 'stream'` 读取 SSE。解析器处理网络分片、UTF-8、多行 data、CR/LF/CRLF 换行、注释和命名事件。恋爱应用逐片追加文本并保留空白；智能体每个事件为一个完整步骤，步骤间增加空行。`[DONE]`、`event: done` 或连接正常关闭表示结束，`event: error` 显示错误。不会自动重连，避免重复触发智能体任务。AI 输出作为纯文本安全显示，不执行 HTML。

进入聊天页和点击「新建会话」都会生成 UUID。恋爱应用在本次会话的每个请求中复用 chatId；智能体接口按后端签名仅传 message，后端每次创建新智能体，页面聊天记录不代表后端支持多轮记忆。聊天记录保存在当前页面内存中，刷新、切换应用或新建会话后清空。

支持 Enter 发送、Shift + Enter 换行、中文输入法、停止生成、发送期间防重复提交、失败保留已生成内容。主动向上滚动阅读时暂停自动滚动；滚动到底部恢复。切换页面或新建会话会取消当前请求。浏览器取消连接不保证后端停止工具执行，取决于后端取消机制。

## 开发与生产请求配置

两种环境浏览器默认都请求相对路径 `/api`，区别在于代理服务：

| 环境 | 请求链路 |
| --- | --- |
| `npm run dev` | 浏览器 → Vite 的 /api → http://localhost:8123/api |
| 生产 Nginx | 浏览器 → 当前网站域名的 /api → nginx.conf 中配置的后端 /api |

`.env.development` 配置开发代理。更换本地后端时，可以复制 `.env.example` 为 `.env.development.local`，修改 `DEV_API_TARGET`（只填协议、主机、端口，不加 /api），然后重启 Vite。

生产构建在 `src/api/chat.js` 中固定使用 `/api`，避免本地环境变量把 localhost 带到线上。后端域名只配置在 Nginx 中。Vite 开发代理不会打进 dist；`npm run preview` 不能用于验证生产 Nginx 配置。

## 部署到 Nginx

1. 执行 `npm run build`，将 dist **目录内的文件**部署到 `/usr/share/nginx/html/`。
2. 将本项目 `nginx.conf` 安装为 `/etc/nginx/conf.d/default.conf`。它只有 server 块，不应直接替换顶层 nginx.conf。
3. 修改 `proxy_pass` 为实际后端地址。当前保留原配置中的 https://yu-ai-agent-backend-119344-6-1256524210.sh.run.tcloudbase.com，请确认它属于本项目，不能直接把教程示例当作实际后端。
4. 在 Nginx 所在服务器或容器中执行 `nginx -t`，检查成功后再执行 `nginx -s reload`。

`proxy_pass` 不带 URI 后缀，所以 /api/ai/... 会原样传给后端，不能随意增加尾部斜线而移除 /api。HTTPS 上游已开启 SNI，Host 自动使用上游域名。若 Nginx 与后端通过同机或容器网络通信，可将该地址换成实际可达的 HTTP 服务地址；容器内 localhost 只代表当前容器。

当前 server 监听 80，适用于由云平台入口终止 HTTPS 的部署；平台服务端口也应设为 80。自行部署公网 HTTPS 时需在入口配置域名和证书。SSE 已关闭响应缓冲、缓存和压缩，并设置 600 秒的上游读取间隔超时；外层网关也必须支持流式响应及足够长的超时。此配置不会延长后端自身的 SseEmitter 超时。

发布后可先访问网站首页，再请求同域健康接口或发送一条聊天消息，检查开发者工具中的请求 URL 是否为当前域名 /api/...。流式连接应持续收到内容，而不是等结束后一次性显示。

配置依据：[Nginx 官方代理模块说明](https://nginx.org/en/docs/http/ngx_http_proxy_module.html)。
