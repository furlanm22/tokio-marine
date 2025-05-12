<!-- 
  Transferencias.vue - Componente de visualização de transferências
  Responsável por:
  - Exibir o formulário de agendamento de transferências
  - Gerenciar o estado da página
-->
<template>
  <div class="form-container">
    <h2 class="page-title">Nova Transferência</h2>
    <TransferenciaForm @transferencia-agendada="onTransferenciaAgendada" />
  </div>
</template>

<script setup>
import TransferenciaForm from '../components/TransferenciaForm.vue'
import '../assets/styles/components.css'

const onTransferenciaAgendada = () => {
  // Aqui podemos adicionar uma lógica para redirecionar para a listagem
  // ou mostrar uma mensagem de sucesso
}
</script> 