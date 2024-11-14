//ok i have an Idea so if i have time i'll do it if not this is a placeholder
import java.util.ArrayList;


public class Room {

    //room description
    public String description = "This is a room.";

    //adjacent rooms
    public Room north = null;
    public Room south = null;
    public Room east = null;
    public Room west = null;

    private final ArrayList<String> contents;

    /**
     * constructor
     */
    public Room(){
        contents = new ArrayList<>();
    }

    /**
     * Adds an item to the room, if it is not already there.
     * @param item the item to remove
     */
    public void addItem(String item){
        if (contents.contains(item)){
            throw new RuntimeException("" + item + " is already in this room.");
        }     
        else {
            contents.add(item);
        }
    }
    
    /**
     * Removes an item from the room, if it is present
     * @param item the item to remove
     * @return the removed item
     */
    public String removeItem(String item){
        if (!contents.contains(item)){
            throw new RuntimeException("" + item + " is not in this room.");
        }     
        else {
            contents.remove(item);
            return item;
        }        
    }

     /**
     * checks if an item is present in the room
     * @param item the item to check
     * @return whether the item is present
     */
    public boolean itemHere(String item){
        return contents.contains(item);
    }

    /**
     * returns the room located in the given direction
     * @param direction the direction to check
     * @return the room in that direction, or null if there is no room there/the direction is invalid
     */
    public Room getDirection(String direction){
        if (direction.equals("north")){
            return north;
        }
        if (direction.equals("south")){
            return south;
        }
        if (direction.equals("east")){
            return east;
        }
        if (direction.equals("west")){
            return west;
        }
        return null;
    }

    /**
     * tostring override
     * @return the room object's individual description & any items present
     */
    @Override
    public String toString(){
        return description + "\nYou can see: " + contents;
    }
}
