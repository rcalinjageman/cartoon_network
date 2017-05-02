import greenfoot.*;
import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 * Write a description of class neuron here.
 * 
 * @Bob Calin-Jageman
 * @1.0
 */
public class neuron extends Actor
{
    //basic neuron parameters...
    public int neuron_type;   // 1 silent, 2 spontaneous, 3 bursting
    public String output_type;    // "None", "Left forward", "Left backward", "Right forward", "Right backward", "Buzz", "Red", "Blue", "Green"
    public String input_type;  //"None", "Left hit", "Right hit", "Left light", "Right light", "Temperature"
    public int activity_level;
    public int epsp_size;    
    
    public float Vm;
    public float threshold;
    public float threshold_max;
    public float threshold_min;
    public float threshold_tc;
    public float Vm_tc;
    public float Vm_rest;
    
    public int current_look;
    public String[] looks = {"neuron_rest.png", "neuron_fire1.png", "neuron_fire2.png", "neuron_fire3.png", "neuron_fire4.png", "neuron_fire5.png", "neuron_fire6.png"};
    public GreenfootImage[] costumes;
    private boolean isGrabbed;
    
    private float size_scale;
    
    public int ntpool_size;
    public int ntpool;
    public float ntpool_frelease;
    
    public boolean firing;
    public double firing_last_change;
    public double actions;
    
    public int neuron_id;
    public brain mybrain;
    
   
    
    public neuron() {
        //should make size scale exposable to enable up/down scaling of neuron sizes
        size_scale = 1;
        prep_costumes(size_scale);
        this.set_look(current_look);
        
        //initialize basic params
        activity_level = 5;
        neuron_type = 1;
        input_type = "None";
        output_type = "None";
        epsp_size = 1;
        
        Vm_rest = -50;
        Vm = Vm_rest;
        Vm_tc = 20;
        threshold_max = 100;
        threshold_min = -30;
        threshold = threshold_min;
        threshold_tc = 20;
        
        ntpool_size = 10;
        ntpool = ntpool_size;
        
        firing = false;
        firing_last_change = 0;
        actions = 0;
        
        isGrabbed = false;
        
    }
  
    protected void addedToWorld(World myworld) {
        //save the creating world, get an id from the world
        mybrain = (brain) myworld;
        neuron_id = mybrain.add_neuron();
    }
    
    /**
     * Act - do whatever the neuron wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * 
     * 
     * 
     */
    
    public void act() 
    {
        
        //deal with movement of neuron in movement mode
        if(mybrain.editing_mode_current == 7 && Greenfoot.mousePressed(this) && !isGrabbed) {
            // grab the object
            isGrabbed = true;
            // the rest of this block will avoid this object being dragged UNDER other objects
            mybrain.removeObject(this);
            greenfoot.MouseInfo mi = Greenfoot.getMouseInfo();
            mybrain.addObject(this, mi.getX(), mi.getY());
            return;
        }
        
        //continue following the mouse during drag event, regardless of edit more
        if ((Greenfoot.mouseDragged(this)) && isGrabbed)
        {
            // follow the mouse
            greenfoot.MouseInfo mi = Greenfoot.getMouseInfo();
            setLocation(mi.getX(), mi.getY());
            return;
        }

        //finish drag event
        if (Greenfoot.mouseDragEnded(this) && isGrabbed)
            {
                // release the object
                isGrabbed = false;
                return;
            }
        
        //deal with mouse clicks based on editing mode
        //passing the editing mode as an integer isn't very informative or easy to modify;
        //should come up with a better solution
        if (Greenfoot.mouseClicked(this)) {
            //delete
            if(mybrain.editing_mode_current == 1) {
                mybrain.removeObject(this);
            }
            
            //rotate
            if(mybrain.editing_mode_current == 2) {
                this.turn(-20);
            }
            
            //grow
            if(mybrain.editing_mode_current == 3) {
                size_scale = size_scale * (float) 1.1;
                prep_costumes(size_scale);
                
            }
            
            //shrink
            if(mybrain.editing_mode_current == 4) {
                size_scale = size_scale * (float) .9;
                prep_costumes(size_scale);
                
            }
                        
            //stimulate
            if(mybrain.editing_mode_current == 6) {
             this.stim(25);
            }
            
            //inhibit
            if(mybrain.editing_mode_current == 15) {
             this.stim(-25);
            }
            
            //edit type
            if(mybrain.editing_mode_current == 8) {
                Object[] possibilities = {"Silent",
                    "Spontaneously active",
                    "Bursting"};
                
                String current = possibilities[neuron_type-1].toString();                    
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select neuron type:","Neuron type", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) && (response.length() > 0)) {
                
                    if (response == "Silent") {
                        neuron_type=1;
                    }
                    if (response == "Spontaneously active") {
                        neuron_type=2;
                    }
                    if (response == "Bursting") {
                        neuron_type =3;
                    }
                    set_neuron_type(neuron_type);
                }    
            }
            
            //edit transmitter
            if(mybrain.editing_mode_current == 9) {
                Object[] possibilities = {"Glutamate", "GABA"};
                String current = "";
                
                if (epsp_size > 0) {
                    current = "Glutamate";
                } else {
                    current = "GABA";
                }
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select transmitter:","Transmitter type", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) && (response.length() > 0)) {
                
                    if (response == "Glutamate") {
                        epsp_size = 3;
                    }
                    if (response == "GABA") {
                        epsp_size = -3;
                    }               
                }    
            }

            //edit activity level
            if(mybrain.editing_mode_current == 10) {
                Object[] possibilities = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
                String current = possibilities[activity_level-1].toString();
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select activity level:","Activity level", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) && (response.length() > 0)) {
                    activity_level = Integer.parseInt(response);
                    set_neuron_type(neuron_type);
                }    
            }
            
            
            if (mybrain.editing_mode_current == 11) {
                Object[] possibilities = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40"};
                String current = possibilities[ntpool_size-1].toString();
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select transmitter amount:","Transmitter amount:", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) &&
                (response.length() > 0)) {
                    ntpool_size = Integer.parseInt(response);
                    ntpool = ntpool_size;
                 }    
            }
            
            //set outputs
            if (mybrain.editing_mode_current == 12) {
                Object[] possibilities = {"None", "Left forward", "Left backward", "Right forward", "Right backward", "Buzz", "Red", "Blue", "Green"};
                String current = output_type;
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select outputs:","Outputs:", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) && (response.length() > 0)) {
                    output_type = response.toString();
                 }    
            }
            
            
            //set inputs
            if (mybrain.editing_mode_current == 13) {
                Object[] possibilities = {"None", "Left hit", "Right hit", "Left light", "Right light", "Temperature"};
                String current = input_type;
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select inputs:","Inputs:", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) && (response.length() > 0)) {
                    input_type = response.toString();
                 }    
            }
                                     
        }

        //track number of action updates
        //this is used for synaptic release delay and costume changed during firing;
        //probably should have a more elegant/modifiable approach
        actions = actions + 1;
        
        //check to see if over threshold
        if (Vm > threshold) {
            this.fire();
        }

        //read inputs, if set
        if (input_type != "None" && mybrain.finch_started) {
            if (input_type == "Left hit" && GreenFinch.get().isLeftHit() ) {
                this.stim(25);
            }
        
            if (input_type == "Right hit" && GreenFinch.get().isRightHit()) {
               this.stim(25);
            }
            
            if (input_type == "Left light") {
                float llight = (float)GreenFinch.get().getLeftLightLevel();
                if (llight > mybrain.light_threshold) {
                    this.stim(llight/4);
                 }
            }
            
            if (input_type == "Right light") {
                float rlight = (float) GreenFinch.get().getRightLightLevel();
                if (rlight > mybrain.light_threshold) {
                    this.stim(rlight/ 4);
            }
            }

            if (input_type == "Temperature") {
                float tempp = (float) GreenFinch.get().getTemperature();
                if (tempp > mybrain.temp_threshold) {
                    this.stim((float) (tempp-mybrain.temp_threshold) * 4);
                }
            }
        }
        
        //if not at rest, decay back towards rest
         if (Vm != Vm_rest) {
            Vm = Vm + ((Vm_rest - Vm) / Vm_tc);
        }
        
        //if threshold has been reset after AP, decay back to normal threshold
         if (threshold != threshold_min) {
             threshold = threshold + ((threshold_min - threshold)/ threshold_tc);
            }
        
        //handle synaptic delay after AP--cycle through costumes and then release NT            
        if (firing) {
            if ( (firing_last_change + 20) > actions) {
                current_look = current_look + 1;
                //probably shouldn't have the total number of costumes hard-coded
                if (current_look == 7) {
                    this.release();
                    current_look = 0;
                    firing = false;
                    firing_last_change = 0;
                }
                this.set_look(current_look);
            }
        }
        
        //update current look -- allows display of current Vm and threshold, but could be
        //made lighter by avoiding this and only setting look for editing events that require
        //a redraw
        this.set_look(current_look);
        
    }
    
    public void stim(float psp) {
        //accept stimulation from NT or mouse click
        //currently, just a step function..could implement a driving force...
        Vm = Vm + psp;
        
               
    }
    
    public void activate_outputs(float psp) {
           //if outputs defined, update these in brain
        //"None", "Left forward", "Left backward", "Right forward", "Right backward", "Buzz", "Red", "Blue", "Green"
        //this is really clunky...probably best done using pointers to the brain class or some other, more elegant solution
        
        if (output_type == "Left forward") {
            mybrain.lmotor = mybrain.lmotor +  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Left backward") {
            mybrain.lmotor = mybrain.lmotor -  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Right forward") {
            mybrain.rmotor = mybrain.rmotor +  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Right backward") {
            mybrain.rmotor = mybrain.rmotor - Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Buzz") {
            mybrain.buzz = mybrain.buzz +  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Red") {
            mybrain.rcolor = mybrain.rcolor +  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Blue") {
            mybrain.bcolor = mybrain.bcolor +  Math.abs(psp * mybrain.output_multiplier);
        }
        
                if (output_type == "Green") {
            mybrain.gcolor = mybrain.gcolor + Math.abs(psp * mybrain.output_multiplier);
        }    
    }
    
    public void fire() {
        //Vm is over threshold, fire ap:  reset threshold, mark firing as true, record action count
        threshold = threshold_max;
        firing = true;
        firing_last_change = actions;
    }
    
    public void set_look(int look) {
        //set current look for neuron, incoporating Vm and Threshold reporters
        //could use a flag to determine if labels are on/off to make this lighter
        setImage(costumes[look]);
       
        GreenfootImage image;
        image = new GreenfootImage(getImage());
        
        if (epsp_size < 0) {
            greenfoot.Color mycolor = image.getColor();
            image.setColor(greenfoot.Color.RED);
            image.fillOval(25,40,10,10);
            image.setColor(mycolor);
        }
        
        float fontSize = 8.0f; //change this to the font size that you want  
        greenfoot.Font font = image.getFont();  
        font = font.deriveFont(fontSize);  
        image.setFont(font);  
        DecimalFormat df = new DecimalFormat("#");
        image.drawString("id=" + neuron_id, 65, 15);
        
        if (output_type == "None" & input_type == "None") {
            image.drawString("Vm=" + df.format(Vm), 65, 25);
            image.drawString("Th=" + df.format(threshold), 65, 35);
        }
        
        fontSize = 12.0f; //change this to the font size that you want  
        font = image.getFont();  
        font = font.deriveFont(fontSize);  
        image.setFont(font);  
        //"None", "Left forward", "Left backward", "Right forward", "Right backward", "Buzz", "Red", "Blue", "Green"
        if (output_type == "Left forward" || output_type == "Left backward") {
            image.drawString("Lmotor= " + df.format(mybrain.lmotor), 45, 25);
        }
        if (output_type == "Right forward" || output_type == "Right backward") {
            image.drawString("Rmotor= " + df.format(mybrain.rmotor), 45, 25);
        }
        if (output_type == "Buzz") {
            image.drawString("Buzz= " + df.format(mybrain.buzz), 45, 25);
        }
        if (output_type == "Red") {
            image.drawString("Red= " + df.format(mybrain.rcolor), 45, 25);
        }
        if (output_type == "Green") {
            image.drawString("Green= " + df.format(mybrain.gcolor), 45, 25);
        }
        if (output_type == "Blue") {
            image.drawString("Blue= " + df.format(mybrain.bcolor), 45, 25);
        }

                   //"None", "Left hit", "Right hit", "Left light", "Right light", "Temperature"
 
        if(input_type != "None")
   //     if (mybrain.finch_started) {
            if (input_type == "Left hit") {
                image.drawString("LHit= " + String.valueOf(GreenFinch.get().isLeftHit()), 45, 25);
            }
        
            if (input_type == "Right hit") {
                image.drawString("RHit= " + String.valueOf(GreenFinch.get().isRightHit()), 45, 25);
            }
            
            if (input_type == "Left light") {
                image.drawString("LLight= " + String.valueOf(GreenFinch.get().getLeftLightLevel()), 45, 25);
            }
            
            if (input_type == "Right light") {
                image.drawString("RLight= " + String.valueOf(GreenFinch.get().getRightLightLevel()), 45, 25);
            }

            if (input_type == "Temperature") {
                image.drawString("Temp= " + String.valueOf(GreenFinch.get().getTemperature()), 45, 25);
            }


   //     }
        
        setImage(image);
        
    }
    
    public void reuptake() {
        //called by NT when NT molecule is degraded, refills pool of nt
        ntpool = ntpool+ 1;
        if (ntpool > ntpool_size) {
            ntpool = ntpool_size;
            //shouldn't every happen, but this just double-checks that reuptake hasn't exceeded pool size
        }
    }
    
    public void release() {
         //release NT pool...currently used fraction release = 1, but could be made
         //more sophisticated to implement STP dynamics
         for (int x = 0; x < ntpool; x++) {
                        NT myNT = new NT(this, epsp_size);
                        World world=getWorld();
                        
                        //figure out where to spit out nt
                        //subtracted 10 degrees from angle to compensate for slight twist in neuron image
                        double ntx = this.getX();
                        double nty = this.getY();
                        GreenfootImage image = getImage();
                        ntx = ntx + ( (image.getWidth()/2) * Math.cos(Math.toRadians(this.getRotation()-10) ) );  
                        nty = nty + ( (image.getWidth()/2) * Math.sin(Math.toRadians(this.getRotation()-10) ) );
                        
                        world.addObject(myNT, (int) ntx, (int) nty);
                        activate_outputs(epsp_size);
                        ntpool = ntpool - 1;
         }
                    
    }
    
   private void prep_costumes(float scale) {
       //preps collection of costumers and scales them in terms of width
       costumes = new GreenfootImage[7];
                        
        for (int x = 0; x < 7; x++) {
            setImage(this.looks[x]);
            costumes[x] = getImage();
            costumes[x].scale(Math.round(150*scale), 69);
        }
    }
    
    public void set_neuron_type(int newtype) {
        //set the activity level for the neuron
        //silent neurons have a rest of -50, which is below threshold
        //spontaneously active have Vrest over threshold
        //doesn't seem like bursting is actually implemented yet?
        if (newtype == 1) {
            Vm_rest = -50;
            Vm = Vm_rest;
            return;
        }
        if (newtype == 2) {
            Vm_rest = threshold_min+1;
            Vm = Vm_rest;
            threshold_tc = 5 + ((10 - activity_level) *10);
            return;
        }
        if (newtype == 3) {
            Vm_rest = threshold_min+1;
            Vm = Vm_rest;
            threshold_tc = 5 + ((10 - activity_level) *10);
            return;
        }
    }
}