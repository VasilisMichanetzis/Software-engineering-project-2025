package gui1;

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
