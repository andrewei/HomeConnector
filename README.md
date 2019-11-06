# HomeConnector

HomeConnector is a smart house system developed by one developer. 
The project was/is made mainly for learning/testing out different technologies.
The code is not tested, and it is partly very hacky, but if the stars align correctly
and it works it can be really cool :) 

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
There is currently no samba authentication functionality for the clients, so if you have username/password
on the music server all the clients needs to login before working.
