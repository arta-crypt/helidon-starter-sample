package com.samples.helidon.starterapp;

/**
 * {@link Message} クラスは、挨拶のためのメッセージを管理します。
 *
 * @author arta-crypt
 */
public class Message {

    /**
     * メッセージの本文を保持します。
     */
    private String message;

    /**
     * メッセージの挨拶文言を保持します。
     */
    private String greeting;

    /**
     * Message のインスタンスを生成します。
     */
    public Message() {
    }

    /**
     * Message のインスタンスを生成します。
     *
     * @param message メッセージの本文
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * メッセージの本文を取得します。
     *
     * @return メッセージの本文
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * メッセージの本文を設定します。
     *
     * @param message メッセージの本文
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * メッセージの挨拶文言を取得します。
     *
     * @return メッセージの挨拶文言
     */
    public String getGreeting() {
        return this.greeting;
    }

    /**
     * メッセージの挨拶文言を設定します。
     *
     * @param greeting メッセージの挨拶文言
     */
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}