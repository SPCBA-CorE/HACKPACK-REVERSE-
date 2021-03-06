package quaeio.com.smartchat;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Random;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BotActivity extends AppCompatActivity {

    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private ImageButton imgSent;

    private MemberData data;
    private Socket socket;
    private Thread t;
    private String to, from, AGENT_ID, choosenLanguage;
    private CharSequence languages[] = new CharSequence[]{"English", "Spanish", "Japanese", "German", "Chinese"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);
        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        imgSent = (ImageButton) findViewById(R.id.sent);
        messagesView.setAdapter(messageAdapter);

        imgSent.setEnabled(false);
        data = new MemberData(getRandomName(), getRandomColor());

        AlertDialog.Builder builder = new AlertDialog.Builder(BotActivity.this);
        builder.setTitle("Choose Language");
        builder.setItems(languages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch (languages[which].toString()){
                    case "English" :
                        choosenLanguage = "en";
                        break;
                    case "Spanish" :
                        choosenLanguage = "es";
                        break;
                    case "Japanese" :
                        choosenLanguage = "ja";
                        break;
                    case "German" :
                        choosenLanguage = "de";
                        break;
                    case "Chinese" :
                        choosenLanguage = "zh";
                        break;
                    default:
                }
                sendRequest();
                Toast.makeText(getApplicationContext(), "You choose  " + languages[which].toString() + " as language.", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

        t = new Thread(new Runnable(){
            public void run(){
                try {
                    socket = IO.socket("Personal URL");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        //socket.emit("foo", "hi");
                        //socket.disconnect();
                    }

                }).on("CUST-0002", new Emitter.Listener() {

                    @Override
                    public void call(final Object... args) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Message _message = null;
                                Gson gson = new Gson();
                                Payload obj = gson.fromJson(args[0].toString(), Payload.class);

                                if(obj.transaction.equalsIgnoreCase("reply")){
                                    Reply reply = gson.fromJson(args[0].toString(), Reply.class);
                                    AGENT_ID = reply.agent;
                                    to = reply.from;
                                    String message = "We are now connected!";

                                    _message = new Message(message, data, false);

                                    //enable the button of send
                                    imgSent.setEnabled(true);
                                }else if(obj.transaction.equalsIgnoreCase("chat")){
                                    MessageDTO messageDTO = gson.fromJson(args[0].toString(), MessageDTO.class);

                                    _message = new Message(messageDTO.message, data, false);
                                }

                                messageAdapter.add(_message);
                                messagesView.setSelection(messagesView.getCount() - 1);
                            }
                        });

                    }

                }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                    @Override
                    public void call(Object... args) {
                        socket.disconnect();
                        socket.off("CUST-0002");
                    }

                });
                socket.connect();
            }
        });

        t.start();
    }

    private void sendRequest(){
        // Tag used to cancel the request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonParams = new JSONObject();

        try {
            jsonParams.put("channel", "AGENTCHANNEL");
            jsonParams.put("customerid", "CUST-0002");
            jsonParams.put("language", choosenLanguage);
            jsonParams.put("message", "request");
            jsonParams.put("transaction", "request");
            jsonParams.put("timestamp", "00:00:00");

            // prepare the Request
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,
                    "Personal URL/smartchat/api/v1/sendrequest.php",
                    jsonParams,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            );

            // add it to the RequestQueue
            requestQueue.add(getRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(View view) {
        final String message = editText.getText().toString();
        if (message.length() > 0) {
            Message _message = null;
            Message _botmessage = null;

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            JSONObject jsonParams = new JSONObject();

            try {
                jsonParams.put("channel", AGENT_ID);
                jsonParams.put("message", message);
                jsonParams.put("translate", true);
                jsonParams.put("transaction", "chat");
                jsonParams.put("from", choosenLanguage);
                jsonParams.put("to", to);

                // prepare the Request
                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,
                        "Personal URL/smartchat/api/v1/sendmessage.php",
                        jsonParams,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                // display response
                                Message _message = null;

                                _message = new Message(message, data, true);
                                messageAdapter.add(_message);
                                messagesView.setSelection(messagesView.getCount() - 1);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", error.getMessage());
                            }
                        }
                );

                // add it to the RequestQueue
                requestQueue.add(getRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editText.getText().clear();
    }

    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        return (
                adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        " " +
                        nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
