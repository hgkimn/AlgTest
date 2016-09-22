import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import java.util.PriorityQueue;
import java.util.Vector;
import java.util.Iterator;
import java.util.Stack;
//import java.util.List;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.Arrays;

//K번째 최단경로, 최단경로
//12개중 4개 통과!
public class Solution {
    static final int MAX_NODE = 100005; //20만 ok
    
    static int N, M, K;
    static Vector[] AdjEdges = new Vector[MAX_NODE]; //인접한 adjacency Edges(Node)
    
    //K Shp
    //static PriorityQueue[] Dist = new PriorityQueue[MAX_NODE]; //시작 ~ Node까지 (비용, 이전노드) Queue(최대비용먼저), K개 보관
    //SHP
    static long[] Dist = new long[MAX_NODE]; //시작 ~ Node까지 최소비용
    static int[] Prev = new int[MAX_NODE]; //path
    static boolean[] Marked = new boolean[MAX_NODE]; //방문여부
    
    //최소값먼저
    static class Node implements Comparable<Node> {
    	long weight;
    	int pos;
    	
    	public Node(long c, int n) {
    		weight = c;
    		pos = n;
    	}
    	
    	public int compareTo(Node old) {
    		return Long.compare(weight,  old.weight);
    	}
    }
    /*
    //K SHP 최대값 먼저
    static class RNode implements Comparable<RNode> {
    	long weight;
    	int pos;
    	
    	public RNode(long c, int n) {
    		weight = c;
    		pos = n;
    	}
    	
    	public int compareTo(RNode old) {
    		return Long.compare(old.weight, weight);
    	}
    }
    
    //K SHP 지나온 Node Chk
    static boolean chkPath(int n, int now) {
    	Iterator in = Dist[now].iterator();
    	while (in.hasNext()) {
    		RNode nn = (RNode)in.next();
    		if (n == nn.pos) return true;
  		}
    	return false;
    }
    */
    
    public static void main(String[] args) throws Exception {
    	long startTime = System.currentTimeMillis();
    	
    	System.setIn(new FileInputStream("sample_input.txt"));
    	InputStreamReader isr = new InputStreamReader(System.in);
    	BufferedReader br = new BufferedReader(isr);
    	
    	int T = Integer.parseInt(br.readLine().trim());
    	for ( int a=1; a<=T; ++a) {
    		StringTokenizer tokenizer = new StringTokenizer(br.readLine().trim());
    		N = Integer.parseInt(tokenizer.nextToken());
    		M = Integer.parseInt(tokenizer.nextToken());
    		K = Integer.parseInt(tokenizer.nextToken());
    		
    		//init
    		for (int i=1; i<=N; ++i) {
    			AdjEdges[i] = new Vector();
    		    //Dist[i] = new PriorityQueue<RNode>(); //K SHP   		    
    		}
    	    //SHP
    		Arrays.fill(Dist,    0, N+1, Integer.MAX_VALUE);
    	    Arrays.fill(Prev,    0, N+1, 0);
    	    Arrays.fill(Marked,  0, N+1, false);

    		
    		//Construct "weighted undirected Graph"
    		for (int i=1; i<=M; ++i) {
    			tokenizer = new StringTokenizer(br.readLine().trim());
    			int v = Integer.parseInt(tokenizer.nextToken()); //start node
    			int w = Integer.parseInt(tokenizer.nextToken()); //end node
    			int weight = Integer.parseInt(tokenizer.nextToken()); //cost
    			
    			AdjEdges[v].add(new Node(weight, w));
    			AdjEdges[w].add(new Node(weight, v));
    		}
    		
    		//Compute Shortest Path
    		int st = 1;
    		int dst = N;
    		PriorityQueue<Node> pq = new PriorityQueue<Node>();
    		pq.add(new Node(0l, st));
    		//Dist[st].add(new RNode(0l, st)); //K SHP
    		Dist[st] = 0; //SHP
    		
    		while (!pq.isEmpty()) {
    			Node now = pq.poll();
    			int node = now.pos;
    			long cost = now.weight;
    			
    			if (Marked[node]) continue;
    			Marked[node] = true;
    			/*
    			//K SHP 보관된 비용보다 크고, K번째 이상이면 다음Q로 통과
    			RNode tt = (RNode)Dist[node].peek();
    			if (cost > tt.weight && Dist[node].size() >= K) continue; 
    			*/
    			
    			//노드와 연결된 모든 인접 Edge를 대상으로BFS
    			for (Object obj : AdjEdges[node]) {
    				Node there = (Node) obj;
    				int next = there.pos;
    				long newcost = cost + there.weight;
    				
    				//SHP
    				if (Dist[next] > newcost) { 
    					Dist[next] = newcost;
    					Prev[next] = node;
    					pq.add(new Node(newcost, next));
    				}
    				/*
    				//K SHP
    				if (chkPath(next, node)) continue; //이미 계산된 지나온 길, 통과
    				if (Dist[next].size() < K) {
    					Dist[next].add(new RNode(newcost, node));
    					pq.add(new Node(newcost, next));
    				}
    				else if (((RNode)Dist[next].peek()).weight > newcost) {
    					Dist[next].add(new RNode(newcost, node));
    					pq.add(new Node(newcost, next));
    					
    					Dist[next].poll();// K보다 넘치는 큰놈 제거
    				}
    				*/
    			}
    		}
    		/*
    		//K SHP
    		Stack stk = new Stack();
    		for (int i=1; !Dist[dst].isEmpty(); ++i) {
    			long cost = ((RNode)Dist[dst].poll()).weight;
    			stk.push(cost);
    		}
    		if (stk.size() < K) System.out.println("#"+a+" -1");
    		else {
    			for (int i=1; i<K; ++i) stk.pop();
    			System.out.println("#"+a+" " + stk.pop());
    		}
    		*/

    		//SHP
    		System.out.println("#"+a+" " + Dist[dst]);
    		for (int i=1; i<=N; ++i) {
    			System.out.println("Dist["+i+"] : " + Dist[i]);
    			System.out.println("Prev["+i+"] : " + Prev[i]);
    			System.out.println("Marked["+i+"] : " + Marked[i]);
    		}
    	}
    	//sc.close():
    	br.close();
    	isr.close();
    	
    	long endTime1 = System.currentTimeMillis();
    	System.out.println("Total took " + (endTime1 - startTime) + " milliseconds");
    }

}
