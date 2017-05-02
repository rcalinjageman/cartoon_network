import greenfoot.*;

/**
 * Write a description of class Finch_output here.
 * 
 * @Bob Calin-Jageman
 * @1.0
 */
public class Finch_output extends Actor
{
    public int output_type;
    public int output_current;
    public int output_decay;
    public int output_return;
    
    public Finch_output(int selected_type) {
        output_type = selected_type;
        
        output_decay = 30;
        output_return = 0;
        output_current = 0;
        
    }
    
    /**
     * Act - do whatever the Finch_output wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (output_current != output_return) {
            output_current = output_current + ((output_return - output_current) / output_decay);
        }
        
        //left motor
        if (output_type == 1) {
            
        }

        
        //right motor
        if (output_type == 2) {
            
        }
        
        //blue light
        if (output_type == 3) {
            
        }
        
        //red light
        if (output_type == 4) {
            
        }
        
        //green light
        if (output_type == 5) {
            
        }
        
        //buzz
        if (output_type == 6) {
            
        }
        
        // Add your action code here.
    }    
}
