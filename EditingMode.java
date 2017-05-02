
import greenfoot.*;
import java.awt.*;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import java.util.*;
import edu.cmu.ri.createlab.terk.robot.finch.DefaultFinchController;
import edu.cmu.ri.createlab.terk.robot.finch.FinchController;

/**
 * Write a description of class EditingMode here.
 * 
 * @Bob Calin-Jageman
 * @1.0
 */
public class EditingMode extends Actor
{
    //this actor controls switching between modes and the menu system
    //it's a terrible set of kludges -- it needs to be re-designed to be more extensible, more transparent, and suitable for localization
    //but, here it is anyway
    
    public brain mybrain;
    public int previous_state;
    public String state_message;
    public boolean expanded = false;
    public String[] menu_items = {"Edit Network:", "(A)dd", "(D)elete", "(R)otate", "(G)row", "(S)hrink", "(M)ove", "----", "Experiment:", "e(X)cite", "i(N)hibit", "----", "Change Neurons:", "Activity (T)ype", "Activity (L)evel", "Transmitter Type (w)", "Transmitter Amount (q)", "----", "FINCH:", "(C)onnect", "(I)nputs", "(O)utputs", "Temp Threshold (-)", "Light Threshold (0)"};
    public int mouse_over;
    public int current_menu;
    public int docommand;
    
    /**
     * Act - do whatever the EditingMode wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public EditingMode(brain thisbrain) {
        mybrain = thisbrain;
        previous_state = thisbrain.editing_mode_current;
        state_message = "Initialize";
        current_menu = -1;
        
        setImage();
        
        
        
        
    }
    
    public void act() 
    {
        //if expanded means the menu has been clicked open
        if(expanded) {
            //clickout is true when the menu is expanded but the click is coming outside the menu; in that case, just close the menu
            if(mybrain.clickout) {
                mybrain.clickout = false;
                expanded = false;
                setImage();
            } else {
                //else, the menu is expanded so get the mouse info an set the current menu item the mouse is over so it can be printed differently
                //note that every item on the menu is 10 pixels tall, so simply dividing theY position by 10 gives the menu item clicked
                //this is clever, but won't scale to other resolutions well
                greenfoot.MouseInfo mi = Greenfoot.getMouseInfo();
                if (mi!=null) {
                if(mi.getX() < 240) {
                    int myx = mi.getY();
                    if(myx > 0) {
                        mouse_over = Math.round(myx/10);
                        setImage();
                    }
                }
                }
            }
        }
        
        
        if (Greenfoot.mousePressed(this)) {
            if(expanded) {
                //mouse has been clicked while menu is open, so get mouse info to set the command
                //each menu item is 10px tall, so we can determine menu item clicked by dividing Y pos by 10
                //should update for different resolutions
                greenfoot.MouseInfo mi = Greenfoot.getMouseInfo();
                if (mi != null) {
                if(mi.getX() < 240) {
                    int myx = mi.getY();
                    if(myx > 0) {
                        docommand = Math.round(myx/10);
                        if(!mybrain.finch_started && docommand > 20) {
                            docommand = 0;
                        }
                    }
                }
                }
                //now that the item is clicked, close the menu
                expanded = false;
            } else {
                docommand = 0;
                expanded = true;
            }
            //redraw the menu based on current state
            setImage();
        }
        // Add your action code here.
        
        // this series of if statements responds to the hot keys or mouse clicks to set the current editing mode
        // there could be made much more elegant-- should be made so that the editing mode could be set to the index of the menu item/key press
        //   so that the code requires just a single routine for any mode
        if (Greenfoot.isKeyDown("a") || docommand == 2) {
               mybrain.editing_mode_current = 0;
               state_message = "Add";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("d") || docommand == 3) {
               mybrain.editing_mode_current = 1;
               state_message = "Delete";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("r") || docommand == 4) {
               mybrain.editing_mode_current = 2;
               state_message = "Rotate";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("g") || docommand == 5) {
               mybrain.editing_mode_current = 3;
               state_message = "Grow";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("s") || docommand == 6) {
               mybrain.editing_mode_current = 4;
               state_message = "Shrink";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("e")) {
               mybrain.editing_mode_current = 5;
               state_message = "Edit";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("x") || docommand == 10) {
               mybrain.editing_mode_current = 6;
               state_message = "Stimulate";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("n") || docommand == 11) {
               mybrain.editing_mode_current = 15;
               state_message = "Inhibit";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("m") || docommand == 7) {
               mybrain.editing_mode_current = 7;
               state_message = "Move";
               current_menu = docommand;
               docommand = 0;
        }
        
         if (Greenfoot.isKeyDown("t") || docommand == 14) {
               mybrain.editing_mode_current = 8;
               state_message = "Type";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("w") || docommand == 16) {
               mybrain.editing_mode_current = 9;
               state_message = "Transmitter";
               current_menu = docommand;
               docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("l") || docommand == 15) {
            mybrain.editing_mode_current = 10;
            state_message = "Activity level";
            current_menu = docommand;
            docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("q") || docommand == 17) {
            mybrain.editing_mode_current = 11;
            state_message = "Transmitter amount";
            current_menu = docommand;
            docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("o") || docommand == 22) {
            mybrain.editing_mode_current = 12;
            state_message = "Select outputs";
            current_menu = docommand;
            docommand = 0;
        }
        
        if (Greenfoot.isKeyDown("i") || docommand == 21) {
            mybrain.editing_mode_current = 13;
            state_message = "Select inputs";
            current_menu = docommand;
            docommand = 0;
        }
        
        if ( (Greenfoot.isKeyDown("c") || docommand == 20) && !mybrain.finch_started) {
            current_menu = docommand;
            docommand = 0;
            mybrain.finch_exists = GreenFinch.get().safestart();
            if (mybrain.finch_exists) {
                GreenFinch.get().start();
                mybrain.finch_started = true;
            }
            setImage();
            
        }
        
        if ( (Greenfoot.isKeyDown("0") || docommand == 24) && mybrain.finch_started) {
                //do the light-threshold setup for the finch
                //this option doesn't need an additional mouse-click; the dialog pops up when this mode is entered
                current_menu = docommand;
                docommand = 0;
                Object[] possibilities = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
                String current = possibilities[Math.round(mybrain.light_threshold)-1].toString();
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select light threshold (current = "+ String.valueOf(mybrain.light_threshold) +") :","Light threshold:", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) &&
                (response.length() > 0)) {
                   mybrain.light_threshold = Float.parseFloat(response);
                    
                 }    
            
        }

        if ( (Greenfoot.isKeyDown("-") || docommand == 23) && mybrain.finch_started) {
                //do the temperature threshold dialog for the finch
                //bring it up immediately, no need for an additional keypress
                    current_menu = docommand;
                    docommand = 0;
                    java.util.List<String> plist = new ArrayList<String>();
                     
                    for (int i = 1900; i < 3000; i = i + 5) {
                        float ifloat = i/100.0f;
                         plist.add(String.valueOf(ifloat));
                        }
                    
                Object[] possibilities = plist.toArray();
                String current = String.valueOf(mybrain.temp_threshold);
 
                String response = (String)JOptionPane.showInputDialog(new JInternalFrame(),"Select temperature threshold (current = "+ String.valueOf(mybrain.temp_threshold) +") :","Temperature threshold:", JOptionPane.PLAIN_MESSAGE,null,possibilities,current);
                
                if ((response != null) &&
                (response.length() > 0)) {
                   mybrain.temp_threshold = Float.parseFloat(response);
                    
                 }    
            
        }

        
        if(previous_state != mybrain.editing_mode_current) {
            //editing mode has changed, so re-draw the menu
            previous_state = mybrain.editing_mode_current;
            setImage();
        }
    }    
    
    public void setImage() {
        //this routine draws the menu system
        
        if(expanded) {
            //menu has been opened, so print all the possible commands
            //  set colors based on command status
            GreenfootImage image;
            image = new GreenfootImage(200,240);
            
            float fontSize = 12.0f;
            greenfoot.Font font = image.getFont();
            font = font.deriveFont(fontSize);
            image.setFont(font);
            
            
            int mystart = 10;
            int mycol = 75;
            int currentitem = 1;
            boolean passedfinch = false;
            
            for (String mystring : menu_items) {
                image.setColor(new greenfoot.Color(0,0,0) );
                if (mystring.contains(":")) {
                    //this is an organizing item
                    image.setColor(new greenfoot.Color(0,0,255) );
                }
                if (mystring.contains("FINCH")) {
                    //we are into the finch menu
                    passedfinch = true;
                }
                if (current_menu == currentitem) {
                    //make the current mode a different color
                    image.setColor(new greenfoot.Color(255,0,0) );
                }
                if (mouse_over == currentitem) {
                    //make the mode with the mouse over a different color
                    image.setColor(new greenfoot.Color(200,0,200) );
                }
                if (passedfinch && !mybrain.finch_started && !mystring.contains("onnect")) {
                    //make the finch options greyed out if finch not connected, except the connect command
                    //terrible kludge!
                    image.setColor(new greenfoot.Color(200,200,200) );
                }
                image.drawString(mystring, mycol, mystart);
                mystart = mystart + 10;
                currentitem = currentitem + 1;
            }
            setImage(image);
            setLocation(31,127);
            //setImage(costumes[0]);
        } else {
            //menu has not been clicked open so just print the current mode and finch status
            GreenfootImage image;
            image = new GreenfootImage(100,50);
            float fontSize = 12.0f; //change this to the font size that you want  
            greenfoot.Font font = image.getFont();  
            font = font.deriveFont(fontSize);  
            image.setFont(font);  
            image.drawString(state_message, 25, 25);
        
            String finch_status = "No Finch";
            if (mybrain.finch_exists) {finch_status = "Plugged";}
            if (mybrain.finch_started) {finch_status = "Finch!";}
            image.drawString(finch_status, 25,35);
            setImage(image);
            setLocation(30,15);
        }
        
   }

    

}


