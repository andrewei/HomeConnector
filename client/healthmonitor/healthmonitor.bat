@ECHO off
:while
    net use \\FENRIZ-PC /user:INSERT-USERNAME INSERT-PASSWORD
    echo checking for config updates...
    for /f "tokens=1,2 delims==" %%G in (config.txt) do set %%G=%%H
    set localVersion=%version%
    set localRestart=%restart%

    for /f "tokens=1,2 delims==" %%G in (\\FENRIZ-PC\mp32\config\config.txt) do set %%G=%%H
    set remoteVersion=%version%
    set remoteRestart=%restart%

    echo localVersion %localVersion%
    echo localRestart %localRestart%
    echo remoteVersion %remoteVersion%
    echo remoteRestart %remoteRestart%

    IF %localRestart% LSS %remoteRestart% (
        echo restart is scheduled, nr %remoteRestart%
        copy \\FENRIZ-PC\mp32\config\config.txt config.txt
        taskkill /F /FI "WindowTitle eq client" /T
        start "client" java -jar client.jar
        echo restarting client
    )

    IF %localVersion% LSS %remoteVersion% (
        echo restart is scheduled, nr %remoteRestart%
        copy \\FENRIZ-PC\mp32\config\config.txt config.txt
        copy \\FENRIZ-PC\mp32\config\client.jar client.jar
        taskkill /F /FI "WindowTitle eq client" /T
        start "client" java -jar client.jar
        echo restarting client
    )
    TIMEOUT 10
goto :while