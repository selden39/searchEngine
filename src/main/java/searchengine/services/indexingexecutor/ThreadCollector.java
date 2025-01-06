package searchengine.services.indexingexecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class ThreadCollector {
    public static HashMap<Thread, ForkJoinPool> indexingThreads = new HashMap<>();

    public static void addIndexingThread(Thread thread, ForkJoinPool forkJoinPool){
        indexingThreads.put(thread, forkJoinPool);
    }

    public static Map<Thread, ForkJoinPool> getIndexingThreads(){
        return indexingThreads;
    }
}
