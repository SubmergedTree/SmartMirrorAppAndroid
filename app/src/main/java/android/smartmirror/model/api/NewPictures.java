package android.smartmirror.model.api;

import android.smartmirror.model.api.pojos.NewPicturePOJO;
import android.smartmirror.model.bluetooth.Connection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.ref.WeakReference;

/**
 * Created by SubmergedTree a.k.a Jannik Seemann on 23.04.18.
 */

public class NewPictures extends BaseRequest {

    private String json;
    private INewPictures callback;
    private int ref;
    private Progress progress;

    private enum Progress{
        SENDNEWPICTURES,
        SENDJSON;
    }

    public NewPictures(NewPicturePOJO newPicturePOJO, INewPictures callback) {
        ref = Connection.use().register(new WeakReference<Connection.Observer>(this));
        try {
            this.json = new ObjectMapper().writeValueAsString(newPicturePOJO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        this.callback = callback;
        Connection.use().send("ADDPICTURES");
        progress = Progress.SENDNEWPICTURES;
    }

    @Override
    public void receive(String msg) {
        if (progress == Progress.SENDNEWPICTURES) {
            if (msg.equals("OK")) {

            } else {

            }
        } else if (progress == Progress.SENDJSON) {
            if (msg.equals("OK")) {

            } else {

            }
            Connection.use().remove(ref);
        }
    }
}
