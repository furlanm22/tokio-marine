<!-- 
  App.vue - Componente principal da aplicação
  Responsável por:
  - Estrutura base do layout (header e main)
  - Navegação entre páginas
  - Estilos globais do layout
-->
<script>
import './assets/styles/components.css'

export default {
  name: 'App'
}
</script>

<template>
  <div class="app">
    <header>
      <div class="header-content">
        <h1>Sistema de Transferências</h1>
        <nav>
          <router-link to="/" class="nav-link">Nova Transferência</router-link>
          <router-link to="/listagem" class="nav-link">Extrato</router-link>
        </nav>
      </div>
    </header>

    <main>
      <router-view></router-view>
    </main>
  </div>
</template>
