package net.cloudburo.polkadot.hello;

import com.onehilltech.promises.Promise;
import org.polkadot.api.promise.ApiPromise;
import org.polkadot.direct.IRpcFunction;
import org.polkadot.rpc.provider.ws.WsProvider;
import org.polkadot.types.type.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class ListenToBlocks {

    private int count = 0;
    private static final Logger log = LoggerFactory.getLogger(ListenToBlocks.class);


    public void run(String endPoint) {
        WsProvider wsProvider = new WsProvider(endPoint);
        Promise<ApiPromise> ready = ApiPromise.create(wsProvider);

        // You have to use here a final variable
        // http://tutorials.jenkov.com/java-util-concurrent/atomicreference.html
        // http://www.javahabit.com/2016/06/16/understanding-java-8-lambda-final-finally-variable/
        // https://stackoverflow.com/questions/34865383/variable-used-in-lambda-expression-should-be-final-or-effectively-final
        final AtomicReference<IRpcFunction.Unsubscribe<Promise>> reference = new AtomicReference<>();

        ready.then( api -> {
            // Subscribe to New Head Event
            Promise<IRpcFunction.Unsubscribe<Promise>> invoke = api.rpc().chain().function("subscribeNewHead").invoke(
                (IRpcFunction.SubscribeCallback<Header>) (Header header) ->
                {
                    System.out.println("Chain is at block: " + header.getBlockNumber()+" trigger "+count);
                    count++;
                    if (count >5) {
                        reference.get().unsubscribe();
                    }
                });
            return invoke;
        }).then((IRpcFunction.Unsubscribe<Promise> result) -> {
            reference.set(result);
            return null;
        })._catch((err) -> {
            err.printStackTrace();
            return Promise.value(err);
        });
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
