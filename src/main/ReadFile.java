package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.LinkedList;
import fw.*;

public class ReadFile {
	
	public static int string2int (String str){
		int thisValue = 0; 
		int length = str.length();
		int times = 1;
		char[] charArray = str.toCharArray();
		for (int i = length - 1; i >= 0; i --){
			thisValue = thisValue + times * (charArray[i] - 48);
			times = times*10;
		}
		return thisValue;
	}

	public static void main(String args[])
	 { 
		  try
		  {			 
			  
			  /*Please copy the test content into the file and unmark the line below to test the corresponding algorithm*/			  
			  
			  FileInputStream fstream = new FileInputStream("src/input_file/correlator.txt");
			  
			  /*----------------------------------------------------------------------------------*/
			
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  int vertexNum=0;
			  int i=0;
			  int numOfRows=0;
			  String fileName = "";
			  String[] anArray=new String[100];
			  String[] originalFile = new String[100];
			  String[] arrayString;
			  int[] vertexWeight = new int[500];
			  String vertexWeightString;
			  int lineOfG = 0;
			  int lineOfE = 0;
			  int flag = 1;
			  		  
			  while ((strLine = br.readLine()) != null)   
			  {
				  // Print the content on the console
				  String original = strLine;
				  String noSpaceString = strLine.replaceAll(" ", "");
				  noSpaceString = noSpaceString.replaceAll("\t", "");
				  
				  if (noSpaceString.indexOf("#")!=-1)
				  {	
					  int indexOfSharp = noSpaceString.indexOf("#");
					  noSpaceString = noSpaceString.substring(0, indexOfSharp);
				  }
				  
				  if (noSpaceString.indexOf(".name")!=-1)
				  {	
					  int fileNameLength = noSpaceString.length();
					  fileName = noSpaceString.substring(5, fileNameLength);
				  }
				  
				  else if (noSpaceString.indexOf(".n")!=-1)
				  {
					  String vertexNumString= noSpaceString.substring(2);
					  vertexNum = string2int(vertexNumString);					  					  					 					  
				  }
				  
				  else if (noSpaceString.indexOf(".d")!=-1)
				  {	
						arrayString = new String[vertexNum];
						String tryString = original.substring(2);
						char[] tryCharArray = tryString.toCharArray();
						int indexLast;
						int t = 0;
						for (int c = 0; c < vertexNum; c ++){
							while (tryCharArray[1] == 32){
								tryString = tryString.substring(1);
								tryCharArray = tryString.toCharArray();
							}
							if (c != vertexNum -1){
								indexLast = tryString.indexOf(32, 1);
								arrayString[t] = tryString.substring(1, indexLast);
								t ++;
								tryString = tryString.substring(indexLast);
								tryCharArray = tryString.toCharArray();
								indexLast = 0;	
							}else {
								if ( tryString.indexOf(32, 1) == -1){
									indexLast = tryString.length();
									arrayString[t] = tryString.substring(1, indexLast);	
								}else{
									indexLast = tryString.indexOf(32, 1);
									arrayString[t] = tryString.substring(1, indexLast);	
								}						
							}				 
						}
						
						for (int j= 0;j < vertexNum; j++){
							vertexWeight[j] = string2int(arrayString[j]);
						}
				  }
				  
				 
				  if (noSpaceString.indexOf(".g")!=-1){
					  lineOfG = numOfRows;
				  }
				  
				  if (noSpaceString.indexOf(".g")!=-1 || flag == 0){
					  
					  anArray[i] = original;
					  flag = 0;
				  }else{
					  anArray[i] = noSpaceString;
				  }
				  
				  
				  if (noSpaceString.indexOf(".e")!=-1)
				  {
					  lineOfE = numOfRows;
				  }
				  originalFile[i] = original;
				  i++;
				  numOfRows++;
			  }

			  
			  int numOfEdges = lineOfE - lineOfG - 1;
			  
			  if (numOfEdges == 0){
				     /*----------------------output files with NO edges-----------------------------------*/
				     
				     /*--------file #1: W and D matrices------------*/
					  Writer output_1 = null;		
					  String fileNameWD = fileName + "-WD.txt";
					  File file_1 = new File( fileNameWD);
					  output_1 = new BufferedWriter(new FileWriter(file_1));
					  String nextLine = "\r\n";	
					  
					  output_1.write(" W matrix: ");
					  output_1.write(nextLine);
					  output_1.write("W ");
					  for (int z = 0; z < vertexNum+1; z++){
						  output_1.write("v" + z + " ");
					  }
					  output_1.write(nextLine);
					  for (int w = 0; w <= vertexNum; w++)
					  {					  
						  String oneLine = " ";
						  for(int n = 0; n <= vertexNum; n++){
							  if (n == 0){
								  oneLine = oneLine + "0" + "  ";
							  }else{
								  oneLine = oneLine + "x" + "  ";
							  }
						  } 
						  output_1.write("v" + w + oneLine);
						  output_1.write(nextLine);					  				  					  
					  }
					  
					  output_1.write(" D matrix: ");
					  output_1.write(nextLine);
					  output_1.write("D ");
					  for (int z = 0; z < vertexNum+1; z++){
						  output_1.write("v" + z + " ");
					  }
					  output_1.write(nextLine);
					  for (int w = 0; w <= vertexNum; w++)
					  {					  
						  String oneLine = " ";
						  for(int n = 0; n <= vertexNum; n++){
							  if(w == n && w == 0){
								  oneLine = " 0" + " ";
							  }else if (w == n && w != 0){
								  oneLine = oneLine + vertexWeight[n-1] + "  ";
							  }else{
								  oneLine = oneLine + "x" + "  ";
							  }
						  } 
						  output_1.write("v" + w + oneLine);
						  output_1.write(nextLine);					  				  					  
					  }
					  
					  output_1.close();
					  System.out.println("\n" + "Your output file #1 - WD matrices has been written");		
					  
					  /*--------------------file #2: opt_phi constraints-------------------*/
					  Writer output_2 = null;		
					  String fileNameConstraints = fileName + "-constraints.txt";
					  File file_2 = new File(fileNameConstraints);
					  output_2 = new BufferedWriter(new FileWriter(file_2));
					  
					  output_2.write("The following are constraints 9.4: ");
					  output_2.write(nextLine);
					  output_2.write("r_i - r_j <=  " + Integer.toString(Integer.MIN_VALUE) + "    i, j <- [0 " + Integer.toString(vertexNum+1) + "]");
					  output_2.write(nextLine);
					  output_2.write("The following are constraints 9.5: ");
					  output_2.write(nextLine);
					  output_2.write("r_i - r_j <=  " + Integer.toString(Integer.MIN_VALUE) + "    i, j <- [0 " + Integer.toString(vertexNum+1) + "]");
					  					  					  
					  output_2.close();
					  System.out.println("\n" + "Your output file #2 - constraints has been written");	
					  
					  /*--------------------file #3: constraint graph-------------------*/
					  Writer output_3 = null;		
					  String fileNameCG = fileName + "-CG.txt";
					  File file_3 = new File(fileNameCG);
					  output_3 = new BufferedWriter(new FileWriter(file_3));
					  int file_3NumOfLines = 12;					  
					  
					  for (int w = 0; w <= lineOfG; w++)
					  {						  
						  output_3.write(originalFile[w]);
						  output_3.write(nextLine);						  					  				  			  
					  }
					  output_3.write("r_i - r_j <=  " + Integer.toString(Integer.MIN_VALUE) + "    i, j <- [0 " + Integer.toString(vertexNum+1) + "]");
					  output_3.write(nextLine);
					  output_3.write("r_i - r_j <=  " + Integer.toString(Integer.MIN_VALUE) + "    i, j <- [0 " + Integer.toString(vertexNum+1) + "]");
					  output_3.write(nextLine);
					  output_3.write(".e");					  					 					  
					  output_3.close();
					  System.out.println("\n" + "Your output file #3 - constraint graph has been written");	
					  
					  /*--------------------file #4: intermidiate BF-------------------*/
					  Writer output_4 = null;		
					  String fileNameBF = fileName + "-BF.txt";
					  File file_4 = new File(fileNameBF);
					  output_4 = new BufferedWriter(new FileWriter(file_4));
					  output_4.write("Following shows the path weight vectors: ");
					  output_4.write(nextLine);
					  for (int t = 0; t < 2; t++){
						 output_4.write("s^" + t + ": ");
			    		 for (int r = 0; r < vertexNum + 1 ; r++){
			    			 if (r == 0){
			    				 output_4.write("0" + " ");
			    			 }else{
			    				 output_4.write(Integer.toString(Integer.MIN_VALUE) + " ");
			    			 }			    			 
			    		 }    		 
			    		 output_4.write(nextLine);
			    	  }
					  output_4.write(nextLine);
					  output_4.write("In the 2th iteration, the algorithm reaches the stable value." );
					  
					  output_4.close();
					  System.out.println("\n" + "Your output file #4 - BF has been written");	
					  
					  
					  /*--------------------file #5: correlator-retiming-summary.txt-------------------*/
                   Writer output_5 = null;        
                   String fileNameSM = "correlator-retiming-summary.txt";
                   File file_5 = new File(fileNameSM);
                   output_5 = new BufferedWriter(new FileWriter(file_5));
                   output_5.write("The optimal retiming vector is: ");                      
                   for (int r = 0; r < vertexNum + 1 ; r++){
		    			 if (r == 0){
		    				 output_5.write("0" + " ");
		    			 }else{
		    				 output_5.write(Integer.toString(Integer.MIN_VALUE) + " ");
		    			 }			    			 
		    	   } 
                   output_5.write(nextLine);
                   output_5.write("The optimal clock cycle time is: N/A");
                   output_5.write(nextLine);
                   output_5.write("The initial(legal) clock cycle time is: N/A" );                       
                   output_5.close();
                   System.out.println("\n" + "Your output file #5 - SM has been written");
                   
                   /*--------------------file #6: correlator-out.txt-------------------*/
                   Writer output_6 = null;        
                   String fileNameOUT = "correlator-out.txt";
                   File file_6 = new File(fileNameOUT);
                   output_6 = new BufferedWriter(new FileWriter(file_6));
					  
				  for (int w = 0; w <= lineOfG; w++ ){
					  output_6.write(originalFile[w]);
					  output_6.write(nextLine);
				  }
                   output_6.write(".e");
                                        
                   output_6.close();
                   System.out.println("\n" + "Your output file #6 - OUT has been written");
                   

			  }else{
				  /*-------initial nodes-----------*/
				  Node node = new Node();
				  node.delay = 1;
				  
				  Node nodes[] = new Node[vertexNum+1];
				  nodes[0] = new Node(0, 0);
				  for (int n = 1; n < vertexNum+1; n++){
					  nodes[n] = new Node(n, vertexWeight[n-1]);			  
				  }
				  
				  /*----------initial edges-----------*/
				  
				  System.out.println("Number of vertices:" + vertexNum);
				  System.out.println("Number of edges: " + numOfEdges);
				  
				  Edge[] edges = new Edge[numOfEdges];
				  for (int e = 0; e < numOfEdges; e++){
					  char[] edgeLine = anArray[lineOfG+1+e].toCharArray();		
					  int firstSpace = anArray[lineOfG+1+e].indexOf(' ');
					  int secondSpace = anArray[lineOfG+1+e].indexOf(' ', firstSpace+1);
					  int mult = 1;
					  int indexFrom = 0;
					  int indexTo = 0;
					  for (int n = firstSpace-1; n >= 0; n--)
					  {
						  indexFrom = (edgeLine[n]-48)*mult + indexFrom;
						  mult = mult*10;					  
					  }
					  mult = 1;
					  for (int n = secondSpace-1; n > firstSpace; n--)
					  {
						  indexTo = (edgeLine[n]-48)*mult + indexTo;
						  mult = mult*10;					  
					  }
					  int edgeWeight = edgeLine[secondSpace+1]-48;
					  edges[e] = new Edge(nodes[indexFrom], nodes[indexTo], edgeWeight);
				  }
				  
				  
				  /*-----------test read file-----------------
				  for (int p = 0; p < numOfEdges; p++){
					  System.out.println("Edge Weight Attr 1" + p + ": " + edges[p].weight[0]);	
					  System.out.println("Edge Weight Attr 2" + p + ": " + edges[p].weight[1]);
				  }
				  */
				  
				  
					 int[][] p;
					 int[][] q;
					 LinkedList<String> wholeConstraints = new LinkedList<String>();
					 LinkedList<String> reducedConstraints = new LinkedList<String>();
					 int possiblePhi;
					 int phi_opt;
				     
				     FloydWarshall FW =new FloydWarshall(nodes,edges);
				     p = FW.W;
				     q = FW.D;
				     
				     int[] unsortedDEle = new int[q.length*q.length];
				     
				     System.out.println("\n W matrix: ");
				     for(int m=0;m<nodes.length;m++){
				    	 for(int n=0;n<nodes.length;n++){
				    		 System.out.print(p[m][n] + " ");
				    	 }
				    	 System.out.print('\n');
				     }
				     
				     System.out.println("\n D matrix: ");
				     
				     for(int m=0;m<nodes.length;m++){
				    	 for(int n=0;n<nodes.length;n++){
				    		 System.out.print(q[m][n] + " ");
				    	 }
				    	 System.out.print('\n');
				     }			
   
                     /*----------insertion sort-----------*/
				     int pointer = 0;
				     for (int n = 0; n < nodes.length; n++){
				    	 for (int m = 0; m < nodes.length; m++){		    			 
				    			 unsortedDEle[pointer] = q[n][m];
				    			 pointer ++;			              			   	                    		   
				       	 } 
				     }
				     
					for (int t=1; t<(vertexNum+1)*(1+vertexNum);t++)
					{
						int key = unsortedDEle[t];
						int s = t-1;
						while (s>=0 && unsortedDEle[s]>key)
						{
							unsortedDEle[s+1]=unsortedDEle[s];
							s = s-1;
						}
						unsortedDEle[s+1] = key;
					}
					
					System.out.println(" \n" + "unsorted D element:  ");				     
				     for(int m = 0;m < (vertexNum+1)*(1+vertexNum); m++){			    	 
				    	 System.out.print(unsortedDEle[m] + " ");				   		
				     }
					
					int adder = 1;
					int numOfTry = 1;
					for (int h = 1; h < (vertexNum+1)*(1+vertexNum); h ++){
						if (unsortedDEle[h] != unsortedDEle[adder-1]){
							unsortedDEle[adder] = unsortedDEle[h];
							adder ++;
							numOfTry ++;
						}
					}
					int[] testDEle = new int[numOfTry];
					for (int x = 0; x < numOfTry; x++){
						testDEle[x] = unsortedDEle[x];
					}	
					
			         System.out.println(" \n" + "sorted D element:  ");				     
				     for(int m = 0;m < numOfTry; m++){			    	 
				    	 System.out.print(testDEle[m] + " ");				   		
				     }	
				     
				     int initialPhi = 0;
				     for (int ii = 0; ii < (vertexNum+1); ii++){
				    	 for (int jj = 0; jj < (vertexNum+1); jj++){
				    		 if (p[ii][jj] == 0 && q[ii][jj] > initialPhi){
				    			 initialPhi = q[ii][jj];
				    		 }
				    	 }
				     }
				     System.out.println(" \n" + "initial Phi:  " + initialPhi + "\n");	
				     
				     
				     int highPointer = testDEle.length-1;
				     int lowPointer = 0;
				     int currentPhi = testDEle[0];
				     int firstLegalPhiFlag = 0;
				     int firstLegalPhi = Integer.MAX_VALUE;
				     int lastLegalPhi = Integer.MAX_VALUE;
				     
				     while(highPointer >= lowPointer){
				    	 
				    	 int currentPointer = (int) (highPointer - lowPointer)/2 + lowPointer;
				    	 currentPhi = testDEle[currentPointer];
				    	 reducedConstraints = FW.reduceConstraint(p, q, nodes, edges, currentPhi);
				    	 wholeConstraints = FW.basicConstraint(p, q, nodes, edges, currentPhi);
				    	 int[][] sortedConstraints = FW.radix(FW.constraintGraph, nodes);
				    	 if (FW.bellmanFord(nodes, FW.constraintGraph, sortedConstraints)){
				    		 highPointer = currentPointer - 1;
				    		 if (firstLegalPhiFlag == 0){
				    			 firstLegalPhiFlag = 1;
				    			 firstLegalPhi = currentPhi;
				    		 }
				    		 if (firstLegalPhiFlag == 1){
				    			 lastLegalPhi = currentPhi;
				    		 }
				    	 }else{
				    		 lowPointer = currentPointer + 1;
				    	 }
				     }
				     reducedConstraints = FW.reduceConstraint(p, q, nodes, edges, currentPhi);
			    	 wholeConstraints = FW.basicConstraint(p, q, nodes, edges, currentPhi);			
				     //FW.bellmanFord(nodes, FW.constraintGraph,sortedConstraints);
			    	 int[][] sortedConstraints = FW.radix(FW.constraintGraph, nodes);
				     System.out.println("\n\n" + "whole contraints: " + "\n" + wholeConstraints);	
				     System.out.println("\n" + "reduced contraints: " + "\n" + reducedConstraints);	
				     System.out.println("\n" + "reduced contraints size: " + reducedConstraints.size());	
				     System.out.println("\n" + "first legal phi: " + firstLegalPhi);
				     System.out.println("\n" + "last legal phi: " + lastLegalPhi);
				     
				     System.out.println("\n\n This is iteration vectors:");
				     if (FW.bellmanFord(nodes, FW.constraintGraph,sortedConstraints)){
				    	 for (int t = 0; t <= FW.numOfBFIteration; t++){
				    		 System.out.print("s^" + t + ": ");
				    		 for (int r = 0; r < vertexNum +1; r++){
				    			 System.out.print(FW.iterationVector.get(t)[r] + " ");
				    		 }    		 
				    		 System.out.print("\n"); 
				    	 }				    	 
				     }
				     
				     
				     System.out.print("\n\n number of iterations: " + FW.numOfBFIteration + "\n");
				     
				     /*----------------------output files-----------------------------------*/
				     
				     /*--------file #1: W and D matrices------------*/
					  Writer output_1 = null;		
					  String fileNameWD = fileName + "-WD.txt";
					  File file_1 = new File( fileNameWD);
					  output_1 = new BufferedWriter(new FileWriter(file_1));
					  String nextLine = "\r\n";	
					  for (int w = 0; w <= vertexNum+1; w++)
					  {
						  if (w==0){
							  output_1.write(" W matrix: ");
							  output_1.write(nextLine);
							  output_1.write("W ");
							  for (int z = 0; z < vertexNum+1; z++){
								  output_1.write("v" + z + " ");
							  }
							  output_1.write(nextLine);
						  }else{							 
							  String oneLine = " ";
							  for(int n = 0; n <= vertexNum; n++){
							 	  oneLine = oneLine + Integer.toString(p[w-1][n]) + " ";
							  }
							  int wcol = w - 1;
							  output_1.write("v" + wcol + oneLine);	
							  output_1.write(nextLine);
						  }				  
					  }
					  
					  output_1.write(nextLine);
					  
					  for (int w = 0; w <= vertexNum+1; w++)
					  {
						  if (w==0){
							  output_1.write(" D matrix: ");
							  output_1.write(nextLine);
							  output_1.write("D ");
							  for (int z = 0; z < vertexNum+1; z++){
								  output_1.write("v" + z + " ");
							  }
							  output_1.write(nextLine);
						  }else{
							  String oneLine = " ";
							  for(int n = 0; n <= vertexNum; n++){
								  oneLine = oneLine + Integer.toString(q[w-1][n]) + " ";
							  } 
							  int dcol = w - 1;
							  output_1.write("v" + dcol + oneLine);
							  output_1.write(nextLine);
						  }					  					  
					  }
					  
					  output_1.close();
					  System.out.println("\n" + "Your output file #1 - WD matrices has been written");		
					  
					  /*--------------------file #2: opt_phi constraints-------------------*/
					  Writer output_2 = null;		
					  String fileNameConstraints = fileName + "-constraints.txt";
					  File file_2 = new File(fileNameConstraints);
					  output_2 = new BufferedWriter(new FileWriter(file_2));
					  int lengthOfFile2 = reducedConstraints.size()+1;
					  
					  for (int w = 0; w < lengthOfFile2; w++)
					  {
						  if (w==0){
							  output_2.write(" This is final constraints file: ");
							  output_2.write(nextLine);
						  }else{							 						  
							  output_2.write(reducedConstraints.get(w-1));	
							  output_2.write(nextLine);
						  }				  
					  }
					  					  
					  output_2.close();
					  System.out.println("\n" + "Your output file #2 - constraints has been written");	
					  
					  /*--------------------file #3: constraint graph-------------------*/
					  Writer output_3 = null;		
					  String fileNameCG = fileName + "-CG.txt";
					  File file_3 = new File(fileNameCG);
					  output_3 = new BufferedWriter(new FileWriter(file_3));
					  int numOfConstraints = sortedConstraints.length;
					  int file_3NumOfLines = numOfConstraints + 10;					  
					  int base = numOfConstraints;
					  
					  for (int w = 0; w < file_3NumOfLines; w++)
					  {						  
						  if (w <= lineOfG){
							  output_3.write(originalFile[w]);
							  output_3.write(nextLine);
						  }else if (base > 0){
							  for (int y = 0; y < 3; y++){
								  output_3.write(Integer.toString(sortedConstraints[numOfConstraints-base][y]) + " ");
							  }			
							  base--;
							  output_3.write(nextLine);
						  }else if (w == file_3NumOfLines-1){
							  output_3.write(".e");
							  output_3.write(nextLine);
						  }	  						  				  			  
					  }
					  
					  output_3.close();
					  System.out.println("\n" + "Your output file #3 - constraint graph has been written");	
					  
					  /*--------------------file #4: intermidiate BF-------------------*/
					  Writer output_4 = null;		
					  String fileNameBF = fileName + "-BF.txt";
					  File file_4 = new File(fileNameBF);
					  output_4 = new BufferedWriter(new FileWriter(file_4));
					  output_4.write("Following shows the path weight vectors: ");
					  output_4.write(nextLine);
					  for (int t = 0; t <= FW.numOfBFIteration; t++){
						  output_4.write("s^" + t + ": ");
			    		 for (int r = 0; r < vertexNum + 1 ; r++){
			    			 output_4.write(FW.iterationVector.get(t)[r] + " ");
			    		 }    		 
			    		 output_4.write(nextLine);
			    	  }
					  output_4.write(nextLine);
					  output_4.write("In the " + FW.numOfBFIteration + "th iteration, the algorithm reaches the stable value." );
					  
					  output_4.close();
					  System.out.println("\n" + "Your output file #4 - BF has been written");	
					  
					  
					  /*--------------------file #5: correlator-retiming-summary.txt-------------------*/
                      Writer output_5 = null;        
                      String fileNameSM = fileName + "-summary.txt";
                      File file_5 = new File(fileNameSM);
                      output_5 = new BufferedWriter(new FileWriter(file_5));
                      output_5.write("The optimal retiming vector is: ");                      
                      for (int r = 0; r < vertexNum ; r++){
                          int m = FW.iterationVector.getLast()[r];
                          output_5.write(Integer.toString(FW.iterationVector.getLast()[r])+" ");
                      }
                      output_5.write(nextLine);
                      output_5.write("The optimal clock cycle time is: ");                 
                      if (lastLegalPhi != Integer.MAX_VALUE){
                    	  output_5.write(Integer.toString(lastLegalPhi));
                      }else{
                    	  output_5.write ("N/A");
                      } 
                      output_5.write(nextLine);
                      output_5.write("The initial(legal) clock cycle time is: " );  
                      if (firstLegalPhi != Integer.MAX_VALUE){
                    	  output_5.write(Integer.toString(initialPhi));
                      }else{
                    	  output_5.write ("N/A");
                      }                     
                      output_5.close();
                      System.out.println("\n" + "Your output file #5 - SM has been written");
                      
                      
                      /*--------------------file #6: correlator-out.txt-------------------*/
                      Writer output_6 = null;        
                      String fileNameOUT = fileName + "-out.txt";
                      File file_6 = new File(fileNameOUT);
                      output_6 = new BufferedWriter(new FileWriter(file_6));
                      output_6.write(".name correlator-out");
                      output_6.write(nextLine);
                      output_6.write(".n "+(nodes.length-1));
                      output_6.write(nextLine);
                      output_6.write(".d ");
                      for(int x=1;x<nodes.length;x++){
                          output_6.write(Integer.toString(nodes[x].delay)+" ");
                      }
                      output_6.write(nextLine);
                      output_6.write(".r ");
                      for(int y=1;y<nodes.length;y++){
                          output_6.write(Integer.toString(FW.iterationVector.getLast()[y])+" ");
                      }
                      output_6.write(nextLine);
                      output_6.write(".g");
                      output_6.write(nextLine);
                      for(int m=0;m<nodes.length;m++){
                          for(int n=0;n<nodes.length;n++){
                              for(int s=0;s<edges.length;s++){
                                  if(edges[s].from.index==m && edges[s].to.index==n){                                      
                                      output_6.write(Integer.toString(m)+" ");                                     
                                      output_6.write(Integer.toString(n)+" ");                                          
                                      output_6.write(Integer.toString(edges[s].weight[0]+FW.iterationVector.getLast()[n]-FW.iterationVector.getLast()[m]));
                                      output_6.write(nextLine);
                                  }
                                  
                              }
                              
                          }
                      }
                      output_6.write(".e");
                                           
                      output_6.close();
                      System.out.println("\n" + "Your output file #6 - OUT has been written");
 					  
			  }
		  }
		  catch (Exception e)
		  {		//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		  }		  
	 }
	 
	
}
