import React, { createContext, useContext, useState, ReactNode, useCallback } from 'react';
import { WalletServiceInfoResDto } from '../apis/models/WalletServiceInfoResDto';

export type ServerStatus = 'ACTIVATE' | 'DEACTIVATE' | 'REQUIRED_ENROLL_ENTITY';

interface ServerStatusContextType {
  serverStatus: ServerStatus | null;
  setServerStatus: (status: ServerStatus | null) => void;
  isLoading: boolean;
  setIsLoading: (loading: boolean, message?: string) => void;
  isLoadingMessage: string;
  walletServiceInfo: WalletServiceInfoResDto | null;
  setWalletServiceInfo: (info: WalletServiceInfoResDto | null) => void;
}

export const ServerStatusContext = createContext<ServerStatusContextType>({
  serverStatus: null,
  setServerStatus: () => {},
  isLoading: false,
  setIsLoading: () => {},
  isLoadingMessage: '',
  walletServiceInfo: null,
  setWalletServiceInfo: () => {},
});

export const ServerStatusProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [serverStatus, setServerStatus] = useState<ServerStatus | null>(null);
  const [isLoading, setIsLoadingState] = useState<boolean>(false);
  const [isLoadingMessage, setIsLoadingMessage] = useState<string>('');
  const [walletServiceInfo, setWalletServiceInfo] = useState<WalletServiceInfoResDto | null>(null);

  const setIsLoading = useCallback((loading: boolean, message?: string) => {
    setIsLoadingState(loading);
    setIsLoadingMessage(message ?? '처리 중입니다...');
  }, []);

  return (
    <ServerStatusContext.Provider 
    value={{ 
        serverStatus, 
        setServerStatus, 
        isLoading, 
        setIsLoading, 
        isLoadingMessage,
        walletServiceInfo,
        setWalletServiceInfo,
      }}
    >
      {children}
    </ServerStatusContext.Provider>
  );
};

export const useServerStatus = () => useContext(ServerStatusContext);
