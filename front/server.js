const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 8080;
const BACKEND_URL = process.env.BACKEND_URL || 'http://localhost:8080';

app.use(createProxyMiddleware({
  pathFilter: '/talento/**',
  target: BACKEND_URL,
  changeOrigin: true,
  on: {
    proxyReq: (proxyReq) => {
      proxyReq.removeHeader('origin');
      proxyReq.removeHeader('referer');
    },
    error: (err, req, res) => {
      res.status(502).json({ error: 'Backend unavailable', detail: err.message });
    }
  }
}));

app.use(express.static(path.join(__dirname, 'dist/mocks-ecopetrol')));

app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'dist/mocks-ecopetrol/index.html'));
});

app.listen(PORT, () => console.log(`Front server on port ${PORT}, backend: ${BACKEND_URL}`));
