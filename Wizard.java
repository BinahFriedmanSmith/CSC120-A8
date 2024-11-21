
import java.util.ArrayList;


public class Wizard implements Contract{

    

    private final ArrayList<String> inventory; //keeps track of held items
    private Number size; //(2: big, 1: normal, 0: small)
    private int spellslots; //# of times the wizard can perform magic. replenish by resting. consistent across time travel

    private Room location; //room the wizard is in
    @SuppressWarnings("unused")
    private final GameMap map; //map the rooms are in

    //these are all for the undo method
    private String prevAction;
    private String prevActionString;
    private int prevActionInt;

    /**
     * Constructor
     * @param startingRoom the room the wizard will be in when they are created
     * @param map environment the wizard is in
     */
    public Wizard(Room startingRoom, GameMap map ){
        inventory = new ArrayList<>();
        size = 1;
        spellslots = 5;
        location = startingRoom;
        this.map = map;
    }

    
    /**
     * Adds an item to the wizard's inventory, if the item is present, the wizard has inventtory space, and they are not already holding it.
     * @param item the item to add
     */
    @Override
    public void grab(String item){
        if (inventory.contains(item)){
            System.out.println("You are already holding the " + item + ".");
        }
        else if (!location.itemHere(item)){
            System.out.println("You don't see that here.");
        }     
        else if (inventory.size() > 4){
            System.out.println("You cannot carry anything else! Try dropping something first.");
        }
        else {
            System.out.println("You pick up the " + item + ".");
            inventory.add(item);
            location.removeItem(item);
        }
        prevAction = "grab";
        prevActionString = item;
    }

    
    /**
     * Removes an item from the wizard's inventory, if they are holding it.
     * @param item the item to remove
     * @return the removed item
     */
    @Override
    public String drop(String item){
        if (!inventory.contains(item)){
            System.out.println("You are not holding the " + item + ".");
            return null;
        }     
        else {
            System.out.println("You drop the " + item + ".");
            inventory.remove(item);
            location.addItem(item);
            prevAction = "drop";
            prevActionString = item;
            return item;
        }        
       
    }

    /**
     * Prints a string that changes based on inputted item and gamestate
     * @param item the item to examine
     */
    @Override
    public void examine(String item){
        if (location.itemHere(item) || inventory.contains(item)){
            System.out.print("It's a(n) " + item + ". ");          
            if (inventory.contains(item)){
                System.out.print("You are holding it. ");
            }
            if (this.size.equals(0))  {
                System.out.print("It seems larger than usual. ");
            }
            if (this.size.equals(2))  {
                System.out.print("It seems smaller than usual. ");
            }
            System.out.println();
        }
        else {
            System.out.println("You don't see that here.");
        }
    }

    /**
     * Uses an item, destroying it. Items can only be used if they are being held.
     * @param item the item to use
     */
    @Override
    public void use(String item){
        if (!inventory.contains(item)){
            System.out.println("You are not holding the " + item + ".");         
        }     
        else {
            System.out.println("You use the " + item + ".");
            inventory.remove(item);
        }        
        prevAction = "use";
        prevActionString = item;
    }

    /**
     * prints the wizard's current inventory
     */
    public void checkInventory(){
        System.out.println("You are carrying: " + inventory);
    }

    public void look(){
        System.out.println(location);
    }

    private void setLocation(Room room){
        location = room;
    } 

    /**
     * Attempts to move to a room in the specified direction (north, south, east, or west)
     * @param direction the direction to move
     * @return whether the action was sucessful
     */
    @Override
    public boolean walk(String direction){
        if (location.getDirection(direction) == null){
            System.out.println("You can't go that way! (" + direction + ")");
            return false;
        }
        else{
            System.out.println("You walk "+ direction + ".");
            prevAction = "walk";
            prevActionString = direction;
            this.setLocation(location.getDirection(direction));            
            return true;
        }      
    }

    /**
     * Attempts to move to a room at the specified coordinates
     * @param x the x-coordinate of the destination room
     * @param y the y-coordinate of the destination room
     * @return whether the action was sucessful
     */
    @Override
    public boolean fly(int x, int y){
        if (spellslots < 1){
            System.out.println("You are spent! You cannot cast any more spells until you rest.");
            return false;
        }    
        //*
        //NOTE: The code flat-out refuses to call most methods from Map. I fear i may be missing something obvious but i cannot figure it out and so i am giving up before i throw my computer clean across the room.
        
        if (!GameMap.inXBounds(x) || !GameMap.inYBounds(y)){
            System.out.println("Cannot fly out of bounds");
            return false;
        } 
        prevAction = "fly";
        prevActionString = "" + x;
        prevActionInt = y;
        System.out.println("You fly to the room at " + x + "," + y);
        location = map.getRoom(GameMap.to1D(x,y));
        spellslots -= 1;
        return true;

    }

    /**
     * Attempts to cast a shrink spell on the wizard
     * @return resulting size (2: big, 1: normal, 0: small)
     */
    @Override
    public Number shrink(){
        if (spellslots < 1){
            System.out.println("You are spent! You cannot cast any more spells until you rest.");
            return size;
        }
        if (this.size.equals(0)){
            System.out.println("You cannot shrink any smaller!");
            return size;           
        }
        if (this.size.equals(1)){
            System.out.println("You shrink to half your size.");
            this.size = (Number)0;            
        }
        if (this.size.equals(2)){
            System.out.println("You shrink down to normal size.");
            this.size = (Number)1;            
        }
        spellslots -= 1;
        prevAction = "shrink";
        return size;
    }

    /**
     * Attempts to cast a growth spell on the wizard
     * @return resulting size (2: big, 1: normal, 0: small)
     */
    @Override
    public Number grow(){
        if (spellslots < 1){
            System.out.println("You are spent! You cannot cast any more spells until you rest.");
            return size;
        }
        if (this.size.equals(2)){
            System.out.println("You cannot grow any larger!");
            return size;
        }
        if (this.size.equals(1)){
            System.out.println("You grow to twice your size.");
            this.size = (Number)2;
        }
        if (this.size.equals(0)){
            System.out.println("You grow to normal size.");
            this.size = (Number)1;
        }
        spellslots -= 1;
        prevAction = "grow";
        return size;
    }

    /**
     * prints a message and recovers spell slots
     */
    @Override
    public void rest(){
        System.out.println("You sleep a while. You feel well-rested.");
        prevAction = "rest";
        prevActionInt = spellslots;
        spellslots = 5;
    }


    /**
     * undoes the last successful action (with the exception of purely information-gathering ones, such as look() and examine() )
     */
    @Override
    public void undo(){
        if (spellslots < 1){
            System.out.println("You are spent! You cannot cast any more spells until you rest.");
            return;
        }
        if (prevAction.equals("undo")){
            System.out.println("You can't reverse time twice in a row! The results would be disastrous.");
            return;
        }
        System.out.println("Time twists backwards.");

        switch (prevAction) { //this WAS a bunch of if elses but VScode started yelling at me so I let it do what it wanted.
            case "rest" -> {
                System.out.println("You suddenly feel tired again.");
                this.spellslots = prevActionInt;
            }
            case "shrink" -> {
                spellslots += 1; //cancels out the spell slot this spell wants to use, since the time travel is the actual spell
                //System.out.println("reversing shrink");
                this.grow();
                //System.out.println(" shrink reversed");
            }
            case "grow" -> {
                spellslots += 1; //cancels out the spell slot this spell wants to use, since the time travel is the actual spell
                //System.out.println("reversing grow");
                this.shrink();
                //System.out.println(" grow reversed");
            }
            case "drop" -> this.grab(prevActionString);
            case "grab" -> this.drop(prevActionString);
            case "use" -> inventory.add(prevActionString);
            case "fly" -> {
                spellslots += 1; //cancels out the spell slot this spell wants to use, since the time travel is the actual spell
                this.fly(Integer.parseInt(prevActionString), prevActionInt);
            }
            case "walk" -> {
            switch (prevActionString) { //this WAS a bunch of if elses but VScode started yelling at me so I let it do what it wanted.
                case "north" -> this.walk("south");
                case "south" -> this.walk("north");
                case "east" -> this.walk("west");
                case "west" -> this.walk("east");
                default -> {
                }
            }
            }
            default -> {
            }
        }
        spellslots -= 1;
    }
    
}
