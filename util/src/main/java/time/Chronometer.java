/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package time;

import java.io.File;
import util.Logger;

/**
 * Chronometer to count the time
 * 
 * @author Daniel Plaza
 */
public class Chronometer extends Logger{
    
    private long ini;
    private boolean running;
    
    public Chronometer(boolean start, File configLogger){
        super(configLogger);
        running=false;
        if(start)
            start();
    }
    
    /**
     * Creates a Chronometer and starts it by default
     */
    public Chronometer(){
        this(true,null);
    }
    
    public Chronometer(boolean start){
        this(start,null);
    }
    
    /**
     * Starts the chronometer
     * 
     */
    public final void start(){
        ini=System.currentTimeMillis();
        running=true;
        log("Chronometer started");
    }
    
    /**
     * Returns the time count in milliseconds but doesn't stop the chronometer
     * 
     * @return time, 0 if the chronometer hadn't started
     */
    public long getTime(){
        if(!running)
            return 0;
        else{
            log("Chronometer time returned");
            return calcTime();
        }
    }
    
    /**
     * Returns the time count in milliseconds and stops the chronometer
     * 
     * @return time, 0 if the chronometer hadn't started
     */
    public long stop(){
        if(!running)
            return 0;
        else{
            long time = calcTime();
            running = false;
            log("Chronometer stopped and time returned");
            return time;
        }
    }
    
    /**
     * Returns the time count in milliseconds, stops the chronometer and start over again
     * 
     * @return time, 0 if the chronometer hadn't started
     */
    public long lap(){
        long time = stop();
        start();
        return time;
    }
    
    /**
     * 
     * @return true if the chronometer has started already 
     */
    public boolean running(){
        return running;
    }
    
    private long calcTime(){
        return System.currentTimeMillis()-ini;
    }
    
}
