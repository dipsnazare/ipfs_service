import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ServiceServlet extends HttpServlet
{
	public static String addFileToIPFS(String data) throws Exception
	{
		File f=File.createTempFile("ipfs",".tmp",new File("c:/ipfs"));

		FileWriter fw=new FileWriter(f);
		fw.write(data);
		fw.close();

		ProcessBuilder pb=new ProcessBuilder("c:\ipfs\ipfs.exe","add",f.getAbsolutePath());
		pb.redirectOutputStream(true);

		Process p=pb.start();

		InputStream is=p.getInputStream();

		ByteArrayOutputStream baos=new ByteArrayOutputStream();

		byte []buffer=new byte[10240];
		int n;

		while((n=is.read(buffer))!=-1)
		{
			baos.write(buffer,0,n);
		}

		buffer=baos.toByteArray();

		baos=null;

		String str=new String(buffer,"UTF-8");
		StringReader sr=new StringReader(str);

		BufferedReader br=new BufferedReader(sr);
		br.readLine();
		String line=br.readLine();
		String []tokens=line.split(" ");
		String hash=tokens[1];

		br.close();

		p.waitFor();
		return hash;

	}

	public static String getFileFromIPFS(String hash) throws Exception
	{
		ProcessBuilder pb=new ProcessBuilder("c:\ipfs\ipfs.exe","cat",hash);
		pb.redirectOutputStream(true);

		Process p=pb.start();

		InputStream is=p.getInputStream();

		ByteArrayOutputStream baos=new ByteArrayOutputStream();

		byte []buffer=new byte[10240];
		int n;

		while((n=is.read(buffer))!=-1)
		{
			baos.write(buffer,0,n);
		}

		buffer=baos.toByteArray();

		baos=null;

		String str=new String(buffer,"UTF-8");
		return str;

	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		InputStream is=request.getInputStream();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();

		byte []buffer=new byte[10240];
		int n;

		while((n=is.read(buffer))!=-1)
		{
			baos.write(buffer,0,n);
		}

		buffer=baos.toByteArray();

		baos=null;

		String str=new String(buffer,"UTF-8");


		Map<String,Object> map=new HashMap<>();  //deserialize json str

		String command=map.get("command");

		if("PUT".equalsIgnoreCase(command))
		{
			String data=(String)map.get("data");

			
		}
	}

	public static void main(String []args) throws Exception
	{
		String hash=addFileToIPFS("Hello from java code");
		System.out.println("Uploaded file : "+hash);

		String data=getFileFromIPFS(hash);
		System.out.println("File Data : "+data);
	}
}