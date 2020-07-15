package tt;

import java.io.UnsupportedEncodingException;

import com.linkallcloud.core.lang.Lang;
import com.linkallcloud.core.security.PBKDF2;

public class Ttt {

    public static void main(String[] args) throws UnsupportedEncodingException {
        
        String userName = "%E5%91%A8%E6%A0%8B";
        String un = java.net.URLEncoder.encode(userName, "UTF-8");
        System.out.println(un);
        
        String userCode = "13770443959B6976E5D25B293DA734E15C39AF3BA7CED21DE5D25B293DA734E12D0BC18A39C7FECE";
        String uc = java.net.URLEncoder.encode(userCode, "UTF-8");
        System.out.println(uc);
        
        System.out.println(Lang.sha256("password"));
        
        testPBKDF2();
        testSHA1();
    }

    public static void testSHA1(){
        System.out.println("passwd sha256 is " + Lang.sha256("passwd") );
        System.out.println("df@tt1992 sha256 is " + Lang.sha256("Df@tt1992") );
        System.out.println("2wsx@WSX sha256 is " + Lang.sha256("2wsx@WSX") );
    }
    
    private static final String myPlainPassword = "TheCorrectPassword";
    private static final String myIncorrectPlainPassword = "AWrongPassword";
    
    public static void testPBKDF2() {
        PBKDF2 pbkdf2Factory = new PBKDF2();
        String hashedPassword = "";
        // Creating a new PBKDF2 password (in mosquitto-auth-plug format)
        System.out.println("Creating a new hash for \"" + Ttt.myPlainPassword + "\"...");
        hashedPassword = pbkdf2Factory.createPassword(Ttt.myPlainPassword);
        System.out.println("\t-> New hash for \"" + Ttt.myPlainPassword + "\": " + hashedPassword);
        // Validating the hashed password (this should be correct)
        System.out.println("Checking if \"" + hashedPassword + "\" is a valid hash for \"" + Ttt.myPlainPassword + "\"...");
        if (pbkdf2Factory.isValidPassword(Ttt.myPlainPassword, hashedPassword)) {
            System.out.println("\t-> Password match. It is valid!");
        } else {
            System.out.println("\t-> Password does not match. It is not valid");
        }
        // Validating the hashed password (this should be wrong)
        System.out.println("Checking if \"" + hashedPassword + "\" is a valid hash for \"" + Ttt.myIncorrectPlainPassword + "\"...");
        if (pbkdf2Factory.isValidPassword(Ttt.myIncorrectPlainPassword, hashedPassword)) {
            System.out.println("\t-> Password match. It is valid!");
        } else {
            System.out.println("\t-> Password does not match. It is not valid");
        }
    }

}
