module.exports = {
  lintOnSave: false,
  publicPath: process.env.NODE_ENV === 'development' ? './' : '././',
  outputDir: 'dist',
  devServer: {
    port: 8082,
    proxy: {
      '/springbooty2rp6': {
        target: 'http://localhost:8080/springbooty2rp6/',
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/springbooty2rp6': ''
        }
      }
    }
  }
}
