import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.JTextArea;


public class Process extends Thread{
	int id;
	int n;
	long timestamp;
	boolean requested=false;
	boolean ack=false;
        JTextArea terminal;
	Process(int id,int n,JTextArea t){
		this.id=id;
		this.n=n;
                this.terminal=t;
	}
	public void run(){
		while(true){
			try{
				Thread.sleep(10);
			}catch(Exception ex){}
		}
	}
	PriorityQueue<Integer> queue=new PriorityQueue<Integer>();
	ArrayList<Container> reqQueue=new ArrayList<Container>();
	public void request(final long timestamp,final int id){
		terminal.append("Process "+id+" sends request to process "+this.id+"\n");
               
		if(!requested)
			new Thread(){
			public void run(){
				try{
					Thread.sleep(new Random().nextInt(500));
				}catch(Exception ex){}
				MainFrame.arr[id].ack(Process.this.id);
				ack=false;
			}
		}.start();
		else{
			if(this.timestamp<timestamp){
				queue.add(id);

			}
			else
				new Thread(){
				public void run(){
					try{
						Thread.sleep(new Random().nextInt(500));
					}catch(Exception ex){}
					MainFrame.arr[id].ack(Process.this.id);
					ack=false;
				}
			}.start();  
		}

	}

	public void ack(int id){
		terminal.append("Process "+this.id+" gets acknowledgement from process "+id+"\n");
                
		reqQueue.remove(new Container(id));
		if(reqQueue.size()==0)
			ack=true;

	}
	public void access(){
		timestamp=System.currentTimeMillis();
		requested=true;
		if(!ack){
			for(int i=0;i<MainFrame.arr.length;i++){
				if(i==id)
					continue;
				reqQueue.add(new Container(i));  
				MainFrame.arr[i].request(timestamp, id);

			}
			while(!ack){
				try{
					Thread.sleep(10);
				}catch(Exception ex){}
			}
		}
		terminal.append("Process "+id+" Entering Critical Section"+"\n");
		try{
			Thread.sleep(500+new Random().nextInt(1000));

		}catch(Exception ex){}
		requested=false;
		terminal.append("Process "+id+" Exiting Critical Section"+"\n");
		for(int i=0;i<queue.size();i++){
			int a=queue.poll();
			MainFrame.arr[a].ack(id);
		}
	}
}
