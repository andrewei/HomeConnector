# HomeConnector

HomeConnector is a smart house system developed mainly as a learning project made by one developer. 

The systems main functionality is to synchronically play playing music from a local server to multiple clients
in the local network. The system also supports video, but it is limited to mp4 at the moment. 

## Other functionalities that are (partly) implemented are:
- Alarm
- Doorbell
- Android remote app (for the music player)
- Client health monitor (restart/update clients)
- TV-remotes.
- Automatically find speakers


## The system consists of:
- Server (Java/Javafx)
- Music server (Local computer with music and/or mp4 files) 
- Clients (Java/Javafx)
- Doorbell (Arduino)
- TV-Remote (Arduino)
- Android app 

To use the system as a music/video player, there is only need to set up a server, client(s) and define a music location. The location
is defined in the GUI in the Server.

## Note: 
The clients use the current time to synchronise, each client needs to be frequently synced via NTP.
