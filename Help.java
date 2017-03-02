import java.io.*;  
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Help implements Runnable {
	
	Database db = new Database();
	Socket s;
	String usr;
	int id;
	int loggedin = 0;
	
	public Help(Socket s){
		this.s = s;
	}
	
	public void run() {
		try{
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			DataInputStream in = new DataInputStream(s.getInputStream());
			String input,pwd;
			
			loggedin = 0;
			//System.out.println(usr+"___"+pwd);
			while(loggedin == 0){
				input = in.readUTF();
				System.out.println(input);
				usr = input;
				System.out.println("usr--"+usr);
				out.writeUTF(usr+"-okay");
				pwd = in.readUTF();
				System.out.println("pwd--"+pwd);
				out.writeUTF(pwd+"-okay");
				
				if(db.checkLogin(usr,pwd)){
					out.writeUTF("login-okay");
					System.out.println("login-okay");
					loggedin = 1;
				}
				else{
					out.writeUTF("not-okay");
					System.out.println("wrongdata");
				}
			}
			input = in.readUTF(); 
			System.out.println(input);
			String source = "/Users/RaghuRRB/Documents/annotator/event/source";
			File[] ff = new File(source).listFiles();
			long n = Files.list(Paths.get("/Users/RaghuRRB/Documents/annotator/event/source")).count();
			//out.writeUTF(String.valueOf(m));
			long m = n;
			for(int k=0; k<n; k++){
				if(ff[k].getName().equals(".DS_Store"))
					m--;
				else System.out.println(ff[k].getName());
			}
			//System.out.println(String.valueOf(Files.list(Paths.get("/Users/RaghuRRB/Documents/annotator/event/source")).count()));
			System.out.println(m);
			out.writeUTF(String.valueOf(m));
			
			while(input != "----end----"){
				input = in.readUTF();
				System.out.println(input+" inside while");
				
				if((input.equals("file1"))||(input.equals("file2"))||(input.equals("file3"))){
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
				
				else if(input.equals("send-Questions")){
					System.out.println("entered el-if");
					String samplePath = "/Users/RaghuRRB/Documents/annotator/event/source/";
					File f = new File(samplePath);
					File[] flist = f.listFiles();
					FileReader fr;
					BufferedReader br;
					String[] ffll = f.list();
					List<Integer> arr = new ArrayList<Integer>();
					List<String> sentences = new ArrayList<String>();
					String parsed = ""; //int iter=0;
					
					BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
					System.out.println("about to add files to sentences");
					System.out.println(flist.length);
					for(int j=0; (j<flist.length); j++){
						System.out.println("j-"+j);//iter =0;
						if((!flist[j].isDirectory())&&(!flist[j].getName().equals(".DS_Store"))) {
							fr = new FileReader(samplePath+ffll[j]);
							br = new BufferedReader(fr);
							
							String line = "";
							parsed = "";
							while ((line = br.readLine()) != null){
								parsed = parsed+line;
							}
							iterator.setText(parsed);
							System.out.println("j-"+j);
							int start = iterator.first();
							for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
							    //System.out.println(content.substring(start,end));
							    sentences.add(parsed.substring(start,end));
							    arr.add(j);
							    //iter++;
							}
							System.out.println("j-"+j);
						}
						
					}
					for(int i=0; i<sentences.size(); i++){
						System.out.println("server-->"+sentences.get(i));
					}
					out.writeUTF("sending");
					input = in.readUTF();
					System.out.println("client-->"+input);
					int i=0;
					while(input.equals("----writestatement----")){
						if(i<sentences.size()){
							
							System.out.println("server-->"+"beforesent-"+i);
							out.writeUTF(sentences.get(i));
							System.out.println("server-->"+"sent-"+sentences.get(i));
							String sent = in.readUTF();
							System.out.println("client-->"+sent);
							String marks = in.readUTF();
							System.out.println(marks);
							db.markSentence(samplePath+i,sent, usr, Integer.parseInt(marks), arr.get(i));
							System.out.println("row created");
							
							i++;
							input = in.readUTF();
							System.out.println("from client --> "+input);
						}
						else {
							System.out.println("ending through break");
							out.writeUTF("--$$-end-$$--");
							break;
						}
						
					}
				}
				
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			
		}
	}
}