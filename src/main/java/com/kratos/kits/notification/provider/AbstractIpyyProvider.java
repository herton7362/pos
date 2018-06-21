package com.kratos.kits.notification.provider;

import com.kratos.exceptions.BusinessException;
import com.kratos.kits.notification.NotificationProvider;
import com.kratos.kits.notification.message.NotificationMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

abstract class AbstractIpyyProvider<B extends NotificationMessage> implements NotificationProvider<B> {
    private final Log logger = LogFactory.getLog(this.getClass());

    void sendLog(HttpResponse response, String message) throws Exception {
        logger.info(response.getStatusLine().getReasonPhrase());
        HttpEntity entity = response.getEntity();
        // 将字符转化为XML
        Document doc = DocumentHelper.parseText(EntityUtils.toString(entity, "UTF-8"));
        // 获取根节点
        Element rootElt = doc.getRootElement();
        // 获取根节点下的子节点的值
        String returnstatus = rootElt.elementText("returnstatus").trim();
        String returnmessage = rootElt.elementText("message").trim();
        String remainpoint = rootElt.elementText("remainpoint").trim();
        String taskID = rootElt.elementText("taskID").trim();
        String successCounts = rootElt.elementText("successCounts").trim();

        logger.info("返回状态为：" + returnstatus);
        logger.info("返回信息提示：" + returnmessage);
        logger.info("返回余额：" + remainpoint);
        logger.info("返回任务批次：" + taskID);
        logger.info("返回成功条数：" + successCounts);

        EntityUtils.consume(entity);
        if(!"Success".equals(returnstatus)) {
            throw new BusinessException("短信发送失败请联系管理员");
        }
    }

    class SSLClient extends DefaultHttpClient {
        SSLClient() throws Exception {
            super();
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

            };
            ctx.init(null, new TrustManager[] { tm }, null);

            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = this.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));
        }
    }
}
