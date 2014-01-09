package cs.bris.cloud.client;

import java.util.Arrays;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

public class PlayerGrid {
    public enum Tile {
        CLEAR, MISS, HIT, SHIP, TARGET
     }
    private int startX,
        startY,
        rowN,
        colN,
        cellSize,
        shipN = 0;
    private int[] selectedTarget = new int[] { -1, -1 };
    private int[][] ships = new int[17][2];
    private Tile[][] grid;
    final private ImageElement
        imageClear,
        imageMiss,
        imageHit,
        imageShip,
        imageTarget;
    
    PlayerGrid(int startX, int startY, int rowN, int colN, int cellSize,
            String[] urlImage) {
        this.startX = startX;
        this.startY = startY;
        this.rowN = rowN;
        this.colN = colN;
        this.cellSize = cellSize;


        imageClear = imageConvert(urlImage[0]);
        imageMiss = imageConvert(urlImage[1]);
        imageHit = imageConvert(urlImage[2]);
        imageShip = imageConvert(urlImage[3]);
        imageTarget = imageConvert(urlImage[4]);
        
        grid = new Tile[rowN][colN];
        for(Tile[] row: grid)
            Arrays.fill(row, Tile.CLEAR);
    }
    
    //redraws grid
    public void drawGrid(Context2d context) {
        for(int i = 0; i < rowN; i++) {
            for(int j = 0; j < colN; j++) {
                switch(grid[i][j]) {
                case MISS:
                    context.drawImage(imageMiss, startX + i*cellSize, startY + j*cellSize, cellSize, cellSize);
                    break;
                case HIT:
                    context.drawImage(imageHit, startX + i*cellSize, startY + j*cellSize, cellSize, cellSize);
                    break;
                case SHIP:
                    context.drawImage(imageShip, startX + i*cellSize, startY + j*cellSize, cellSize, cellSize);
                    break;
                    default:
                        context.drawImage(imageClear, startX + i*cellSize, startY + j*cellSize, cellSize, cellSize);
                }
            }
            
            if(selectedTarget[0] != -1 )
                context.drawImage(imageTarget, startX + selectedTarget[0]*cellSize, startY + selectedTarget[1]*cellSize, cellSize, cellSize);
        }
        
        
        
    }
    
    public int[][] getShipLocations() {
        return ships;
    }
    
    
    public void updateUI(Boolean hit, int[] position) {
        if(hit)
            grid[position[0]][position[1]] = Tile.HIT;
        else
            grid[position[0]][position[1]] = Tile.MISS;
    }
    
    
    //converts pixel coordinates of canvas to grid
    private int coordToCell(int n) {
        n = n / cellSize;
        if (n > 9 || n < 0) n = 0;
        return n;
    }
    
    //enemy
    //checks target is valid, returns -ive if not
    //otherwise returns indexes of target, updates board
    public int[] selectTarget(int x, int y) {
        int[] pos = new int [2];
        pos[0] = coordToCell(x);
        pos[1] = coordToCell(y);
 
        switch(grid[pos[0]][pos[1]]) {
            case CLEAR:
                selectedTarget[0] = pos[0];
                selectedTarget[1] = pos[1];
                break;
            default: //target, ship, hit, miss
                pos[0] = pos[1] = -1;
        }
            
        return pos;
    }
    
    //enemy
    //selection locked, returns selection
    public int[] confirmsTarget() {
        return selectedTarget;
        
    }
    
    public void restartSelection() {
        selectedTarget[0] = selectedTarget[1] = -1;
    }
    
    //player
    //returns Tile value, updates board
//    public Tile firedOn(int x, int y) {
//        int i = coordToCell(x);
//        int j = coordToCell(y);
//        
//        switch(grid[i][j]) {
//        case SHIP:
//            grid[i][j] = Tile.HIT;
//            break;
//        case CLEAR:
//            grid[i][j] = Tile.MISS;
//            break;
//            default:
//        }
//        
//        return grid[i][j];
//    }
    
    //player
    //returns true if placed, updates grid, returns false else
    public Boolean placeShip(int x, int y, int length, Boolean horiz) {
        int i = coordToCell(x);
        int j = coordToCell(y-startY);
        int end = 0;
        if(horiz)
            end = i + length - 1;
        else
            end = j + length - 1;
        
        if(end<10) { //checking the length
            if(horiz) { //horizontal
                for(int k = i; k<=end; k++) { //checking no ships in way
                    if(grid[k][j] == Tile.SHIP)
                        return false;
                }
                
                for(int k = i; k<=end; k++) {//nothing in way, place ships
                    grid[k][j]= Tile.SHIP;
                    ships[shipN][0] = k;
                    ships[shipN++][1] = j;
                    
                }
            } else { //vertical
                for(int k = j; k<=end; k++) { //checking no ships in way
                    if(grid[i][k] == Tile.SHIP)
                        return false;
                }
            
                for(int k = j; k<=end; k++) {//nothing in way, place ships
                    grid[i][k] = Tile.SHIP;
                    ships[shipN][0] = i;
                    ships[shipN++][1] = k;
                }
            }
      
        } else return false; //too long, overclipping edge
        
        return true;
    }
    
    //converts image to image element, for use with grid
    static public ImageElement imageConvert(String url) {
        Image img = new Image();
        img.setUrl(GWT.getModuleBaseURL() + url);
        ImageElement imgE = ImageElement.as(img.getElement());
        return imgE;
    }

}
