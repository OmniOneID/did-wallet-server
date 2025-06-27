import StorageIcon from '@mui/icons-material/Storage';
import { type Navigation } from '@toolpad/core/AppProvider';


export const getNavigationByStatus = (serverStatus: string | null): Navigation=> {
  if (serverStatus !== 'ACTIVATE') {
    return [{ segment: 'wallet-service-registration', title: 'Wallet Service Registration'}];
  } 
  return [
    {
      kind: 'divider',
    },
    { 
      segment: 'wallet-service-management', 
      title: 'Wallet Service Management', 
    },
    { 
      segment: 'wallet-management', 
      title: 'User Wallet Management', 
    },
    {
      segment: 'admin-management',
      title: 'Admin Management', 
    },
    {
      kind: 'divider',
    },
  ];
};
