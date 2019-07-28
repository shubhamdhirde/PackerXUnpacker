import java.io.File;
import java.util.Scanner;
import java.security.Key;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.NumberFormat.Style;
import java.io.FileNotFoundException;

import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class CipherFile 
{
    static int attempt = 3;
    String Ciphered;
    String inputKey;

    Scanner sobj = new Scanner(System.in);

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          CipherFile
    //  Description :   Constructor of class CipherFile
    //  Date :          28 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    CipherFile(String File)
    {
        Ciphered = File;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          EncryptFile
    //  Input :         None
    //  Output :        None
    //  Description :   This function accept 16 bit key for encryption
    //  Date :          28 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void EncryptFile()throws Exception
    {
        System.out.println("Enter 16 bit key");
        while(true)
        {
            inputKey = sobj.nextLine();
            if(inputKey.length() == 16)
            {
                break;
            }
            else if((inputKey.length()) < 16 || (inputKey.length())>16)
            {
                attempt--;
                if(attempt == 0)
                {
                    System.exit(0);
                }
                System.out.println("Enter again");
            }
        }
        File inputFile = new File(Ciphered);
        File outputFile = new File(Ciphered+".enc");
        
        processFile(true,inputFile,outputFile,inputKey);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          DecryptFile
    //  Input :         None
    //  Output :        None
    //  Description :   This function accept 16 bit key for Decryption
    //  Date :          28 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void DecryptFile()throws Exception
    {
        System.out.println("Enter 16 bit key");
        while(true)
        {
            inputKey = sobj.nextLine();
            if(inputKey.length() >= 8)
            {
                break;
            }
            else
            {
                attempt--;
                if(attempt == 0)
                {
                    System.exit(0);
                }
                System.out.println("Enter again");
            }
        }

        File inputFile = new File(Ciphered);
        String Token[] = Ciphered.split(".enc");
        File outputFile = new File(Token[0]);
    
        processFile(false,inputFile,outputFile,inputKey);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          processFile
    //  Input :         BOOLEAN, FILE, FILE, STRING
    //  Output :        None
    //  Description :   This function encrypts/ decrypts packed file
    //  Date :          28 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////    
    
    public void processFile(boolean encrypt,File inputFile, File outputFile,String inputKey)
    {
        try
        {
            Key key = new SecretKeySpec(inputKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
    
            if(encrypt)
            {
                cipher.init(Cipher.ENCRYPT_MODE,key);
            }
            else
            {
                cipher.init(Cipher.DECRYPT_MODE,key);
            }
    
            FileInputStream fileInputStream = new FileInputStream(inputFile);
    
            byte inputBytes[] = new byte[(int)inputFile.length()];
            fileInputStream.read(inputBytes);
    
            byte outputBytes[] = cipher.doFinal(inputBytes);
    
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
    
            fileOutputStream.write(outputBytes);
    
            fileInputStream.close();
            fileOutputStream.close();
        }
        catch(NoSuchAlgorithmException n)
        {
            System.out.println(n);
        }
        catch(InvalidKeyException k)
        {
            System.out.println(k);
        }      
        catch(FileNotFoundException f)
        {
            System.out.println(f);
        }
        catch(IOException io)
        {
            System.out.println(io);
        }
        catch(IllegalBlockSizeException il)
        {
            System.out.println(il);
        }   
        catch(NoSuchPaddingException np)
        {

        } 
        catch(BadPaddingException bp)
        {
            System.out.println("Wrong key");
        }
    }
}