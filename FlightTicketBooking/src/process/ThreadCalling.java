package process;

public class ThreadCalling extends Thread{

	public void welcome(String greetings) {
		Thread call=new Thread();
		System.out.println("-------------------------------------------------------------");
		for(int i=0;i<greetings.length();i++) {
			System.err.print(greetings.charAt(i));
			try {
				call.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(greetings.length()-1==i)
				System.out.println();
		}
		System.out.println("-------------------------------------------------------------");
	}
   
}
