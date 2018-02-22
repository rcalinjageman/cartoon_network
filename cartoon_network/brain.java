import greenfoot.*;
import java.io.Serializable;

/**
 * Write a description of class brain here.
 * 
 * @Bob Calin-Jageman
 * @1.0
 */
public class brain extends World 
{
    // basic parameters for the world, included an editing opbject that will control which editing mode is selected
    public int neuron_count;
    public int editing_mode_current;
    public EditingMode editing_mode_object;
    public boolean finch_started;
    public boolean finch_exists;
    public int height, width;
    
    //parameters for controlling the Finch, if available
    public float output_multiplier;
    public float lmotor, lmotor_return, lmotor_decay, lmotor_max;
    public float rmotor, rmotor_return, rmotor_decay, rmotor_max;
    public float buzz, buzz_return, buzz_decay, buzz_max, buzz_threshold, buzz_multiplier, buzz_time;
    public float rcolor, rcolor_return, rcolor_decay, rcolor_max;
    public float gcolor, gcolor_return, gcolor_decay, gcolor_max;
    public float bcolor, bcolor_return, bcolor_decay, bcolor_max;
    public float light_threshold;
    public float temp_threshold;
    
    //used to tell if there has been a click outside the editor menu
    public boolean clickout; 

    /**
     * Constructor for objects of class brain.
     * 
     */
    public brain()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // Would be nice to enable the screen size and elements to scale with the monitor resolution being used
      
        
       
        
        //setup some basics
        super((int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() *.75), (int) (java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight() *.75), 1); 
        neuron_count = 0;
        editing_mode_current = 6;
        

        
      
        //no finch yet
        finch_started = false;
        finch_exists = false;

        //startup the editing mode object
        editing_mode_object = new EditingMode(this);
        addObject(editing_mode_object, 30,15);
        clickout = false;
        
        //initialize finch parameters in case one is connected
        output_multiplier = 5;
              
        lmotor_return = 0;
        rmotor_return = 0;
        buzz_return = 0;
        rcolor_return = 0;
        gcolor_return = 0;
        bcolor_return = 0;
        
        lmotor_max = 100;
        rmotor_max = 100;
        buzz_max = 100;
        buzz_multiplier = 200;
        buzz_threshold = 10;
        buzz_time = 100;
        rcolor_max = 255;
        bcolor_max = 255;
        gcolor_max = 255;
        
        lmotor = lmotor_return;
        rmotor = rmotor_return;
        buzz = buzz_return;
        rcolor = rcolor_return;
        gcolor = gcolor_return;
        bcolor = bcolor_return;
        
        lmotor_decay = 30;
        rmotor_decay = 30;
        buzz_decay = 30;
        rcolor_decay = 30;
        gcolor_decay = 30;
        bcolor_decay = 30;
        
        light_threshold = 15;
        temp_threshold = 18;
        
    }
    
    public int add_neuron() {
           //this is a bit deceptively named.. this function is actually an ID generator
           //every time a neuron is created, it calls this function to obtain a unique ID
           //this function doesn't actually create a new instance of the neuron class
           //but it is essential for when a new instance is created
           neuron_count = neuron_count + 1;
           return neuron_count;
    }
    
    public void pack_neuron(neuron thisneuron) {
        thisneuron.x = thisneuron.getX();
        thisneuron.y = thisneuron.getY();
        thisneuron.rotation = thisneuron.getRotation();
        
    }
    
    public void load_neuron(neuron loadedneuron) {
        neuron_count = neuron_count+1;
        loadedneuron.neuron_id = neuron_count;
        loadedneuron.mybrain = (brain) this;
        loadedneuron.unpack_type();
        loadedneuron.prep_costumes(loadedneuron.size_scale);
        loadedneuron.setRotation(loadedneuron.rotation);
        loadedneuron.set_look(loadedneuron.current_look);
        addObject(loadedneuron, loadedneuron.x, loadedneuron.y); 
        
    }
    
    public void started() {
        //never seems to work to connect the Finch immediately
        //GreenFinch.get().start();
    }
    
    public void stopped() {
        //if finch was started, close it up
       if (finch_started) { GreenFinch.get().stop(); }
    }
    
    public void act() {
       //let all output states decay back towards their return state
       if (lmotor != lmotor_return) {
           lmotor = lmotor + ( (lmotor_return - lmotor) / lmotor_decay) ;
        }
        
        if (rmotor != rmotor_return) {
           rmotor = rmotor + ( (rmotor_return - rmotor) / rmotor_decay) ;
        }
        
       if (buzz != buzz_return) {
           buzz = buzz + ( (buzz_return - buzz) / buzz_decay) ;
       }
       
       if (rcolor != rcolor_return) {
           rcolor = rcolor + ( (rcolor_return - rcolor) / rcolor_decay) ;
        }

        if (bcolor != bcolor_return) {
           bcolor = bcolor + ( (bcolor_return - bcolor) / bcolor_decay) ;
        }
        
        if (gcolor != gcolor_return) {
           gcolor = gcolor + ( (gcolor_return - gcolor) / gcolor_decay) ;
        }

       //sent outputs to greenfinch
       
       if(finch_started) {        
           GreenFinch.get().setLED((rcolor > rcolor_max) ? Math.round(rcolor_max) : Math.round(rcolor), (gcolor > gcolor_max) ? Math.round(gcolor_max) : Math.round(gcolor), (bcolor > bcolor_max) ? Math.round(bcolor_max) : Math.round(bcolor));
           GreenFinch.get().setWheelSpeeds((lmotor > lmotor_max) ? Math.round(lmotor_max) : Math.round(lmotor), (rmotor > rmotor_max) ? Math.round(rmotor_max) : Math.round(rmotor));
           if (buzz > buzz_threshold) {
               GreenFinch.get().buzz(Math.round(buzz * buzz_multiplier) , Math.round(buzz_time));
            }
        }
        
       if( Greenfoot.mouseClicked(this)) {
           clickout = false;
           greenfoot.MouseInfo mouse = Greenfoot.getMouseInfo(); 
                      
           if (mouse != null) {
               //check to see if there is a mouse click outside the menu area, if so, set clickout true so the menu system can tell
               if (mouse.getX() >50 && mouse.getY() > 50) {
                   clickout = true;
                } else {
                    clickout = false;
                }
           
           //if we are in add neuron mode and there is a mouse click, add the neuron
           if (editing_mode_current == 0) {
           
               int mx = 300;
               int my = 300;
                
            
               mx = mouse.getX();  
               my = mouse.getY();  
            
               neuron myneuron = new neuron();
               addObject(myneuron, mx,my); 
            }
          }
        }
    }

}
