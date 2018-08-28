package cn.com.caogen.cash.util;


/**
 * 商户信息常量表
 * @author hardyHuang
 *
 */
public class MerchantConstants {
    //服务器公钥
    public static  String SERVERPUBLICKEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCabJDz/66tGW6J0SBHI3zTqz+vB7lkBwEcSnnaNJ6mAZ64Garc4Ax9lcFV9aUI3/v/w7LRnhPRnMCHc9HeBFS66jPixlvk3cB/TYsVoxuQInTE/VmQDv+9cRlKYpemULGr6VoeOzAoEHz68g/YUZCjFBxbhTyOKutBoCorsAmQeQIDAQAB";
    //商户私钥
    public static  String PRIVATEKEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOU0oJCwwcoQmHELPuR78LDEVL6cWhxaloTg7E7dE+mhuYkVN6i1WSI4SeaewVg/MpgEZJzL3T3oN6oeLVgBu2HwfGYfDG5XAm1EpkgmkXZUojqoZ8AXXZULXN7qlIkPS/XTo2Qsk3K6WaL0EHdrM5LZww+D0U/JCtx1EleT+VvvAgMBAAECgYBHhjOw0ye2CZW4ePzoSfDjCHE1hg7smCwuTQ4Q0hZbqrb+GxviTq0A67XL/LBy/E2qeHx5HXjtz6BHOuGFfAzfvNhGKspr2vbh16ZuTgd5IN6co1JlHv+t9Fh5wmmQjO6ENepOMSxncOn1+iTRB2KXVFGFrS6ZGoFmkWiFbgHBqQJBAP2KRc2Bf0rBR9So8AdajMCBsp9CDvQKDBAQFtcVF8G3aCSJSau7YKU1ohER6wZaAybrjh0jMolNEXL3Oezkbe0CQQDnben0BybPO9295YLTn2vCOHiwmK/OYi5wzGYATdkLMqzyp1b1OfOZEorn5C8VlHEoD0Wh9ezEPDVmFXjs4NXLAkADu7Z9aSu7qQ7qtbYbFZ0+9Gnx6GIZ+8/jqKj9b8USyCWTbCYWjkLFC/4f7DietlKXjx5flXYYCXd5TuSedVqdAkEA1hZVwIsFGdwVqt+wALiPZOH8s9DrgY5Ny8cp7MoXKEa+utKWnM/5SvdeTFpIPDxP2XEXR1ZKqtXPyBnA/dCXtQJBAOg/rXGcBlpXmf6RvB3fjep8FhEpv3IeABf1v5t4TvGs8Q92ET9OKv+ohvEgjY3BbgwXfSchYM/MzII5cIK+Bpw=";
    //测试商户号
    public static  String MERID="201805141416324";
    //测试终端号
    public static  String TERID="201806131315523";
    //测试服务器版本号
    public static  String VERSION="1.0.9";
    //商城supplierKey
    public static  String APIKEY="de03c7732f3cf45f35615ab7dbffa350";
    // 域名
    public static String URL = "https://gateway.zcmaopay.com";

    // 查询账号余额
    public static String QUERYBALANCE = URL + "/supApi/queryBalance";
    // 查询订单 状态
    public static String QUERYTRANSFERSTATUS = URL + "/supApi/queryTransferResult";
    // 单笔转账
    public static String SINGLETRANSFER = URL + "/supApi/singlePenTransfer";
    // 批量转账
    public static String BATCHTRANSFER = URL + "/supApi/batchPenTransfer";


}
