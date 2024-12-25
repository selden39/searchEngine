package searchengine.services.indexingexecutor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadCollector {
    public static Set<Thread> indexingThreads = new CopyOnWriteArraySet<>();

    public static void addIndexingThread(Thread thread){
        indexingThreads.add(thread);
    }

    public static Set<Thread> getIndexingThreads(){
        return indexingThreads;
    }
}
