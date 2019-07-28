
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;

public class Unpacker extends Checksum
{
    String PackedFile;

    Unpacker(String Packed)
    {   
        PackedFile = Packed;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          Validation
    //  Input :         None
    //  Output :        Integer
    //  Description :   This function is used to validate checksum of file
    //  Date :          13 July 2019 / 20 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int Validation()
    {
        File extract = new File(PackedFile+".enc");
        if(extract.exists())
        {
            String str1 = null;
            String str2 = null;
            FileReader frobj1 = null;
            FileReader frobj2 = null;
            BufferedReader bobj1 = null;
            BufferedReader bobj2 = null;
            FileOutputStream foobj = null;
    
            try 
            {
                File file = new File(PackedFile+".enc");
                Checksum cobj = new Checksum();
                
                MessageDigest shaDigest = MessageDigest.getInstance("SHA-1");
               
                String shaChecksum = cobj.getFileChecksum(shaDigest, file);  //checksum SHA-1 call
                if(shaChecksum.equals("err"))
                {
                    System.out.println("Exception thrown");
                    System.exit(-1);
                }

                foobj = new FileOutputStream("./validation.log");
                foobj.write(shaChecksum.getBytes());
                foobj.close();

                // comapare checksum
                File src = new File("checksum.log");
                File dest = new File("validation.log");

                if(src.length() != dest.length())
                {
                    return -1;
                }

                frobj1 = new FileReader(src);
                bobj1 = new BufferedReader(frobj1);

                frobj2 = new FileReader(dest);
                bobj2 = new BufferedReader(frobj2);

                while(((str1 = bobj1.readLine()) != null) &&
                      ((str2 = bobj2.readLine()) != null))
                {
                    if(! str1.equals(str2))
                    {
                        break;
                    }
                }

                foobj.close();
                bobj1.close();
                bobj2.close();

                if((str1 != null)&&
                   (str2 != null))
                {
                    return -1;
                }
                else
                {
                    int iRet = FileUnpack(PackedFile);
                    
                    return iRet;
                }
            }
            catch (Exception e) 
            {
                System.out.println(e);
                return -3;
            }    
        }
        else
        {
            return -2;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //  Name :          FileUnpack
    //  Input :         String
    //  Output :        Integer
    //  Description :   This function is used to unpack archieved files
    //  Date :          14 July 2019 / 20 July 2019
    //  Author :        Shubham Girish Dhirde
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    int FileUnpack(String Unpack)
    {
        int i = 0;
        int len = 100;
        int length = 0;   

        String fname = null;
        String tokens[] = null;

        byte buffer[] = new byte[len];

        BufferedReader bobj = null;
        FileOutputStream foobj = null;
        FileOutputStream foobj1 = null;

        ArrayList<String> FName = new ArrayList<String>();
        ArrayList<Integer> Size = new ArrayList<Integer>();
        
        try
        {
            File dcry = new File(PackedFile+".enc");
            CipherFile cfobj = new CipherFile(dcry.getName());
            cfobj.DecryptFile();
            dcry.delete();

            FileInputStream fiobj = new FileInputStream(PackedFile);
            
            // Create ArrayList of Filenames and their respective sizes
            while((length = fiobj.read(buffer)) > 0)
            {
                foobj1 = new FileOutputStream("header.txt");
                foobj1.write(buffer,0,length);

                bobj = new BufferedReader(new FileReader("header.txt"),100);
                
                fname = bobj.readLine();
                tokens = fname.split(" ");
                FName.add(tokens[0]);

                len = Integer.valueOf(tokens[1]);
                Size.add(len);

                foobj1.close();

                buffer = new byte[len-1];
                fiobj.read(buffer);
                
                buffer = new byte[100];
            }
            File header = new File("header.txt");
            header.delete();
            fiobj.close();
            bobj.close();

            // Exctract files from packed file
            fiobj = new FileInputStream(PackedFile);
            buffer = new byte[99];
            len = 0;

            while((length = fiobj.read(buffer)) > 0)
            {
                fname = FName.get(i);
                len = Size.get(i);

                buffer = new byte[len];
                fiobj.read(buffer);

                foobj = new FileOutputStream(fname);
                foobj.write(buffer);
                foobj.close();
                
                buffer = new byte[99];
                i++;
            }
            fiobj.close();
            
            File src = new File("checksum.log");
            File dest = new File("validation.log");

            dest.delete();
            src.delete();

            return 1;
        }
        catch(Exception e)
        {
            System.out.println(e);
            return -4;
        }
    }
}