<script setup>
import { apps } from '../apps'
import AppIcon from '../components/AppIcon.vue'
</script>

<template>
  <div class="home-page">
    <header class="topbar"><span>工作空间 <span class="breadcrumb">/ 应用广场</span></span><span class="workspace-badge"><span /> 你的 AI 空间</span></header>
    <section class="home-content">
      <div class="eyebrow"><span /> 为每一个想法，点亮可能</div>
      <h1>你好，欢迎来到 <span>Lumina</span></h1>
      <p class="hero-description">一个空间，多种可能。选择你的 AI 伙伴，开启一段新的对话。</p>
      <div class="section-heading"><h2>探索应用 <span>02</span></h2><span>为生活添温度，为工作添效率</span></div>
      <div class="app-grid">
        <RouterLink v-for="app in apps" :key="app.id" :to="`/${app.id}`" :class="['app-card', app.id]">
          <div class="card-art">
            <span class="art-orbit orbit-one" /><span class="art-orbit orbit-two" />
            <span class="art-dot dot-one" /><span class="art-dot dot-two" />
            <div class="art-symbol"><AppIcon :name="app.icon" :size="57" /></div>
            <span class="art-caption">{{ app.id === 'love' ? 'A LITTLE MORE CONNECTION' : 'TURN IDEAS INTO ACTION' }}</span>
          </div>
          <div class="card-content">
            <div class="card-title"><h3>{{ app.name }}</h3><span class="app-badge">AI 应用</span></div>
            <div class="card-label">{{ app.label }}</div>
            <p>{{ app.description }}</p>
            <div class="tags"><span v-for="tag in app.tags" :key="tag">{{ tag }}</span></div>
            <div class="card-link">开始对话 <span><AppIcon name="arrow" :size="18" /></span></div>
          </div>
        </RouterLink>
      </div>
      <div class="home-note"><AppIcon name="spark" :size="19" /><span>好问题，是一切可能的开始。试着向你的 AI 伙伴说声「你好」。</span></div>
      <footer>让智能融入日常 <span>·</span> Made with Lumina</footer>
    </section>
  </div>
</template>
