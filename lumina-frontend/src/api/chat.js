import axios from 'axios'
import { createSseParser } from './sse'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  adapter: 'fetch',
})

export async function streamChat({ appId, message, chatId, signal, onChunk }) {
  const love = appId === 'love'
  const response = await client.get(love ? '/ai/love_app/chat/sse' : '/ai/manus/chat', {
    params: love ? { message, chatId } : { message },
    responseType: 'stream',
    headers: { Accept: 'text/event-stream' },
    signal,
  })
  if (!response.headers['content-type']?.includes('text/event-stream')) {
    await response.data?.cancel?.()
    throw new Error('服务器未返回 SSE 数据，请检查接口地址和后端服务。')
  }
  const reader = response.data.getReader()
  const decoder = new TextDecoder()
  let done = false
  const parser = createSseParser(({ event, data }) => {
    if (done) return
    if (event === 'error') throw new Error(data || '服务器处理失败，请稍后重试。')
    if (data === '[DONE]' || event === 'done') {
      done = true
      return
    }
    onChunk(data)
  })
  try {
    while (!done) {
      const result = await reader.read()
      if (result.done) {
        parser.feed(decoder.decode())
        parser.end()
        break
      }
      parser.feed(decoder.decode(result.value, { stream: true }))
    }
  } finally {
    await reader.cancel().catch(() => {})
    reader.releaseLock()
  }
}
