@ECHO off

set SERVER_HOST="HOST"
set SERVER_CLIENT_FOLDER="FULL URL TO CLIENT FOLDER"
set USER="SERVER USER"
set PASSWORD="SERVER PASSWORD"

taskkill /F /FI "WindowTitle eq client" /T
start "client" java -jar client.jar

:while
    net use %SERVER_HOST% /user:%USER% %PASSWORD%
    echo checking for config updates...

    copy %SERVER_CLIENT_FOLDER%\config.txt configTemp.txt

    for /f "tokens=1,2 delims==" %%G in (config.txt) do set %%G=%%H
    set localVersion=%version%
    set localRestart=%restart%

    echo localVersion %localVersion%
    echo localRestart %localRestart%

    echo checking remote config
    for /f "tokens=1,2 delims==" %%G in (configTemp.txt) do set %%G=%%H
    set remoteVersion=%version%
    set remoteRestart=%restart%

    echo remoteVersion %remoteVersion%
    echo remoteRestart %remoteRestart%

    IF %localVersion% LSS %remoteVersion% (
        echo restart is scheduled, nr %remoteRestart%
        copy configTemp.txt config.txt
        copy %SERVER_CLIENT_FOLDER%\client.jar client.jar
        taskkill /F /FI "WindowTitle eq client" /T
        start "client" java -jar client.jar
        echo restarting client
    )

    IF %localRestart% LSS %remoteRestart% (
        echo restart is scheduled, nr %remoteRestart%
        copy configTemp.txt config.txt
        taskkill /F /FI "WindowTitle eq client" /T
        echo restarting client
        start "client" java -jar client.jar
    )
    TIMEOUT 10
goto :while
