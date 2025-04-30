import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    host: '0.0.0.0',
    proxy: {
      // /wallet/admin/v1 → http://localhost:8095/wallet/admin/v1
      '/wallet/admin/v1': {
        target: 'http://localhost:8095',
        changeOrigin: true,
      },
    },
  },
});
