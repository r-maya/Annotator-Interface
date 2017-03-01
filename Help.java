import java.io.*;  
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Help implements Runnable {
	
	Database db = new Database();
	Socket s;
	
	public Help(Socket s){
		this.s = s;
	}
	
	public void run() {
		try{
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			DataInputStream in = new DataInputStream(s.getInputStream());
			
			String input = in.readUTF();
			System.out.println(input);
			String usr = input;
			System.out.println("usr--"+usr);
			out.writeUTF(usr+"-okay");
			String pwd = in.readUTF();
			System.out.println("pwd--"+pwd);
			out.writeUTF(pwd+"-okay");
			
			//System.out.println(usr+"___"+pwd);
			if(db.checkLogin(usr,pwd)){
				out.writeUTF("login-okay");
				System.out.println("login-okay");
			}
			else
				out.writeUTF("no-okay");
			input = in.readUTF(); 
			System.out.println(input);
			out.writeUTF(String.valueOf(Files.list(Paths.get("/Users/RaghuRRB/Documents/annotator/event/source")).count()));
			System.out.println(String.valueOf(Files.list(Paths.get("/Users/RaghuRRB/Documents/annotator/event/source")).count()));
			String source = "/Users/RaghuRRB/Documents/annotator/event/source";
			
			while(input != "----end----"){
				input = in.readUTF();
				System.out.println(input);
				if((input.equals("file1"))||(input.equals("file1"))||(input.equals("file1"))){
					String filepath = source+"/"+input+".txt";
					try{
					      
						 System.out.println("check");
					       FileReader reader = new FileReader(filepath);
					       System.out.println("check");
					        BufferedReader bufferedReader = new BufferedReader(reader);
				        String line;
				        String xx = "";
				        System.out.println("check");
				        while ((line = bufferedReader.readLine()) != null) {
				        	xx = xx+line;
				        }
				        System.out.println(xx);
				        out.writeUTF(xx);
				        //out.writeUTF("end");
	
				    }
				    catch(FileNotFoundException e){
				    	System.out.println(e);
				    }  
				}
			}
		}
		catch(Exception e){
			
		}
		finally{
			
		}
	}
}