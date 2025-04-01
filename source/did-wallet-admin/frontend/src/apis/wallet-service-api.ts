import { getData } from "../utils/api";

const API_BASE_URL = "/wallet/admin/v1";

export const getWalletServiceInfo = async () => {
    return getData(API_BASE_URL, "wallet-service/info");
}