import test from 'node:test'
import assert from 'node:assert/strict'
import { createSseParser } from '../src/api/sse.js'

test('preserves text, multiline events, comments and metadata across split CRLF', () => {
  const events = []
  const parser = createSseParser(event => events.push(event))
  for (const char of ': heartbeat\r\nid: 1\r\nevent: step\r\ndata: 你好 \r\ndata: 世界\r\n\r\ndata: next\n\n') parser.feed(char)
  assert.deepEqual(events, [
    { event: 'step', data: '你好 \n世界' },
    { event: 'message', data: 'next' },
  ])
})

test('handles bare CR, empty data, multiple events and ignores unfinished frames', () => {
  const events = []
  const parser = createSseParser(event => events.push(event))
  parser.feed('data:\r\rdata: a\r\rdata: incomplete')
  parser.end()
  assert.deepEqual(events, [{ event: 'message', data: '' }, { event: 'message', data: 'a' }])
})

test('retains unicode and whitespace when UTF-8 bytes are split arbitrarily', () => {
  const events = []
  const parser = createSseParser(event => events.push(event))
  const decoder = new TextDecoder()
  const bytes = new TextEncoder().encode('data:  心动 💜\n\ndata: [DONE]\n\n')
  for (const byte of bytes) parser.feed(decoder.decode(Uint8Array.of(byte), { stream: true }))
  parser.feed(decoder.decode())
  assert.deepEqual(events, [{ event: 'message', data: ' 心动 💜' }, { event: 'message', data: '[DONE]' }])
})
