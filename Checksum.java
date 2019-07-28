import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Checksum
{
    public String getFileChecksum(MessageDigest digest, File file) 
    {
        try 
        {
            int Length = 0;
            byte buffer[] = new byte[1024];

            FileInputStream fis = new FileInputStream(file);
            
            //Read file data and update in message digest
            while ((Length = fis.read(buffer)) != -1) 
            {
                digest.update(buffer, 0, Length);
            }
            fis.close();

            //Get the hash's bytes
            byte bytes[] = digest.digest();

            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            //return complete hash
            return sb.toString();
        }
        catch (Exception e) 
        {
            System.out.println(e);
            return "err";
        }
    }
}