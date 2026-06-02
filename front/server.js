const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 8080;
const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

// Proxy de API al backend (endpoints por feature)
app.use(
  createProxyMiddleware({
    pathFilter: [
      '/autenticacion/**',
      '/usuario/**',
      '/vacaciones/**',
      '/incapacidad/**',
      '/cumpleanio/**',
      '/calamidad/**',
      '/administracion/**',
    ],
    target: BACKEND_URL,
    changeOrigin: true,
    on: {
      proxyReq: (proxyReq) => {
        proxyReq.removeHeader('origin');
        proxyReq.removeHeader('referer');
      },
      error: (err, req, res) => {
        res.status(502).json({ error: 'Backend unavailable', detail: err.message });
      },
    },
  })
);

// Estáticos del build de Angular (configurado en angular.json -> dist/mocks-ecopetrol)
app.use(express.static(path.join(__dirname, 'dist/mocks-ecopetrol')));

// SPA fallback
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/mocks-ecopetrol/index.html'));
});

app.listen(PORT, () => console.log(`Front server on port ${PORT}, backend: ${BACKEND_URL}`));
