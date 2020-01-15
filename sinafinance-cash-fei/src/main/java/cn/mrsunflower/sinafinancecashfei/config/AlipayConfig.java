package cn.mrsunflower.sinafinancecashfei.config;


import org.springframework.stereotype.Component;

@Component
public class AlipayConfig {

    // 商户ID
    private String appid = "";
    // 私钥
    private String rsa_private_key = "";
    // 异步回调地址
    private String notify_url;
    // 同步回调地址
    private String return_url;
    // 请求网关地址
    private String gateway_url;
    // 编码
    private String charset = "UTF-8";
    // 返回格式
    private String format = "json";
    // 支付宝公钥
    private String alipay_public_key = "";
    // RSA2
    private String signtype = "RSA2";

    // 省略get set


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRsa_private_key() {
        return rsa_private_key;
    }

    public void setRsa_private_key(String rsa_private_key) {
        this.rsa_private_key = rsa_private_key;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getGateway_url() {
        return gateway_url;
    }

    public void setGateway_url(String gateway_url) {
        this.gateway_url = gateway_url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getAlipay_public_key() {
        return alipay_public_key;
    }

    public void setAlipay_public_key(String alipay_public_key) {
        this.alipay_public_key = alipay_public_key;
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }
}
