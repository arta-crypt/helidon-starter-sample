package com.samples.helidon.starterapp;

import io.helidon.metrics.api.MetricsFactory;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * MainTest クラスは、REST API のエンドポイントをテストします。
 * REST API のさまざまなエンドポイントに対して HTTP リクエストを行い、
 * 期待されるレスポンスが返されるかどうかを検証します。
 *
 * @author arta-crypt
 */
@HelidonTest
class MainTest {

    /**
     * メトリクスレジストリのインスタンスを保持します。
     */
    @Inject
    private MetricRegistry registry;

    /**
     * WebTarget のインスタンスを保持します。REST API のエンドポイントをターゲットにします。
     */
    @Inject
    private WebTarget target;

    /**
     * テストの後にメトリクスをクリアします。
     */
    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }

    /**
     * ヘルスチェックエンドポイントのステータスコードを確認します。
     */

    @Test
    void testHealth() {
        Response response = target
                .path("health")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    /**
     * マイクロプロファイルメトリクスの動作を確認します。
     * personalizedGets カウンタの増加を確認します。
     */
    @Test
    void testMicroprofileMetrics() {
        Message message = target.path("simple-greet/Joe")
                .request()
                .get(Message.class);

        assertThat(message.getMessage(), is("Hello Joe"));
        Counter counter = registry.counter("personalizedGets");
        double before = counter.getCount();

        message = target.path("simple-greet/Eric")
                .request()
                .get(Message.class);
        assertThat(message.getMessage(), is("Hello Eric"));
        double after = counter.getCount();
        assertEquals(1d, after - before, "Difference in personalized greeting counter between successive calls");
    }

    /**
     * デフォルトの挨拶メッセージを確認します。
     */

    @Test
    void testGreet() {
        Message message = target
                .path("simple-greet")
                .request()
                .get(Message.class);
        assertThat(message.getMessage(), is("Hello World!"));
    }

    /**
     * 挨拶メッセージの変更を確認します。
     */
    @Test
    void testGreetings() {
        Message jsonMessage = target
                .path("greet/Joe")
                .request()
                .get(Message.class);
        assertThat(jsonMessage.getMessage(), is("Hello Joe!"));

        try (Response r = target
                .path("greet/greeting")
                .request()
                .put(Entity.entity("{\"greeting\" : \"Hola\"}", MediaType.APPLICATION_JSON))) {
            assertThat(r.getStatus(), is(204));
        }

        jsonMessage = target
                .path("greet/Jose")
                .request()
                .get(Message.class);
        assertThat(jsonMessage.getMessage(), is("Hola Jose!"));
    }
}