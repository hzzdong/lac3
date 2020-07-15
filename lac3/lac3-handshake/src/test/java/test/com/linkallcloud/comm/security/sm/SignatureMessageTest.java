package test.com.linkallcloud.comm.security.sm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.linkallcloud.sh.sm.ISignatureMessage;
import com.linkallcloud.sh.sm.SignatureMessage;

public class SignatureMessageTest {
    private static final String MSG_KEY = "kCCKeySizeAES128";
    private static final String SIGN_ID = "zhoudong";
    private static final String SIGN_KEY = "test123456";
    private static final String SIGN_ALG = "SHA1";

    /**
     * 简单的文本消息，明文传输方式
     * 
     * @throws Exception
     */
    @Test
    public void testSimpleMessage() throws Exception {
        String simpleMessage = "<person><id>10</id><name>zhoudong</name></person>";// 原始消息对象

        /*
         * 消息发送方签名、打包消息
         */
        ISignatureMessage sendSM = new SignatureMessage(simpleMessage, SIGN_ID, SIGN_ALG);
        sendSM.sign(SIGN_KEY);// 签名
        String sendMsgPkg = sendSM.packMessage();// 打包后的安全消息

        /*
         * 消息接收方验证签名、解密解包消息
         */
        ISignatureMessage receiveSM = SignatureMessage.from(sendMsgPkg);
        receiveSM.verifyStrict(SIGN_ID, SIGN_KEY, SIGN_ALG);// 验证签名
        String originalMsgObj = (String) receiveSM.unpackMessage(String.class);// 解包出原始消息
        // 或者 可以这样调用
        // String originalMsgObj = (String) receiveSM.unpackMessage(String.class,
        // SIGN_KEY, (String) null);//
        // 先验证签名，再解包出原始消息

        assertNotNull(originalMsgObj);
        assertEquals("<person><id>10</id><name>zhoudong</name></person>", originalMsgObj);
    }

    /**
     * 复杂的消息对象自动序列化成JSON格式，明文传输方式。
     * 
     * @throws Exception
     */
    @Test
    public void testMessage() throws Exception {
        Consumer c = new Consumer(1L, "zhoudong");

        /*
         * 消息发送方签名、打包消息
         */
        ISignatureMessage sendSM = new SignatureMessage(c, SIGN_ID);
        sendSM.sign(SIGN_KEY);// 签名
        String sendMsgPkg = sendSM.packMessage();// 签名后的安全消息

        /*
         * 消息接收方验证签名、解密解包消息
         */
        ISignatureMessage receiveSM = SignatureMessage.from(sendMsgPkg);
        receiveSM.verify(SIGN_KEY);// 验证签名
        Consumer originalMsgObj = (Consumer) receiveSM.unpackMessage(Consumer.class);// 解包出原始消息
        // 或者 可以这样调用
        // Consumer originalMsgObj = (Consumer) receiveSM.unpackMessage(Consumer.class,
        // SIGN_KEY, (String) null);//
        // 先验证签名，再解包出原始消息

        assertNotNull(originalMsgObj);
        assertEquals(1L, originalMsgObj.getId().longValue());
        assertEquals("zhoudong", originalMsgObj.getName());
    }

    /**
     * 复杂的消息对象自动序列化成JSON格式，密文传输方式
     * 
     * @throws Exception
     */
    @Test
    public void testEncryptMessage() throws Exception {
        Consumer c = new Consumer(1L, "zhoudong");
        MessageA msg = new MessageA(10L, "Hello zhoudong.", c);// 原始消息对象

        /*
         * 消息发送方签名、打包消息
         */
        ISignatureMessage sendSM = new SignatureMessage(msg, "AES", MSG_KEY, SIGN_ID, "SHA1");
        sendSM.sign(SIGN_KEY);// 签名
        String sendMsgPkg = sendSM.packMessage();// 签名后的安全消息

        /*
         * 消息接收方验证签名、解密解包消息
         */
        ISignatureMessage receiveSM = SignatureMessage.from(sendMsgPkg);
        receiveSM.verify(SIGN_KEY);// 验证签名
        MessageA originalMsgObj = (MessageA) receiveSM.unpackMessage(MessageA.class, MSG_KEY);// 解包出原始消息
        // 或者 可以这样调用
        // MessageA originalMsgObj = (MessageA) receiveSM.unpackMessage(MessageA.class,
        // SIGN_KEY, MSG_KEY);//
        // 先验证签名，再解包出原始消息

        assertNotNull(originalMsgObj);
        assertEquals(10L, originalMsgObj.getId().longValue());
        assertEquals("Hello zhoudong.", originalMsgObj.getContent());
        assertNotNull(originalMsgObj.getConsumer());
        assertEquals(1L, originalMsgObj.getConsumer().getId().longValue());
        assertEquals("zhoudong", originalMsgObj.getConsumer().getName());
    }
}
