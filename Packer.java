import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

public class Packer extends Checksum
{
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          PackerFile
    //  Input :         String String
    //  Output :        Integer
    //  Description :   This function is used to pack multiple files into one file
    //  Date :          11 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int PackerFile(String folderName,String PackedFile)
    {
        File folder = new File(folderName);

        // Check given foldername is directory or not
        if(folder.isDirectory())
        {
            try 
            {
                int len = 0;
                int length = 0;
                long fsize = 0;
                String size = null;
                String filen = null;
                byte buffer[] = new byte[1024];
                File files[] = folder.listFiles();

                ArrayList<Long> Size = new ArrayList<Long>();
                ArrayList<String> FName = new ArrayList<String>();

                FileOutputStream foobj = new FileOutputStream(PackedFile);

                // Creating ArrayList of file name and their size in directory
                for (File file : files)
                {
                    if(!file.isDirectory())
                    {
                        FName.add((file.getName()));
                        Size.add((file.length()));
                        len++;
                    }
                }   

                //File packing 
                for(int i=0; i<len; i++)
                {
                    filen = FName.get(i);
                    fsize = Size.get(i);
                    FileInputStream fobj = new FileInputStream("./"+folderName+"/"+filen);

                    //  Header of packed file  

                    foobj.write((filen).getBytes());
                    foobj.write(" ".getBytes());
                    foobj.write(String.valueOf(fsize).getBytes());

                    size = String.valueOf(fsize);
                    foobj.write((" ".repeat(100-filen.length()-2-size.length())).getBytes());

                    //  writing data into packed file  
                
                    while((length = fobj.read(buffer)) > 0)
                    {
                        foobj.write(buffer,0,length);
                    }
                    fobj.close();
                }
                foobj.close();

                File encr = new File(PackedFile);

                CipherFile cfobj = new CipherFile(encr.getName());
                cfobj.EncryptFile();

                File rm = new File(PackedFile);
                rm.delete();

                //creating checksum
                Checksum cobj = new Checksum();

                File file = new File(PackedFile+".enc");

                MessageDigest shaDigest = MessageDigest.getInstance("SHA-1");
                String shaChecksum = cobj.getFileChecksum(shaDigest, file);
                
                if(shaChecksum.equals("err"))
                {
                    System.out.println("Exception thrown");
                    System.exit(-1);
                }

                foobj = new FileOutputStream("checksum.log");
                foobj.write(shaChecksum.getBytes());
                foobj.close();

                return 0;
            } 
            catch (Exception e) 
            {
                System.out.println(e);
                return -2;
            }
        }
        else
        {
            return -1;
        }
    }
}