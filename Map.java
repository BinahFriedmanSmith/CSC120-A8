//ok i have an Idea so if i have time i'll do it if not this is a placeholder
public class Map{

    private static final int width = 3;
    private static final int height = 3;
    private Room[] rooms;

    /**  
     * Constructor. creates a grid of rooms and connects them to eacj other
    */
    public Map(){
        rooms = new Room[width * height];
        for (int i = 0; i < rooms.length; i++){
            rooms[i] = new Room();
        }
        for (int i = 0; i < rooms.length; i++){
            if (getX(i) < width-1){
                rooms[i].east = rooms[i+1];
            }
            if (getX(i) > 0){
                rooms[i].west = rooms[i-1];
            }
            if (getY(i) < height-1){
                rooms[i].south = rooms[to1D(getX(i) , getY(i) + 1)];
            }
            if (getY(i) > 0){
                rooms[i].north = rooms[to1D(getX(i) , getY(i) - 1)];
            }
        }
    }

    /**
     * @return whether coordinate is in bounds horizontally
     */
    public boolean inXBounds(int x){
        return (x>= 0 && x < width);
    }

     /**
     * @return whether coordinate is in bounds vertically
     */
    public static boolean inYBounds(int y){
        return (y>= 0 && y < height);
    }

    /**
     * @param pos position of desired room
     * @return room at positon pos
     */
    public Room getRoom(int pos){
        return rooms[pos];
    }

    /** 
     * converts 2d coordinates to position in 1d array
     * @param x 2d x ccordinate
     * @param y 2d y ccordinate
     * @return position in 1d array
    */
    public static int to1D(int x, int y)
    {
        return y*width + x;
    }

    /** 
     * converts  position in 1d array to 2d x coordinate
     * @param pos position in 1d array
     * @return 2d x ccordinate
    */
    public static int getX(int pos)
    {
        return pos % width;
    }

    /** 
     * converts position in 1d array to 2d y coordinate
     * @param pos position in 1d array
     * @return 2d y ccordinate
    */
    public static int getY(int pos)
    {
        return pos / width;
    }

    public static void main(String[] args){
        Map myMap = new Map();
        String orb = "Orb";
        myMap.getRoom(4).addItem(orb);
        myMap.getRoom(5).addItem("Wand");
        myMap.getRoom(4).addItem("Staff");
        myMap.getRoom(3).addItem("Scroll");
        myMap.getRoom(3).addItem("Hat");
        myMap.getRoom(3).addItem("Robe");
        Wizard pesto = new Wizard(myMap.getRoom(4), myMap);
        pesto.look();
        pesto.grab(orb);
        pesto.grab("Staff");
        pesto.checkInventory();
        pesto.look();
        pesto.use(orb);
        pesto.undo();
        pesto.fly(2,1);
        pesto.walk("east");
        pesto.walk("east");
        pesto.look();
        pesto.grab("Wand");
        pesto.grab("Scroll");
        pesto.walk("west");
        pesto.walk("away");
        pesto.walk("west");
        pesto.grab("Scroll");
        pesto.grab("Hat");
        pesto.grab("Robe");
        pesto.checkInventory();
        pesto.use("Robe");
        pesto.use("Scroll");
        pesto.grab("Robe");
        pesto.use("Robe");
        pesto.look();
        pesto.examine("Hat");
        pesto.drop("Hat");
        pesto.examine("Hat");
        pesto.shrink();
        pesto.examine(orb);
        pesto.undo();
        pesto.examine("Hat");
        pesto.grow();
        pesto.examine("Hat");
        pesto.undo();        
        pesto.rest();  
        pesto.grow();
    }
}