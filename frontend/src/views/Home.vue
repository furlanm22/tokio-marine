<!-- 
  Home.vue - Componente da página inicial
  Responsável por:
  - Exibir a página de boas-vindas
  - Apresentar os principais recursos do sistema
-->
<template>
  <div class="home">
    <h1>Tokio Marine</h1>
    <p>Sistema de Agendamento de Transferências</p>
    <div class="features">
      <div class="feature-card">
        <h3>Agendamento</h3>
        <p>Agende suas transferências com facilidade</p>
      </div>
      <div class="feature-card">
        <h3>Taxas</h3>
        <p>Cálculo automático de taxas</p>
      </div>
      <div class="feature-card">
        <h3>Histórico</h3>
        <p>Acompanhe todas as suas transferências</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import '../assets/styles/home.css'
</script> 