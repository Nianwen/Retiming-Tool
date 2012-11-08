package fw;

public class Node {	   
	   public int delay = Integer.MAX_VALUE;         
	   public int index = -1;
	   public String name = "";
	   public int r;
	   
	   public Node(){
		   delay = Integer.MAX_VALUE;
		   index = -1;
		   name = "";
		   r = Integer.MIN_VALUE;
	   }
	      
	   public Node(int index, int delay){
		   this.index = index;
		   this.delay = delay;
		   this.name = "r_" + Integer.toString(index);
		   this.r = Integer.MIN_VALUE;;	   
	   }
	   
	  // public int getde
	}
