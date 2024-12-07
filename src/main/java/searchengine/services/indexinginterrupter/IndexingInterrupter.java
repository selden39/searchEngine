package searchengine.services.indexinginterrupter;

public class IndexingInterrupter implements Runnable{
    private Thread thread;

    public IndexingInterrupter(Thread thread){
        this.thread = thread;
    }

    @Override
    public void run(){
        System.out.println("call interrupt");
        thread.interrupt();
    }
}
