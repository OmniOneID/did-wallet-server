import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router';
import App from './App';
import Layout from './layout/Layout';
import AdminManagementPage from './pages/admins/AdminManagementPage';
import SignInPage from './pages/auth/SignIn';
import ErrorPage from './pages/ErrorPage';
import WalletServiceManagementPage from './pages/wallet-service/WalletServiceManagementPage';
import TrustAgentRegistrationPage from './pages/wallet-service/WalletServiceRegistrationPage';
import AdminDetailPage from './pages/admins/AdminDetailPage';
import AdminRegisterPage from './pages/admins/AdminRegisterPage';
import DashboardPage from './pages/dashboard/DashboardPage';
import WalletManagementPage from './pages/wallet/WalletManagementPage';
import WalletDetailPage from './pages/wallet/WalletDetailPage';

const router = createBrowserRouter([
  {
    Component: App,
    children: [
      {
        path: '/',
        Component: Layout,
        children: [
          {
            path: '/',
            Component: DashboardPage
          },
          {
            path: '/wallet-service-registration',
            Component: TrustAgentRegistrationPage,
          },
          {
            path: '/wallet-service-management',
            Component: WalletServiceManagementPage,
          },
          {
            path: '/wallet-management/:id',
            Component: WalletDetailPage,
          },
          {
            path: '/wallet-management',
            Component: WalletManagementPage,
          },
          {
            path: 'admin-management/admin-registration',
            Component: AdminRegisterPage,
          },
          {
            path: 'admin-management/:id',
            Component: AdminDetailPage,
          },
          {
            path: 'admin-management',
            Component: AdminManagementPage,
          },
        ],
      },
      {
        path: '/sign-in',
        Component: SignInPage,
      },
      {
        path: '/error',
        Component: ErrorPage,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
);
