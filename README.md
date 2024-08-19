# VoiceLib

Minecraft text to speech library

# Usage

VoiceLibApi is the class for the API.
The following are the methods you will be using
```
VoiceLibApi.registerServerPlayerSpeechListener(Consumer<ServerPlayerTalkEvent> consumer)
This method registers a ServerPLayerTalkEvent Consumer. Whenever a player speaks,
This event will be fired. The ServerPlayerTalkEvent provides the player, and a string of what they said.

VoiceLibApi.registerClientSpeechListener(Consumer<ClientTalkEvent> consumer)
This method registers a ClientTalkEvent Consumer. This method is only fired on the client.
Whenever the user speaks, this will be fired.

VoiceLibApi.setPrintToChat(boolean printToChat)
This is only on the client. This sets whether or not to 
print client speak events to chat. (Default False)

VoiceLibApi.setPrintToConsole(boolean printToConsole)
Same as printToChat but for the console instead. (Default False)
```
All methods have java docs for information on their usages.

# Example
This method makes it so if any player on the server says "ouch" they light on fire
```
VoiceLibApi.registerServerPlayerSpeechListener((serverPlayerTalkEvent -> {
    if (serverPlayerTalkEvent.getText().contains("ouch"))
        serverPlayerTalkEvent.getPlayer().igniteForSeconds(2);
}));
```
You can also see this in VoiceLibExample
