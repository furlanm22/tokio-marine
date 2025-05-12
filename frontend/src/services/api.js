/**
 * Configuração do cliente Axios para comunicação com a API
 * Define a URL base e headers padrão para todas as requisições
 */
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8081',
  headers: {
    'Content-Type': 'application/json',
    'Cache-Control': 'no-cache',
    'Pragma': 'no-cache'
  }
})

export default api 