import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;

public class FileFinder{
	
	DataInputStream in;
	DataOutputStream out;
	JButton[] src;
	JFrame frame ;
	Handler handler;
	int n;
	Socket s;
	
	FileFinder(Socket ss) throws Exception{
		
		this.s = ss;
		in=new DataInputStream(s.getInputStream());
		out=new DataOutputStream(s.getOutputStream());
		handler = new Handler();

		//System.out.println("read-input-1");
		frame = new JFrame("Select source in an event");
		frame.setSize(400,180);
		frame.setLocation(300, 200);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		out.writeUTF("number of source files");
		n=Integer.parseInt(in.readUTF());
		

		// JButton src1 = new JButton("src1");
		// src1.setSize(src1.getPreferredSize());
		// src1.addActionListener(handler);
		// JButton src2 = new JButton("src2");	src2.addActionListener(handler);
		// JButton src3 = new JButton("src3");	src3.addActionListener(handler);
		
		src = new JButton[n];
		for(int i=0; i<n; i++){
			src[i] = new JButton("sourcfile-"+(i+1));
			System.out.println("read-input-2");
			src[i].addActionListener(handler);
			src[i].setSize(src[i].getPreferredSize());
			src[i].setLocation(100, 40+(30*i));
			
			frame.add(src[i]);
			
		}


		// frame.add(src1);
		// frame.add(src2);
		// frame.add(src3);
		frame.setVisible(true);
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
					System.out.println("file"+sel);
					out.writeUTF("file"+sel);
					String input = in.readUTF();
					System.out.println(input);
					System.out.println("calling fetchfile");
					new Thread(new interfetcher(input)).start();
					//f1.main(new String[1]);
				//}
				
			}
			catch(Exception e){

			}
		}
	}


}