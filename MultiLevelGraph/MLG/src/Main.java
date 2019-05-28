import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
	static int vertex, edge;
	static int[][] graph; // 인접행렬
	static boolean[] visited;
	static int pos;
	static int level;
	static int dlevel;
	static int start;
	static int[][] data; // 레벨별 정점을 보관
	static int[][] bcost;
	static int[][] bd;
	
	/** 
	 * BFS(너비우선순회)를 이용하여 각 노드의 Level 구하기 
	 * (참고 : https://sunpil.tistory.com/22)
	 * */
	public static void bfs(int start) {
		Queue<Integer> q = new <Integer> LinkedList(); // LinkedList를 이용한 큐 구현
		
		visited = new boolean[vertex+1];
		// 시작지점 처리
		visited[start] = true; 
		q.offer(start);
		
		level = 1;

		int num = 0;
		
		data = new int[vertex+1][vertex+1];
		
		while(!q.isEmpty()) {
			
			System.out.println("--------level "+level+"--------"); dlevel = level;
			
			int qSize = q.size(); // 현재 큐 사이즈를 qSize 변수에 담아두기 !!!!!!
			for(int i = 0; i < qSize; i++) {
				pos = q.poll(); 
				System.out.println("node "+ pos + " visited");
				
				data[level][num] = pos;
				num++;
			
				// 인접영역 탐색
				for(int j = 1 ; j <= vertex ; j ++) {
					if(graph[pos][j] !=0 && visited[j] == false) { // 연결되어있고 방문하지 않은경우
						q.offer(j);
						visited[j] = true;
						System.out.println("visited ["+ j +"] = true");
					}
				}
			}
			level++;
		}
			
	}
	public static void main(String[] args) {
		// 사용자 입력
		System.out.println("정점의 수와 간선의 수를 입력 : ");
		Scanner in = new Scanner(System.in);
		vertex = in.nextInt(); // 정점 수
		edge = in.nextInt(); // 간선 수
		
		// 인접행렬 동적생성
		graph = new int[vertex+1][vertex+1]; // 정점번호는 1부터 n까지 부여
		
		// 인접행렬 초기화
		for(int i = 0 ; i < vertex+1 ; i++) {
			for(int j = 0 ; j < vertex+1 ; j++) {
				graph[i][j] = 0;
			}
		}
		/* 주의 : 각 노드의 번호는 전체 정점의 갯수보다 작게 만들자 */
		
		// 사용자 입력
		for(int i = 1 ; i <= edge ; i++) {
			System.out.println(i+"번째 간선: ");
			int row = in.nextInt(); if(i==1) start = row;
			int col = in.nextInt();
			int weight = in.nextInt();
			graph[row][col] = weight; // 인접행렬에 저장
		}
		/* 출력 */
		System.out.println("\n============ 출력 ============");
		
		System.out.println("인접행렬 ㅡㅡㅡ");
		for(int i = 0 ; i < vertex+1 ; i++) {
			for(int j = 0 ; j < vertex+1 ; j++) {
				System.out.print(graph[i][j]);
			}
			System.out.println();
		}
		
		bfs(start); // BFS 탐색 
		
		System.out.println("\n현재 최고 레벨 : " + dlevel + " 입니다.");
		
		bcost = new int[dlevel+1][vertex+1]; // 레벨 1부터 시작
		bd = new int[dlevel+1][vertex+1];
		
		/**
		 * 현재 구하려는것 (BackWard방식)
		 * : 최종으로 V5(5,12) 와 bcost(5,12) 를 구하는것이 목표 !
		 * V5(단계번호,정점번호)
		 * */

		bcost[1][start] = 0; // 시작정점
		
		int index = 0; // 현재 선택된 정점
		int preVertex = 0; // index의 이전정점
		
		/* bcost, bd 값 지정하는 부분 */
		for(int i = 2 ; i <= dlevel ; i++) { // 단계
			System.out.println("\n레벨 ["+ i + "] => V("+i+") : "); // 단계 표시 (2단계부터 표시)
			for(int j = i ; j<= vertex; j++) {
				if(visited[j]) {
					for(int k = 0 ; k < vertex ; k++) {
						if(j == data[i][k]) { // 레벨 i에 해당하는 정점 j를 찾았음
							int min = 9999;
							for(int h = 0 ; h < vertex ; h ++) {
								if(graph[h][j] !=0) {							
									if(bcost[i-1][h] + graph[h][j] < min) {
										 min = bcost[i-1][h] + graph[h][j];
										 index = j;
										 preVertex = h;
									}
								}
								bcost[i][index] = min;
								bd[i][index] = preVertex;
								/* 출력 */
								System.out.println("bcost["+i+"][" + index +"] = " +bcost[i][index]);
								System.out.println("bd["+i+"][" + index +"] = " +bd[i][index]);
							}
						}
					}
				}
			}
		}
		/* bd를 이용하여 최단경로 구하기 */
		System.out.print("\n---\npath: "+index); 
		int v = bd[dlevel][index]; // v = bd[3][4] = 3 
		for(int u = dlevel-1; u > 0 ; u--) {
			System.out.print(" <- " + v); 
			v = bd[u][v]; // v = bd[2][3] = 1
		}
	}
}