<!-- 
  ListagemTransferencias.vue - Componente de listagem de transferências
  Responsável por:
  - Exibir todas as transferências agendadas
  - Ordenação por data de agendamento ou transferência
  - Atualização da lista
-->
<template>
  <div class="container">
    <h2 class="page-title">Transferências Agendadas</h2>

    <div class="ordenacao">
      <label>Ordenar por:</label>
      <select v-model="ordenacao">
        <option value="dataAgendamento">Data de Agendamento</option>
        <option value="dataTransferencia">Data de Transferência</option>
      </select>
    </div>

    <table class="transferencias-table">
      <thead>
        <tr>
          <th>Conta Origem</th>
          <th>Conta Destino</th>
          <th>Valor</th>
          <th>Taxa</th>
          <th>Valor Total</th>
          <th>Data Agendamento</th>
          <th>Data Transferência</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="t in transferenciasOrdenadas" :key="t.id">
          <td>{{ t.contaOrigem }}</td>
          <td>{{ t.contaDestino }}</td>
          <td>R$ {{ t.valor.toFixed(2) }}</td>
          <td>R$ {{ t.taxa.toFixed(2) }}</td>
          <td>R$ {{ (parseFloat(t.valor) + parseFloat(t.taxa)).toFixed(2) }}</td>
          <td>{{ formatarData(t.dataAgendamento) }}</td>
          <td>{{ formatarData(t.dataTransferencia) }}</td>
        </tr>
      </tbody>
    </table>

    <button @click="carregarTransferencias" class="btn-atualizar">
      Atualizar Lista
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../services/api'
import '../assets/styles/components.css'

// Estado local
const transferencias = ref([])
const ordenacao = ref('dataAgendamento')

// Computed property para ordenar transferências
const transferenciasOrdenadas = computed(() => {
  return [...transferencias.value].sort((a, b) => {
    const dataA = new Date(a[ordenacao.value])
    const dataB = new Date(b[ordenacao.value])
    return dataA - dataB // Ordem crescente (mais antiga primeiro)
  })
})

// Carrega as transferências da API
const carregarTransferencias = async () => {
  try {
    const response = await api.get('/api/transferencias')
    transferencias.value = response.data
  } catch (error) {
    console.error('Erro ao carregar transferências:', error)
  }
}

// Formata a data para o padrão brasileiro
const formatarData = (data) => {
  return new Date(data).toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// Carrega as transferências ao montar o componente
onMounted(() => {
  carregarTransferencias()
})
</script> 