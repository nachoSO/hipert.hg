package hipert.hg.frontend.amalthea;

import java.io.IOException;
import java.util.ArrayList;


public class LETCommunication {
	
	private int Task_1 = 0;
	private int Task_2 = 0;
	private int Task_3 = 0;
	
	// Tasks are given in us!!!
	// Effect-chain Task1 --> Task2 --> Task3
	public LETCommunication (int Task1, int Task2, int Task3) {
		Task_1 = Task1;
		Task_2 = Task2;
		Task_3 = Task3;
		
	}
	
	public LETCommunication (int Task1, int Task2) {
		Task_1 = Task1;
		Task_2 = Task2;
		
	}
	
	// n-th publishing point
	int Ppoint (int Task_W, int Task_R, int n) throws IOException {
		int Task_max = Math.max(Task_W, Task_R);
		int P = (int)Math.floor(((double)(n*Task_max))/((double)Task_W))*Task_W;
		return P;
	}
	
	// n-th reading point
	int Qpoint (int Task_W, int Task_R, int n) throws IOException {
		int Task_max = Math.max(Task_W, Task_R);
		int Q = (int)Math.ceil(((double)(n*Task_max))/((double)Task_R))*Task_R;
		return Q;
	}
	
	// new calculation
	int nth_Window (int Task_W, int Task_R, int n) throws IOException{
		int w = 0;
		w = Qpoint(Task_W,Task_R,n) - Ppoint(Task_W,Task_R,n);
		return w;
	}
	
	//old calculation
	/*int nth_Window (int Task_W, int Task_R, int n) throws IOException{
		int w = 0;
		int Task_max = Math.max(Task_W, Task_R);
		int Task_min = Math.min(Task_W, Task_R);
		int t1 = n*Task_max;
		double t2 = ((double)t1)/((double)Task_min) ;

		if (Task_W == Task_max){
			w = (int)Math.ceil(t2)*Task_min - t1;
		}
		
		else {
			w = t1 - (int)Math.floor(t2)*Task_min;
		}
		return w;
	}*/
	
	int Hyperperiod (int Task_A, int Task_B) throws IOException{
		int x = 0;
		int Hyperperiod = 1;

		int max = Math.max(Task_A, Task_B);
		int min = Math.min(Task_A, Task_B);
		 
		for(int i=1;i<=min;i++)
		   {
		    x=max*i; //finding multiples of the maximum number
		    if(x%min==0) //Finding the multiple of maximum number which is divisible by the minimum number.
		     {
		      Hyperperiod=x; //making the 1st multiple of maximum number as lcm, which is divisible by the minimum number
		      break; //exiting from the loop, as we don’t need anymore checking after getting the LCM
		     }
		    }
		//System.out.println(lcm);
		return Hyperperiod;
	}
	
	int n_WR (int Task_A,int Task_B) throws IOException{
		int nWR = 0;
		nWR = Hyperperiod(Task_A, Task_B)/Math.max(Task_A, Task_B);
		return nWR;
	}
	
	int ECHyperperiod (int Task_A, int Task_B, int Task_C) throws IOException{
		int ECHyperperiod = Hyperperiod (Task_C, Hyperperiod (Task_A, Task_B));
		return ECHyperperiod;
	}
	
	String CommunicationType (int Task_A, int Task_B) throws IOException {
		String ComType = "";
		if (Hyperperiod(Task_A, Task_B) == Math.max(Task_A, Task_B) ){
			ComType = "Harmonic Communication";
		}
		else{
			ComType = "Non-Harmonic Communication";
		}
		return ComType;
	}
	
	// Calculating \zeta
	int BasicPathNumber() throws IOException{
		int zeta = 0;
		int LongestPeriod =Math.max(Task_1, Math.max(Task_2, Task_3));
		//System.out.println(Task_1 + "ms -->" + Task_2 + "ms -->" + Task_3 + "ms");
		//System.out.println("The hyperperiod of the EC is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		zeta = ECHyperperiod(Task_1, Task_2, Task_3)/LongestPeriod;
		//System.out.println("Number of bacis paths : " + zeta);
		return zeta;
	}
	
	
	
	// Calculating n_{W,R}^{EC}
	int n_EC_WR(int Task_W, int Task_R) throws IOException{
		int NECWR = ECHyperperiod(Task_1, Task_2, Task_3)/Math.max(Task_W, Task_R);
		//System.out.println(Task_W + "ms and " + Task_R +"ms establish " + CommunicationType(Task_W, Task_R));
		//System.out.println("Number of jobs released by " + Math.max(Task_W, Task_R) + " ms in a EC-hyperperiod : " + NECWR);
		return NECWR;
	}
	

	
	//public ArrayList<Integer> Algorithm1 () throws IOException{
	ArrayList<Integer> Algorithm1 () throws IOException{
		ArrayList<Integer> PointList = new ArrayList<>();
		int z = BasicPathNumber();
		int Q=0;
		int P=0;
		int NECIJ = n_EC_WR(Task_1, Task_2);
		int NECJK = n_EC_WR(Task_2, Task_3);
		int w_IJ = 0;
		int w_JK = 0;
		int d=0;
		
		if (z == NECIJ){
			//System.out.println(" we are here ");
			//int n=1;
			for (int n=1; n <= z; n++){
				int m=1;
				for(m=1;m <= NECJK; m++ ){
					P = Ppoint(Task_2,Task_3,m);
					Q = Qpoint(Task_1, Task_2, (n-1));
					d = P- Q;
					//System.out.println(n + " : " + m + " : " + Q + " " + P + " " + d);
					if (d >0) {
						//System.out.println(" we broke ");
						break;	
					}
				}
				w_IJ = nth_Window(Task_1, Task_2, n-1);
				w_JK = nth_Window(Task_2, Task_3, m);
				PointList.add(w_IJ);
				PointList.add(n-1);
				PointList.add(Q);
				PointList.add(m);
				PointList.add(P);
				PointList.add(w_JK);
				
			}
		}
		
		else {
			//System.out.println(" we are there ");
			for (int n=1; n <= z; n++){
				//int Pre_m = 0;
				int Pre_Qpoint = 0;
				int m=0;
				for(m=0;m <= NECIJ; m++ ){
					P = Ppoint(Task_2,Task_3,n);
					Q = Qpoint(Task_1, Task_2, m);
					
					//System.out.println(n + " : " + m + " : " + Q + " " + P + " " + d);
					d = P - Q;
					if (d <= 0) {
						//System.out.println(" we broke ");
						Pre_Qpoint = Qpoint(Task_1, Task_2, m-1);
						break;	
					}
				}
				w_IJ = nth_Window(Task_1, Task_2, m-1);
				w_JK = nth_Window(Task_2, Task_3, n);
				PointList.add(w_IJ);
				PointList.add(m-1);
				PointList.add(Pre_Qpoint);
				PointList.add(n);
				PointList.add(P);
				PointList.add(w_JK);
				
				
			}
			
		}
		return PointList;
		//System.out.println(PointList);
		


		//return PointList;
		//BasicPathNumber ();
		//n_EC_WR(Task_1,Task_2);
		//n_EC_WR(Task_2,Task_3);
		/*int i=0;
		for (i=0;i<= 10;i++){
			Ppoint (Task_1,Task_2,i);
			Qpoint (Task_1, Task_2,i);
		}*/

		//ECHyperperiod(Task_1, Task_2, Task_3);
	}
	
	ArrayList<Integer> ThetaCalculation () throws IOException{
		ArrayList<Integer> InputList = Algorithm1();
		ArrayList<Integer> ThetaList = new ArrayList<>();
		int theta = 0;
		int pathCounter = 0;
		for(int k=0;k<InputList.size();k=k+6){
			pathCounter ++;
			theta = InputList.get(k) - InputList.get(k+2)+ InputList.get(k+4)+ InputList.get(k+5);
			ThetaList.add(theta);
			//System.out.println("length of path " + pathCounter + " : " + theta);
		}
		
		return ThetaList ;
	}

	public int L2FCalculation () throws IOException {
		System.out.println(Task_1 + "us and " + Task_2 +"us establish " + CommunicationType(Task_1, Task_2));
		System.out.println(Task_2 + "us and " + Task_3 +"us establish " + CommunicationType(Task_2, Task_3));
		System.out.println("The hyperperiod of the EC : " + Task_1 + "us -->" + Task_2 + "us -->" + Task_3 + "us is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		System.out.println("Number of bacis paths : " + BasicPathNumber());
		
		ArrayList<Integer> Thetas = ThetaCalculation();
		int L2FDelay = 0;
		int i=0;
		int max =Thetas.get(0);
		// Calculating max{Theta_EC^n}
		for (i=1;i<Thetas.size();i++){
			max=Math.max(Thetas.get(i), max);
		}
		L2FDelay = Task_1 + max + Task_3;
		System.out.println("The last-2-first propagation delay of the EC is : " + L2FDelay );
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		return L2FDelay;
	}
	
	public int L2LCalculation () throws IOException {
		System.out.println(Task_1 + "ms and " + Task_2 +"us establish " + CommunicationType(Task_1, Task_2));
		System.out.println(Task_2 + "ms and " + Task_3 +"us establish " + CommunicationType(Task_2, Task_3));
		System.out.println("The hyperperiod of the EC : " + Task_1 + "us -->" + Task_2 + "us -->" + Task_3 + "us is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		System.out.println("Number of bacis paths : " + BasicPathNumber());
		// we are interested in the last three elements of the tuple
		ArrayList<Integer> List1 = Algorithm1();
		ArrayList<Integer> List2 = ThetaCalculation();
		int n = 0;
		int P_JK_0 = 0;
		int w_JK_0 = 0;
		int P_JK_1 = 0;
		int w_JK_1 = 0;
		int Q_JK_0 = 0;
		int Q_JK_1 = 0;
		int L2L = 0;
		int maxL2L = 0;
		int Theta = 0;
		System.out.println("Theta, Q_(n+1), Q_n, L2L");
		for(int k=0;k<List1.size();k=k+6){
	
			n = List1.get(k + 3);
			P_JK_0 = List1.get(k + 4);
			w_JK_0 = List1.get(k + 5);
			
			if (k == ((List1.size()/6)-1)*6  ){
			P_JK_1 = List1.get(4) + ECHyperperiod(Task_1, Task_2, Task_3);
			w_JK_1 = List1.get(5);
			}
			
			else {
				P_JK_1 = List1.get(k + 10);
				w_JK_1 = List1.get(k + 11);
			}
				
			Q_JK_0 = P_JK_0 + w_JK_0;
			Q_JK_1 = P_JK_1 + w_JK_1;
			L2L = Q_JK_1 - Q_JK_0 + List2.get(Theta) + Task_1;

			System.out.println(List2.get(Theta) + ", " + Q_JK_0 +", " +  Q_JK_1 + ", " +  L2L);
			maxL2L=Math.max(L2L, maxL2L);
			Theta ++;
		}
		
		//maxL2L = maxL2L;
		System.out.println("The last-2-last propagation delay of the EC is : " + maxL2L  );
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		return maxL2L;
		
	}
	
	public int F2FCalculation () throws IOException {
		System.out.println(Task_1 + "ms and " + Task_2 +"us establish " + CommunicationType(Task_1, Task_2));
		System.out.println(Task_2 + "ms and " + Task_3 +"us establish " + CommunicationType(Task_2, Task_3));
		System.out.println("The hyperperiod of the EC : " + Task_1 + "us -->" + Task_2 + "us -->" + Task_3 + "ms is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		System.out.println("Number of bacis paths : " + BasicPathNumber());
		// we are interested in the last three elements of the tuple
		ArrayList<Integer> List1 = Algorithm1();
		ArrayList<Integer> List2 = ThetaCalculation();
		int n = 0;
		int P_JK_0 = 0;
		int w_JK_0 = 0;
		int P_JK_1 = 0;
		int w_JK_1 = 0;
		int Q_JK_0 = 0;
		int Q_JK_1 = 0;
		int F2F = 0;
		int maxF2F = 0;
		int Theta = 0;
		//System.out.println("Theta, Q_(n+1), Q_n, F2F");
		for(int k=0;k<List1.size();k=k+6){
	
			n = List1.get(k + 3);
			P_JK_0 = List1.get(k + 4);
			w_JK_0 = List1.get(k + 5);
			
			if (k == ((List1.size()/6)-1)*6  ){
			P_JK_1 = List1.get(4) + ECHyperperiod(Task_1, Task_2, Task_3);
			w_JK_1 = List1.get(5);
			}
			
			else {
				P_JK_1 = List1.get(k + 10);
				w_JK_1 = List1.get(k + 11);
			}
				
			Q_JK_0 = P_JK_0 + w_JK_0;
			Q_JK_1 = P_JK_1 + w_JK_1;
			F2F = Q_JK_1 - Q_JK_0 + List2.get(Theta) + Task_1 + Task_3;

			//System.out.println(List2.get(Theta) + ", " + Q_JK_0 +", " +  Q_JK_1 + ", " +  F2F);
			maxF2F=Math.max(F2F, maxF2F);
			Theta ++;
		}
		
		//maxL2L = maxL2L;
		System.out.println("The first-2-first propagation delay of the EC is : " + maxF2F  );
		System.out.println("--------------------------------------------------------------------------------------------------------------------");
		return maxF2F;
	}
	
	/*public int F2FCalculation () throws IOException {
		System.out.println(Task_1 + "ms and " + Task_2 +"ms establish " + CommunicationType(Task_1, Task_2));
		System.out.println(Task_2 + "ms and " + Task_3 +"ms establish " + CommunicationType(Task_2, Task_3));
		System.out.println("The hyperperiod of the EC : " + Task_1 + "ms -->" + Task_2 + "ms -->" + Task_3 + "ms is : " + ECHyperperiod(Task_1, Task_2, Task_3));
		System.out.println("Number of bacis paths : " + BasicPathNumber());
		// we are interested in the last three elements of the tuple
		ArrayList<Integer> List1 = Algorithm1();
		ArrayList<Integer> List2 = ThetaCalculation();
		int n = 0;
		int Q_IJ_0 = 0;
		int w_IJ_0 = 0;
		int P_IJ_0 = 0;
		int Q_IJ_pre = 0;
		int w_IJ_pre = 0;
		int P_IJ_pre = 0;
		int F2F = 0;
		int maxF2F = 0;
		int Theta = 0;
		for(int k=0;k<List1.size();k=k+6){
			//w,n,q
			w_IJ_0 = List1.get(k);
			n = List1.get(k + 1);
			Q_IJ_0= List1.get(k + 2);
			
			if (k == 0 ){
			w_IJ_pre = List1.get (((List1.size()/6)-1)*6);
			Q_IJ_pre = List1.get(((List1.size()/6)-1)*6 + 2) - ECHyperperiod(Task_1, Task_2, Task_3);
			}
			
			else {
				w_IJ_pre = List1.get(k -6);
				Q_IJ_pre = List1.get(k -4);
			}
				
			P_IJ_0 = Q_IJ_0 - w_IJ_0;
			P_IJ_pre = Q_IJ_pre - w_IJ_pre;
			F2F = P_IJ_0 - P_IJ_pre + List2.get(Theta);
			System.out.println(P_IJ_pre +", " +  P_IJ_0 + ", " + List2.get(Theta) + ", " + F2F);
			maxF2F=Math.max(F2F, maxF2F);
			Theta ++;
		}
		
		maxF2F = maxF2F + Task_3;
		System.out.println("The first-2-first propagation delay of the EC is : " + maxF2F  );
		return maxF2F;
	}*/
	
}
