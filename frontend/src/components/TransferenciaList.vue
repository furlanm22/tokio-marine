<!-- 
  TransferenciaList.vue - Componente de lista de transferências
  Responsável por:
  - Exibir a lista de transferências em formato de cards
  - Gerenciar estados de carregamento e erro
  - Formatar valores e datas
-->
<template>
  <div class="transferencia-list">
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Carregando transferências...</p>
    </div>

    <div v-else-if="error" class="error-message">
      <h3>Não foi possível carregar as transferências</h3>
      <p>{{ error }}</p>
      <button @click="carregarTransferencias" class="retry-button">
        Tentar novamente
      </button>
    </div>

    <div v-else-if="transferencias.length === 0" class="no-data">
      <h3>Nenhuma transferência encontrada</h3>
      <p>Você ainda não possui transferências agendadas.</p>
    </div>

    <div v-else class="transferencias">
      <div v-for="transferencia in transferencias" :key="transferencia.id" class="transferencia-item">
        <div class="transferencia-info">
          <div class="info-row">
            <span class="label">Conta Origem:</span>
            <span class="value">{{ transferencia.contaOrigem }}</span>
          </div>
          <div class="info-row">
            <span class="label">Conta Destino:</span>
            <span class="value">{{ transferencia.contaDestino }}</span>
          </div>
          <div class="info-row">
            <span class="label">Valor:</span>
            <span class="value">R$ {{ formatarValor(transferencia.valor) }}</span>
          </div>
          <div class="info-row">
            <span class="label">Taxa:</span>
            <span class="value">R$ {{ formatarValor(transferencia.taxa) }}</span>
          </div>
          <div class="info-row">
            <span class="label">Data Agendamento:</span>
            <span class="value">{{ formatarData(transferencia.dataAgendamento) }}</span>
          </div>
          <div class="info-row">
            <span class="label">Data Transferência:</span>
            <span class="value">{{ formatarData(transferencia.dataTransferencia) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'
import '../assets/styles/transferencia-list.css'

// Estado local
const transferencias = ref([])
const loading = ref(true)
const error = ref('')

// Carrega as transferências da API
const carregarTransferencias = async () => {
  try {
    loading.value = true
    error.value = ''
    const response = await api.get('/api/transferencias', {
      headers: {
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache'
      }
    })
    transferencias.value = response.data
  } catch (err) {
    console.error('Erro ao carregar transferências:', err)
    error.value = err.response?.data?.message || 'Não foi possível carregar as transferências. Por favor, tente novamente mais tarde.'
  } finally {
    loading.value = false
  }
}

// Formata o valor para o padrão brasileiro
const formatarValor = (valor) => {
  return valor.toFixed(2).replace('.', ',')
}

// Formata a data para o padrão brasileiro
const formatarData = (data) => {
  return new Date(data).toLocaleDateString('pt-BR', {
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