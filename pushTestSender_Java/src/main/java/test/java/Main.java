package test.java;

import com.khlim.push.Android;
import com.khlim.push.Client;
import com.khlim.push.FcmMessenger;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FcmMessenger fcm = new FcmMessenger();

        fcm.setType(new Android());

        String title = "Push Test";
        String body = "Hello push";
        String target = "fc8XrFCLTja-9tpdwe6QbI:APA91bEV-BaLPkVVQNzW7z2J4jyYxxokD3_6eK_5782_6IVwJnl63zG8WnJ10vkN5ZMMBJKv4hHU-uFAxDPYqBeOqOgg7I7_ly1yBaxF5YxhmMBUZjKZL0kd7wbpUVx3Qulbekn09U4i";

        List<Client> list = new ArrayList<Client>();

        for(int i = 1 ; i<=100; i++){
            list.add(new Client(target, title + i, body));
        }

        try {
            System.out.println(fcm.push(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
