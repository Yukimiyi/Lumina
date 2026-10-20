<script setup>
import { apps } from './apps'
import AppIcon from './components/AppIcon.vue'
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <RouterLink to="/" class="brand" aria-label="Lumina 首页">
        <span class="brand-mark"><AppIcon name="spark" :size="25" /></span>
        <span>Lumina</span>
      </RouterLink>
      <div class="nav-label">工作空间</div>
      <nav aria-label="主导航">
        <RouterLink to="/" class="nav-item" exact-active-class="active"><AppIcon name="grid" :size="19" /> 应用广场</RouterLink>
        <div class="nav-label apps-label">我的 AI 应用</div>
        <RouterLink v-for="app in apps" :key="app.id" :to="`/${app.id}`" class="nav-item" active-class="active">
          <AppIcon :name="app.icon" :size="19" /> {{ app.shortName }}
        </RouterLink>
      </nav>
      <div class="sidebar-bottom">
        <div class="small-spark">✧</div>
        <strong>用心相处，让爱发生</strong>
        <p>从倾听心事，到准备心意。</p>
        <span class="version">LUMINA WORKSPACE · 1.0</span>
      </div>
    </aside>
    <main class="main-panel">
      <RouterView v-slot="{ Component, route }">
        <component :is="Component" :key="route.path" />
      </RouterView>
    </main>
  </div>
</template>
