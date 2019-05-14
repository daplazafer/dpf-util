/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package time;

/**
 *
 * @author dpf
 */
public class ParallelTask {
    
    private final ThreadTask[] tasks;
    private final int nthreads;
    private int f;
    
    public ParallelTask(Task... tasks){
        this.tasks = new ThreadTask[tasks.length];
        for(int i = 0 ; i<tasks.length ; i++){
            this.tasks[i] = new ThreadTask(tasks[i]);
        }
        this.nthreads = tasks.length;
    }
    
    public int get_nthreads(){
        return nthreads;
    }
    
    public static int availableProcessors(){
        return Runtime.getRuntime().availableProcessors();
    }
    
    public void startTasks(boolean waitEnd){
        f = 0;
        for(ThreadTask t : tasks){
            t.start();
        }
        if(waitEnd){
            while(f<nthreads){
                try {
                    Thread.sleep((long)10);
                } catch (InterruptedException ex) {
                }
            }
        }
    }
    
    private class ThreadTask extends Thread{
        
        Task t;
        
        private ThreadTask(Task task){
            t = task;
        }
        
        @Override
        public void run() {
            t.doThis();
            f++;
        }
    }
    
    public interface Task{
        public void doThis();
    }
   
}
