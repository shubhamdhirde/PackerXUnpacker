import java.util.Scanner;

class PackerXUnpackerMain
{
    public static void main(String arg[])throws Exception
    {   
        if(arg.length != 2)
        {
            System.out.println("Invalid number of arguments");
            System.exit(0);
        }        

        Scanner sobj = new Scanner(System.in);
        System.out.println("Enter (pack/unpack)");

        String options = sobj.nextLine();
        int iRet = 0;

        if(options.equals("pack"))
        {
            Packer pobj = new Packer();

            iRet = pobj.PackerFile(arg[0],arg[1]);
            if(0 == iRet)
            {
                System.out.println("Packed Successfully");
            }
            else if(-1 == iRet)
            {
                System.out.println(arg[0]+" is not a directory");
            }
        }
        else if(options.equals("unpack"))
        {
            Unpacker uobj = new Unpacker(arg[1]);

            iRet = uobj.Validation();
            if(iRet > 0)
            {
                System.out.println("File Unpacked Successfully");
            }
            else if(-1 == iRet)
            {
                System.out.println("Dammaged file");
            }
            else if(-2 == iRet)
            {
                System.out.println(arg[1]+" does not exists");
            }
        }
    }
}