package com.iitc.cn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Dijkstra {
	static int option;
	public static String fileName=null;
	public static int[][] topologyMatrix=null;
	public static int row,column,sourceNode,destinationNode;
	
	static int nextNode;
	static int nextNodePosition;
	static int resultFlag;
	public static Boolean flag1=false;
	public static Boolean flag2=false;
	public static Boolean sourceFlag = false;
	
	public static int connection=0;
	public static int lPath=0;
	public static int insertNode,deleteNode=0;
	public static int[] temp;
	
	public static Scanner sc=new Scanner(System.in);
	
		static ArrayList<Integer> traversedNodes = new ArrayList<Integer>();
		static ArrayList<Integer> listOfCurrMinVal = new ArrayList<Integer>();
		static ArrayList<Integer> neighboursList = new ArrayList<Integer>();
		static ArrayList<Integer> parentNodes = new ArrayList<Integer>();
		static ArrayList<Integer> intermediateNode = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		readChoice();
	}
	
	public static void readChoice()
	{
		Boolean isNumber;
		do
		{
			displayMenu();
			if(sc.hasNextInt())
			{
				option=sc.nextInt();
				if(option<=0||option>6)
				{
					System.err.println("Please enter option 1 to 5 only!!!!!");
					isNumber=false;
				}
				else
				{
					isNumber=true;
				}
			}
			else
			{
				System.err.println("Error!!! Please enter numbers only");
				isNumber=false;
				sc.next();
			}
		}while(!(isNumber));
		menuFunction();
	}
	
	public static void displayMenu()
	{
		System.out.println("Welcome to Dijkstra Algorithm/ Link State Routing Algorithm Simulator");
		System.out.println("=============================================");
		System.out.println("|              Enter your Choice            |");
		System.out.println("|-------------------------------------------|");
		System.out.println("| 1.) Build a network topology              |");
		System.out.println("| 2.) Print Routing Table                   |");
		System.out.println("| 3.) Find the shortest path to destination |");
		System.out.println("| 4.) Add node                              |");
		System.out.println("| 5.) Delete node                           |");
		System.out.println("| 6.) Exit                                  |");
		System.out.println("=============================================");
	}
	
	public static void menuFunction()
	{
			try{
						switch(option)
						{
							case 1: System.out.println("**********You have selected Option 1**********");
									fileName=getFile();
									topologyMatrix=readMatrix(fileName);
									System.out.println("Input topologyMatrix is : ");
									displayMatrix(topologyMatrix);
									flag1=true;
									readChoice();
									break;
									
							case 2: System.out.println("**********You have selected Option 2**********");
									getSourceNode();
									flag2=true;
									findRoutingTable(sourceNode-1);
									System.out.println("**************Routing table for other nodes**************");
									for(int i=1;i<=column;i++)
									{
										if(i!=sourceNode)
											findRoutingTable(i-1);
									}
									readChoice();
									break;
									
							case 3: System.out.println("**********You have selected Option 3**********");
								
									try {
										flag2=false;
										getSourceNode();
										sourceNode=sourceNode-1;
										System.out.println("Enter the destination node : \t");
										destinationNode = sc.nextInt();
										if (destinationNode <= 0 || destinationNode > column) {
											System.err.println("Value of destination node must be between 1 and "+column);
											break;
										}
									} catch (Exception e) {
										System.err.println("Source node must between 1 and "+column);
										break;
									}
									findRoutingTable(sourceNode);
									destinationNode = destinationNode-1;
									printPathandCost(sourceNode,destinationNode);
									readChoice();
									break;
									
							case 4: System.out.println("**********You have selected Option 4**********");
									if(flag1==true)
									{
										System.out.println("Enter the position where you want to insert new node");
										insertNode=sc.nextInt();
										if(insertNode > column+1 || insertNode<1)
										{
											System.out.println("Out of Range");
											break;
										}
										insertNode=insertNode-1;
										temp=new int[column+1];
										System.out.println("Enter the matrix values for the new node");
										System.out.println("Note : Each value must be entered with enter");
										for(int i=0;i<column+1;i++){
											temp[i]=sc.nextInt();
										}
										Insertnode();
									}
									else
									{
										System.err.println("Please enter the topology topologyMatrix first");
										readChoice();
										sc.next();
									}
									break;
									
							case 5:	System.out.println("**********You have selected Option 5**********");
									if(flag1==true)
									{
										System.out.println("Enter The Node to be deleted");
										deleteNode=sc.nextInt();
										if(deleteNode > column || deleteNode<1)
										{
											System.out.println("Incorrect node");
											break;
										}
										deleteNode=deleteNode-1;
										deleteNode();
									}
									else
									{
										System.err.println("Please enter the topology topologyMatrix first");
										readChoice();
										sc.next();
									}
									break;	
									
							case 6: System.out.println("**********You have selected Option 6**********");
									System.out.println("***************Exit from program**************");
									break;	
									
							}//switch
			}//try
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}//getInput
	
	public static String getFile()
	{
		try
		{
			System.out.println("Enter the file name (.txt file) : ");
			while(sc.hasNext())
			{
				fileName=sc.next();
				System.out.println("The file name entered is : "+fileName);
				File f=new File(fileName);
				if(!f.exists())
				{
					System.err.println("File entered does not exist!!!");
					readChoice();
				}
				else
				{
					System.out.println("Entered file exists");
					break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return fileName;
	}
	
	public static int[][] readMatrix(String fileName)
	{
		try{
			BufferedReader br=new BufferedReader(new FileReader(fileName));
			row=0;
			String line;
			topologyMatrix =new int[5][5];
			
			while((line=br.readLine())!=null)
			{
				String [] space = line.split(" ");
				for(int i=0;i<space.length;i++)
				{
					topologyMatrix[row][i]=Integer.parseInt(space[i]);
				}
				row++;
			}
			column=row;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return topologyMatrix;
	}
	
	public static void displayMatrix(int[][] topologyMatrix)
	{
		String str = "|\t";
		System.out.println("-------------------------------------------------");
		for(int m=0;m<row;m++)
		{
			for(int n=0;n<column;n++)
				str += topologyMatrix[m][n] + "\t";
			System.out.println(str + "|");
            str = "|\t";
		}
		System.out.println("-------------------------------------------------");
	}
	
	//////////////////////////////////////////////////Case 2//////////////////////////////////////////////////////////
	
	public static int getSourceNode()
	{
		try
		{
			if(flag1==true)
			{
				System.out.println("Enter the source node : ");
				Boolean loopFlag;
				do
				{
					if(sc.hasNextInt())
					{
						sourceNode=sc.nextInt();
						if(sourceNode<1||sourceNode>column)
						{
							System.err.println("Value of source node must be between 1 and "+column+" !!!");
							System.out.println("Please enter new source value : _");
							loopFlag=false;
						}
						else
						{
							loopFlag=true;
							sourceFlag=true;
							return sourceNode;
						}
					}
					else
					{
						System.err.println("Error!!! Please enter numbers only");
						loopFlag=false;
						sc.next();
					}
				}while(!loopFlag);
				readChoice();
			}
			else
			{
				System.err.println("Please enter the topology topologyMatrix first");
				readChoice();
				sc.next();
			}
		}//try
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return sourceNode;
	}
	
		public static void findRoutingTable(int sourceNode) {
			nextNodePosition = 0;
			nextNode = 0;
			listOfCurrMinVal.clear();
			parentNodes.clear();
			traversedNodes.clear();
			intermediateNode.clear();
			
			for (int i=0;i<column;i++) 
			{
				if (i==sourceNode) 
				{
					listOfCurrMinVal.add(0);
				} 
				else 
				{
					listOfCurrMinVal.add(1000);
				}//if
			}//for
			traversedNodes.add(sourceNode);
			for (int j = 0; j < column; j++) {
				parentNodes.add(-1);
			}
			nextNodePosition = sourceNode;
			while (traversedNodes.size() != column) {
				neighbouringNodes(nextNodePosition);
				UpdateArray(nextNodePosition);
				getMinValue();
				traversedNodes.add(nextNodePosition);
			}
			connectionTableArray(sourceNode);
			resultFlag = 0;
			while (resultFlag != 1) {
				printTableValues();
				if (intermediateNode.contains(0)) 
					resultFlag = 0;
				 else 
					resultFlag = 1;
				
			}
			if(flag2==true)
			{
				System.out.println("=================================================================");
				System.out.println("|\t\tRouting table for node "+(sourceNode+1)+"\t\t\t|");
				System.out.println("|---------------------------------------------------------------|");
				System.out.println("|\tDestination Node\t"+"|"+"\tNext-hop Node\t\t|");
				System.out.println("|===============================================================|");
				for (int k=0; k < column; k++) 
				{
					System.out.println("|\t\t"+(k+1) + "\t\t|\t\t" + intermediateNode.get(k)+"\t\t|");
				}
				System.out.println("=================================================================");
			}
		}

				public static void neighbouringNodes(int nextNodePosition) {
					int x;
					neighboursList.clear();
					for (int j = 0; j < column; j++) 
					{
						x = topologyMatrix[nextNodePosition][j];
						if (!traversedNodes.contains(j)) 
						{
							if ((x != -1) && (x > 0)) 
							{
								neighboursList.add(j);
							}
						}
					}//for
				}
				
		public static void printTableValues() {
			int number,i=0;
			for (int m=0;m<column;m++) 
			{
				if (intermediateNode.get(m)==0) 
				{
					number=parentNodes.get(m);
					i=0;
					while (i!=1) {
						if (intermediateNode.get(number) == (number + 1)) 
						{
							intermediateNode.remove(m);
							intermediateNode.add(m, number + 1);
							i = 1;
						} 
						else 
						{
							number = intermediateNode.get(number);
							if (number == 0) {
								intermediateNode.remove(m);
								intermediateNode.add(m, 0);
								i = 1;
							} else {
								number--;
							}
						}//if
					}//while
				}//if
			}//for
		}

		public static void connectionTableArray(int sourceNode) {
			for (int node = 0; node < column; node++) {
				if (sourceNode==parentNodes.get(node)) 
				{
					intermediateNode.add(node+1);
				} 
				else 
				{
					if (parentNodes.get(node)==-1)
						intermediateNode.add(-1);
					else
						intermediateNode.add(0);
				}
			}
		}

		public static void UpdateArray(int nextNodePosition) {
			int y,nextMin = 0;
			for (int j = 0; j < neighboursList.size(); j++) {
				y = neighboursList.get(j);
				nextMin = topologyMatrix[nextNodePosition][y] + nextNode;
				if (nextMin < listOfCurrMinVal.get(y)) {
					listOfCurrMinVal.add(y, nextMin);
					listOfCurrMinVal.remove(y + 1);
					parentNodes.add(y, nextNodePosition);
					parentNodes.remove(y + 1);
				}//if
			}//for
		}

		public static void getMinValue() {
			Integer i = 0;
			int min = 1000;
			for (Integer x : listOfCurrMinVal) {
				if ((x != -1) && (x > 0)) {
					if (x <= min && !(traversedNodes.contains(i))) {
						nextNode = x;
						nextNodePosition = i;
						min = x;
					}
				}
				i++;
			}//for
		}

		////////////////////////////////////////////////////////////////////////Case 3///////////////////////////////////////////////////////////
		public static void printPathandCost(int srcNode,int destNode) {
			int minCost = 0;
			int currRouter;
			if(flag2==true)
				currRouter=sourceNode;
			else
				currRouter=srcNode;
			System.out.println("current node is:"+currRouter);
			int findRouter = 0;
			Stack<Integer> st = new Stack<Integer>();
			if (parentNodes.get(destNode) == currRouter) {
				minCost = minCost + topologyMatrix[currRouter][destNode];
				st.push(destNode);

			} else {
				if (parentNodes.get(destNode) == -1) {
					// skip
				} else {
					int i = 0;
					findRouter = destNode;
					int currSource;
					while (i != 1) {

						st.push(findRouter);
						currSource = parentNodes.get(findRouter);
						minCost = minCost + topologyMatrix[currSource][findRouter];

						if (parentNodes.get(currSource) == currRouter) {
							minCost = minCost + topologyMatrix[currRouter][currSource];
							st.push(currSource);
							i = 1;
						} else {
							findRouter = currSource;
						}
					}
				}
			}
			st.push(currRouter);
			System.out.println("The shortest path from node "+ (sourceNode + 1) + " to node " + (destinationNode + 1)+ " is : ");

			if (st.size() == 1) {
				System.out.println("There is no path between node "+ (sourceNode + 1) + " to node " + (destinationNode + 1));
			} else {

				while (!st.empty()) {
					int x = (Integer) st.pop();
					x++;
					System.out.print(x);
					if (!st.empty())
						System.out.print("-");
				}
			}
			System.out.println("\nTotal cost of the shotest path is : " + minCost);
		}
		///////////////////////////////////////////////////////Case 4/////////////////////////////////////////////////////////

		//Adding new node
		public static void Insertnode(){
			int oldNode=column;
			column=column+1;
			int[][]matrix=new int[column][column];
			
			for(int m=0;m<column;m++){
				for(int n=0;n<column;n++){
					matrix[m][n]=0;
				}
			}
			for(int m=0;m<oldNode;m++){
			for(int n=0;n<oldNode;n++){
					matrix[m][n]=topologyMatrix[m][n];
				}
			}
			if(insertNode>0){
				for(int m=0;m<insertNode;m++){
					for(int n=column-1;n>insertNode;n--){
						matrix[m][n]=matrix[m][n-1];
						matrix[n][m]=matrix[m][n];
					}
				}
			}
			for(int m=column-2;m>insertNode;m--){
				for(int n=column-1;n>m;n--){
					matrix[m][n]=matrix[m-1][n-1];
					matrix[n][m]=matrix[m][n];
				}
			}                                 
			for(int m=0;m<column;m++){
				matrix[insertNode][m]=temp[m];
				matrix[m][insertNode]=temp[m];
			}
			matrix[insertNode][insertNode]=0;
			topologyMatrix=new int[column][column];
			
			for(int m=0;m<column;m++){
				for(int n=0;n<column;n++){
					topologyMatrix[m][n]=matrix[m][n];
				}
			}
			System.out.println("Total Number of Nodes present in new matrix is :"+column);
			System.out.println("The modified matrix is : ");
			displayMatrix(topologyMatrix);
			readChoice();
		}
		
		//Delete node
		public static void deleteNode(){
			int oldNode=column;
			int[][]matrix=new int[column][column];
			for(int m=0;m<column;m++){
				for(int n=0;n<column;n++)
				{
					matrix[m][n]=topologyMatrix[m][n];
				}
			}
			column=column-1;
			for(int m=deleteNode;m<oldNode-1;m++){
				matrix[m][m]=matrix[m+1][m+1];
			}
			if(deleteNode>0){
				for(int m=0;m<deleteNode;m++){
					for(int n=deleteNode;n<oldNode-1;n++){
						matrix[m][n]=matrix[m][n+1];
						matrix[n][m]=matrix[m][n];
					}
				}
			}
			for(int m=deleteNode;m<oldNode-2;m++){
				for(int n=m+1;n<oldNode-1;n++){
					matrix[m][n]=matrix[m+1][n+1];
					matrix[n][m]=matrix[m][n];
				}
			}                                 
			for(int m=0;m<column;m++){
				for(int n=0;n<column;n++){
					topologyMatrix[m][n]=matrix[m][n];
				}
			}
			System.out.println("The Total Number of Routers present after deletion of a node are:"+column);
			System.out.println("The modified matrix is : ");
			displayMatrix(topologyMatrix);
			readChoice();
		}
}
