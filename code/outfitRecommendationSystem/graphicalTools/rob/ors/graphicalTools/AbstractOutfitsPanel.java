package rob.ors.graphicalTools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import rob.ors.core.model.api.AbstractOutfit;
import rob.ors.core.model.api.ConcreteOutfit;

public class AbstractOutfitsPanel extends JPanel implements ComponentListener{
	
	private AbstractOutfit abstractOutfit;
	private JPanel concreteOutfitsPanel ;
	private JScrollPane scroller;
	private int page = 0;
	private int numPages;
	private int elementsPerPage = 3;
	private JButton next;
	private JButton prev;
	private JLabel paginationLabel;
	public AbstractOutfitsPanel(AbstractOutfit abstractOutfit)
	{	
		this.abstractOutfit = abstractOutfit;
		concreteOutfitsPanel = new JPanel();
		concreteOutfitsPanel.setLayout(new BoxLayout(concreteOutfitsPanel, BoxLayout.Y_AXIS));
		scroller = new JScrollPane(concreteOutfitsPanel);  
		numPages = (int) Math.ceil( ((float)abstractOutfit.getOutfits().size()/(float)elementsPerPage))-1;
		
		prev = new JButton("<");
		prev.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(page>0)
				{
					page-=1;
					updateContents();
				}
				
			}
		});
		
		next = new JButton(">");
		next.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(page<numPages)
				{
					page+=1;
					updateContents();
				}
				
			}
		});
		paginationLabel = new JLabel("0/"+numPages);
		
		add(prev);
		add(paginationLabel);
		add(next);
		
		
		
		add(scroller);
		updateContents();
		
	}


	private void updateContents()
	{
		concreteOutfitsPanel.removeAll();
		paginationLabel.setText((page+1)+"/"+(numPages+1));
		
		int start = page*elementsPerPage;
		int end = Math.min((page+1)*elementsPerPage, abstractOutfit.getOutfits().size());

		
		for(ConcreteOutfit concreteOutfit : new LinkedList<ConcreteOutfit>(abstractOutfit.getOutfits()).subList(start,end))
		{			

			ConcreteOutfitsPanel comp = new ConcreteOutfitsPanel(concreteOutfit);
			
			concreteOutfitsPanel.add(comp);
			//addComponentListener(comp);
		}
	    revalidate();
	    repaint();
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		scroller.setPreferredSize(getSize());
	    revalidate();
	    repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		scroller.setPreferredSize(getSize());
	    revalidate();
	    repaint();
		
	}
	
	
	

}
