import { getData, postData } from "../utils/api";

const API_BASE_URL = "/wallet/admin/v1";

export const getWalletServiceInfo = async () => {
    return getData(API_BASE_URL, "wallet-service/info");
}


export const registerWalletServiceInfo = async (data: any) => {
    return postData(API_BASE_URL, `wallet-service/register-wallet-service-info`, data);
};

export const generateWalletServiceDidDocument = async () => {
    return postData(API_BASE_URL, `wallet-service/generate-did-auto`, undefined);
}

export const registerWalletServiceDidDocument = async (data: any) => {
    return postData(API_BASE_URL, `wallet-service/register-did`, data);
};

export const requestEntityStatus = async () => {
    return getData(API_BASE_URL, "wallet-service/request-status");
}

export const requestEnrollEntity = async () => {
    return postData(API_BASE_URL, "wallet-service/request-enroll-entity", undefined);
}