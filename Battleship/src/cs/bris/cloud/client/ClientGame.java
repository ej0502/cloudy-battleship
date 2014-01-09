package cs.bris.cloud.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class ClientGame extends Composite {
    
    private final GameServiceAsync gameService = GWT.create(GameService.class);
    
    static final int cellSize = 40,
            canvasHeight = 900,
            canvasWidth = 400;
    final int enemyGridTop = 50,
            playerGridTop = 500;
    Canvas canvasPlayer,
            canvasEnemy;
    Button rotateButton,
            confirmButton;
    PlayerGrid player,
            enemy;
    Context2d context;
    static int pX = 0,
            pY = 0;
    Label label1 = new Label(),
            label2 = new Label();
    static int placement = 0;
    static Boolean horiz = true,
            confirmed = false,
            ready = false;
    static String rotateButtonStr = "Click to place Vertically";
    static Boolean clicked = false, 
            firing = false,
            firingEnd = false,
            selected = false;
    private String opponent;
    
    
    public ClientGame(String opponent) {
        this.opponent = opponent;
        canvasPlayer = Canvas.createIfSupported();
        canvasEnemy = Canvas.createIfSupported();
        if (canvasPlayer == null) {
            RootPanel
                    .get()
                    .add(new Label(
                            "Sorry, your browser doesn't support the HTML5 Canvas element"));
            return;
        }

        //set up canvases
        canvasPlayer.setStyleName("playerCanvas");
        canvasPlayer.setWidth(canvasWidth + "px");
        canvasPlayer.setCoordinateSpaceWidth(canvasWidth);
        canvasPlayer.setHeight(canvasHeight + "px");
        canvasPlayer.setCoordinateSpaceHeight(canvasHeight);
        
        canvasPlayer.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                pX = event.getClientX();
                pY = event.getClientY();
                clicked = true;
            }
        });

        String[] urlImage = new String[] { "images/clear.png",
                "images/miss.png", "images/hit.png", "images/ship.png", "images/target.png" };
        
        //rotate button
        rotateButton = new Button();
        rotateButton.setText(rotateButtonStr);
        rotateButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                horiz = !horiz;
                if(horiz) rotateButtonStr = "Click to place Vertically";
                else rotateButtonStr = "Click to place Horizontally";
                //System.out.println("Mouse clicked here: "+ pX + ", " + pY);
            }
        });
        
        confirmButton = new Button();
        confirmButton.setText("Confirm");
        confirmButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                confirmed= true;
            }
        });
        confirmButton.setVisible(false);
        
        //layout UI
        HorizontalPanel container = new HorizontalPanel();
        VerticalPanel panelLeft = new VerticalPanel();
        VerticalPanel panelRight = new VerticalPanel();
        panelRight.add(label1);
        panelRight.add(label2);
        panelRight.add(rotateButton);
        panelRight.add(confirmButton);
        panelLeft.add(canvasPlayer);
        container.add(panelLeft);
        container.add(panelRight);
        initWidget(container);
        //RootPanel.get().add(container);
        
        
        
        context = canvasPlayer.getContext2d();

        enemy = new PlayerGrid(0, enemyGridTop, 10, 10, cellSize, urlImage);
        player = new PlayerGrid(0, playerGridTop, 10, 10, cellSize, urlImage);
        

        final Timer timer = new Timer() {
            @Override
            public void run() {
                drawSomethingNew();
            }
        };
        timer.scheduleRepeating(500);

    }
    
    //////////////////
    
    public void getTarget() {
        firing = true;
        confirmed = true;
    }
    
    public void updateUI(Boolean hit, Boolean here, int[] position) {
        if(here)
            player.updateUI(hit, position);
        else
            enemy.updateUI(hit, position);
    }
    
    /////////////
    
    private void endPlacement() {
        placement = 5;
        rotateButton.setVisible(false);
        int[][] ships = player.getShipLocations(); //get this to server
        
        
     // RPC to GameService to pass ship positions
        gameService.sendPositions(UserController.getInstance().getUser(), opponent, ships, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                System.out.println("Game.java: RPC failed.");
            }
            public void onSuccess(Boolean result) {
            }
        });
        
        label1.setText("Server Processing");
        label2.setText("Please be patient");
        //getTarget(); // will be called by server
    }
    
    
    //player
    public void sectionPlacement() {

        if(pY > playerGridTop + canvasPlayer.getAbsoluteTop() && pY < 400 + playerGridTop + canvasPlayer.getAbsoluteTop())
            switch(placement) {
            case 0:
                if(player.placeShip(pX-canvasPlayer.getAbsoluteLeft(), pY-canvasPlayer.getAbsoluteTop(), 5, horiz)) {
                    label2.setText("Place the Battleship (length 4)");
                    placement = 1;
                }     
                break;
            case 1:
                
                if(player.placeShip(pX-canvasPlayer.getAbsoluteLeft(), pY-canvasPlayer.getAbsoluteTop(), 4, horiz)){
                    label2.setText("Place the Submarine (length 3)");
                    placement = 2;
                }
                break;
            case 2:
                if(player.placeShip(pX-canvasPlayer.getAbsoluteLeft(), pY-canvasPlayer.getAbsoluteTop(), 3, horiz)){
                    label2.setText("Place the Destroyer (length 3)");
                    placement = 3;
                }
                break;
            case 3:
                if(player.placeShip(pX-canvasPlayer.getAbsoluteLeft(), pY-canvasPlayer.getAbsoluteTop(), 3, horiz)){
                    label2.setText("Place the Patrol Boat (length 2)");
                    placement = 4;
                }
                break;
            case 4:
                if(player.placeShip(pX-canvasPlayer.getAbsoluteLeft(), pY-canvasPlayer.getAbsoluteTop(), 2, horiz)) {
                    endPlacement();
                } 
                break;
                default:
            }
    }

    public void drawSomethingNew() {
        rotateButton.setText(rotateButtonStr);
        
        if(placement == 0) {
            initPlacement();
        } else if(confirmed) {
            if(firing) {
                if(selected) { //firing started, confirmed selection
                    firing = false;
                    confirmButton.setVisible(false);
                    confirmed = false;
                    firingEnd();
                }
                else //firing not started
                initFiring();
            }
            else
                initFiredAt();
        }
        
        if(clicked) {
            if(placement < 5)
                sectionPlacement();
            else if(firing)
                Firing();
            clicked = false;
        }
        enemy.drawGrid(context);
        player.drawGrid(context);
    }
    
    private void initPlacement() {
        //System.out.println("placementStart");
        label1.setText("Place your ships!");
        label2.setText("Place the aircraft carrier (length 5)");
    }
    
    private void initFiring() {
        System.out.println("firingStart");
        label1.setText("Click on the top grid to aim your shot!");
        label2.setText("Good luck!");
        confirmButton.setVisible(false);
        confirmed = false;
    }
    
  //enemy
    //selected cell to fire at
    //will pass on returned values later
    public void Firing() {
        int[] selectedTarget = new int[] { -1, -1 };
        if(pY > enemyGridTop + canvasPlayer.getAbsoluteTop() && pY < 400 + enemyGridTop + canvasPlayer.getAbsoluteTop())
            selectedTarget = enemy.selectTarget(pX - canvasPlayer.getAbsoluteLeft(), pY - canvasPlayer.getAbsoluteTop() - enemyGridTop);
        if(selectedTarget[0] > -1) {
            confirmButton.setVisible(true);
            selected = true;
        }
    }

    //enemy
    //after selection chosen and results back
    public void firingEnd() {
        final int[] target = enemy.confirmsTarget();
        System.out.println("Selection: " + target[0] + ", " + target[1]); //RPC CALL
        
        
     // RPC to GameService to send move
        gameService.checkHit(UserController.getInstance().getUser(), opponent, target, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                System.out.println("Game.java: RPC failed.");
            }
            public void onSuccess(Boolean result) {
                updateUI(result, true, target);
            }
        });
        
        
        enemy.restartSelection();
        selected = false;
        confirmed = true;
    }

    //player
    //wait for results to come in
    private void initFiredAt() {
        label1.setText("Wait for your enemy to fire");
        label2.setText("Good luck!");
    }
}
