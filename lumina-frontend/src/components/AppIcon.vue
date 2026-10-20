<script setup>
defineProps({ name: String, size: { type: Number, default: 24 } })
</script>

<template>
  <svg :width="size" :height="size" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
    <path v-if="name === 'heart'" d="M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1.1-1.1a5.5 5.5 0 0 0-7.8 7.8L12 21l8.8-8.6a5.5 5.5 0 0 0 0-7.8Z" />
    <path v-else-if="name === 'spark'" d="m12 3 2.8 6.2L21 12l-6.2 2.8L12 21l-2.8-6.2L3 12l6.2-2.8L12 3Z" />
    <path v-else-if="name === 'grid'" d="M3 3h7v7H3zM14 3h7v7h-7zM3 14h7v7H3zM14 14h7v7h-7z" />
    <path v-else-if="name === 'arrow'" d="M5 12h14m-6-6 6 6-6 6" />
    <path v-else-if="name === 'send'" d="M12 19V5m-6 6 6-6 6 6" />
    <path v-else-if="name === 'plus'" d="M12 5v14M5 12h14" />
    <path v-else-if="name === 'stop'" d="M6 6h12v12H6z" />
    <path v-else-if="name === 'user'" d="M20 21a8 8 0 0 0-16 0M12 13a5 5 0 1 0 0-10 5 5 0 0 0 0 10Z" />
    <path v-else d="M12 3v18M3 12h18" />
  </svg>
</template>
