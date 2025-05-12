<!-- 
  TransferenciaForm.vue - Componente de formulário de transferência
  Responsável por:
  - Agendamento de transferências
  - Validação dos campos
  - Cálculo de taxas
  - Envio dos dados para a API
-->
<template>
  <form @submit.prevent="enviarTransferencia" class="transferencia-form">
    <div class="form-group">
      <label for="contaOrigem">Conta de Origem</label>
      <input
        type="text"
        id="contaOrigem"
        v-model="form.contaOrigem"
        :class="{ error: erros.contaOrigem }"
        placeholder="Digite o número da conta de origem"
      />
      <span class="error-message" v-if="erros.contaOrigem">{{ erros.contaOrigem }}</span>
    </div>

    <div class="form-group">
      <label for="contaDestino">Conta de Destino</label>
      <input
        type="text"
        id="contaDestino"
        v-model="form.contaDestino"
        :class="{ error: erros.contaDestino }"
        placeholder="Digite o número da conta de destino"
      />
      <span class="error-message" v-if="erros.contaDestino">{{ erros.contaDestino }}</span>
    </div>

    <div class="form-group">
      <label for="valor">Valor da Transferência</label>
      <input
        type="text"
        id="valor"
        v-model="valorDigitado"
        @input="onValorInput"
        @blur="onValorBlur"
        :class="{ error: erros.valor }"
        placeholder="Digite o valor da transferência"
        inputmode="decimal"
      />
      <span class="error-message" v-if="erros.valor">{{ erros.valor }}</span>
    </div>

    <div class="form-group">
      <label for="dataTransferencia">Data e hora da transferência</label>
      <div class="datetime-inputs">
        <input
          type="date"
          id="dataTransferencia"
          v-model="form.dataTransferencia"
          :class="{ error: erros.dataTransferencia }"
          :min="dataMinima"
        />
        <input
          type="text"
          id="horaTransferencia"
          v-model="horaFormatada"
          @input="onHoraInput"
          @blur="onHoraBlur"
          maxlength="5"
          placeholder="HH:mm"
          :class="{ error: erros.horaTransferencia }"
          inputmode="numeric"
        />
      </div>
      <span class="error-message" v-if="erros.dataTransferencia">{{ erros.dataTransferencia }}</span>
      <span class="error-message" v-if="erros.horaTransferencia">{{ erros.horaTransferencia }}</span>
    </div>

    <button type="submit" :disabled="enviando">
      {{ enviando ? 'Enviando...' : 'Agendar Transferência' }}
    </button>

    <div v-if="erro" class="error-message">
      <h3>Erro ao agendar transferência</h3>
      <p>{{ erro }}</p>
    </div>

    <div v-if="sucesso" class="success-message">
      <h3>Transferência agendada com sucesso!</h3>
      <p>A transferência foi agendada e será processada na data especificada.</p>
    </div>
  </form>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import api from '../services/api'
import '../assets/styles/transferencia-form.css'

// Estado local
const form = ref({
  contaOrigem: '',
  contaDestino: '',
  valor: '',
  dataTransferencia: '',
  horaTransferencia: ''
})

const valorDigitado = ref('')
const horaFormatada = ref('')

// Sincronizar valorDigitado <-> form.value.valor
function onValorInput(e) {
  // Permite apenas números, vírgula e ponto
  let limpo = e.target.value.replace(/[^\d,.]/g, '')
  valorDigitado.value = limpo
  // Atualiza o valor numérico no form
  let valorNumerico = limpo.replace(',', '.')
  form.value.valor = valorNumerico
}
function onValorBlur() {
  // Ao sair do campo, formata como moeda se possível
  let num = parseFloat(valorDigitado.value.replace(',', '.'))
  if (!isNaN(num)) {
    valorDigitado.value = num.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
    form.value.valor = num.toFixed(2)
  } else {
    valorDigitado.value = ''
    form.value.valor = ''
  }
}

function onHoraInput(e) {
  horaFormatada.value = e.target.value
}

function onHoraBlur() {
  let limpo = horaFormatada.value.replace(/[^\d]/g, '');
  if (limpo.length === 4) {
    let horas = limpo.slice(0, 2);
    let minutos = limpo.slice(2, 4);
    horaFormatada.value = `${horas}:${minutos}`;
  }
  form.value.horaTransferencia = horaFormatada.value;
}

const erros = ref({})
const erro = ref('')
const sucesso = ref(false)
const enviando = ref(false)

// Computed properties
const dataMinima = computed(() => {
  const hoje = new Date()
  return hoje.toISOString().split('T')[0]
})

function getSaoPauloNow() {
  const now = new Date();
  const sp = new Date(now.toLocaleString('en-US', { timeZone: 'America/Sao_Paulo' }));
  return sp;
}

// Funções
const validarFormulario = () => {
  erros.value = {}
  let valido = true

  if (!form.value.contaOrigem) {
    erros.value.contaOrigem = 'A conta de origem é obrigatória'
    valido = false
  }

  if (!form.value.contaDestino) {
    erros.value.contaDestino = 'A conta de destino é obrigatória'
    valido = false
  }

  if (!form.value.valor || parseFloat(form.value.valor) <= 0) {
    erros.value.valor = 'O valor deve ser maior que zero'
    valido = false
  }

  if (!form.value.dataTransferencia) {
    erros.value.dataTransferencia = 'A data é obrigatória'
    valido = false
  }

  if (!form.value.horaTransferencia) {
    erros.value.horaTransferencia = 'A hora é obrigatória'
    valido = false
  }

  return valido
}

const enviarTransferencia = async () => {
  if (!validarFormulario()) return

  enviando.value = true
  erro.value = ''
  sucesso.value = false

  try {
    // Monta a data UTC correspondente ao horário escolhido em São Paulo
    const [ano, mes, dia] = form.value.dataTransferencia.split('-').map(Number)
    const [hora, minuto] = form.value.horaTransferencia.split(':').map(Number)
    const dataSaoPaulo = new Date(Date.UTC(ano, mes - 1, dia, hora, minuto))

    const dados = {
      contaOrigem: form.value.contaOrigem,
      contaDestino: form.value.contaDestino,
      valor: parseFloat(form.value.valor),
      dataTransferencia: dataSaoPaulo.toISOString()
    }

    await api.post('/api/transferencias', dados)
    sucesso.value = true
    form.value = {
      contaOrigem: '',
      contaDestino: '',
      valor: '',
      dataTransferencia: '',
      horaTransferencia: ''
    }
    valorDigitado.value = ''
    horaFormatada.value = ''
  } catch (error) {
    erro.value = error.response?.data?.message || 'Erro ao agendar transferência'
  } finally {
    enviando.value = false
  }
}
</script> 