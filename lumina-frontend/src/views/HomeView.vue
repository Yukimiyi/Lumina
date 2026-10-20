<script setup>
import { apps } from '../apps'
import AppIcon from '../components/AppIcon.vue'
</script>

<template>
  <div class="home-page">
    <header class="topbar"><span>工作空间 <span class="breadcrumb">/ 应用广场</span></span><span class="workspace-badge"><span /> 你的 AI 空间</span></header>
    <section class="home-content">
      <div class="eyebrow"><span /> 让心意，有更好的表达</div>
      <h1>你好，欢迎来到 <span>Lumina</span></h1>
      <p class="hero-description">从读懂彼此，到准备一次特别的约会，Lumina 陪你一起用心经营关系。</p>
      <div class="section-heading"><h2>探索应用 <span>02</span></h2><span>聊聊心事，或一起准备下一次心动</span></div>
      <div class="app-grid">
        <RouterLink v-for="app in apps" :key="app.id" :to="`/${app.id}`" :class="['app-card', app.id]">
          <div class="card-art">
            <span class="art-orbit orbit-one" /><span class="art-orbit orbit-two" />
            <span class="art-dot dot-one" /><span class="art-dot dot-two" />
            <div class="art-symbol"><AppIcon :name="app.icon" :size="57" /></div>
            <span class="art-caption">{{ app.id === 'love' ? 'A LITTLE MORE CONNECTION' : 'MAKE ROOM FOR ROMANCE' }}</span>
          </div>
          <div class="card-content">
            <div class="card-title"><h3>{{ app.name }}</h3><span class="app-badge">{{ app.badge }}</span></div>
            <div class="card-label">{{ app.label }}</div>
            <p>{{ app.description }}</p>
            <div class="tags"><span v-for="tag in app.tags" :key="tag">{{ tag }}</span></div>
            <div class="card-link">{{ app.action }} <span><AppIcon name="arrow" :size="18" /></span></div>
          </div>
        </RouterLink>
      </div>
      <div class="home-note"><AppIcon name="spark" :size="19" /><span>有心事，就聊一聊；有心意，就一起把它变成行动。</span></div>
      <footer>让陪伴更懂你，让心意更具体 <span>·</span> Made with Lumina</footer>
    </section>
  </div>
</template>
