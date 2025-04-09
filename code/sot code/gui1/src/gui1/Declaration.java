package gui1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Declaration extends JPanel{
	
	private int[] varinframe = new int[5];
	private int varcount = 0;
	
	private Output outpanel;

	
	Declaration(Output outpanel)
	{
		
		this.setBackground(Color.green);
		this.setPreferredSize(new Dimension(150,150));
		Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
	    this.setBorder(border);
	    this.setLayout(null);
		
	    this.outpanel = outpanel;
	    
		Declbutton declbutton1 = new Declbutton(10,10);
		
		
	    declbutton1.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	addvar();
	            }
	        });
		
		
		this.add(declbutton1);
		
		JButton checkbutton = new JButton();
		checkbutton.setBounds(140, 10, 90, 30);
		checkbutton.setText("check");
	    checkbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	checkAllPanels();
	            	VarList.printAll();
	                outpanel.create_out_elements();
	            }
	        });
		
		
		this.add(checkbutton);
		
		
		
		
		JButton runbutton = new JButton();
		runbutton.setBounds( 1000, 20, 100, 100);
		runbutton.setText("run");
	    runbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	CodeList.ProgCounter=0;
	            	while(CodeList.ProgCounter<=CodeList.ProgMax) {
	            	//System.out.println("Prog Max = "+CodeList.ProgMax);	
	            	CodeList.run(CodeList.ProgCounter);
	            	VarList.printAll();
	            	outpanel.create_out_elements();
	            	//System.out.println("Prog Counter = "+CodeList.ProgCounter);
	            	CodeList.ProgCounter++;
	            	}
	            }
	        });
		
		runbutton.setVisible(true);
		this.add(runbutton);
		
		JButton compbutton = new JButton();
		compbutton.setBounds( 900, 20, 100, 100);
		compbutton.setText("Comp");
		compbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	CodeList.compile();
	            }
	        });
		
		compbutton.setVisible(true);
		this.add(compbutton);
			
		
	}

	
	public void addvar() {
	    if (varcount < varinframe.length) {
	        int firstZeroIndex = -1;
	        for (int i = 0; i < varinframe.length; i++) {
	            if (varinframe[i] == 0) {
	                firstZeroIndex = i;
	                break;
	            }
	        }

	        if (firstZeroIndex != -1) {
	            Varpanel var1 = new Varpanel(firstZeroIndex, this, outpanel);
	            this.add(var1);
	            this.revalidate();
	            this.repaint();
	            varinframe[firstZeroIndex] = 1;
	            varcount++;
	        }
	    }
	}

	public void remvar(Varpanel rempan, int index) {
	    this.remove(rempan);
	    this.revalidate();
	    this.repaint();
	    varinframe[index] = 0;
	    varcount--;
	}

	public void checkAllPanels() {
	    for (java.awt.Component comp : this.getComponents()) {
	        if (comp instanceof Varpanel) {
	            ((Varpanel) comp).getEntry();
	        }
	    }
	}

}
