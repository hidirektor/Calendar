module me.takvim.glsm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jlayer;


    opens me.takvim.glsm to javafx.fxml;
    exports me.takvim.glsm;
}