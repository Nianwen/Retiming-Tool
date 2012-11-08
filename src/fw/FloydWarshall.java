package fw;
import java.util.LinkedList;


public class FloydWarshall {
	public int[][] W;
	public int [][][] weightpair;
	public int[][] D;
	public LinkedList<Edge> constraintGraph;
	public int numOfBFIteration;
	public LinkedList<int[]> iterationVector;
	
	public FloydWarshall(Node[] nodes,Edge[] edges){
		W=new int[nodes.length][nodes.length];
		D=new int[nodes.length][nodes.length];
		weightpair=new int[nodes.length][nodes.length][2];
		computeWD(nodes,edges);
		this.numOfBFIteration = 0;
	}
	
	
	
	private int[][][] initializeWeight(Node[] nodes, Edge[] edges){
	       int[][][] Weight = new int[nodes.length][nodes.length][2];
	       for(int i=0; i<nodes.length; i++){
	    	   for(int j=0;j<nodes.length;j++){
	    		   Weight[i][j][0]=Integer.MAX_VALUE;
	    		   Weight[i][j][1]=0;
	    	   }	           
	       }
	       for(Edge e : edges){
	           Weight[e.from.index][e.to.index][0] = e.weight[0];
	           Weight[e.from.index][e.to.index][1] = e.weight[1];
	       }
	       return Weight;
	   }
	
	 public  void computeWD(Node[] nodes, Edge[] edges) {
		   weightpair = initializeWeight(nodes, edges);
		   int p = 0;
		   
		   for(int i=0;i<weightpair.length;i++){
			   for(int j=0;j<weightpair[0].length;j++){
				   W[i][j]=weightpair[i][j][0];
				   D[i][j]=nodes[j].delay - weightpair[i][j][1];
			   }
		   }
	       for(int k=0; k<nodes.length; k++){
	           for(int i=0; i<nodes.length; i++){
	               for(int j=0; j<nodes.length; j++){
	            	   
	            	   /*---------init W------------*/
	                   if(W[i][k] != Integer.MAX_VALUE && W[k][j] != Integer.MAX_VALUE && W[i][k]+W[k][j] < W[i][j]){
	                       W[i][j] = W[i][k]+W[k][j];         
	                       if (i==j){
	                    	   W[i][j] = 0;
	                       }
	                   }
	                   
	                   /*---------init D-----------*/
	                   if(W[i][k] != Integer.MAX_VALUE && W[k][j] != Integer.MAX_VALUE && W[i][k]+W[k][j]== W[i][j] && weightpair[i][k][1]+weightpair[k][j][1] < weightpair[i][j][1]){
	                       weightpair[i][j][1] = weightpair[i][k][1]+weightpair[k][j][1];
	                       D[i][j]= nodes[j].delay-weightpair[i][j][1];	                       
	                   } 
	               }
	           }
	       }       
	   }

	 
	 
	 public LinkedList<String> basicConstraint(int[][] W, int[][] D, Node[] nodes, Edge[] edges,int phi_opt){
		 
		 LinkedList<String> wholeConstraints = new LinkedList<String>();
		 wholeConstraints.addLast("The following are constraints 7.1:");
		 for(int k=0;k<nodes.length;k++){
			 for(int p=0;p<nodes.length;p++){
				 for(int i=0;i<edges.length;i++){
					 if(edges[i].from.index==k && edges[i].to.index==p){
					 wholeConstraints.addLast(edges[i].from.name + " - " + edges[i].to.name + " <= " + Integer.toString(-edges[i].weight[0])); 
					 }
				 }
			 }
		 }
         wholeConstraints.addLast("\n");
         wholeConstraints.addLast("The following are constraints 7.2:");
         for(int j = 0; j < nodes.length; j++){
             for(int m = 0; m < nodes.length; m++){
                 if(D[j][m] > phi_opt){
                	 wholeConstraints.addLast(nodes[j].name + " - " + nodes[m].name + " <= " + Integer.toString(-W[j][m]+1));
                 }
             }
         }
         return wholeConstraints;
	 }
	 
	  public LinkedList<String> reduceConstraint(int[][] W, int[][] D, Node[] nodes, Edge[] edges,int phi_opt){
	      
		  this.constraintGraph=new LinkedList<Edge>();
		  LinkedList<String> reducedConstraints = new LinkedList<String>();
		  reducedConstraints.addLast("The following are constraints 9.4:");
		  for(int k=0;k<nodes.length;k++){
			 for(int p=0;p<nodes.length;p++){
				 for(int i = 0; i < edges.length; i ++){
					 if(edges[i].from.index==k && edges[i].to.index==p){
						 Edge newone=new Edge(edges[i].from, edges[i].to, -edges[i].weight[0]);
						 this.constraintGraph.addLast(newone);			  
						 reducedConstraints.addLast(edges[i].from.name + " - " + edges[i].to.name + " <= " + Integer.toString(-edges[i].weight[0]));					 
					 }
				 }
			 }
		  }
		  reducedConstraints.addLast("\n");
		  reducedConstraints.addLast("The following are reduced constraints 9.5:");
	      for(int j = 0; j < nodes.length; j++){
	          for(int m = 0; m < nodes.length; m++){
	              if (D[j][m] > phi_opt && (((D[j][m] - nodes[m].delay) <= phi_opt ) && ((D[j][m] - nodes[j].delay) <= phi_opt))){
	            	  this.constraintGraph.addLast(new Edge(nodes[j], nodes[m] , -W[j][m]+1));
	            	  reducedConstraints.addLast(nodes[j].name + " - " + nodes[m].name + " <= " + Integer.toString(-W[j][m]+1));
	              }
	          }
	      }
	      return reducedConstraints;
	  }

	  
	  public int[][] radix(LinkedList<Edge> graph, Node[] nodes){
	         Edge[] newgraph=new Edge[graph.size()];
	         
	         Edge[] sortgraph=new Edge[graph.size()];
	         
	         int[][] sortresult=new int[graph.size()][3];
	         int[] C=new int[nodes.length];
	         
	             for(int j=0;j<nodes.length;j++){
	                 C[j]=0;
	             }
	             for(int p=0;p<graph.size();p++){
	                 C[graph.get(p).to.index]=C[graph.get(p).to.index]+1;
	             }
	             for(int q=1;q<C.length;q++){
	                 C[q]=C[q]+C[q-1];
	             }
	             for(int n=graph.size()-1;n>=0;n--){
	                
	                 newgraph[C[graph.get(n).to.index]-1]=graph.get(n);
	                 C[graph.get(n).to.index]=C[graph.get(n).to.index]-1;
	                 }
	             
	         for(int j=0;j<nodes.length;j++){
	                 C[j]=0;
	             }
	             for(int p=0;p<graph.size();p++){
	                 C[newgraph[p].from.index]=C[newgraph[p].from.index]+1;
	             }
	             for(int q=1;q<C.length;q++){
	                 C[q]=C[q]+C[q-1];
	             }
	             for(int n=graph.size()-1;n>=0;n--){
	                 sortgraph[C[newgraph[n].from.index]-1]= newgraph[n];
	                 C[newgraph[n].from.index]=C[newgraph[n].from.index]-1;
	                 }
	             
	             for(int k=0;k<sortgraph.length;k++){
	                 sortresult[k][0]=sortgraph[k].from.index;
	                 sortresult[k][1]=sortgraph[k].to.index;
	                 sortresult[k][2]=sortgraph[k].weight[0];
	                 
	             }
	         return sortresult;
	         
	     }
	  
	  
	  public boolean bellmanFord(Node[] nodes, LinkedList<Edge> constraints, int[][] constraintMatrix){
		  
		  Node[] functionNodes = new Node[nodes.length];		  
		  int nodeLength = nodes.length;
		  int constraintLength = constraints.size();
		  int[][] checkMatrix = new int[nodeLength+1][nodeLength];
		  this.iterationVector = new LinkedList<int[]>();
		  this.iterationVector.clear();
		  this.numOfBFIteration = 0;
		  int checker = 0;
		  int constraintSize = constraints.size();
		  LinkedList<Edge> functionConstraints = new LinkedList<Edge>();
		  
		  nodes[0].r = 0;		  
		  for (int s = 0; s < nodeLength; s++){
			  functionNodes[s] = new Node(0,0);
			  functionNodes[s].delay = nodes[s].delay;
			  functionNodes[s].index = nodes[s].index;
			  functionNodes[s].name = nodes[s].name;
			  functionNodes[s].r = nodes[s].r;
		  }		  
		 
		  for (int e = 0; e < constraintSize; e ++){
			  Edge thisEdge = new Edge(functionNodes[constraintMatrix[e][0]], functionNodes[constraintMatrix[e][1]], constraintMatrix[e][2]);
			  functionConstraints.addLast(thisEdge);
		  }
		  
		  
		  /*--------init checkMatrix---------*/
		  for (int j = 0; j < nodeLength+1; j++){
			  for (int k = 0; k < nodeLength; k ++){
				  if (k == 0){
					  checkMatrix[j][k] = 0;
				  }else {
					  checkMatrix[j][k] = Integer.MIN_VALUE;
				  }				  
			  }
		  }
		  
		  /*-----init Si------*/
		  for (int i = 1; i < nodeLength; i ++){
			  for (int j = 0; j < constraintLength; j ++ ){
				  if (functionConstraints.get(j).from.name == functionNodes[0].name && functionConstraints.get(j).to.name == functionNodes[i].name && functionConstraints.get(j).weight[0] > functionNodes[i].r){
					  functionNodes[i].r = functionConstraints.get(j).weight[0];
				  }
			  }
			  checkMatrix[0][i] = functionNodes[i].r;
		  }
		  this.iterationVector.addLast(checkMatrix[0]);
		  
		  for (int p = 1; p <= nodeLength; p ++ ){
			  for (int q = 1; q < nodeLength; q ++ ){
				  for (int r = 0; r < constraintLength; r ++ ){
					  if ((functionConstraints.get(r).to.name == functionNodes[q].name) && (functionConstraints.get(r).weight[0] + functionConstraints.get(r).from.r > functionNodes[q].r) && functionConstraints.get(r).from.r != Integer.MIN_VALUE){
						  
						  functionNodes[q].r = functionConstraints.get(r).weight[0] + functionConstraints.get(r).from.r;
						  checkMatrix[p][q] = functionNodes[q].r;
					  }
				  }
				  if (checkMatrix[p][q] < checkMatrix[p-1][q] && checkMatrix[p-1][q] != Integer.MIN_VALUE){
					  checkMatrix[p][q] = checkMatrix[p-1][q];
				  }
			  }
			  this.iterationVector.addLast(checkMatrix[p]);
			  for (int u = 0; u < nodeLength; u++){
				  if (checkMatrix[p-1][u] == checkMatrix[p][u]){
					  checker ++;
				  }
			  }
			  if (checker == nodeLength){
				  this.numOfBFIteration = p;
				  return true;
			  }
			  checker = 0;
		  }
		  return false;	
	  }
}
		 
			 
	 