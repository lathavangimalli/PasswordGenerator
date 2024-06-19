import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class PasswordGeneratorApp {
    private static final String ALGORITHM = "AES";
    private static SecretKey secretKey;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Generate a secret key for encryption
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128);
            secretKey = keyGen.generateKey();
        } catch (Exception e) {
            System.out.println("Error initializing encryption: " + e.getMessage());
            return;
        }
        
        System.out.print("Enter password length: ");
        int length = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Include special characters (yes/no): ");
        boolean includeSpecialChars = scanner.nextLine().equalsIgnoreCase("yes");

        String password = generatePassword(length, includeSpecialChars);
        System.out.println("Generated Password: " + password);

        String encryptedPassword = encryptPassword(password);
        System.out.println("Encrypted Password: " + encryptedPassword);
    }

    private static String generatePassword(int length, boolean includeSpecialChars) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String specialChars = "!@#$%^&*()-_=+<>?";
        String allowedChars = includeSpecialChars ? chars + specialChars : chars;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            password.append(allowedChars.charAt(index));
        }
        return password.toString();
    }

    private static String encryptPassword(String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            System.out.println("Error encrypting password: " + e.getMessage());
            return null;
        }
    }
}