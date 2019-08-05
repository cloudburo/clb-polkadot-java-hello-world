package net.cloudburo.polkadot.hello;

import com.onehilltech.promises.Promise;
import org.polkadot.api.promise.ApiPromise;
import org.polkadot.rpc.provider.ws.WsProvider;

public class HelloWorld {

    static String endPoint = "wss://poc3-rpc.polkadot.io/";
    // Flaming Flir
    //static String endPoint =  "wss://substrate-rpc.parity.io/";
    //static String endPoint ="wss://alex.unfrastructure.io/public/ws";
    //static String endPoint = "ws://45.76.157.229:9944/";
    //static String endPoint = "ws://127.0.0.1:9944";


    public void run(String endPoint) {
        WsProvider wsProvider = new WsProvider(endPoint);

        Promise<ApiPromise> ready = ApiPromise.create(wsProvider);
        System.out.println("ApiPromise loaded");
        ready.then(api -> Promise.all(

                //api.rpc().system().function("chain").invoke()

                api.rpc().system().function("chain").invoke(),
                api.rpc().system().function("name").invoke(),
                api.rpc().system().function("version").invoke()
        )).then((results) -> {
            System.out.println("You are connected to chain [" + results.get(0) + "] using [" + results.get(1) + "] v[" + results.get(2) + "]");
            //System.out.println("You are connected to chain [" + results.get(0) + "]");

            return null;
        })._catch((err) -> {
            err.printStackTrace();
            return null;
        });

    }

    public static void main(String[] args) {
        // Create an await for the API
        //Promise<ApiPromise> ready = ApiPromise.create();
        ListenToBlocks test = new ListenToBlocks();
        test.run(endPoint);

    }

}
