# HomeConnector

HomeConnector is a smart house system under development. 
The project was/is mainly made as a platform to test out different technologies.
The codebase is not tested, and it is partly very hacky, but if the stars align correctly
and it works it can be really cool :)

The system's main functionality is to synchronically play music from a local server to multiple clients
in the local network. The system also supports video, but it is limited to mp4 at the moment. 

## Functionalities of the system are:
- Synchronised music player
- Synchronised video player
- Multiple speakers (clients)
- Alarm
- Doorbell
- Android remote app (for the music player)
- Client health monitor (restart/update clients)
- TV-remote (Sony TV).
- Automatically find speakers (System finds clients on the local network)


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
- The clients use the current time to synchronise, each client needs to be synced via NTP. I recommend setting up a local
NTP server and sync the clients frequently to keep them in sync.
- This project has no license at the moment. It will probably become an open-source project later when it is in a sharable state. Just contact me if you are interested in the project.
