import java.awt.Container;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginNew{
	
	JFrame frame ;
	JTextField uf ;
	JPasswordField pf;
	JLabel l,m; JButton ok,logout;
	//Database db;
	Container c;
	Handler handler;
	Socket s;
	DataInputStream in; DataOutputStream out;
	
	LoginNew() throws Exception{
		
	//setting frame
		frame = new JFrame("Anotator-Login");
		frame.setSize(400,180);
		frame.setLocation(300, 200);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	//handler and Database
		handler = new Handler();
		//db = new Database();
		s = new Socket("localhost",9999);
		in=new DataInputStream(s.getInputStream());
		out=new DataOutputStream(s.getOutputStream());
		
	//setting & adding label for user-name and password.
		l = new JLabel("Username : ");
		l.setLocation(30, 35);
		l.setSize(l.getPreferredSize());
		frame.add(l);
		
		m = new JLabel("Password : ");
		m.setLocation(30, 70);
		m.setSize(l.getPreferredSize());
		frame.add(m);
		
	//setting and adding fields for user-name and password.
		uf = new JTextField();
		uf.setColumns(15);
		uf.setSize(uf.getPreferredSize());
		uf.setLocation(110, 30) ; //
		uf.setToolTipText("enter username");
		frame.add(uf);
		
		pf = new JPasswordField();
		pf.setColumns(15);
		pf.setSize(pf.getPreferredSize());
		pf.setLocation(110, 65) ; //
		pf.setToolTipText("enter password");
		frame.add(pf);
		
	//OK button
		ok = new JButton("OK");
		ok.setSize(ok.getPreferredSize());
		ok.setLocation(300, 120);
		ok.addActionListener(handler);
		frame.add(ok);
	//logout button
		logout = new JButton("logout");
		logout.setSize(ok.getPreferredSize());
		logout.setLocation(300, 120);
		logout.addActionListener(handler);
		frame.add(logout);
	//all set to go.
		frame.setVisible(true);
		
	}
	Socket getSocket(){
		return s;
	};
	
	class Handler implements ActionListener{
		
		public void actionPerformed(ActionEvent ae){
			String input;
			if(ae.getSource() == ok){
				
				String usr = uf.getText();
				char[] char_pwd = pf.getPassword();
				String pwd = String.copyValueOf(char_pwd);
				System.out.println(usr+"----"+ pwd);
				//out.writeUTF(usr+"$$$$--$$$$"+pwd);
				try{
					out.writeUTF(usr);
					input = in.readUTF();
					if(input.equals(usr+"-okay")) System.out.println("usr delivered");
					out.writeUTF(pwd);
					input = in.readUTF();
					if(input.equals(pwd+"-okay")) System.out.println("pwd delivered");
					
					input = in.readUTF();
					
					if(input.equals("login-okay")){
	                    //a pop-up box
						JOptionPane.showMessageDialog(null, "You have logged in successfully","Success",
	                            JOptionPane.INFORMATION_MESSAGE);
						
						frame.dispose();
						System.out.println("read-input");
						FileFinder ff = new FileFinder(s);
					}
                
	                else if(input.equals("not-okay")) {
	                	JOptionPane.showMessageDialog(null, "Wrong username or password!","Failed!!",
	                            JOptionPane.ERROR_MESSAGE);
	                }
				}
				catch(Exception e){
					System.out.println(e);
				}
				finally{
					
				}
			}
		}
		
	}
	
	
	
	public static void main(String args[]) throws Exception{
		
		LoginNew ln = new LoginNew();
		
	}
}