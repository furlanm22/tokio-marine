/**
 * Arquivo principal da aplicação Vue
 * Responsável por:
 * - Importar e configurar o Vue
 * - Importar e configurar o router
 * - Montar a aplicação no elemento raiz
 */
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router)
app.mount('#app')
