import { useDialogs } from '@toolpad/core';
import React, { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router';
import { Box, Button, Popover, styled, TextField, Typography, useTheme } from '@mui/material';
import CustomDialog from '../../components/dialog/CustomDialog';
import FullscreenLoader from '../../components/loading/FullscreenLoader';
import { formatErrorMessage } from '../../utils/error-handler';
import { getWalletInfo } from '../../apis/wallet-api';

type Props = {}

type WalletFormData = {
    walletId: string;
    walletDid: string;
    createdAt: string;
    updatedAt: string;
};

const WalletDetailPage = (props: Props) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const dialogs = useDialogs();
    const theme = useTheme();

    const numericId = id ? parseInt(id, 10) : null;
    const [isLoading, setIsLoading] = useState<boolean>(true); 
    const [formData, serFormData] = useState<WalletFormData>({
        walletId: '',
        walletDid: '',
        createdAt: '',
        updatedAt: '',
    });

    useEffect(() => {
        const fetchData = async () => {
            if (numericId === null || isNaN(numericId)) {
                await dialogs.open(CustomDialog, { 
                    title: 'Notification', 
                    message: 'Invalid Path.', 
                    isModal: true 
                },{
                    onClose: async () => navigate('/list-settings/allowed-ca', { replace: true }),
                });
                return;
            }

            setIsLoading(true);

            try {
                const { data } = await getWalletInfo(numericId);
                serFormData({
                    walletId: data.walletId,
                    walletDid: data.walletDid,
                    createdAt: data.createdAt,
                    updatedAt: data.updatedAt,
                });
                setIsLoading(false);
            } catch (err) {
                  console.error('Failed to fetch Wallet information:', err);
                  setIsLoading(false);
                  navigate('/error', { state: { message: formatErrorMessage(err, "Failed to fetch Wallet Schema") } });
            }
        };

        fetchData();
    }, [numericId]);

    const StyledContainer = useMemo(() => styled(Box)(({ theme }) => ({
        width: 500,
        margin: 'auto',
        marginTop: theme.spacing(3),
        padding: theme.spacing(3),
        border: 'none',
        borderRadius: theme.shape.borderRadius,
        backgroundColor: '#ffffff',
        boxShadow: '0px 4px 8px 0px #0000001A',
    })), []);
        
    const StyledSubTitle = useMemo(() => styled(Typography)({
        textAlign: 'left',
        fontSize: '24px',
        fontWeight: 700,
    }), []);

    const StyledDescription = useMemo(() => styled(Box)(({ theme }) => ({
        maxWidth: 500, 
        marginTop: theme.spacing(1),
        padding: theme.spacing(0),
    })), []);

    const StyledInputArea = useMemo(() => styled(Box)(({ theme }) => ({
        marginTop: theme.spacing(2),
    })), []);

    return (
        <>
            <FullscreenLoader open={isLoading} />
            <Typography variant="h4">User Wallet Management</Typography>
            <StyledContainer>
                <StyledSubTitle>User Wallet Detail Information</StyledSubTitle>
                <StyledInputArea>
                    <TextField 
                        fullWidth
                        label="ID" 
                        variant="standard"
                        margin="normal" 
                        value={formData.walletId || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    <TextField 
                        fullWidth
                        label="ID" 
                        variant="standard"
                        margin="normal" 
                        value={formData.walletDid || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    <TextField 
                        fullWidth 
                        label="Registered At" 
                        variant="standard" 
                        margin="normal" 
                        value={formData.createdAt || ''} 
                        slotProps={{ input: { readOnly: true } }} 
                    />

                    {formData.updatedAt && (
                        <TextField 
                            fullWidth 
                            label="Updated At" 
                            variant="standard" 
                            margin="normal" 
                            value={formData.updatedAt} 
                            slotProps={{ input: { readOnly: true } }} 
                        />
                    )}

                    <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
                        <Button variant="outlined" color="primary" onClick={() => navigate('/wallet-management')}>
                            Back
                        </Button>
                    </Box>

                </StyledInputArea>
            </StyledContainer>
        </>
    )
}

export default WalletDetailPage