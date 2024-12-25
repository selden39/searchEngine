package searchengine.services.indexinginterrupter;

public class IndexingInterrupter implements Runnable{
    private Thread thread;

    public IndexingInterrupter(Thread thread){
        this.thread = thread;
    }

    @Override
    public void run(){
        thread.interrupt();
    }
}
