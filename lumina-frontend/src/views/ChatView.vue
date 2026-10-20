<script setup>
import { computed, nextTick, onBeforeUnmount, ref } from 'vue'
import { apps } from '../apps'
import AppIcon from '../components/AppIcon.vue'
import { streamChat } from '../api/chat'

const props = defineProps({ appId: { type: String, required: true } })
const app = computed(() => apps[props.appId])
const createId = () => crypto.randomUUID()
const chatId = ref(createId())
const messages = ref([])
const draft = ref('')
const busy = ref(false)
const error = ref('')
const messageList = ref(null)
const input = ref(null)
let controller
let generation = 0
let followBottom = true
let scrollPending = false

function scrollToBottom(force = false) {
  if ((!followBottom && !force) || scrollPending) return
  scrollPending = true
  nextTick(() => {
    if (messageList.value) messageList.value.scrollTop = messageList.value.scrollHeight
    scrollPending = false
  })
}
function handleScroll() {
  const el = messageList.value
  followBottom = el.scrollHeight - el.scrollTop - el.clientHeight < 100
}
function stop() {
  controller?.abort()
}
function newChat() {
  generation++
  stop()
  controller = undefined
  busy.value = false
  messages.value = []
  error.value = ''
  draft.value = ''
  chatId.value = createId()
  followBottom = true
  nextTick(() => input.value?.focus())
}
async function send(text = draft.value) {
  const message = text.trim()
  if (!message || busy.value) return
  const run = ++generation
  const request = new AbortController()
  controller = request
  error.value = ''
  draft.value = ''
  busy.value = true
  messages.value.push({ id: createId(), role: 'user', content: message })
  messages.value.push({ id: createId(), role: 'assistant', content: '', steps: [], status: 'streaming' })
  const reply = messages.value[messages.value.length - 1]
  followBottom = true
  scrollToBottom(true)
  try {
    await streamChat({
      appId: props.appId,
      message,
      chatId: chatId.value,
      signal: request.signal,
      onChunk(chunk) {
        if (generation !== run) return
        // The backend emits one complete execution step per Manus SSE event.
        if (props.appId === 'manus' && chunk) reply.steps.push(chunk)
        reply.content += (props.appId === 'manus' && reply.content ? '\n\n' : '') + chunk
        scrollToBottom()
      },
    })
    reply.status = request.signal.aborted ? 'stopped' : 'complete'
    if (!reply.content && !request.signal.aborted) {
      reply.status = 'error'
      if (generation === run) error.value = '本次未收到回复，请重新发送消息。'
    }
  } catch (err) {
    reply.status = request.signal.aborted ? 'stopped' : 'error'
    if (!request.signal.aborted && generation === run) {
      error.value = err.response?.status
        ? `请求失败（HTTP ${err.response.status}），请检查后端服务后重试。`
        : err.message === 'Network Error'
          ? '无法连接服务，请确认后端已在 localhost:8123 启动。'
          : err.message || '连接中断，请稍后重试。'
    }
  } finally {
    if (generation === run) {
      busy.value = false
      controller = undefined
      scrollToBottom()
      nextTick(() => input.value?.focus())
    }
  }
}
function handleKey(event) {
  if (event.key === 'Enter' && !event.shiftKey && !event.isComposing && event.keyCode !== 229) {
    event.preventDefault()
    send()
  }
}
onBeforeUnmount(() => {
  generation++
  stop()
})
</script>

<template>
  <div :class="['chat-page', appId]">
    <header class="topbar chat-topbar">
      <div class="chat-title"><span :class="['mini-app-icon', appId]"><AppIcon :name="app.icon" :size="21" /></span><div><h1>{{ app.name }}</h1><p>{{ app.label }}</p></div></div>
      <button class="new-chat" @click="newChat"><AppIcon name="plus" :size="17" /><span>新建会话</span></button>
    </header>
    <div ref="messageList" class="messages-scroll" role="log" aria-label="聊天记录" :aria-busy="busy" @scroll="handleScroll">
      <div v-if="!messages.length" class="chat-welcome">
        <span class="welcome-symbol"><AppIcon :name="app.icon" :size="37" /></span>
        <div class="eyebrow">{{ appId === 'love' ? 'A SPACE FOR YOUR HEART' : 'A LITTLE THOUGHT, A LOT OF LOVE' }}</div>
        <h2>{{ app.greeting }}</h2>
        <p>{{ app.hint }}</p>
        <div class="suggestions"><button v-for="prompt in app.prompts" :key="prompt" @click="send(prompt)">{{ prompt }}<AppIcon name="arrow" :size="16" /></button></div>
      </div>
      <div v-else class="message-container">
        <div class="conversation-start">一段新的对话，从这里开始</div>
        <article v-for="message in messages" :key="message.id" :class="['message', message.role]">
          <span class="avatar"><AppIcon :name="message.role === 'user' ? 'user' : app.icon" :size="19" /></span>
          <div class="message-body">
            <div class="message-author">{{ message.role === 'user' ? '你' : app.name }}</div>
            <div v-if="appId === 'manus' && message.role === 'assistant' && message.steps.length" class="planning-progress">
              <div class="planning-heading">{{ message.status === 'streaming' ? '正在为你策划' : message.status === 'complete' ? '本次策划记录' : '已接收的策划进展' }}</div>
              <section v-for="(step, index) in message.steps" :key="index" class="planning-step">
                <span class="step-number">{{ String(index + 1).padStart(2, '0') }}</span>
                <div class="step-content">{{ step }}</div>
              </section>
              <div v-if="message.status === 'streaming'" class="planning-waiting" role="status">正在继续整理<span class="stream-cursor" /></div>
            </div>
            <div v-else class="bubble">
              <span v-if="message.content">{{ message.content }}</span>
              <span v-else-if="message.status === 'streaming'" class="thinking"><i /><i /><i /><span>正在思考</span></span>
              <span v-else class="empty-response">{{ message.status === 'stopped' ? '已停止生成' : '未收到回复' }}</span>
              <span v-if="message.status === 'streaming' && message.content" class="stream-cursor" />
            </div>
            <span v-if="message.status === 'stopped' && message.content" class="message-status">已停止生成</span>
            <span v-if="message.status === 'error' && message.content" class="message-status">回复中断，以上为已接收内容</span>
          </div>
        </article>
      </div>
    </div>
    <div class="composer-area">
      <div v-if="error" class="error-banner" role="alert">{{ error }}<button aria-label="关闭错误提示" @click="error = ''">×</button></div>
      <form class="composer" @submit.prevent="send()">
        <label class="sr-only" for="message-input">输入消息</label>
        <textarea id="message-input" ref="input" v-model="draft" rows="2" :placeholder="appId === 'love' ? '说说你的心事吧…' : '说说你想准备的惊喜、你们的喜好和预算…'" @keydown="handleKey" />
        <div class="composer-toolbar"><span><AppIcon :name="app.icon" :size="15" /> {{ app.shortName }}</span>
          <button v-if="busy" type="button" class="send-button stop-button" aria-label="停止生成" title="停止生成" @click="stop"><AppIcon name="stop" :size="17" /></button>
          <button v-else type="submit" class="send-button" :disabled="!draft.trim()" aria-label="发送消息" title="发送消息"><AppIcon name="send" :size="20" /></button>
        </div>
      </form>
      <div class="composer-footer"><span>Enter 发送 · Shift + Enter 换行</span><span :title="chatId">会话 {{ chatId.slice(0, 8) }}</span></div>
      <p class="ai-notice">内容由 AI 生成，请结合实际情况判断。</p>
    </div>
  </div>
</template>
