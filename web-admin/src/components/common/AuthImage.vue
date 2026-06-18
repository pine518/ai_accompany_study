<template>
  <div ref="hostRef" class="auth-image-host">
    <img
      v-if="imageSrc"
      :src="imageSrc"
      :alt="alt"
      :class="imgClass"
      :style="imgStyle"
      :loading="lazy ? 'lazy' : 'eager'"
      decoding="async"
      @error="handleError"
      @load="handleLoad"
    >
  </div>
</template>

<script setup>
/**
 * AuthImage - 支持认证的图片组件
 *
 * 用法：
 * <AuthImage :src="fileId" />
 * <AuthImage :src="fileId" fallback="/default.png" />
 * <AuthImage :src="fileId" :width="100" :height="100" />
 */
import { nextTick, onUnmounted, ref, watch } from 'vue'
import { removeCachedFileAccessUrl, resolveRenderableFileUrl } from '@/utils'

const props = defineProps({
  // 图片地址：可以是 fileId、filePath 或完整 URL
  src: {
    type: String,
    default: '',
  },
  // 加载失败时的默认图片
  fallback: {
    type: String,
    default: '',
  },
  // alt 属性
  alt: {
    type: String,
    default: '',
  },
  // 自定义 class
  imgClass: {
    type: [String, Array, Object],
    default: '',
  },
  // 自定义 style
  imgStyle: {
    type: [String, Object],
    default: '',
  },
  // 是否启用懒加载
  lazy: {
    type: Boolean,
    default: true,
  },
  // 访问地址缓存有效期（秒）
  expires: {
    type: Number,
    default: 43200,
  },
})

const emit = defineEmits(['load', 'error'])

const imageSrc = ref('')
const hostRef = ref(null)
const retried = ref(false)
let observer = null
let currentBlobUrl = ''

function isDirectUrl(url) {
  if (!url)
    return false
  return url.startsWith('http://')
    || url.startsWith('https://')
    || url.startsWith('data:')
    || url.startsWith('blob:')
    || url.startsWith('/api/file/')
}

// 加载图片
async function loadImage() {
  if (currentBlobUrl) {
    URL.revokeObjectURL(currentBlobUrl)
    currentBlobUrl = ''
  }

  if (!props.src) {
    imageSrc.value = props.fallback || ''
    return
  }

  try {
    const url = await resolveRenderableFileUrl(props.src, props.expires)
    if (!url) {
      throw new Error('图片地址为空')
    }
    imageSrc.value = url
    if (url.startsWith('blob:')) {
      currentBlobUrl = url
    }
  }
  catch (error) {
    console.warn('AuthImage: 图片加载异常', error)
    imageSrc.value = props.fallback || ''
  }
}

function observeLazyLoad() {
  if (!props.lazy || !hostRef.value) {
    loadImage()
    return
  }

  observer?.disconnect?.()
  observer = new IntersectionObserver((entries) => {
    const entry = entries[0]
    if (!entry?.isIntersecting) {
      return
    }
    observer?.disconnect?.()
    observer = null
    loadImage()
  }, {
    rootMargin: '220px 0px',
  })
  observer.observe(hostRef.value)
}

async function handleError(error) {
  if (!retried.value && props.src && !isDirectUrl(props.src)) {
    retried.value = true
    // 缓存的临时访问地址可能过期或仍是旧的相对路径，清理后强制重新解析一次。
    removeCachedFileAccessUrl(props.src)
    try {
      imageSrc.value = await resolveRenderableFileUrl(props.src, props.expires, true)
      return
    }
    catch (retryError) {
      emit('error', retryError)
    }
  }

  if (props.fallback && imageSrc.value !== props.fallback) {
    imageSrc.value = props.fallback
    return
  }
  emit('error', error)
}

function handleLoad() {
  emit('load')
}

// 监听 src 变化
watch(() => props.src, () => {
  imageSrc.value = ''
  retried.value = false
  nextTick(() => observeLazyLoad())
}, { immediate: true })

onUnmounted(() => {
  observer?.disconnect?.()
  observer = null
  if (currentBlobUrl) {
    URL.revokeObjectURL(currentBlobUrl)
    currentBlobUrl = ''
  }
})
</script>
