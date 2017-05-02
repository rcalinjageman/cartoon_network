import greenfoot.*;
import java.util.List;
import java.util.Iterator;

/**
 * Write a description of class NT here.
 * 
 * @Bob Calin-Jageman
 * @1.0
 */
public class NT extends Actor
{
    //basic parameters for an NT molecule, especially the emitting neuron (author)
    public int decay;
    public neuron author;
    public int strength;
    
    public NT(neuron calling_neuron, int calling_strength) {
        //should probably make decay rate a parameter available for change rather than hard coded
        decay = 10;
        author = calling_neuron;
        strength = calling_strength;
        if (strength < 0) {
            setImage("gaba.png");
        } else {
            setImage("glutamate.png");
        }
    }
    
    /**
     * Act - do whatever the NT wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        
        //check to see if we've bumped into any neurons other than the emitting (author) neuron
        //if so, stimulate the neuron with that NT's strength
        List<neuron> binding = getIntersectingObjects(neuron.class);
        for (int i = 0; i < binding.size(); i++) {
            if (binding.get(i) != author) {
                binding.get(i).stim(strength);
            }
        }
        
        //check to see if we've decayed, otherwise brownian motion
        if (Greenfoot.getRandomNumber(100) < decay) {
            World myworld;
            myworld = getWorld();
            myworld.removeObject(this);
            author.reuptake();
            return;
        } else {
            turn(Greenfoot.getRandomNumber(360));
            move(Greenfoot.getRandomNumber(5));
        }
        
    }    
    
    
 
}
