module cz.reindl.arkanoidfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires jaco.mp3.player;


    opens cz.reindl.arkanoidfx.view to javafx.fxml;
    exports cz.reindl.arkanoidfx.view;
}