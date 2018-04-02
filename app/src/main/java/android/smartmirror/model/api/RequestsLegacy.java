package android.smartmirror.model.api;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 17.03.18.
 */

public enum RequestsLegacy {
    // only the app can start a request.
    GETUSERS     ("GETUSERS"),   // retrieve list with name, prename and username of all users
    DELETEUSER   ("DELETEUSER"), // mirror should delete an user identified by username
    ADDPICTURE   ("ADDPICTURE"), // add a picture to an user identified by username
    GETWIDGETS   ("GETWIDGETS"), // get name and position of all used widgets
    UPDATEWIDGETS("UPDATEWIDGETS"), // update widgets for a person
    UPDATEUSER    ("UPDATEUSER"), //udate name and prename -->compare: Updatewidgets // remove
    NEWUSER    ("NEWUSER");

    private final String message;

    RequestsLegacy(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

/*
Protocol
            APP     direction       message      Mirror
GETUSERS:
                        ->          GETUSERS
                        <-            JSON

DELETEUSER:
                        ->          DELETUSER
                        <-              OK
                        ->             JSON
                        <-           OK or FAILED

ADDPICTURE:             ->           ADDPICTURE
                        <-              OK
                        ->             JSON
                        <-              OK

GETWIDGETS:             ->            GETWIDGETS
                        <-               JSON

UPDATEWIDGETS:          ->            UPDATEWIDGETS
                        <-                OK
                        ->               JSON
                        ->             OK Or FAILED

NEWUSER:                ->              NEWUSER
                        <-                 OK
                        ->                JSON
                        <-                  OK

Timeout after generous 30 seconds.

 */