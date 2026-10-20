/**
 * Incremental SSE parser. Handles CR, LF, CRLF (including split chunks),
 * multiline data, comments and event metadata without trimming model text.
 */
export function createSseParser(onEvent) {
  let line = ''
  let skipLf = false
  let data = []
  let type = ''
  function consumeLine() {
    if (line === '') {
      if (data.length) onEvent({ event: type || 'message', data: data.join('\n') })
      data = []
      type = ''
    } else if (!line.startsWith(':')) {
      const colon = line.indexOf(':')
      const field = colon < 0 ? line : line.slice(0, colon)
      let value = colon < 0 ? '' : line.slice(colon + 1)
      if (value.startsWith(' ')) value = value.slice(1)
      if (field === 'data') data.push(value)
      if (field === 'event') type = value
    }
    line = ''
  }
  return {
    feed(text) {
      for (const char of text) {
        if (skipLf) {
          skipLf = false
          if (char === '\n') continue
        }
        if (char === '\r' || char === '\n') {
          consumeLine()
          skipLf = char === '\r'
        } else line += char
      }
    },
    // An event is only complete after its blank line (SSE specification).
    end() {
      line = ''
      data = []
      type = ''
    },
  }
}
