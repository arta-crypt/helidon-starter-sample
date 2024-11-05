package com.samples.helidon.starterapp;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

/**
 * 簡単なJAX-RSリソースで、あなたを挨拶します。例：
 * <p>
 * デフォルトの挨拶メッセージを取得します：
 * curl -X GET http://localhost:8080/greet
 * <p>
 * Joeへの挨拶メッセージを取得します：
 * curl -X GET http://localhost:8080/greet/Joe
 * <p>
 * 挨拶を変更します
 * curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting
 * <p>
 * メッセージはJSONオブジェクトとして返されます。
 */
@Path("/greet")
@RequestScoped
public class GreetResource {

    /**
     * 挨拶メッセージプロバイダー。
     */
    private final GreetingProvider greetingProvider;

    /**
     * コンストラクタインジェクションを使用して設定プロパティを取得します。
     * デフォルトでは、META-INF/microprofile-configから値を取得します。
     *
     * @param greetingConfig 設定された挨拶メッセージ
     */
    @Inject
    public GreetResource(GreetingProvider greetingConfig) {
        this.greetingProvider = greetingConfig;
    }

    /**
     * 世界への挨拶メッセージを返します。
     *
     * @return {@link Message}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getDefaultMessage() {
        return createResponse("World");
    }

    /**
     * 提供された名前を使用して挨拶メッセージを返します。
     *
     * @param name 挨拶する名前
     * @return {@link Message}
     */
    @Path("/{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getMessage(@PathParam("name") String name) {
        return createResponse(name);
    }

    /**
     * 将来のメッセージで使用する挨拶を設定します。
     *
     * @param message 新しい挨拶を含むメッセージ
     * @return {@link Response}
     */
    @Path("/greeting")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RequestBody(name = "greeting",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(type = SchemaType.OBJECT, requiredProperties = {"greeting"})))
    @APIResponses({
            @APIResponse(name = "normal", responseCode = "204", description = "Greeting updated"),
            @APIResponse(name = "missing 'greeting'", responseCode = "400",
                    description = "JSON did not contain setting for 'greeting'")})
    public Response updateGreeting(Message message) {

        if (message.getGreeting() == null || message.getGreeting().isEmpty()) {
            Message error = new Message();
            error.setMessage("No greeting provided");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        greetingProvider.setMessage(message.getGreeting());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private Message createResponse(String who) {
        String msg = String.format("%s %s!", greetingProvider.getMessage(), who);

        return new Message(msg);
    }
}