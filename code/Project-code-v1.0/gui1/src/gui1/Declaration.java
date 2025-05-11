package gui1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class Declaration extends JPanel{
	
	private int[] varinframe = new int[5];
	private int varcount = 0;
	
	private Output outpanel;

	private volatile int stopflag = 0; 
	
	private JButton runbutton;
	
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
		
		
		// Progress bar under run button
		JProgressBar progressBar = new JProgressBar(0, 100);
		progressBar.setBounds(1000, 120, 100, 20); // under the run button
		progressBar.setStringPainted(false);
		progressBar.setVisible(false);
		this.add(progressBar);

		
		JButton stopbutton = new JButton();
		stopbutton.setBounds( 1100, 40, 60, 60);
		stopbutton.setText("stop");
		stopbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            stopflag=1;
            }
        });
		this.add(stopbutton);
		
		
		runbutton = new JButton();
		runbutton.setBounds( 1000, 20, 100, 100);
		runbutton.setText("run");
		runbutton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	
		    	 if (CodeList.IsCompiled == 0) {
		             javax.swing.JOptionPane.showMessageDialog(
		                 null,
		                 "Your program is not compiled!",
		                 "Compilation Required",
		                 javax.swing.JOptionPane.WARNING_MESSAGE
		             );
		             return;
		         }
		    	
		    	
		        CodeList.ProgCounter = 0;
		        int total = CodeList.ProgMax;
		  
		        stopflag=0;
		        runbutton.setEnabled(false);
		        new Thread(() -> {
		        	
		        	progressBar.setVisible(true);
		            while (CodeList.ProgCounter < total && stopflag==0) {
		            	
		            	int prcount = CodeList.ProgCounter;
		            	
		            	CodeList.run(CodeList.ProgCounter);
		                // Simulate delay using progress bar
		                for (int i = 0; i <= 100; i=i+2) {
		                    final int val = i;
		                    SwingUtilities.invokeLater(() -> progressBar.setValue(val));
		                    try {
		                        Thread.sleep(10); // controls how fast the bar fills
		                    } catch (InterruptedException ex) {
		                        ex.printStackTrace();
		                    }
		                }

		                // After bar is full, reset UI elements
		                SwingUtilities.invokeLater(() -> {
		                    
		                    CodeList.resetborder(prcount);
		                    VarList.printAll();
		                    outpanel.create_out_elements();
		                    progressBar.setValue(0); // reset bar
		                });

		                
		                try {
		                    Thread.sleep(10);
		                } catch (InterruptedException ex) {
		                    ex.printStackTrace();
		                }
		            }
		            runbutton.setEnabled(true);
		            progressBar.setVisible(false);
		        }).start();
		        
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
	            	checkAllPanels();
	            	VarList.printAll();
	                outpanel.create_out_elements();
	            	CodeList.compile();
	            	if (CodeList.CompError==1) {runbutton.setEnabled(false);}
	            	else {runbutton.setEnabled(true);}
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
