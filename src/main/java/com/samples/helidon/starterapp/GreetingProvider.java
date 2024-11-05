package com.samples.helidon.starterapp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.concurrent.atomic.AtomicReference;

/**
 * このクラスは、アプリケーションの挨拶メッセージを提供します。
 * スレッドセーフにメッセージを管理します。
 *
 * @author arta-crypt
 */
@ApplicationScoped
public class GreetingProvider {
    /**
     * メッセージを保持するためのAtomicReferenceです。
     */
    private final AtomicReference<String> message = new AtomicReference<>();

    /**
     * コンストラクタです。初期の挨拶メッセージを設定します。
     *
     * @param message 初期設定の挨拶メッセージ
     */
    @Inject
    public GreetingProvider(@ConfigProperty(name = "app.greeting") String message) {
        this.message.set(message);
    }

    /**
     * 現在の挨拶メッセージを取得します。
     *
     * @return 現在の挨拶メッセージ
     */
    String getMessage() {
        return message.get();
    }

    /**
     * 挨拶メッセージを新しい値に設定します。
     *
     * @param message 新しい挨拶メッセージ
     */
    void setMessage(String message) {
        this.message.set(message);
    }
}