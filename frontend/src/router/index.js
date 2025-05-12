/**
 * Configuração das rotas da aplicação
 * Define os caminhos e componentes associados a cada rota
 */
import { createRouter, createWebHistory } from 'vue-router'
import Transferencias from '../views/Transferencias.vue'
import ListagemTransferencias from '../components/ListagemTransferencias.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'transferencias',
      component: Transferencias
    },
    {
      path: '/listagem',
      name: 'listagem',
      component: ListagemTransferencias
    }
  ]
})

export default router 