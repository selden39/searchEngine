package searchengine.services.indexingexecutor;

import java.util.HashSet;
import java.util.Set;

//TODO подумать над применением lombok аннотаций
public class ThreadCollector {
    public static Set<Thread> indexingThreads = new HashSet<>();

    public static void addIndexingThread(Thread thread){
        indexingThreads.add(thread);
    }

    public static Set<Thread> getIndexingThreads(){
        return indexingThreads;
    }
}
