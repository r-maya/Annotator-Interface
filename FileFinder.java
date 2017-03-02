import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class FileFinder{
	String input;
	DataInputStream in;
	DataOutputStream out;
	JButton[] src;
	JButton ok;
	JFrame frame,f2 ;
	JLabel l; JTextField uf;
	Handler handler;
	int n,m;
	Socket s;
	static int flag,flag2;
	boolean correct;
	
	FileFinder(Socket ss) throws Exception{
		flag = 0;flag2=0;
		this.s = ss;
		in=new DataInputStream(s.getInputStream());
		out=new DataOutputStream(s.getOutputStream());
		handler = new Handler();

		//System.out.println("read-input-1");
		f2 = new JFrame("Select source in an event");
		f2.setSize(700,180);
		f2.setLocation(250, 230);
		f2.setLayout(null);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		

		frame = new JFrame("Select source in an event");
		frame.setSize(400,180);
		frame.setLocation(300, 200);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		out.writeUTF("number of source files");
		n=Integer.parseInt(in.readUTF());
		

		src = new JButton[n];
		for(int i=0; i<n; i++){
			src[i] = new JButton("sourcfile-"+(i+1));
			//System.out.println("read-input-2");
			src[i].addActionListener(handler);
			src[i].setSize(src[i].getPreferredSize());
			src[i].setLocation(100, 40+(30*i));
			
			frame.add(src[i]);
			
		}
		ok = new JButton("Go");
		ok.setSize(ok.getPreferredSize());
		ok.setLocation(300, 120);
		ok.addActionListener(handler);
		frame.add(ok);

		uf = new JTextField();
		uf.setColumns(3);
		uf.setSize(uf.getPreferredSize());
		uf.setLocation(110, 90) ; //
		uf.setToolTipText("enter marks on scale (NNV)0-(NV)4");
		f2.add(uf);

	
		frame.setVisible(true);
	}

	public class Marks{

		public boolean getMarks(String s){
			System.out.println("entered marks");
			int m;
			String marks = s;
			try{
				m = Integer.parseInt(marks);
				if((m>=0 && m<5)) return true;
				else{
					JOptionPane.showMessageDialog(null, "between 0 & 5","Failed!!",JOptionPane.ERROR_MESSAGE);
					return false;//return getMarks();
				}
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(null, e,"Failed!!",JOptionPane.ERROR_MESSAGE);
				return false;//return getMarks();
			}
		}

	}
	

	class Handler implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){

			System.out.println("before");
			
			try{

				//if ((ae.getSource() == src[0])||(ae.getSource() == src[1])||(ae.getSource() == src[2])){
					String sel="";
					for(int l=0; l<src.length; l++){
						if(ae.getSource() == src[l])	sel = String.valueOf(l+1);
						//System.out.println(l);
					}
					if(ae.getSource() == ok){
						correct = true;
						System.out.println("ok-pressed");
						if(flag ==0){
							flag = 1;
							out.writeUTF("send-Questions");
							input = in.readUTF();
							System.out.println("flag0--"+input);
						}
						else{
							correct = false;
							Marks m1 = new Marks();
							correct = m1.getMarks(uf.getText());
							System.out.println("this is the uf.text --> "+uf.getText());
							if(correct) {
								//out.writeUTF("----writestatement----");
								//out.writeUTF(input);
								
								System.out.println("entered correct");
								// m = Integer.parseInt(uf.getText());
								out.writeUTF(input);
								String mm = uf.getText();
								System.out.println("-"+mm+"-");
								out.writeUTF(mm);
								//System.out.println(this.getMarks());
								f2.setVisible(false);
								f2.remove(l);
								f2.remove(ok);
							}
						}
						
						if(correct){
							//System.out.println("entered 2nd correct");
								//System.out.println(input);
							//input = "";
							out.writeUTF("----writestatement----");
							input = in.readUTF();
							//System.out.println("client-->"+input);
							
							//input = in.readUTF();
							//System.out.println(input+"------after send");
							//while(input != "--$$-end-$$--"){
							if(!input.equals("--$$-end-$$--")){
								l = new JLabel("<html><body style='width: "+400+"px'>"+input);
								l.setLocation(30, 35);
								l.setSize(l.getPreferredSize());
								f2.add(l);
								f2.add(ok);

								f2.setVisible(true);
							}
							//else out.writeUTF("----end----");
						}


							//input = in.readUTF();
							
						
					}
					else{
						//System.out.println("file"+sel);
						out.writeUTF("file"+sel);
						String input = in.readUTF();
						//System.out.println(input);
						//System.out.println("calling fetchfile");
						new Thread(new interfetcher(input)).start();
					}
					//f1.main(new String[1]);
				//}
				
			}
			catch(Exception e){

			}
		}
	}


}