import { Box, Button, TextField, Typography, styled } from '@mui/material';
import React, { useEffect, useMemo, useState } from 'react'
import { englishRegex, ipRegex, urlRegex } from '../../../utils/regex';
import { verifyServerUrl } from '../../../apis/server-api';
import CustomDialog from '../../../components/dialog/CustomDialog';
import { formatErrorMessage } from '../../../utils/error-handler';
import { useDialogs } from '@toolpad/core';
import { getWalletServiceInfo, registerWalletServiceInfo } from '../../../apis/wallet-service-api';

interface Props {
  step: number;
  onRegister: (step: number, validate: () => boolean, afterValidate?: () => Promise<void>) => void;
  setIsLoading: (loading: boolean) => void;
}

interface formData {
  name: string;
  serverUrl: string;
}

interface ErrorState {
  name?: string;
  serverUrl?: string;
}

const Step1WalletServiceInfo: React.FC<Props> = ({ step, onRegister, setIsLoading }) => {
  const [formData, setFormData] = useState<formData>({ name: '', serverUrl: '' });
  const [errors, setErrors] = useState<ErrorState>({});
  const [isServerValid, setIsServerValid] = useState(false);
  const dialogs = useDialogs();

  const handleChange = (field: keyof formData) => (event: React.ChangeEvent<HTMLInputElement>) => {
      const newValue = event.target.value;
      setFormData((prev) => ({ ...prev, [field]: newValue }));

      if (field === 'serverUrl') {
        setIsServerValid(false); 
        setErrors((prev) => ({ ...prev, serverUrl: undefined }));
      }
  };

  const validate = () => {
    let tempErrors: ErrorState = {};

    tempErrors.name = validateName(formData.name);
    tempErrors.serverUrl = validateServerUrl(formData.serverUrl);

    setErrors(tempErrors);
    return Object.values(tempErrors).every((error) => !error);
  };

  const validateName = (name?: string): string | undefined => {
    if (!name?.trim()) return 'Please enter a name.';
    if (name.length < 3 || name.length > 20) return 'Name must be between 3 and 20 characters.';
    if (!englishRegex.test(name)) return 'Name must be in English.';
    return undefined;
  };

  const validateServerUrl = (serverUrl?: string): string | undefined => {
      if (!serverUrl?.trim()) return 'Please enter the server URL.';
      if (!urlRegex.test(serverUrl) && !ipRegex.test(serverUrl)) return 'Please enter a valid URL.';
      if (serverUrl.length > 200) return 'URL must be less than 200 characters.';
      if (!isServerValid) return 'Please test the server connection.';
      return undefined;
  };

  const handleTestServerConnection = async () => {
    if (!formData.serverUrl) {
      setErrors((prev) => ({ ...prev, serverUrl: 'Please enter the server URL.' }));
      setIsServerValid(false);
      return;
    }

    if (!urlRegex.test(formData.serverUrl) && !ipRegex.test(formData.serverUrl)) {
        setErrors((prev) => ({ ...prev, serverUrl: 'Please enter a valid URL.' }));
        setIsServerValid(false);
        return;
    }

    let baseUrl;
    try {
        const url = new URL(formData.serverUrl);
        baseUrl = `${url.protocol}//${url.host}`;
    } catch (error) {
        setErrors((prev) => ({ ...prev, serverUrl: 'Invalid URL format.' }));
        setIsServerValid(false);
        return;
    }

    try {
        const response = await verifyServerUrl({ serverUrl: baseUrl });
        if (response.data.isAvailable === false) {
            setErrors((prev) => ({ ...prev, serverUrl: 'Test Connection failed.' }));
            setIsServerValid(false);
        } else {
            setIsServerValid(true);
            setErrors((prev) => ({ ...prev, serverUrl: undefined }));
        }
    } catch (error) {
        setErrors((prev) => ({ ...prev, serverUrl: 'Error occurred while testing connection.' }));
        setIsServerValid(false);
    }
  };

  const afterValidate = async () => {
    setIsLoading(true);
    await registerWalletServiceInfo(formData).then((response) => {
    }).catch((error) => {
      setIsLoading(false);
      dialogs.open(CustomDialog, {
        title: 'Notification',
        message: formatErrorMessage(error, `Failed to register Wallet Service info`),
        isModal: true,
      });
      throw error;
    });
  };

  useEffect(() => {
      const fetchWalletServiceInfo = () => {
        setIsLoading(true);
        getWalletServiceInfo()
            .then(({ data }) => {
              if (data.name || data.serverUrl) {
                setFormData({
                  name: data.name || '',
                  serverUrl: data.serverUrl || '',
                });
              
                setIsServerValid(!!data.serverUrl);
              }
            setIsLoading(false);
        })
        .catch((err) => {
          console.error('Error fetching Wallet Service info:', err);
          setIsLoading(false);
        });
      };

      fetchWalletServiceInfo();
  }, []);

  useEffect(() => {
    onRegister(step, validate, afterValidate);
  }, [formData, isServerValid]);

  const StyledDescription = useMemo(() => styled(Box)(({ theme }) => ({
      maxWidth: 600, 
      marginTop: theme.spacing(1),
      padding: theme.spacing(0),
  })), []);
    
  const StyledInputArea = useMemo(() => styled(Box)(({ theme }) => ({
      marginTop: theme.spacing(2),
  })), []);

  return (
    <>
      <Typography variant="h6" gutterBottom>
          Step 1 – Enter Wallet Service Info
      </Typography>
      <StyledDescription>
        <Typography variant="body1">
          Please enter the name and URL of the Wallet Service.
        </Typography>
        <Typography variant="body1" sx={{ mt: 1 }}>
          The URL should follow the format shown below:
        </Typography>
        <Box
          sx={(theme) => ({
            backgroundColor: theme.palette.mode === 'dark' ? '#333' : '#f5f5f5',
            color: theme.palette.mode === 'dark' ? '#fff' : '#000',
            padding: '8px 12px',
            borderRadius: '4px',
            fontFamily: 'monospace',
            display: 'inline-block',
            mt: 1,
            border: `1px solid ${theme.palette.divider}`,
          })}
        >
          http://{'{IP}'}:8095/wallet
        </Box>
        <Typography variant="body1" sx={{ mt: 1 }}>
          <strong>Note:</strong> The Wallet Service server and Admin Console currently use the same base URL,  
          as they are part of the same application and are separated by package.
        </Typography>
      </StyledDescription>

      <StyledInputArea>
        <TextField
            fullWidth
            label="Name *"
            variant="outlined"
            margin="normal"
            value={formData.name}
            onChange={handleChange('name')}
            error={!!errors.name}
            helperText={errors.name}
            sx={{ minLength: 3, maxLength: 20 }}
            slotProps={{ htmlInput: {maxLength: 20,},}}
        />

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <TextField
              fullWidth
              label="Wallet Service URL *"
              variant="outlined"
              margin="normal"
              value={formData.serverUrl}
              onChange={handleChange('serverUrl')}
              error={!!errors.serverUrl}
              helperText={errors.serverUrl}
              sx={{ minLength: 3, maxLength: 200 }}
              slotProps={{ htmlInput: {maxLength: 200,},}}
          />
          <Button 
              variant="outlined" 
              onClick={handleTestServerConnection} 
              disabled={!formData.serverUrl}
              sx={{ 
                  minWidth: 150,  
                  whiteSpace: 'nowrap',  
                  textTransform: 'none'
              }}
          >
              Test Connection
          </Button>
        </Box>
      </StyledInputArea>

    </>
  )
}

export default Step1WalletServiceInfo;