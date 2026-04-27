export function normalizeFilePath(value) {
  const first = String(value || '').split(',')[0].split('?')[0].trim()
  if (!first) return ''
  if (/^(https?:)?\/\//i.test(first) || /^data:/i.test(first) || /^blob:/i.test(first)) return first
  return first.replace(/^\/+/, '')
}

export function imageUrl(value, baseUrl) {
  const path = normalizeFilePath(value)
  if (!path) return ''
  if (/^(https?:)?\/\//i.test(path) || /^data:/i.test(path) || /^blob:/i.test(path)) return path
  const base = String(baseUrl || '').replace(/\/+$/, '')
  return `${base}/${path}`
}

export const fallbackImage = 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(`
<svg xmlns="http://www.w3.org/2000/svg" width="480" height="320" viewBox="0 0 480 320">
  <rect width="480" height="320" fill="#f3f6f1"/>
  <rect x="72" y="64" width="336" height="192" rx="18" fill="#ffffff" stroke="#dce6d8" stroke-width="2"/>
  <path d="M118 218l72-76 48 48 34-34 90 62H118z" fill="#b7c9ad"/>
  <circle cx="327" cy="116" r="24" fill="#d9e5d4"/>
  <text x="240" y="286" text-anchor="middle" font-family="Arial, sans-serif" font-size="22" fill="#6b7f65">图片暂不可用</text>
</svg>`)

export function installImageFallback() {
  if (typeof window === 'undefined') return
  window.addEventListener('error', event => {
    const target = event.target
    if (!target || target.tagName !== 'IMG' || target.dataset.fallbackApplied) return
    target.dataset.fallbackApplied = 'true'
    target.src = fallbackImage
  }, true)
}
