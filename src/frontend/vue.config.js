const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})

module.exports = {
  devServer: {
    port: 8081, // Change le port du serveur de développement Vue.js
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Cible le backend Jetty
        changeOrigin: true
      }
    }
  }
};
