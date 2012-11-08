package fw;

public class Edge{
		
	public Node from, to; 
	public int[] weight;

   
	public Edge(Node from, Node to, int weight){
	    this.weight = new int[2];
		this.from = from;
	    this.to = to;
	    this.weight[0] = weight;
	    this.weight[1] = 0 - from.delay;
	   
	}   
}
